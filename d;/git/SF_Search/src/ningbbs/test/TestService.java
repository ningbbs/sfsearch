package ningbbs.test;

import ningbbs.kd.SfSearch;
import ningbbs.service.ServiceController;
import ningbbs.util.Constants;

import org.junit.Test;

public class TestService {
//	static SfScanService scan;
//	static SfDbService dao;
//	static{
//		scan=new SfScanService();
//		scan.initParams("611345", 203723);
//		dao= Constants.ctx.getBean(SfDbService.class);
//	}
//	
//	@Test
//	public  void test1(){
//		List<SfSearch> list=scan.scan(scan);
//		for(SfSearch sf:list){
//			System.out.println(sf);
//		}
//		dao.add_kd_DB(list);
//	}
	static ServiceController serviceController;
	static{
		serviceController=Constants.ctx.getBean(ServiceController.class);
		serviceController.init();
		
	}
	public static void main(String[] args) {
		test1();
	}
	public static void test1(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						SfSearch sf=serviceController.send();
						System.err.println(sf);
						Thread.sleep(6000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0;i<100000;i++){
					try {
						serviceController.scan();
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
