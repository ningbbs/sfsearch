package ningbbs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import ningbbs.dydh.util.HtmlJosnUtil;
import ningbbs.kd.SfSearch;
import ningbbs.service.SfScanService;
import ningbbs.util.Constants;
import ningbbs.util.date.DateStyle;
import ningbbs.util.date.DateUtil;

import org.springframework.jdbc.core.JdbcTemplate;
/*@Repository*/
public class SfDaoImpl implements DbDao{
	/*@Autowired*/
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void addOlder(SfSearch sf) {
		String sql="insert into kd("
				+ "id,"
				+ "productName,"
				+ "productCode,"
				+ "recipientTime,"
				+ "scanDateTime,"
				+ "origin,"
				+ "originIds,"
				+ "destination,"
				+ "destinationIds,"
				+ "routes,"
				+ "limitTypeName,"
				+ "limitTypeCode,"
				+ "addtime) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String routes="";
		try {
			routes=ningbbs.dydh.util.HtmlJosnUtil.toJson(sf.getRoutes());
			System.out.println(routes);
			jdbcTemplate.update(sql,
					sf.getId(),
					sf.getProductName(),
					sf.getProductCode(),
					sf.getRecipientTime(),
					SfScanService.getScanTime(sf),
					sf.getOrigin(),
					sf.getOriginIds(),
					sf.getDestination(),
					sf.getDestinationIds(),
					routes,
					sf.getLimitTypeName(),
					sf.getLimitTypeCode(),
					DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS)
					);
		} catch (Exception e) {
			if(e.getMessage().indexOf("PRIMARY")==-1){
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SfSearch> getOlder(String id) {
		List<SfSearch> list = new ArrayList<>();
		try {
			//支持模糊查询
			String sql="select * FROM kd where id LIKE '%"+id+"%'";
			List rows=jdbcTemplate.queryForList(sql); 
			Iterator it = rows.iterator();  
			while(it.hasNext()) {
				try {
					SfSearch sf=new SfSearch();
					Map userMap = (Map) it.next();  
					sf.setId(userMap.get("id").toString());
					sf.setOrigin(userMap.get("origin").toString());
					sf.setDestination(userMap.get("destination").toString());
					sf.setRoutes(HtmlJosnUtil.handleRoutes(userMap.get("routes").toString()));
					sf.setLimitTypeCode(userMap.get("limitTypeCode").toString());
					list.add(sf);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void addWdInfo(SfSearch sf) {
		try {
			/*
			if(sf.getRoutes().size()>=2){
				String routes=sf.getRoutes().get(1).remark;
				Matcher m=Constant.p.matcher(routes);
				String info="";
				String code="";
				if(m.find()){
					info=m.group().replace("</font></a>已装车，准备发往", "");
				}
				m=Constant.code_p.matcher(routes);
				if(m.find()){
					code=m.group().replace("'","").replace("virtual-address-code=", "");
				}
				String sql="insert into sf_wd(info,code) values(?,?)";
				jdbcTemplate.update(sql,info,code);
			}
			*/
			for(int i=0;i<sf.getRoutes().size();i++){
				String info="";
				String r=sf.getRoutes().get(i).toString();
				/*
				Matcher m1=Constant.code_p.matcher(r);
				while(m.find() && m1.find()){
					info=m.group();
					code=m1.group();
					code=code.replace("'","").replace("virtual-address-code=", "");
					String sql="insert into sf_wd(info,code) values(?,?)";
					jdbcTemplate.update(sql,info,code);
					System.out.println("路由:"+code+"\t"+info);
				}
				*/
				String[] arr=r.split("】");
				for(int j=0;j<arr.length;j++){
					String str=arr[j]+"】";
					Matcher m=Constants.p1.matcher(str);
					if(m.find()){
						try {
							info=m.group();
							String sql="insert into sf_wd(info,code) values(?,?)";
							jdbcTemplate.update(sql,info,"");
							System.out.println("路由:"+info);
						} catch (Exception e) {
							if(e.getMessage().indexOf("PRIMARY")==-1){
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if(e.getMessage().indexOf("PRIMARY")==-1){
				e.printStackTrace();
			}
		}
	}
	@Override
	public void addShi(String info,String shi){
		String sql="UPDATE sf_wd SET shi = '"+shi+"' WHERE info = '"+info+"' ";
		jdbcTemplate.update(sql);
	}
	@Override
	public void addQu(String info,String qu){
		String sql="UPDATE sf_wd SET qu = '"+qu+"' WHERE info = '"+info+"' ";
		jdbcTemplate.update(sql);
	}
	@Override
	public void addSheng(String info,String sheng){
		String sql="UPDATE sf_wd SET sheng = '"+sheng+"' WHERE info = '"+info+"' ";
		jdbcTemplate.update(sql);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getWdIds(String info) {
		String rs="";
		try {
			String sql="select * FROM sf_wd where info = '"+info+"'";
			List rows=jdbcTemplate.queryForList(sql); 
			Iterator it = rows.iterator();  
			while(it.hasNext()) {  
				Map userMap = (Map) it.next();  
				String sheng=userMap.get("sheng").toString();
				String shi=userMap.get("shi").toString();
				String qu=userMap.get("qu").toString();
				rs=sheng+","+shi+","+qu;
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	public void updataIds(String info, String ids) {
		String sql="UPDATE sf_wd SET ids = '"+ids+"' WHERE info = '"+info+"' ";
		jdbcTemplate.update(sql);
	}

	@Override
	public List<String> getSf_Wd() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getKdnum(String date) {
		// TODO Auto-generated method stub
		return null;
	}
}
