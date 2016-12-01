package ningbbs.service;

import java.util.List;

import ningbbs.dao.DbDao;
import ningbbs.kd.SfSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 顺丰数据库服务
 * @author wm
 *
 */
@Scope("prototype")
@Service
public class SfDbService {
	@Autowired
	private DbDao sfDao;
	
	//把订单列表加入数据库
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
     * 测式事务处理,必需抛出异常 spring 才会帮事务回滚 
     * @param orders 
     */  
    @Transactional 
	public void testTransactional(){
    	for(int i=10000;i<10300;i++){
    		if(i>10200){
    			throw new RuntimeException();
    		}
			sfDao.updataIds("【 赤水市红军大道宜佳苑小区速运营业点】", i+"");
		}
	}
}
