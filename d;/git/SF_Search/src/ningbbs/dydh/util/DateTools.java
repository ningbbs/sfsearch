package ningbbs.dydh.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTools {
	/**
	 * 时间格式化
	 */
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 时间格式化
	 */
	public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * 时间格式化
	 */
	public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 获取前几天 0为当天,1为前1天,2为前2天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(Date time, int seek, SimpleDateFormat sdf) {// 可以用new
		// Date().toLocalString()传递参数
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = sdf.parse(sdf.format(time));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - seek);
		String dayBefore = sdf.format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得后几天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(Date time, int seek) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(sdf.format(time));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + seek);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

	/**
	 * 取几小时前日期
	 * 
	 * @param time
	 * @param seek
	 * @return
	 */
	public static Date getSpecifiedHourBefore(Date time, int seek) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			date = sdf.parse(sdf.format(time));
			c.setTime(date);
			int month = c.get(Calendar.HOUR);
			c.set(Calendar.HOUR, month - seek);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c.getTime();
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @return str
	 */
	public static String DateToStr(Date date) {
		String str = sdf1.format(date);
		return str;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 * @throws ParseException
	 */
	public static Date StrToDate(String str) throws ParseException {
		Date date = null;
		date = sdf1.parse(str);
		return date;
	}

	/**
	 * 比较时间信息comparisonTime(sdf1.parse("2015-08-17 12:10"),sdf1.parse(
	 * "2015-08-17 11:12"),sdf1.parse("2015-08-17 12:11"));
	 * 当time在startTime和endTime时间段内,则返回真,否则返回假
	 * 
	 * @param time
	 *            对比的时间
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param methodName
	 *            调用此方法的method名
	 * @return
	 * @throws ParseException
	 */
	public static boolean comparisonTime(Date time, Date startTime, Date endTime)
			throws ParseException {
		boolean result = false;
		if (time.before(startTime) && time.after(endTime)) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	/**
	 * 比较时间,给定小时差
	 * @param time 要与当前系统时间对比的时间
	 * @return 返回时间差(决对值)
	 */
	public static long comparisonTime(Date time){
		long a=System.currentTimeMillis();
		long b=time.getTime();
		return Math.abs(((a-b)/1000)/3600);
	}
}
