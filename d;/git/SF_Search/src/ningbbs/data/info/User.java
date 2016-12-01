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
 * ��һ�����û���
 * 
 * @author shining
 * 
 */
public class User {

	/**
	 * һ���û����������Ӷ�ֻ��һ��cookie�����Է���user��,��COOKIE����©����
	 */
	private String cookie;
	/**
	 * �û���
	 */
	protected String userName;
	/**
	 * ����
	 */
	protected String passWord;

	public long oldSendTime = 0;
	private ExecutorService pool = Executors.newSingleThreadExecutor();
	private int threadSize=0;//����δ�����������
	private int OK_NUM=0;//���ͳɹ�����

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

	//�������һ�η��ͼ�� ʱ��
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
		String msg = "����" + sf.getId()
				+ "��������״̬:���ŷ����ɹ�����" 
				+ SfService.getScanTime(sf)
				+ "����"
				+ sf.getOrigin()
				+ "����"
				+ sf.getDestination()
				+ "����";
		Constants.sendMsg(msg, true, true);
		*/
		return rs;
	}
	
	/**
	 * �����ͷ��ص�״̬
	 * 
	 * @param status
	 *            ״̬
	 * @param kdInfo
	 *            ������
	 * @return �Ƿ��ͳɹ�
	 */
	public int handleSendState(User user, Status status, SfSearch sf) {
		int flag = -1;
		switch (status.getStatus()) {
		case 0:
			// ���ͳɹ�,�����͹�Ƶ��Ҳ���ش�ֵ
			if (status.getInfo().indexOf("���ŷ����ɹ���") != -1) {
				Constants.sendMsg("��"+OK_NUM+ "��"+sf.getId()+"��"+SfScanService.getScanTime(sf)+"��"+sf.getLimitTypeCode()+"��"+sf.getOrigin()+"��"+sf.getDestination()+"��"+status.getInfo()+"��"+getThreadSize(), true, true);
				flag = 0;
			} else {
				switch (status.getInfo()) {
				case "���շ���Ŷ�����Ժ��ٷ���":
					Constants.sendMsg(status.getInfo(), false, true);
					flag=-2;
					break;
				case "�ջ��ص㣺���ز���Ϊ�գ�":
					Constants.sendMsg(status.getInfo(), false, true);
					flag=-3;
					break;
				case "�����ص㣬�ջ��ص㣺ʡ��/����Ϊ��ѡ�":
					Constants.sendMsg(status.getInfo(), false, true);
					flag=-4;
					break;
				case "ֻ�ܷ���2������ɨ�赥�ţ��ٴγ��Իᱻ���ö����ʺţ�":
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
			case "δ��¼���Զ���ת����¼ҳ�棡":
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
		// ���췢��ͷ
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
		// �������
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
		// �Ƿ�ɨ��
		nvps.add(new BasicNameValuePair("send_type", "scan"));
		// ����
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
	 * ��ȡ������
	 * 
	 * @throws IOException
	 */
	public void loadImageCode(JLabel imageLabel) throws IOException {
		// ��������Ҫ����Ϣ
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
	 * �õ�ǰ�û�����POST����POST����Ӧ�Ӵ˴�����
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
			result.setInfo("�����ֽ����ɹ�");
			result.setExtData(post.getBytes(headerList, "utf8"), byte[].class);
			break;
		case STR:
			result.setStatus(0);
			result.setInfo("�����ı����ɹ�");
			result.setExtData(post.getHtml(headerList, "utf8"), String.class);
			break;
		default:
			result.setStatus(0);
			result.setInfo("�����ı����ɹ�");
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
	 * �õ�ǰ�û�����get,����GET����Ӧ�Ӵ˴�����
	 * 
	 * @param url
	 *            ��ַ
	 * @param headerList
	 *            ͷ��Ϣ
	 * @param type
	 *            ��������
	 * @return
	 */
	public ResultInfo sendGet(String url, List<BasicHeader> headerList,
			RETURN_HTML_TYPE type) {
		ResultInfo result = new ResultInfo();
		Get get = new Get(url);
		switch (type) {
		case BYTE:
			result.setStatus(0);
			result.setInfo("�����ֽ����ɹ�");
			result.setExtData(get.getBytes(headerList), byte[].class);
			break;
		case STR:
			result.setStatus(0);
			result.setInfo("�����ı����ɹ�");
			result.setExtData(get.getHtml(headerList, "utf8"), String.class);
			break;
		default:
			result.setStatus(0);
			result.setInfo("�����ı����ɹ�");
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
	 * ��¼(����¼��ʽ����ʾ������������)
	 * 
	 * @param name
	 *            �û���
	 * @param pwd
	 *            ����
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
					Constants.sendMsg("������ʶ��������:" + code, true, true);
				} catch (Exception e) {
					Constants.sendException(e);
				}
				Thread.sleep(60000);
			}
			Constants.sendMsg("���Զ�ʶ�������벢��¼:" + code, true, true);
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
	 * ��¼(�˵�¼��ʽ��Ҫ��������)
	 * 
	 * @param name
	 *            �û���
	 * @param pwd
	 *            ����
	 * @param code
	 *            ������
	 * @return
	 */
	private ResultInfo login(String code) {
		ResultInfo result = new ResultInfo();
		Constants.sendMsg("��¼�û�����" + userName, true, true);
		// ��������Ҫ����Ϣ
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
		// ����
		result = sendPost(url, headerList, nvps, RETURN_HTML_TYPE.STR);
		if (result != null && result.getExtData() != null) {
			String html = result.getExtData().toString();
			Status status = HtmlJosnUtil.handleStatus(html); // ����JSONתΪ״̬����
			if (status != null) {
				// ����JSON���� ����״̬
				result.setStatus(status.getStatus());
				result.setInfo(status.getInfo());
			} else {
				result.setStatus(-100);
				result.setInfo("δ֪����");
			}
			result.setExtData(html, String.class);// ������չ����
		}
		return result;
	}

	/**
	 * ȡ���
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
	 * ������(�ڲ���)
	 * 
	 * @author shining
	 * 
	 */
	class Get {
		/**
		 * ��ַ
		 */
		private String uri;
		/**
		 * ��Ӧͷ�б�
		 */
		private Header[] requestHeader;

		public Get(String uri) {
			this.uri = uri;
		}

		/**
		 * GET������ҳ����
		 * 
		 * @param headerList
		 *            ���ӵ�header��Ϣ
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
				requestHeader = request.getAllHeaders();// ȡ��Ӧͷ��������requestHeader����Ҫʱ��ȡ
				byte[] bytearray = EntityUtils.toByteArray(request.getEntity());
				responseBody = new String(bytearray, charsetName);
				responseBody = FileUtil.unicodeDecode(responseBody);// ת��
			} catch (Exception e) {
				Constants.sendException(e);
				responseBody = null;
			} finally {
				httpget.abort();
			}
			return responseBody;
		}

		/**
		 * ��ȡԭʼ�ַ�����
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
				requestHeader = request.getAllHeaders();// ȡ��Ӧͷ��������requestHeader����Ҫʱ��ȡ
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
	 * ������
	 * 
	 * @author shining
	 * 
	 */
	class Post {
		/**
		 * ��ַ
		 */
		private String uri;
		/**
		 * �����б�
		 */
		private List<NameValuePair> nvps;
		/**
		 * ��Ӧͷ�б�
		 */
		private Header[] requestHeader;

		public Post(String uri, List<NameValuePair> nvps) {
			this.uri = uri;
			this.nvps = nvps;
		}

		/**
		 * Post������ҳ����
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
				requestHeader = response.getAllHeaders();// ȡ��Ӧͷ��������requestHeader����Ҫʱ��ȡ
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					InputStream instream = entity.getContent();
					// ��ѷ���
					ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
					int l;
					byte[] tmp = new byte[500000];
					while ((l = instream.read(tmp)) != -1) {
						bytestream.write(tmp, 0, l);
					}
					bytestream.close();
					instream.close();
					urlStr = new String(bytestream.toByteArray(), charsetName);
					urlStr = FileUtil.unicodeDecode(urlStr);// ת��
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
				requestHeader = response.getAllHeaders();// ȡ��Ӧͷ��������requestHeader����Ҫʱ��ȡ
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					InputStream instream = entity.getContent();
					// ��ѷ���
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
