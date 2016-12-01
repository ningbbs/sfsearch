package ningbbs.dydh.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CityUtil {
	/**
	 * ������Ϣ�����ı�(javascript��JSON������ʽ)
	 */
	private String CITY_ARR_FILE = "cityArr.txt";
	private String jsonAllStr;
	public Map<String, String> sheng = new HashMap<>();
	public Map<String, Map<String, String>> shi = new HashMap<>();
	public Map<String, Map<String, String>> qu = new HashMap<>();
	private static CityUtil cityUtil;
	
	private CityUtil() {
		try {
			jsonAllStr = readHtml(CITY_ARR_FILE);
			loadCity_Sheng();
			loadCity_Shi();
			loadCity_qu();
			System.out.println("������["+sheng.size()+"]��ʡ");
			System.out.println("������["+shi.size()+"]����");
			System.out.println("������["+qu.size()+"]����");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static CityUtil getInstance() {
		if (cityUtil == null) {
			cityUtil = new CityUtil();
		}
		return cityUtil;
	}
	public static void printQuList(){
		CityUtil cu=getInstance();
		for(Map.Entry<String,Map<String, String>> entry : cu.qu.entrySet())   
		{   
			Map<String, String> m=entry.getValue();
			System.err.println(entry.getKey());
			for(Map.Entry<String, String> e : m.entrySet()){ 
				System.out.println("\t"+e.getKey()+" "+e.getValue());
			}
		}
	}
	/**
	 * �ѳ��д���ת��ΪIDS����
	 * @param city ����,����,������
	 * @return 0,0,0
	 * @throws Exception 
	 */
	public static String city_To_Ids(String city) throws Exception{
		String rs=null;
		String[] params=city.split(",");
		if(params.length==3){
			int shiId=CityUtil.searchShi_ID(params[1]);
			if(shiId!=-1){
				int quId=CityUtil.searchQu_ID(shiId, params[2], null);
				if(quId!=-1){
					rs=CityUtil.searchShi_Info(shiId).getParentID()+","+shiId+","+quId;
				}
			}
		}
		return rs;
	}
	/**
	 * ��ids����ת��Ϊ���в���
	 * @param ids 12,3,4,
	 * @param writeDB
	 * @return ����,����,����
	 */
	public static String ids_To_Citys(final String ids){
		String rs="";
		String[] params=ids.split(",");
		if(params.length==3){
			int sheng=Integer.parseInt(params[0]);
			int shi=Integer.parseInt(params[1]);
			int qu=Integer.parseInt(params[2]);
			try {
				CityInfo shengInfo= CityUtil.searchSheng_Info(sheng);
				CityInfo shiInfo=CityUtil.searchShi_Info(shi);
				CityInfo quInfo=CityUtil.searchQu_Info(qu);
				rs=shengInfo.getName()+","+shiInfo.getName()+","+quInfo.getName();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

	/**
	 * ���ݸ���ʡ��,��ʡ�б�������ָ����ʡID
	 * @param name ʡ��
	 * @return ʡID
	 */
	public static int searchSheng_ID(String name)throws Exception{
		int rs=-1;
		CityUtil cu=getInstance();
		for(Map.Entry<String,String> entry : cu.sheng.entrySet())   
		{   
			if(name.startsWith(entry.getValue()) || entry.getValue().startsWith(name)){
				rs=Integer.parseInt(entry.getKey());
				break;
			}
		} 
		return rs;
	}
	/**
	 * ���ݸ�����ID��ʡ�б������� ָ����ʡ��Ϣ
	 * @param id ʡID
	 * @return ʡ��Ϣ
	 */
	public static CityInfo searchSheng_Info(int id)throws Exception{
		CityInfo info = null;
		CityUtil cu=getInstance();
		for(Map.Entry<String,String> entry : cu.sheng.entrySet())   
		{   
			if(entry.getKey().equals(""+id)){
				info=new CityInfo(entry.getKey(), entry.getKey(), entry.getValue());
				break;
			}
		} 
		return info;
	}
	
	/**
	 * ���ݸ�����ID�����б������� ָ��������Ϣ
	 * @param id ��ID
	 * @return ����Ϣ
	 */
	public static CityInfo searchShi_Info(int id) throws Exception{
		CityInfo info = null;
		CityUtil cu=getInstance();
		for(Map.Entry<String,Map<String, String>> entry : cu.shi.entrySet())   
		{   
			Map<String, String> m=entry.getValue();
			for(Map.Entry<String, String> e : m.entrySet())   
			{   
			    //System.out.println(entry.getKey()+"\t" +e.getKey()+": "+e.getValue());  
				if(e.getKey().equals((id+""))){
					info=new CityInfo(entry.getKey(), e.getKey(), e.getValue());
					break;
				}
			} 
		} 
		return info;
	}
	/**
	 * ���ݸ�����ID�����б������� ָ��������Ϣ
	 * @param id ��ID
	 * @return ����Ϣ
	 */
	public static CityInfo searchQu_Info(int id)throws Exception{
		CityInfo info = null;
		CityUtil cu=getInstance();
		for(Map.Entry<String,Map<String, String>> entry : cu.qu.entrySet())   
		{   
			Map<String, String> m=entry.getValue();
			for(Map.Entry<String, String> e : m.entrySet())   
			{   
			    //System.out.println(entry.getKey()+"\t" +e.getKey()+": "+e.getValue());  
				if(e.getKey().equals((id+""))){
					info=new CityInfo(entry.getKey(), e.getKey(), e.getValue());
					break;
				}
			} 
		} 
		return info;
	}
	/**
	 * ���ݸ���������,�����б������� ָ������(ģ��ƥ��)
	 * @param name ����
	 * @return ��ID
	 */
	public static int searchShi_ID(String name)throws Exception{
		int rs=-1;
		CityUtil cu=getInstance();
		for(Map.Entry<String,Map<String, String>> entry : cu.shi.entrySet())   
		{   
			Map<String, String> m=entry.getValue();
			for(Map.Entry<String, String> e : m.entrySet())   
			{   
			    //System.out.println(entry.getKey()+"\t" +e.getKey()+": "+e.getValue());  
				if(e.getValue().startsWith(name) || name.startsWith(e.getValue())){
					rs=Integer.parseInt(e.getKey());
					break;
				}
			} 
		} 
		return rs;
	}
	/**
	 * ���ݸ���������,�����б������� ָ������(ģ��ƥ��)
	 * @param shiId ��ָ������������
	 * @param name ����
	 * @param filter ������
	 * @return
	 * @throws Exception
	 */
	public static int searchQu_ID(int shiId,String name,List<String> filter)throws Exception{
		int rs = -1;
		CityUtil cu=getInstance();
		for(Map.Entry<String,Map<String, String>> entry : cu.qu.entrySet())   
		{   
			if(!entry.getKey().equals(shiId+"")){
				continue;
			}
			//System.out.println(shiId+"   "+"  "+name+"   "+ entry.getKey()+" @@@ "+entry.getValue());
			Map<String, String> m=entry.getValue();
			if(m.size()==1){
				//��ֻ��һ��Ļ�,����ѡ��,ֱ�ӷ��������,
				for(Map.Entry<String, String> e : m.entrySet())   
				{   
				    rs=Integer.parseInt(e.getKey());
				} 
				break;
			}
			for(Map.Entry<String, String> e : m.entrySet())   
			{   
			    //System.out.println(entry.getKey()+"\t" +e.getKey()+": "+e.getValue());
				String value=e.getValue();
				if(filter!=null){
					//���������ƽ��,��Ӧ������:���ڰ�һ·Ӫҵ�������� ��������һ·,��ʱֻҪfilter������,�����ģ��ƥ��
					for(int i=0;i<filter.size();i++){
						if(value.endsWith(filter.get(i))){
							value=value.substring(0, value.length()-(filter.get(i).length()));
						}
					}
				}
				if(value.startsWith(name) || name.startsWith(value)){
					rs=Integer.parseInt(e.getKey());
					break;
				}
			} 
		} 
		return rs;
	}
	
	
	/**
	 * ����ָ����ID�ĸ�ID
	 * @param id ��ID
	 * @return ��ID
	 
	protected static int searchQu_ParentID(int id){
		String rs=null;
		CityUtil cu=getInstance();
		for(Map.Entry<String,Map<String, String>> entry : cu.qu.entrySet())   
		{   
			Map<String, String> m=entry.getValue();
			for(Map.Entry<String, String> e : m.entrySet())   
			{   
			   //System.out.println(entry.getKey()+"\t" +e.getKey()+": "+e.getValue());  
			   if(e.getKey().equals((id+""))){
					rs=entry.getKey();
					break;
			   }
			} 
		} 
		return rs==null ? null:Integer.parseInt(rs);
	}
	*/
	/**
	 * ���ݸ����ĳ���ID ���ȡһ������ID
	 * @param id ����ID
	 * @param ft �����ƹ�����(���Ա����������� ���ߴ����ֵ�)
	 * @return
	 */
	public static CityInfo searchRandomQu(int id,String ft)throws Exception{
		CityInfo info = null;
		CityUtil cu=getInstance();
		List<CityInfo> cList=new ArrayList<>();
		for(Map.Entry<String, String> e : cu.qu.get(id+"").entrySet())   
		{   
		    //System.out.println(e.getKey()+": "+e.getValue()); 
			//һ��ft="��"
			if(e.getValue().indexOf(ft)!=-1){
				cList.add(new CityInfo(id+"", e.getKey(), e.getValue()));
			}
		} 
		int r=new Random().nextInt(cList.size());
		for(int i=0;i<cList.size();i++){
			if(r==i){
				info=cList.get(i);//���ȡһ����
				System.out.println(info);
			}
		}
		return info;
	}
	
	


//------------------------------------------------------------------------------	
	private static String readHtml(String path) throws IOException {
		String html = "";
		FileInputStream fis = new FileInputStream(path);
		int length = -1;
		byte[] b = new byte[1024000];
		while ((length = fis.read(b)) != -1) {
			html += new String(b, 0, length);
		}
		fis.close();
		return html;
	}

	private void loadCity_Sheng() throws IOException {
		Pattern p = Pattern.compile("var sheng=eval\\('\\(.*?\\)'\\)");
		Matcher m = p.matcher(jsonAllStr);
		if (m.find()) {
			// ����ʡ,
			String json = m.group();
			json = json.replace("var sheng=eval('(", "");
			json = json.replace(")')", "");
			sheng = HtmlJosnUtil.handleCity_Sheng(json);
		}
	}

	private void loadCity_Shi() throws IOException {
		Pattern p = Pattern.compile("shi=eval\\('\\(.*?\\)'\\)");
		Matcher m = p.matcher(jsonAllStr);
		if (m.find()) {
			// ������
			String json = m.group();
			json = json.replace("shi=eval('(", "");
			json = json.replace(")')", "");
			shi = HtmlJosnUtil.handleCity_Shi_Or_Qu(json);
		}
	}

	private void loadCity_qu() throws IOException {
		Pattern p = Pattern.compile("qu=eval\\('\\(.*?\\)'\\)");
		Matcher m = p.matcher(jsonAllStr);
		if (m.find()) {
			// ������
			String json = m.group();
			json = json.replace("qu=eval('(", "");
			json = json.replace(")')", "");
			qu = HtmlJosnUtil.handleCity_Shi_Or_Qu(json);
		}
	}
}
