package ningbbs.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ningbbs.kd.SfSearch;
import ningbbs.kd.SfSearchUtil;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository
public class SfDaoImpl1 implements DbDao{
	@Autowired
	SqlSessionTemplate session;

	@Override
	public void addOlder(SfSearch sf) {
		String statement = "ningbbs.mapping.sfSearchMapper.addOlder";//映射sql的标识字符串
        //执行查询返回一个唯一user对象的sql
		session.insert(statement, SfSearchUtil.toMap(sf));
	}

	@Override
	public List<SfSearch> getOlder(String id) {
		String statement = "ningbbs.mapping.sfSearchMapper.getSfSearch";//映射sql的标识字符串
        //执行查询返回一个唯一user对象的sql
        List<SfSearch> list=session.selectList(statement, id);
		return list;
	}

	@Override
	public void addWdInfo(SfSearch sf) {
		List<String> rList=SfSearchUtil.toRoutesList(sf);
		String statement = "ningbbs.mapping.sfSearchMapper.getSfSearch";//映射sql的标识字符串
		for(int i=0;i<rList.size();i++){
			session.insert(statement, rList.get(i));
		}
	}

	@Override
	public void addShi(String info, String shi) {
		Map<String,Object> param=new HashMap<>();
		param.put("info", info);
		param.put("shi", shi);
		String statement = "ningbbs.mapping.sfSearchMapper.addShi";//映射sql的标识字符串
		session.update(statement, param);
	}

	@Override
	public void addQu(String info, String qu) {
		Map<String,Object> param=new HashMap<>();
		param.put("info", info);
		param.put("qu", qu);
		String statement = "ningbbs.mapping.sfSearchMapper.addQu";//映射sql的标识字符串
		session.update(statement, param);
	}

	@Override
	public void addSheng(String info, String sheng) {
		Map<String,Object> param=new HashMap<>();
		param.put("info", info);
		param.put("sheng", sheng);
		String statement = "ningbbs.mapping.sfSearchMapper.addSheng";//映射sql的标识字符串
		session.update(statement, param);
	}

	@Override
	public void updataIds(String info, String ids) {
		Map<String,Object> param=new HashMap<>();
		param.put("info", info);
		param.put("ids", ids);
		String statement = "ningbbs.mapping.sfSearchMapper.updataIds";//映射sql的标识字符串
		session.update(statement, param);

	}

	@Override
	public String getWdIds(String info) {
		String rs=null;
		String statement = "ningbbs.mapping.sfSearchMapper.getWdIds";//映射sql的标识字符串
		rs=session.selectOne(statement, info);
		return rs;
	}

	@Override
	public List<String> getSf_Wd() {
		List<String> list=new ArrayList<>();
		String statement = "ningbbs.mapping.sfSearchMapper.search_sf_wd";//映射sql的标识字符串
		list=session.selectList(statement);
		return list;
	}
	
	@Override
	public List<String> getKdnum(String date) {
		List<String> list=new ArrayList<>();
		String statement = "ningbbs.mapping.sfSearchMapper.getKdnum";//映射sql的标识字符串
		list=session.selectList(statement,date);
		return list;
	}
	
	public SqlSessionTemplate getSession() {
		return session;
	}

	public void setSession(SqlSessionTemplate session) {
		this.session = session;
	}
}
