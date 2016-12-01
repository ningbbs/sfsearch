package ningbbs.service;

import javax.swing.JLabel;

import ningbbs.dao.DbDao;
import ningbbs.data.info.User;
import ningbbs.kd.SfSearch;
import ningbbs.util.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 服务控制类
 * @author wm
 *
 */
@Controller

public class ServiceController {
	@Autowired
	protected SfScanService scanService;
	@Autowired
	protected SfDbService dbService;
	@Autowired
	protected DbDao dao;
	protected User user; //用户
	
	public void init(){
		scanService.initParams("611395", 0);
		user=new User("mrwjbhj", "bctv041");
		user.login(new JLabel());
	}
	/**
	 * 扫描(生产者模式)
	 * @return
	 * @throws Exception
	 */
	public void scan() throws Exception{
		new Scan().scan();
	}
	/**
	 * 发送(消费者模式)
	 * @throws Exception
	 */
	public SfSearch send() throws Exception{
		return new Send().send();
	}
	/**
	 * 发送线程
	 * @author ningbbs
	 *
	 */
	class Send extends Thread{
		public SfSearch send() throws Exception{
			return Constants.STORAGE.consume(user);
		}
	}
	/**
	 * 扫描线程
	 * @author ningbbs
	 *
	 */
	class Scan extends Thread{
		public void scan() throws Exception{
			Constants.STORAGE.produce(scanService);
		}
	}
}
