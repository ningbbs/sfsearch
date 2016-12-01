package ningbbs.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;


public class Post {
	/**
	 * 地址
	 */
	private String uri;
	/**
	 * COOKIE
	 */
	private String cookie;
	/**
	 * 参数列表
	 */
	private List<NameValuePair> nvps;
	/**
	 * 响应头列表
	 */
	private Header[] requestHeader;

	public Post(String uri, String cookie, List<NameValuePair> nvps) {
		this.uri = uri;
		this.cookie = cookie;
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
		HttpPost httpost=null;
		CloseableHttpClient http=null;
		try {
			http = HttpClients.createDefault();
		httpost = new HttpPost(uri);
		httpost.setConfig(UrlUtil.REQUEST_CONFIG);
		if (cookie != null && !cookie.equals("")) {
			if (headerList != null && headerList.size() > 0) {
				for (int i = 0; i < headerList.size(); i++) {
					httpost.addHeader(headerList.get(i));
				}
			}
			httpost.addHeader(new BasicHeader("Cookie", cookie));
		}
		
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
			}
			http.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(http!=null)
					http.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} finally {
			if(httpost!=null){
				httpost.abort();
			}
		}
		return urlStr;
	}

	public Header[] getRequestHeader() {
		return requestHeader;
	}
}
