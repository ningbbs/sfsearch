package ningbbs.service;

import javax.swing.JLabel;

import ningbbs.dao.DbDao;
import ningbbs.data.info.User;
import ningbbs.kd.SfSearch;
import ningbbs.util.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * ���������
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
	protected User user; //�û�
	
	public void init(){
		scanService.initParams("611395", 0);
		user=new User("mrwjbhj", "bctv041");
		user.login(new JLabel());
	}
	/**
	 * ɨ��(������ģʽ)
	 * @return
	 * @throws Exception
	 */
	public void scan() throws Exception{
		new Scan().scan();
	}
	/**
	 * ����(������ģʽ)
	 * @throws Exception
	 */
	public SfSearch send() throws Exception{
		return new Send().send();
	}
	/**
	 * �����߳�
	 * @author ningbbs
	 *
	 */
	class Send extends Thread{
		public SfSearch send() throws Exception{
			return Constants.STORAGE.consume(user);
		}
	}
	/**
	 * ɨ���߳�
	 * @author ningbbs
	 *
	 */
	class Scan extends Thread{
		public void scan() throws Exception{
			Constants.STORAGE.produce(scanService);
		}
	}
}
