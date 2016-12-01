package ningbbs.data.info;

import ningbbs.util.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 分析网页工具类
 * 
 * @author shining
 *
 */
public class HtmlJsoupUtil {

	/**
	 * 分析网页内容里的余额
	 * 
	 * @param html
	 * @return
	 */
	public static double getMoney(String html) {
		double self = 0;
		Document doc = Jsoup.parse(html);
		Element e = doc.select("a[target=\"_self\"]").first();
		try {
			self = Double.parseDouble(e.text());
		} catch (Exception e1) {
			Constants.sendException(e1);
		}
		return self;
	}

}
