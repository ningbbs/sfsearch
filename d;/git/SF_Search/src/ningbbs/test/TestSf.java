package ningbbs.test;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;

import ningbbs.dydh.util.CityInfo;
import ningbbs.dydh.util.CityUtil;
import ningbbs.util.Constants;

public class TestSf {

	// @Test
	public void test1() throws Exception {
		int id = CityUtil.searchQu_ID(2, "东城区", null);
		System.out.println("test1	" + id);
	}

	// @Test
	public void test2() throws Exception {
		int id = CityUtil.searchShi_ID("北京");
		System.out.println("test2	" + id);
	}

	// @Test
	public void test3() throws Exception {
		CityInfo info = CityUtil.searchQu_Info(3516);
		System.out.println("test3	" + info);
	}

	// @Test
	public void test4() throws Exception {
		CityInfo info = CityUtil.searchShi_Info(52);
		System.out.println("test4	" + info);
	}

	// @Test
	public void test5() throws Exception {
		for (int i = 0; i < 100; i++) {
			CityInfo info = CityUtil.searchRandomQu(52, "区");
			System.out.println("test4	" + info);
		}
	}

	// @Test
	public void test6() {
		for (int i = 0; i < 100; i++) {
			int r = new Random().nextInt(10);
			System.out.println(r);
		}
	}


	// @Test
	public void testRex() {
		String s = "";
		String[] arr = s.split("】");
		for (int i = 0; i < arr.length; i++) {
			String str = arr[i] + "】";
			System.err.println(str);
			Matcher m = Constants.p1.matcher(str);
			if (m.find())
				System.out.println(m.group());
		}
	}

	public static void main(String[] args) throws InterruptedException {
		int s=5;
		int n=999999/s;
		for(int i=0;i<s;i++){
			int start=(i*n);
			int end=(i*n)+n;
			System.out.println(start+" "+end);
		}
	}

	public static void testGet() throws InterruptedException {
		 final ScheduledExecutorService pool=Executors.newSingleThreadScheduledExecutor();  
	        for(int i=0;i<4;i++){  
	        	pool.submit(new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println("a");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				})); 
	        	System.out.println(i);
	        }  
	        Thread.sleep(30000);
	        new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					 for(int i=0;i<4;i++){  
				        	pool.submit(new Thread(new Runnable() {
								@Override
								public void run() {
									System.out.println("a");
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							})); 
				        	System.out.println(i);
				        }  
				}
			}).start();
	}

}
