package ningbbs.kd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;

import ningbbs.data.info.RouInfo;
import ningbbs.dydh.util.CityUtil;
import ningbbs.dydh.util.DateTools;
import ningbbs.service.SfScanService;
import ningbbs.util.Constants;
import ningbbs.util.date.DateStyle;
import ningbbs.util.date.DateUtil;

public class SfSearchUtil {
	// ��ʽ��ʽ�Ƿ�ϸ�
	public static void testSearchList(List<SfSearch> list, SfScanService service)
			throws Exception {
		for (int i = 0; i < list.size(); i++) {
			testSearch(list.get(i), service);
		}
	}

	// ת��Ϊmybatis�õ�map
	public static Map<String, Object> toMap(SfSearch sf) {
		String routes = ningbbs.dydh.util.HtmlJosnUtil.toJson(sf.getRoutes());
		System.err.println("ROU"+routes+" "+sf.getId());
		Map<String, Object> map = new HashMap<>();
		map.put("id", sf.getId());
		map.put("productName", sf.getProductName());
		map.put("productCode", sf.getProductCode());
		map.put("recipientTime", sf.getRecipientTime());
		map.put("scanDateTime", SfScanService.getScanTime(sf));
		map.put("originIds", sf.getOriginIds());
		map.put("origin", sf.getOrigin());
		map.put("destinationIds", sf.getDestinationIds());
		map.put("destination", sf.getDestination());
		map.put("routes", routes);
		map.put("limitTypeName", sf.getLimitTypeName());
		map.put("limitTypeCode", sf.getLimitTypeCode());
		map.put("addtime", sf.getAddtime());
		return map;
	}


	public static List<String> toRoutesList(SfSearch sf) {
		List<String> routesList = new ArrayList<>();
		try {
			for (int i = 0; i < sf.getRoutes().size(); i++) {
				String info = "";
				String r = sf.getRoutes().get(i).toString();
				String[] arr = r.split("��");
				for (int j = 0; j < arr.length; j++) {
					String str = arr[j] + "��";
					Matcher m = Constants.p1.matcher(str);
					if (m.find()) {
						try {
							info = m.group();
							routesList.add(info);
						} catch (Exception e) {
							if (e.getMessage().indexOf("PRIMARY") == -1) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if (e.getMessage().indexOf("PRIMARY") == -1) {
				e.printStackTrace();
			}
		}
		return routesList;
	}
	
	

	//�ж���������߳�ʱ��
	public static boolean testSearch1(SfSearch sf){
		long time = DateTools.comparisonTime(DateUtil.StringToDate(sf
				.getRoutes().get(0).scanDateTime));
		if (!sf.getOrigin().equals(sf.getDestination())) {
			// ����ͬ��,���ʵ��ſ�ʱ����ֹ
			if (time >= Constants.SEND_HOUR2) {
				System.out.print("��س�ʱ:[" + sf.getOrigin() + "] - ["
						+ sf.getDestination() + "]");
				return false;// ����12Сʱ��ȡ
			}
		} else if (time >= Constants.SEND_HOUR1) {
			System.out.print("ͬ�ǳ�ʱ:" + time);
			return false;// ����12Сʱ��ȡ
		}
		if (sf.getRoutes().toString().indexOf("��") != -1) {
			System.out.println("�˵�Ϊ�˻ؼ�");
			Constants.log.info(sf.getId() + " �˵�Ϊ�˻ؼ�");
			return false;
		}
		if (sf.getProductName().equals("productName")) {
			System.out.println("�������Ʒ");
			return false;
		}
		return true;
	}
	// ��������
	public static boolean testSearch(SfSearch sf, SfScanService service)
			throws Exception {
		if(testSearch1(sf)!=true){
			return false;
		}
//		if(testRouPath(sf, service)!=true){
//			return false;
//		}
		return true;
	}

	private static boolean testRouPath(SfSearch sf, SfScanService service) throws Exception {
		// ���澭����Ӫҵ��/��/����
				Set<String> set = new LinkedHashSet<>();
				Matcher m = Constants.p2.matcher(sf.getRoutes().toString());
				while (m.find()) {
					set.add(m.group());
				}
				List<RouInfo> yyd = calYYDNum(set); // ������Ӫҵ���б�
				// List<RouInfo> zx=calZXNum(set); //�����������б�
				int zx = set.size() - yyd.size();
				if (yyd.size() < 2) {
					System.out.print("	Ӫҵ�����	" + yyd.size());
					return false; // ������Ӫҵ������2�򷵻�
				} else if (yyd.get(0).getIndex() != 0) {
					System.out.print("	Ӫҵ��δ��������	");
					return false; // ��һ��Ӫҵ�����ܾ���վ��Ӧ���ǵ�1��,�Ⱦ������ĺ󾭹�Ӫҵ�㲻����Ҫ��
				}
				String startYydName = yyd.get(0).getName(); // �ʼ������Ӫҵ������
				String endYydName = yyd.get(yyd.size() - 1).getName(); // ��󾭹���Ӫҵ������
				/* ��̬��ѯ,Ϊ�˼ӿ��ٶ�,����ֱ�Ӵ�sf_wd��ȡids,����ÿ�ζ�ȥ��̬����
				String originYydIds = AddressUtil.addressHandle(startYydName, false,
						service); // ��Ӫҵ���б���ȡ�ʼ������Ӫҵ��
				String destinationYydIds = AddressUtil.addressHandle(endYydName, false,
						service); // ��Ӫҵ���б���ȡ��󾭹���Ӫҵ��
				*/
				
				String originYydIds=service.getSfDao().getWdIds(startYydName);
				String destinationYydIds=service.getSfDao().getWdIds(endYydName);
				if (originYydIds.equals("") || destinationYydIds.equals("")) {
					System.out.print("	δ�ҵ�IDS	");
					return false; // û���ҵ���Ӧ�����ids�򷵻�
				}
				int originId = CityUtil.searchShi_ID(sf.getOrigin()); // �ļ�����ID,����Ӫҵ���
				int destinationId = CityUtil.searchShi_ID(sf.getDestination()); // �ռ�����ID,����Ӫҵ���
				if (destinationYydIds.indexOf("," + destinationId + ",") == -1) {
					// ����󾭹���Ӫҵ�������ռ�����ID�Ƿ����,��������֤�����Ӫҵ�㻹δ���ռ�����
					// ��ݸ��(79)����ݸ����ɽ�ô�Ӫҵ�㡿[6,79,739]
					// System.out.print("	δ�����ռ�����	");
					return false;
				}
				sf.setOriginIds(originYydIds);// ����IDS
				sf.setDestinationIds(destinationYydIds);// ����IDS
				String originYydCitys = CityUtil.ids_To_Citys(originYydIds);
				String destinationYydCitys = CityUtil.ids_To_Citys(destinationYydIds);
				String s = sf.getId() + "   �ܾ���[" + set.size() + "],����[" + zx
						+ "],Ӫҵ��[" + yyd.size() + "] [" + sf.getOrigin() + "] -- ["
						+ sf.getDestination() + "] "
						+ ningbbs.dydh.util.HtmlJosnUtil.toJson(sf.getRoutes());
				Constants.log.info(s);
				s = sf.getOrigin() + "(" + originId + ")[" + startYydName + "["
						+ originYydIds + "][" + originYydCitys + "]] - "
						+ sf.getDestination() + "(" + destinationId + "[" + endYydName
						+ "[" + destinationYydIds + "][" + destinationYydCitys + "]]";
				Constants.log.info(s);
				return true;
	}

	// ��Set���㾭��������/��ת������
	public static List<RouInfo> calZXNum(Set<String> set) {
		List<RouInfo> list = new ArrayList<>();
		int i = 0;
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String str = iterator.next();
			if (str.indexOf("����") != -1 || str.indexOf("��ת��") != -1) {
				list.add(new RouInfo(str, i));
			}
			i++;
		}
		return list;
	}

	// ��Set���㾭��Ӫҵ�����
	private static List<RouInfo> calYYDNum(Set<String> set) {
		List<RouInfo> list = new ArrayList<>();
		int i = 0;
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String str = iterator.next();
			if (str.indexOf("Ӫҵ�㡿") != -1 || str.indexOf("Ӫҵ����") != -1) {
				list.add(new RouInfo(str, i));
			}
			i++;
		}
		return list;
	}
}
