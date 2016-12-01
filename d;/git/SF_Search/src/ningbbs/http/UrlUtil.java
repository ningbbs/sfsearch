package ningbbs.http;

import org.apache.http.client.config.RequestConfig;


public class UrlUtil {
	public static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setSocketTimeout(21000)
			.setConnectTimeout(21000).build();

	
}
