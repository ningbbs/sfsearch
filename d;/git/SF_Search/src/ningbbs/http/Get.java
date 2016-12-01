package ningbbs.http;

import java.util.List;

import ningbbs.util.Constants;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;


public class Get {
	/**
	 * 地址
	 */
	private String uri;
	/**
	 * COOKIE
	 */
	private String cookie;
	/**
	 * 响应头列表
	 */
	private Header[] requestHeader;

	public Get(String uri, String cookie) {
		this.uri = uri;
		this.cookie = cookie;
	}

	/**
	 * GET返回网页内容
	 * 
	 * @param headerList
	 *            附加的header信息
	 * @return
	 */
	public String getHtml(List<BasicHeader> headerList, String charsetName) {
		String responseBody = "";
		HttpGet httpget = null;
		try {CloseableHttpClient http = HttpClients.createDefault();
		httpget = new HttpGet(uri);
		httpget.setConfig(UrlUtil.REQUEST_CONFIG);
		if (cookie != null && !cookie.equals("")) {
			if (headerList != null && headerList.size() > 0) {
				for (int i = 0; i < headerList.size(); i++) {
					httpget.addHeader(headerList.get(i));
				}
			}
			httpget.addHeader(new BasicHeader("Cookie", cookie));
		}
		CloseableHttpResponse request = http.execute(httpget);
			requestHeader = request.getAllHeaders();// 取响应头，保存在requestHeader供需要时获取
			byte[] bytearray = EntityUtils.toByteArray(request.getEntity());
			responseBody = new String(bytearray, charsetName);
			request.close();
		} catch (Exception e) {
			if(e.getMessage().indexOf("Read timed out")!=-1){
				Constants.sendMsg("连接超时", false, false);
			}else{
				e.printStackTrace();
			}
			responseBody = null;
		} finally {
			if(httpget!=null){
				httpget.abort();
			}
		}
		return responseBody;
	}
	
	public String getHtml(List<BasicHeader> headerList, String charsetName,HttpHost proxy) {
		String responseBody = "";
		HttpGet httpget = null;
		try {
			CloseableHttpClient http = HttpClients.createDefault();
			httpget = new HttpGet(uri);
			RequestConfig rc=RequestConfig.custom().setSocketTimeout(6000).setProxy(proxy)
					.setConnectTimeout(6000).build();
			httpget.setConfig(rc);
			if (cookie != null && !cookie.equals("")) {
			if (headerList != null && headerList.size() > 0) {
				for (int i = 0; i < headerList.size(); i++) {
					httpget.addHeader(headerList.get(i));
				}
			}
			httpget.addHeader(new BasicHeader("Cookie", cookie));
		}
		CloseableHttpResponse request = http.execute(httpget);
			requestHeader = request.getAllHeaders();// 取响应头，保存在requestHeader供需要时获取
			byte[] bytearray = EntityUtils.toByteArray(request.getEntity());
			responseBody = new String(bytearray, charsetName);
			request.close();
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			if(httpget!=null){
				httpget.abort();
			}
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
		httpget.setConfig(UrlUtil.REQUEST_CONFIG);
		if (cookie != null && !cookie.equals("")) {
			if (headerList != null && headerList.size() > 0) {
				for (int i = 0; i < headerList.size(); i++) {
					httpget.addHeader(headerList.get(i));
				}
			}
		}
		byte[] bytearray = null;
		try {
			CloseableHttpResponse request = http.execute(httpget);
			requestHeader = request.getAllHeaders();// 取响应头，保存在requestHeader供需要时获取
			bytearray = EntityUtils.toByteArray(request.getEntity());
			request.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpget.abort();
		}
		return bytearray;
	}

	public Header[] getRequestHeader() {
		return requestHeader;
	}
}
