package ningbbs.dydh.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ningbbs.kd.SfSearch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * ��һ����ҳ��������
 * 
 * @author shining
 *
 */
public class HtmlJosnUtil {

	/**
	 * ������һ����״̬����Ҫ�Ƿ��ͺ͵�¼ʱ���ص�JSON��Ϣ
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
	 * ��һ��״̬JSONӳ����(�ڲ���̬��)
	 * 
	 * @author ningbbs
	 *
	 */
	public static class Status {
		/**
		 * �ı���Ϣ
		 */
		private String info;
		/**
		 * ״̬��
		 */
		private int status;

		/**
		 * ȡ��ʾ��Ϣ�ı�
		 * 
		 * @return
		 */
		public String getInfo() {
			return info;
		}

		/**
		 * ������ʾ��Ϣ�ı�
		 * 
		 * @param info
		 */
		public void setInfo(String info) {
			this.info = info;
		}

		/**
		 * ȡ״̬��
		 * 
		 * @return
		 */
		public int getStatus() {
			return status;
		}

		/**
		 * ����״̬��
		 * 
		 * @param status
		 */
		public void setStatus(int status) {
			this.status = status;
		}
	}
}
