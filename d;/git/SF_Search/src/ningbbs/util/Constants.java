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
	public static int SEND_HOUR1=12;//发送小时数量 
	public static int SEND_HOUR2=12;//发送小时数量 
	public static int sendSleepTime = 6000;// 发送睡眠
	public static int thread=3;//发着线程数量 
	public static int splistSize=2;//区间分段数量
	public static int threadSize=50;
	public static int scanSleepTime=100;//每个线程中的每次扫描间隔
	public static int roundNum=5;//轮回数
	public static String typeCodeStr="T801,T104";
	public static String[] TypeCodeArr=new String[]{"T801","T104","T4","null"};//过滤类型列表
	public static String ROOT_URL="www.diyidanhao.net";
	public static Log log = LogFactory.getFactory().getInstance(Constants.class);
	public static String KeyIdStr;
	public static Pattern p=Pattern.compile("【.*?】</font></a>已装车，准备发往");
	public static Pattern p1=Pattern.compile("【.*?部】|【.*?点】");
	public static Pattern p2=Pattern.compile("【.*?】");
	public static Pattern code_p=Pattern.compile("virtual-address-code='.*?'");
	public static Storage STORAGE = new Storage(); //生产者消费者仓库
	private static Constants constants;
	
	/**
	 * 年份标识
	 */
	public static int YEAR = 2016;
	/**
	 * 时间格式化
	 */
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 时间格式化
	 */
	public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * 时间格式化
	 */
	public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 网络超时设置
	 */
	public static int timeout=15000;
	/**
	 * HTTP超时设置
	 */
	public static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setSocketTimeout(timeout)
			.setConnectTimeout(timeout).build();
	public static String configFile="config.properties";
	/**
	 * 读取网页的返回类型
	 * 
	 * @author shining
	 *
	 */
	public static enum RETURN_HTML_TYPE {
		STR/* 字符串 */, BYTE/* 字节 */
	};

	/**
	 * 用户类型
	 * 
	 * @author shining
	 *
	 */
	public static enum USER_TYPE {
		BUY/* 买家 */, SELL/* 卖家 */
	}

	/**
	 * 发送状态
	 * 
	 * @author shining
	 *
	 */
	public static enum SEND_STATE {
		NEW_ORDER/* 新单 */, OUT_TIME/* 超级时单 */, ERR/* 错误单 */, SALE/* 出售中 */, SOLD/* 售出 */,
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
	 * 显示消息
	 * 
	 * @param msg
	 *            消息
	 * @param sendToGui
	 *            是否在GUI显示
	 * @param sendToLog
	 *            是否加入LOG
	 */
	public static void sendMsg(String msg, boolean sendToGui, boolean sendToLog) {
		msg = Constants.sdf2.format(new Date()) + ":    " + msg;
		if (sendToGui) {
			if (!msg.endsWith("\n")) {
				msg += "\n";
			}
			if(MainGui.textArea==null){return;}
			MainGui.textArea.append(msg);
			MainGui.textArea.setCaretPosition(MainGui.textArea.getText().length());//滾動到底端
			if(MainGui.textArea.getText().length()>=50000){
				MainGui.textArea.setText("");//清空
			}
		}
		if (sendToLog) {
			addLog(msg.replaceAll("\n", ""));
		}
		System.out.println(msg);
	}
	
	/**
	 * 加入日志
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
