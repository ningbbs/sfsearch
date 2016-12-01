package ningbbs.dydh.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import ningbbs.util.Constants;
import ningbbs.util.date.DateUtil;
//��ʱ���ķ���Сʱ����
public class SendTimeUtil implements Runnable{
	private int sleep=60000;
	private List<String> timeList=new ArrayList<>();
	public SendTimeUtil(int sleep){
		this.sleep=sleep;
		loadProperties();
	}
	
	private void loadProperties(){
		try {
			Properties prop = new Properties();// ���Լ��϶���
			FileInputStream fis = new FileInputStream("time.properties");// �����ļ���
			prop.load(fis);// �������ļ���װ�ص�Properties������
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
				//��ʽ [12,18]    ˵�� [,ͬ�Ǽ��Сʱ,��ؼ��Сʱ]
				String timeStr=timeList.get(hour);//ȡ��ӦСʱ�Ĳ���
				String[] params=timeStr.split(",");
				if(params.length>=2){
					Constants.SEND_HOUR1=Integer.parseInt(params[0]);
					Constants.SEND_HOUR2=Integer.parseInt(params[1]);
					Constants.sendMsg(hour+":�ı�Сʱ����:"+timeStr, true, true);
				}
				Thread.sleep(sleep);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
