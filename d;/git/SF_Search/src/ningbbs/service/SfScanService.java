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
 * ˳������ ���� 
 * @author wm
 *
 */
@Scope("prototype")
@Service
public class SfScanService {
	public static OrcService orc=new OrcService();
	public static final int numSize = 20;
	
	private String Send_Older="606640";//��ʼ��
	private int Send_Index=0;//��������
	private int ORDER_SIZE=20;//�������
	@Autowired
	private DbDao sfDao;

	
	public DbDao getSfDao() {
		return sfDao;
	}

	public void setSfDao(DbDao sfDao) {
		this.sfDao = sfDao;
	}

	/**
	 * ��ʼ������
	 * @param Send_Older ��ʼ��
	 * @param Send_Index ��ʼ����
	 */
	public void initParams(String Send_Older,int Send_Index){
		this.Send_Older=Send_Older;
		this.Send_Index=Send_Index;
	}

	/**
	 * ȡɨ�� ʱ��
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
			//�Ա�·��ǰ�� �ͺ��,�ж��ĸ����ռ� ʱ��(��ʱ����ʱ������ǰ,��ʱ�����)
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
	 * ɨ��,����ɨ����
	 * @return
	 */
	public List<SfSearch> scan(SfScanService ss){
		Constants.sendMsg(Send_Older+" : "+ Send_Index,false,false);
		List<SfSearch> list=null;
		List<String> orders=formatOlrdeList(Send_Older, Send_Index); //ȡ20������
		try {
			list=conversionOrders(orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setSend_Index(Send_Index+ORDER_SIZE);//��������ֵ��
		return list==null ? new ArrayList<SfSearch>():list;
	}
	
	/**
	 * �Ѷ���������ת��ΪSfSearch����
	 * @param orders �����б�
	 * @return
	 * @throws Exception
	 */
	private List<SfSearch> conversionOrders(List<String> orders) throws Exception {
		List<SfSearch> list=new ArrayList<>();
		// �ָ��
		int index = 0;
		for (int i = 0; i < orders.size();) {
			index += numSize;
			index = index > orders.size() ? orders.size() : index;
			List<String> temp = orders.subList(i, index);
			// ------���첢����----------------
			String nums = "";
			for (String o : temp) {
				nums = nums + "," + o;
			}
			if (orders.size() >= 1) {
				nums = nums.substring(1);
			}
			//String url = SfService.url.replace("%nums", nums);// ����URL
			//String html = getSfInfo(url);// ȡ�ʣӣϣ�
			String html=orc.repeatQuery(nums);
			list = test_To_List(html);
			// -----------------------------
			i += numSize;
		}
		return list;
	}

	/**
	 * ת��JSON
	 * @param html
	 * @return
	 */
	private List<SfSearch> test_To_List(String html) {
		List<SfSearch> ps = null;
		Gson gson = new Gson();
		try {
			// jsonת����������
			ps = gson.fromJson(html, new TypeToken<List<SfSearch>>() {
			}.getType());
			// ��ӡ��ʾ���
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
	 * ��һ�����ſ�ʼ,����20�������б�
	 * @param startStr ��ʼ��
	 * @param orderNum ��ʼ����
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
	 * ��ʽ������,��ʽ����Ϊ12λ
	 * @param startStr ���ſ�ʼ��
	 * @param olderNum ����
	 * @return
	 */
	private String formatOlderNum(String startStr,int olderNum){
		int olderLength=12-startStr.length();//ȡҪ��ʽ���ĳ���
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
