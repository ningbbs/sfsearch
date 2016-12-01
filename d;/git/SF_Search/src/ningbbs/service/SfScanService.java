package ningbbs.service;

import java.util.ArrayList;
import java.util.List;

import ningbbs.dao.DbDao;
import ningbbs.kd.SfSearch;
import ningbbs.orc.service.OrcService;
import ningbbs.util.Constants;
import ningbbs.util.date.DateStyle;
import ningbbs.util.date.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 顺风搜索 服务 
 * @author wm
 *
 */
@Scope("prototype")
@Service
public class SfScanService {
	public static OrcService orc=new OrcService();
	public static final int numSize = 20;
	
	private String Send_Older="606640";//开始段
	private int Send_Index=0;//结束单号
	private int ORDER_SIZE=20;//处理个数
	@Autowired
	private DbDao sfDao;

	
	public DbDao getSfDao() {
		return sfDao;
	}

	public void setSfDao(DbDao sfDao) {
		this.sfDao = sfDao;
	}

	/**
	 * 初始化参数
	 * @param Send_Older 开始段
	 * @param Send_Index 开始索引
	 */
	public void initParams(String Send_Older,int Send_Index){
		this.Send_Older=Send_Older;
		this.Send_Index=Send_Index;
	}

	/**
	 * 取扫描 时间
	 * @param sf
	 * @return
	 */
	public static String getScanTime(SfSearch sf){
		String time=null;
		if(sf.getRecipientTime()!=null && !sf.getRecipientTime().equals("")){
			time=sf.getRecipientTime();
			return time;
		}
		if(sf.getRoutes()!=null && sf.getRoutes().size()>=1){
			//对比路由前端 和后端,判断哪个是收件 时间(有时发件时间在最前,有时在最后)
			long t1= DateUtil.StringToDate(sf.getRoutes().get(0).getScanDateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS).getTime();
			long t2= DateUtil.StringToDate(sf.getRoutes().get(sf.getRoutes().size()-1).getScanDateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS).getTime();
			if(t1<t2){
				time=sf.getRoutes().get(0).getScanDateTime();
			}else{
				time=sf.getRoutes().get(sf.getRoutes().size()-1).getScanDateTime();
			}
		}
		return time;
	}
	/**
	 * 扫描,返回扫描结果
	 * @return
	 */
	public List<SfSearch> scan(SfScanService ss){
		Constants.sendMsg(Send_Older+" : "+ Send_Index,false,false);
		List<SfSearch> list=null;
		List<String> orders=formatOlrdeList(Send_Older, Send_Index); //取20个订单
		try {
			list=conversionOrders(orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setSend_Index(Send_Index+ORDER_SIZE);//增加索引值得
		return list==null ? new ArrayList<SfSearch>():list;
	}
	
	/**
	 * 把订单号数组转换为SfSearch数组
	 * @param orders 单号列表
	 * @return
	 * @throws Exception
	 */
	private List<SfSearch> conversionOrders(List<String> orders) throws Exception {
		List<SfSearch> list=new ArrayList<>();
		// 分割处理
		int index = 0;
		for (int i = 0; i < orders.size();) {
			index += numSize;
			index = index > orders.size() ? orders.size() : index;
			List<String> temp = orders.subList(i, index);
			// ------构造并处理----------------
			String nums = "";
			for (String o : temp) {
				nums = nums + "," + o;
			}
			if (orders.size() >= 1) {
				nums = nums.substring(1);
			}
			//String url = SfService.url.replace("%nums", nums);// 构造URL
			//String html = getSfInfo(url);// 取ＪＳＯＮ
			String html=orc.repeatQuery(nums);
			list = test_To_List(html);
			// -----------------------------
			i += numSize;
		}
		return list;
	}

	/**
	 * 转换JSON
	 * @param html
	 * @return
	 */
	private List<SfSearch> test_To_List(String html) {
		List<SfSearch> ps = null;
		Gson gson = new Gson();
		try {
			// json转换对象数组
			ps = gson.fromJson(html, new TypeToken<List<SfSearch>>() {
			}.getType());
			// 打印显示结果
			for (SfSearch sf : ps) {
				if (sf.getRoutes().size() >= 1) {
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return ps;
	}

	/**
	 * 从一个单号开始,构建20个单号列表
	 * @param startStr 开始段
	 * @param orderNum 开始单号
	 * @return
	 */
	private List<String> formatOlrdeList(String startStr,int orderNum){
		List<String> orders=new ArrayList<>();
		for(int j=0;j<ORDER_SIZE;j++){
			int n=orderNum+j; 
			orders.add(formatOlderNum(startStr, n));
		}
		return orders;
	}

	/**
	 * 格式化单号,格式化后为12位
	 * @param startStr 单号开始段
	 * @param olderNum 单号
	 * @return
	 */
	private String formatOlderNum(String startStr,int olderNum){
		int olderLength=12-startStr.length();//取要格式化的长度
		return startStr+String.format("%0"+olderLength+"d", olderNum);
	}
	public String getSend_Older() {
		return Send_Older;
	}
	public void setSend_Older(String send_Older) {
		Send_Older = send_Older;
	}
	public int getSend_Index() {
		return Send_Index;
	}
	public void setSend_Index(int send_Index) {
		Send_Index = send_Index;
	}
}
