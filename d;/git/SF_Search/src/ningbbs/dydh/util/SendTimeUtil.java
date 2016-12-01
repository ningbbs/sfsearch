package ningbbs.dydh.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import ningbbs.util.Constants;
import ningbbs.util.date.DateUtil;
//定时更改发送小时参数
public class SendTimeUtil implements Runnable{
	private int sleep=60000;
	private List<String> timeList=new ArrayList<>();
	public SendTimeUtil(int sleep){
		this.sleep=sleep;
		loadProperties();
	}
	
	private void loadProperties(){
		try {
			Properties prop = new Properties();// 属性集合对象
			FileInputStream fis = new FileInputStream("time.properties");// 属性文件流
			prop.load(fis);// 将属性文件流装载到Properties对象中
			for(int i=0;i<=23;i++){
				timeList.add(prop.getProperty("hour"+i));
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				int hour=DateUtil.getHour(new Date());
				//格式 [12,18]    说明 [,同城间隔小时,异地间隔小时]
				String timeStr=timeList.get(hour);//取对应小时的参数
				String[] params=timeStr.split(",");
				if(params.length>=2){
					Constants.SEND_HOUR1=Integer.parseInt(params[0]);
					Constants.SEND_HOUR2=Integer.parseInt(params[1]);
					Constants.sendMsg(hour+":改变小时参数:"+timeStr, true, true);
				}
				Thread.sleep(sleep);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
