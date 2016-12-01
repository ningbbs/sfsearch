package ningbbs.dydh.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ningbbs.kd.SfSearch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * 第一单网页解析工具
 * 
 * @author shining
 *
 */
public class HtmlJosnUtil {

	/**
	 * 分析第一单的状态，主要是发送和登录时返回的JSON信息
	 * 
	 * @param json
	 * @return
	 */
	public static Status handleStatus(String json) {
		Status status = null;
		Gson gson = new Gson();
		try {
			status = gson.fromJson(json, new TypeToken<Status>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	public static List<SfSearch.Routes> handleRoutes(String json) {
		List<SfSearch.Routes> list=new ArrayList<>();
		Gson gson = new Gson();
		try {
			list = gson.fromJson(json, new TypeToken<List<SfSearch.Routes>>() {
			}.getType());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return list;
	}
	public static String toJson(Object obj){
		String str = null;
		Gson gson = new Gson();
		try {
			str =gson.toJson(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static Map<String, String> handleCity_Sheng(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<Map<String, String>>() {
		}.getType());
	}

	public static Map<String, Map<String, String>> handleCity_Shi_Or_Qu(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<Map<String, Map<String, String>>>() {
		}.getType());
	}

	/**
	 * 第一单状态JSON映射类(内部静态类)
	 * 
	 * @author ningbbs
	 *
	 */
	public static class Status {
		/**
		 * 文本消息
		 */
		private String info;
		/**
		 * 状态码
		 */
		private int status;

		/**
		 * 取提示信息文本
		 * 
		 * @return
		 */
		public String getInfo() {
			return info;
		}

		/**
		 * 设置提示信息文本
		 * 
		 * @param info
		 */
		public void setInfo(String info) {
			this.info = info;
		}

		/**
		 * 取状态码
		 * 
		 * @return
		 */
		public int getStatus() {
			return status;
		}

		/**
		 * 设置状态码
		 * 
		 * @param status
		 */
		public void setStatus(int status) {
			this.status = status;
		}
	}
}
