package ningbbs.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import ningbbs.gui.MainGui;
import ningbbs.storage.Storage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.config.RequestConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Constants {
	public static ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
	public static int SEND_HOUR1=12;//����Сʱ���� 
	public static int SEND_HOUR2=12;//����Сʱ���� 
	public static int sendSleepTime = 6000;// ����˯��
	public static int thread=3;//�����߳����� 
	public static int splistSize=2;//����ֶ�����
	public static int threadSize=50;
	public static int scanSleepTime=100;//ÿ���߳��е�ÿ��ɨ����
	public static int roundNum=5;//�ֻ���
	public static String typeCodeStr="T801,T104";
	public static String[] TypeCodeArr=new String[]{"T801","T104","T4","null"};//���������б�
	public static String ROOT_URL="www.diyidanhao.net";
	public static Log log = LogFactory.getFactory().getInstance(Constants.class);
	public static String KeyIdStr;
	public static Pattern p=Pattern.compile("��.*?��</font></a>��װ����׼������");
	public static Pattern p1=Pattern.compile("��.*?����|��.*?�㡿");
	public static Pattern p2=Pattern.compile("��.*?��");
	public static Pattern code_p=Pattern.compile("virtual-address-code='.*?'");
	public static Storage STORAGE = new Storage(); //�����������ֿ߲�
	private static Constants constants;
	
	/**
	 * ��ݱ�ʶ
	 */
	public static int YEAR = 2016;
	/**
	 * ʱ���ʽ��
	 */
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * ʱ���ʽ��
	 */
	public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * ʱ���ʽ��
	 */
	public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * ���糬ʱ����
	 */
	public static int timeout=15000;
	/**
	 * HTTP��ʱ����
	 */
	public static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setSocketTimeout(timeout)
			.setConnectTimeout(timeout).build();
	public static String configFile="config.properties";
	/**
	 * ��ȡ��ҳ�ķ�������
	 * 
	 * @author shining
	 *
	 */
	public static enum RETURN_HTML_TYPE {
		STR/* �ַ��� */, BYTE/* �ֽ� */
	};

	/**
	 * �û�����
	 * 
	 * @author shining
	 *
	 */
	public static enum USER_TYPE {
		BUY/* ��� */, SELL/* ���� */
	}

	/**
	 * ����״̬
	 * 
	 * @author shining
	 *
	 */
	public static enum SEND_STATE {
		NEW_ORDER/* �µ� */, OUT_TIME/* ����ʱ�� */, ERR/* ���� */, SALE/* ������ */, SOLD/* �۳� */,
	};
	
	private Constants(){
	}
	
	public static Constants getConstant(){
		if(constants==null){
			constants=new Constants();
		}
		return constants;
	}
	/**
	 * ��ʾ��Ϣ
	 * 
	 * @param msg
	 *            ��Ϣ
	 * @param sendToGui
	 *            �Ƿ���GUI��ʾ
	 * @param sendToLog
	 *            �Ƿ����LOG
	 */
	public static void sendMsg(String msg, boolean sendToGui, boolean sendToLog) {
		msg = Constants.sdf2.format(new Date()) + ":    " + msg;
		if (sendToGui) {
			if (!msg.endsWith("\n")) {
				msg += "\n";
			}
			if(MainGui.textArea==null){return;}
			MainGui.textArea.append(msg);
			MainGui.textArea.setCaretPosition(MainGui.textArea.getText().length());//�L�ӵ��׶�
			if(MainGui.textArea.getText().length()>=50000){
				MainGui.textArea.setText("");//���
			}
		}
		if (sendToLog) {
			addLog(msg.replaceAll("\n", ""));
		}
		System.out.println(msg);
	}
	
	/**
	 * ������־
	 * 
	 * @param obj
	 */
	public static void addLog(Object obj) {
		Constants.log.info(obj);
	}

	public static void sendException(Exception e) {
		e.printStackTrace();
		Constants.log.error("ERR", e);
	}

}
