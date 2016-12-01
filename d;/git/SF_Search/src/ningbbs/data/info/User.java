package ningbbs.data.info;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ningbbs.dydh.util.HtmlJosnUtil;
import ningbbs.dydh.util.HtmlJosnUtil.Status;
import ningbbs.gui.MainGui;
import ningbbs.kd.SfSearch;
import ningbbs.service.SfScanService;
import ningbbs.util.Constants;
import ningbbs.util.Constants.RETURN_HTML_TYPE;
import ningbbs.util.FileUtil;
import ningbbs.util.Orc;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 第一单号用户类
 * 
 * @author shining
 * 
 */
public class User {

	/**
	 * 一个用户的所有连接都只有一个cookie，所以放在user里,此COOKIE不暴漏在外
	 */
	private String cookie;
	/**
	 * 用户名
	 */
	protected String userName;
	/**
	 * 密码
	 */
	protected String passWord;

	public long oldSendTime = 0;
	private ExecutorService pool = Executors.newSingleThreadExecutor();
	private int threadSize=0;//计算未完成任务数量
	private int OK_NUM=0;//发送成功次数

	public User(String userName, String passWord) {
		this.userName = userName;
		this.passWord = passWord;
	}
	
	public synchronized void addPool(final SfSearch sf){
		addSize();
		pool.execute(new Runnable() {
			public void run() {
				try {
					if(send(sf)==0){
						OK_NUM++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				subSize();
			}
		});
	}
	public void addSize(){
		threadSize++;
	}
	public void subSize(){
		threadSize--;
	}

	//检测与上一次发送间隔 时间
	private void sleep() {
		while ((System.currentTimeMillis() - oldSendTime) < Constants.sendSleepTime) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		oldSendTime = System.currentTimeMillis();
	}
	
	private int send(final SfSearch sf) {
		int rs=0;
		sleep();
		Status state = sendNum(sf);
		rs=handleSendState(this, state, sf);
		/*
		String msg = "■■" + sf.getId()
				+ "■■发送状态:单号发布成功■■" 
				+ SfService.getScanTime(sf)
				+ "■■"
				+ sf.getOrigin()
				+ "■■"
				+ sf.getDestination()
				+ "■■";
		Constants.sendMsg(msg, true, true);
		*/
		return rs;
	}
	
	/**
	 * 处理发送返回的状态
	 * 
	 * @param status
	 *            状态
	 * @param kdInfo
	 *            所发单
	 * @return 是否发送成功
	 */
	public int handleSendState(User user, Status status, SfSearch sf) {
		int flag = -1;
		switch (status.getStatus()) {
		case 0:
			// 发送成功,但发送过频繁也返回此值
			if (status.getInfo().indexOf("单号发布成功！") != -1) {
				Constants.sendMsg("■"+OK_NUM+ "■"+sf.getId()+"■"+SfScanService.getScanTime(sf)+"■"+sf.getLimitTypeCode()+"■"+sf.getOrigin()+"■"+sf.getDestination()+"■"+status.getInfo()+"■"+getThreadSize(), true, true);
				flag = 0;
			} else {
				switch (status.getInfo()) {
				case "您刚发完哦，请稍后再发！":
					Constants.sendMsg(status.getInfo(), false, true);
					flag=-2;
					break;
				case "收货地点：州县不能为空！":
					Constants.sendMsg(status.getInfo(), false, true);
					flag=-3;
					break;
				case "发货地点，收货地点：省份/城市为必选项！":
					Constants.sendMsg(status.getInfo(), false, true);
					flag=-4;
					break;
				case "只能发布2天内已扫描单号，再次尝试会被永久冻结帐号！":
					Constants.sendMsg(status.getInfo(), false, true);
					flag=-5;
					break;
				default:
					Constants.sendMsg(status.getInfo(), false, true);
					flag=-6;
					break;
				}
				break;
			}
			break;
		case -1:
			switch (status.getInfo()) {
			case "未登录，自动跳转到登录页面！":
				Constants.sendMsg("state=" + status.getStatus() + " stateInfo=" + status.getInfo(), true, true);
				user.login(MainGui.imageCodeLabel);
				flag=-8;
				break;
			default:
				Constants.sendMsg("default2: state=" + status.getStatus() + " stateInfo=" + status.getInfo(), false,
						true);
				flag=-9;
				break;
			}
			break;
		default:
			Constants.sendMsg("default3 state=" + status.getStatus() + " stateInfo=" + status.getInfo(), false, false);
			flag=-10;
			break;
		}
		return flag;
	}

	private Status sendNum(SfSearch sf) {
		String url = "http://" + Constants.ROOT_URL + "/sell/general_insert/";
		// 构造发送头
		List<BasicHeader> headerList = new ArrayList<>();
		headerList.add(new BasicHeader("Host", "" + Constants.ROOT_URL + ""));
		headerList
				.add(new BasicHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0"));
		headerList.add(new BasicHeader("Accept",
				"application/json, text/javascript, */*; q=0.01"));
		headerList.add(new BasicHeader("Accept-Language",
				"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		headerList.add(new BasicHeader("Content-Type",
				" application/x-www-form-urlencoded; charset=UTF-8"));
		headerList.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
		headerList.add(new BasicHeader("Referer", "http://"
				+ Constants.ROOT_URL + "/sell/general/"));
		headerList.add(new BasicHeader("Connection", "keep-alive"));
		headerList.add(new BasicHeader("Pragma", "no-cache"));
		headerList.add(new BasicHeader("Cache-Control", "no-cache"));
		// 构造参数
		String num = sf.getId();
		String scan_date = SfScanService.getScanTime(sf);
		scan_date = scan_date.substring(0, scan_date.length() - 3);
		String[] sArr = sf.getOriginIds().split(",");
		String[] rArr = sf.getDestinationIds().split(",");
		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("num", num));
		nvps.add(new BasicNameValuePair("send_date", scan_date));
		nvps.add(new BasicNameValuePair("com", "5"));
		nvps.add(new BasicNameValuePair("s1", sArr[0]));
		nvps.add(new BasicNameValuePair("s2", sArr[1]));
		nvps.add(new BasicNameValuePair("s3", sArr[2]));
		nvps.add(new BasicNameValuePair("r1", rArr[0]));
		nvps.add(new BasicNameValuePair("r2", rArr[1]));
		nvps.add(new BasicNameValuePair("r3", rArr[2]));
		// 是否扫描
		nvps.add(new BasicNameValuePair("send_type", "scan"));
		// 发送
		ResultInfo result = sendPost(url, headerList, nvps,
				RETURN_HTML_TYPE.STR);
		String json = result.getExtData().toString();
		Status status = HtmlJosnUtil.handleStatus(json);
		if (status == null) {
			Constants.sendMsg("jsonerr: " + json, false, true);
		}
		return status;
	}

	/**
	 * 获取验正码
	 * 
	 * @throws IOException
	 */
	public void loadImageCode(JLabel imageLabel) throws IOException {
		// 构造所需要的信息
		String url = "http://" + Constants.ROOT_URL + "/verify/?0";
		List<BasicHeader> headerList = new ArrayList<>();
		headerList.add(new BasicHeader("Host", "" + Constants.ROOT_URL + ""));
		headerList
				.add(new BasicHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0"));
		headerList.add(new BasicHeader("Accept",
				"image/png,image/*;q=0.8,*/*;q=0.5"));
		headerList.add(new BasicHeader("Accept-Language",
				"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		headerList.add(new BasicHeader("Referer", "http://"
				+ Constants.ROOT_URL + "/user/index/"));
		headerList.add(new BasicHeader("Connection", "keep-alive"));
		ResultInfo result = sendGet(url, headerList, RETURN_HTML_TYPE.BYTE);
		if (result != null) {
			FileUtil.saveImage("code.jpg", (byte[]) result.getExtData());
			if (imageLabel != null) {
				BufferedImage bi = ImageIO.read(new File("code.jpg"));
				imageLabel.setIcon(new ImageIcon(bi));
			}
		}
	}

	/**
	 * 让当前用户发送POST所有POST请求都应从此处发送
	 * 
	 * @param url
	 * @param nvps
	 * @param type
	 * @return
	 */
	public ResultInfo sendPost(String url, List<BasicHeader> headerList,
			List<NameValuePair> nvps, RETURN_HTML_TYPE type) {
		ResultInfo result = new ResultInfo();
		Post post = new Post(url, nvps);
		switch (type) {
		case BYTE:
			result.setStatus(0);
			result.setInfo("返回字节流成功");
			result.setExtData(post.getBytes(headerList, "utf8"), byte[].class);
			break;
		case STR:
			result.setStatus(0);
			result.setInfo("返回文本流成功");
			result.setExtData(post.getHtml(headerList, "utf8"), String.class);
			break;
		default:
			result.setStatus(0);
			result.setInfo("返回文本流成功");
			result.setExtData(post.getHtml(headerList, "utf8"), String.class);
			break;
		}
		for (Header header : post.getRequestHeader()) {
			if (header.getName().equals("Set-Cookie")) {
				setCookie(header.getValue());
			}
		}
		return result;
	}

	/**
	 * 让当前用户发送get,所有GET请求都应从此处发送
	 * 
	 * @param url
	 *            地址
	 * @param headerList
	 *            头信息
	 * @param type
	 *            返回类型
	 * @return
	 */
	public ResultInfo sendGet(String url, List<BasicHeader> headerList,
			RETURN_HTML_TYPE type) {
		ResultInfo result = new ResultInfo();
		Get get = new Get(url);
		switch (type) {
		case BYTE:
			result.setStatus(0);
			result.setInfo("返回字节流成功");
			result.setExtData(get.getBytes(headerList), byte[].class);
			break;
		case STR:
			result.setStatus(0);
			result.setInfo("返回文本流成功");
			result.setExtData(get.getHtml(headerList, "utf8"), String.class);
			break;
		default:
			result.setStatus(0);
			result.setInfo("返回文本流成功");
			result.setExtData(get.getHtml(headerList, "utf8"), String.class);
			break;
		}
		for (Header header : get.getRequestHeader()) {
			if (header.getName().equals("Set-Cookie")) {
				setCookie(header.getValue());
			}
		}
		return result;
	}

	/**
	 * 登录(本登录方式带提示框输入验正码)
	 * 
	 * @param name
	 *            用户名
	 * @param pwd
	 *            密码
	 * @return
	 */
	public ResultInfo login(JLabel label) {
		ResultInfo result = new ResultInfo();
		try {
			loadImageCode(label);
			Orc orc = new Orc();
			String code = "";
			if (orc != null) {
				code = orc.getNums();
			}
			while (code.equals("") || code.length() != 4) {
				try {
					loadImageCode(label);
					code = new Orc().getNums();
					Constants.sendMsg("☆重新识别验正码:" + code, true, true);
				} catch (Exception e) {
					Constants.sendException(e);
				}
				Thread.sleep(60000);
			}
			Constants.sendMsg("★自动识别验正码并登录:" + code, true, true);
			result = login(code);
			Constants.sendMsg(result.getInfo(), false, true);
		} catch (IOException e) {
			Constants.sendException(e);
			result.addException(e);
		} catch (Exception e) {
			Constants.sendException(e);
			result.addException(e);
		}
		return result;
	}

	/**
	 * 登录(此登录方式需要给定验正)
	 * 
	 * @param name
	 *            用户名
	 * @param pwd
	 *            密码
	 * @param code
	 *            验正码
	 * @return
	 */
	private ResultInfo login(String code) {
		ResultInfo result = new ResultInfo();
		Constants.sendMsg("登录用户名：" + userName, true, true);
		// 构造所需要的信息
		String url = "http://" + Constants.ROOT_URL + "/user/login/";
		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("name", userName));
		nvps.add(new BasicNameValuePair("pwd", passWord));
		nvps.add(new BasicNameValuePair("code", code));
		nvps.add(new BasicNameValuePair("ajax", "1"));
		List<BasicHeader> headerList = new ArrayList<>();
		headerList.add(new BasicHeader("Host", "" + Constants.ROOT_URL + ""));
		headerList
				.add(new BasicHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0"));
		headerList.add(new BasicHeader("Accept",
				"application/json, text/javascript, */*; q=0.01"));
		headerList.add(new BasicHeader("Accept-Language",
				"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		headerList.add(new BasicHeader("Content-Type",
				" application/x-www-form-urlencoded; charset=UTF-8"));
		headerList.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
		headerList.add(new BasicHeader("Referer", "http://"
				+ Constants.ROOT_URL + "/user/index/"));
		headerList.add(new BasicHeader("Connection", "keep-alive"));
		headerList.add(new BasicHeader("Pragma", "no-cache"));
		headerList.add(new BasicHeader("Cache-Control", "no-cache"));
		// 发送
		result = sendPost(url, headerList, nvps, RETURN_HTML_TYPE.STR);
		if (result != null && result.getExtData() != null) {
			String html = result.getExtData().toString();
			Status status = HtmlJosnUtil.handleStatus(html); // 分析JSON转为状态对象
			if (status != null) {
				// 根据JSON对象 设置状态
				result.setStatus(status.getStatus());
				result.setInfo(status.getInfo());
			} else {
				result.setStatus(-100);
				result.setInfo("未知错误");
			}
			result.setExtData(html, String.class);// 设置扩展数据
		}
		return result;
	}

	/**
	 * 取余额
	 * 
	 * @return
	 */
	public double getMoney() {
		double money = 0;
		String url = "http://" + Constants.ROOT_URL + "/user/";
		List<BasicHeader> headerList = new ArrayList<>();
		headerList.add(new BasicHeader("Host", "" + Constants.ROOT_URL + ""));
		headerList
				.add(new BasicHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0"));
		headerList.add(new BasicHeader("Accept",
				"application/json, text/javascript, */*; q=0.01"));
		headerList.add(new BasicHeader("Accept-Language",
				"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		headerList.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
		headerList.add(new BasicHeader("Referer", "http://"
				+ Constants.ROOT_URL + "/sell/general/"));
		headerList.add(new BasicHeader("Connection", "keep-alive"));
		ResultInfo result = sendGet(url, headerList, RETURN_HTML_TYPE.STR);
		if (result != null && result.getExtData() != null) {
			money = HtmlJsoupUtil.getMoney(result.getExtData().toString());
		}
		return money;
	}

	private synchronized String getCookie() {
		return cookie;
	}

	private synchronized void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public int getThreadSize() {
		return threadSize;
	}

	/**
	 * 接收类(内部类)
	 * 
	 * @author shining
	 * 
	 */
	class Get {
		/**
		 * 地址
		 */
		private String uri;
		/**
		 * 响应头列表
		 */
		private Header[] requestHeader;

		public Get(String uri) {
			this.uri = uri;
		}

		/**
		 * GET返回网页内容
		 * 
		 * @param headerList
		 *            附加的header信息
		 * @return
		 */
		public String getHtml(List<BasicHeader> headerList, String charsetName) {
			CloseableHttpClient http = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(uri);
			httpget.setConfig(Constants.REQUEST_CONFIG);
			if (getCookie() != null && !getCookie().equals("")) {
				httpget.addHeader(new BasicHeader("Cookie", getCookie()));
			}
			if (headerList != null && headerList.size() > 0) {
				for (int i = 0; i < headerList.size(); i++) {
					httpget.addHeader(headerList.get(i));
				}
			}
			String responseBody = "";
			try {
				CloseableHttpResponse request = http.execute(httpget);
				requestHeader = request.getAllHeaders();// 取响应头，保存在requestHeader供需要时获取
				byte[] bytearray = EntityUtils.toByteArray(request.getEntity());
				responseBody = new String(bytearray, charsetName);
				responseBody = FileUtil.unicodeDecode(responseBody);// 转码
			} catch (Exception e) {
				Constants.sendException(e);
				responseBody = null;
			} finally {
				httpget.abort();
			}
			return responseBody;
		}

		/**
		 * 获取原始字符数组
		 * 
		 * @param headerList
		 * @return
		 */
		public byte[] getBytes(List<BasicHeader> headerList) {
			CloseableHttpClient http = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(uri);
			httpget.setConfig(Constants.REQUEST_CONFIG);
			if (getCookie() != null && !getCookie().equals("")) {
				httpget.addHeader(new BasicHeader("Cookie", getCookie()));
			}
			if (headerList != null && headerList.size() > 0) {
				for (int i = 0; i < headerList.size(); i++) {
					httpget.addHeader(headerList.get(i));
				}
			}
			byte[] bytearray = null;
			try {
				CloseableHttpResponse request = http.execute(httpget);
				requestHeader = request.getAllHeaders();// 取响应头，保存在requestHeader供需要时获取
				bytearray = EntityUtils.toByteArray(request.getEntity());
			} catch (Exception e) {
				Constants.sendException(e);
			} finally {
				httpget.abort();
			}
			return bytearray;
		}

		public Header[] getRequestHeader() {
			return requestHeader;
		}
	}

	/**
	 * 发送类
	 * 
	 * @author shining
	 * 
	 */
	class Post {
		/**
		 * 地址
		 */
		private String uri;
		/**
		 * 参数列表
		 */
		private List<NameValuePair> nvps;
		/**
		 * 响应头列表
		 */
		private Header[] requestHeader;

		public Post(String uri, List<NameValuePair> nvps) {
			this.uri = uri;
			this.nvps = nvps;
		}

		/**
		 * Post返回网页内容
		 * 
		 * @param uri
		 * @param cookie
		 * @param nvps
		 * @return
		 */
		public String getHtml(List<BasicHeader> headerList, String charsetName) {
			String urlStr = "";
			CloseableHttpClient http = HttpClients.createDefault();
			HttpPost httpost = new HttpPost(uri);
			httpost.setConfig(Constants.REQUEST_CONFIG);
			if (getCookie() != null && !getCookie().equals("")) {
				httpost.addHeader(new BasicHeader("Cookie", getCookie()));
			}
			if (headerList != null && headerList.size() > 0) {
				for (int i = 0; i < headerList.size(); i++) {
					httpost.addHeader(headerList.get(i));
				}
			}
			try {
				httpost.setEntity(new UrlEncodedFormEntity(nvps));
				HttpResponse response = http.execute(httpost);
				requestHeader = response.getAllHeaders();// 取响应头，保存在requestHeader供需要时获取
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					InputStream instream = entity.getContent();
					// 最佳方案
					ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
					int l;
					byte[] tmp = new byte[500000];
					while ((l = instream.read(tmp)) != -1) {
						bytestream.write(tmp, 0, l);
					}
					bytestream.close();
					instream.close();
					urlStr = new String(bytestream.toByteArray(), charsetName);
					urlStr = FileUtil.unicodeDecode(urlStr);// 转码
				}
			} catch (Exception e) {
				Constants.sendException(e);
			} finally {
				httpost.abort();
			}
			return urlStr;
		}

		public byte[] getBytes(List<BasicHeader> headerList, String charsetName) {
			byte[] bytearray = null;
			CloseableHttpClient http = HttpClients.createDefault();
			HttpPost httpost = new HttpPost(uri);
			httpost.setConfig(Constants.REQUEST_CONFIG);
			if (getCookie() != null && !getCookie().equals("")) {
				httpost.addHeader(new BasicHeader("Cookie", getCookie()));
			}
			if (headerList != null && headerList.size() > 0) {
				for (int i = 0; i < headerList.size(); i++) {
					httpost.addHeader(headerList.get(i));
				}
			}
			try {
				httpost.setEntity(new UrlEncodedFormEntity(nvps));
				HttpResponse response = http.execute(httpost);
				requestHeader = response.getAllHeaders();// 取响应头，保存在requestHeader供需要时获取
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					InputStream instream = entity.getContent();
					// 最佳方案
					ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
					int l;
					byte[] tmp = new byte[500000];
					while ((l = instream.read(tmp)) != -1) {
						bytestream.write(tmp, 0, l);
					}
					bytestream.close();
					instream.close();
					bytearray = bytestream.toByteArray();
				}
			} catch (Exception e) {
				Constants.sendException(e);
			} finally {
				httpost.abort();
			}
			return bytearray;
		}

		public Header[] getRequestHeader() {
			return requestHeader;
		}

		public void setRequestHeader(Header[] requestHeader) {
			this.requestHeader = requestHeader;
		}
	}

}
