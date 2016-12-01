package ningbbs.service;

import java.util.List;

import ningbbs.dao.DbDao;
import ningbbs.kd.SfSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ˳�����ݿ����
 * @author wm
 *
 */
@Scope("prototype")
@Service
public class SfDbService {
	@Autowired
	private DbDao sfDao;
	
	//�Ѷ����б�������ݿ�
	public void add_kd_DB(List<SfSearch> list){
		for(int k=0;k<list.size();k++){
			SfSearch sf=list.get(k);
			sfDao.addOlder(sf);
		}
	}
	
	
    public List<String> getSf_Wd(){
    	return sfDao.getSf_Wd();
    }
    public List<String> getKdnum(String date){
    	return sfDao.getKdnum(date);
    }
    
	/** 
     * ��ʽ������,�����׳��쳣 spring �Ż������ع� 
     * @param orders 
     */  
    @Transactional 
	public void testTransactional(){
    	for(int i=10000;i<10300;i++){
    		if(i>10200){
    			throw new RuntimeException();
    		}
			sfDao.updataIds("�� ��ˮ�к������˼�ԷС������Ӫҵ�㡿", i+"");
		}
	}
}
