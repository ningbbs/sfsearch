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
	// 测式测式是否合格
	public static void testSearchList(List<SfSearch> list, SfScanService service)
			throws Exception {
		for (int i = 0; i < list.size(); i++) {
			testSearch(list.get(i), service);
		}
	}

	// 转换为mybatis用的map
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
				String[] arr = r.split("】");
				for (int j = 0; j < arr.length; j++) {
					String str = arr[j] + "】";
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
	
	

	//判断特殊件或者超时件
	public static boolean testSearch1(SfSearch sf){
		long time = DateTools.comparisonTime(DateUtil.StringToDate(sf
				.getRoutes().get(0).scanDateTime));
		if (!sf.getOrigin().equals(sf.getDestination())) {
			// 不是同城,可适当放宽时间限止
			if (time >= Constants.SEND_HOUR2) {
				System.out.print("异地超时:[" + sf.getOrigin() + "] - ["
						+ sf.getDestination() + "]");
				return false;// 大于12小时不取
			}
		} else if (time >= Constants.SEND_HOUR1) {
			System.out.print("同城超时:" + time);
			return false;// 大于12小时不取
		}
		if (sf.getRoutes().toString().indexOf("退") != -1) {
			System.out.println("此单为退回件");
			Constants.log.info(sf.getId() + " 此单为退回件");
			return false;
		}
		if (sf.getProductName().equals("productName")) {
			System.out.println("便利箱产品");
			return false;
		}
		return true;
	}
	// 分析订单
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
		// 保存经过的营业点/部/中心
				Set<String> set = new LinkedHashSet<>();
				Matcher m = Constants.p2.matcher(sf.getRoutes().toString());
				while (m.find()) {
					set.add(m.group());
				}
				List<RouInfo> yyd = calYYDNum(set); // 经过的营业点列表
				// List<RouInfo> zx=calZXNum(set); //经过的中心列表
				int zx = set.size() - yyd.size();
				if (yyd.size() < 2) {
					System.out.print("	营业点过少	" + yyd.size());
					return false; // 经过的营业点少于2则返回
				} else if (yyd.get(0).getIndex() != 0) {
					System.out.print("	营业点未经过中心	");
					return false; // 第一个营业点在总经过站中应该是第1个,先经过中心后经过营业点不符合要求
				}
				String startYydName = yyd.get(0).getName(); // 最开始经过的营业点名字
				String endYydName = yyd.get(yyd.size() - 1).getName(); // 最后经过的营业点名字
				/* 动态查询,为了加快速度,可以直接从sf_wd里取ids,不用每次都去动态处理
				String originYydIds = AddressUtil.addressHandle(startYydName, false,
						service); // 从营业点列表中取最开始经过的营业点
				String destinationYydIds = AddressUtil.addressHandle(endYydName, false,
						service); // 从营业点列表中取最后经过的营业点
				*/
				
				String originYydIds=service.getSfDao().getWdIds(startYydName);
				String destinationYydIds=service.getSfDao().getWdIds(endYydName);
				if (originYydIds.equals("") || destinationYydIds.equals("")) {
					System.out.print("	未找到IDS	");
					return false; // 没有找到对应网点的ids则返回
				}
				int originId = CityUtil.searchShi_ID(sf.getOrigin()); // 寄件城市ID,不是营业点的
				int destinationId = CityUtil.searchShi_ID(sf.getDestination()); // 收件城市ID,不是营业点的
				if (destinationYydIds.indexOf("," + destinationId + ",") == -1) {
					// 从最后经过的营业点搜索收件城市ID是否存在,不存在则证明最后营业点还未到收件城市
					// 东莞市(79)【东莞横沥山厦村营业点】[6,79,739]
					// System.out.print("	未到达收件城市	");
					return false;
				}
				sf.setOriginIds(originYydIds);// 设置IDS
				sf.setDestinationIds(destinationYydIds);// 设置IDS
				String originYydCitys = CityUtil.ids_To_Citys(originYydIds);
				String destinationYydCitys = CityUtil.ids_To_Citys(destinationYydIds);
				String s = sf.getId() + "   总经过[" + set.size() + "],中心[" + zx
						+ "],营业点[" + yyd.size() + "] [" + sf.getOrigin() + "] -- ["
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

	// 从Set计算经常的中心/中转场次数
	public static List<RouInfo> calZXNum(Set<String> set) {
		List<RouInfo> list = new ArrayList<>();
		int i = 0;
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String str = iterator.next();
			if (str.indexOf("中心") != -1 || str.indexOf("中转场") != -1) {
				list.add(new RouInfo(str, i));
			}
			i++;
		}
		return list;
	}

	// 从Set计算经常营业点次数
	private static List<RouInfo> calYYDNum(Set<String> set) {
		List<RouInfo> list = new ArrayList<>();
		int i = 0;
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String str = iterator.next();
			if (str.indexOf("营业点】") != -1 || str.indexOf("营业部】") != -1) {
				list.add(new RouInfo(str, i));
			}
			i++;
		}
		return list;
	}
}
