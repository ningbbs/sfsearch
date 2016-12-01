package ningbbs.data.info;

import ningbbs.util.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * ������ҳ������
 * 
 * @author shining
 *
 */
public class HtmlJsoupUtil {

	/**
	 * ������ҳ����������
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
