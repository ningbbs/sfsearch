package ningbbs.dydh.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTools {
	/**
	 * ʱ���ʽ��
	 */
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * ʱ���ʽ��
	 */
	public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * ʱ���ʽ��
	 */
	public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * ��ȡǰ���� 0Ϊ����,1Ϊǰ1��,2Ϊǰ2��
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(Date time, int seek, SimpleDateFormat sdf) {// ������new
		// Date().toLocalString()���ݲ���
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
	 * ��ú���
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
	 * ȡ��Сʱǰ����
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
	 * ����ת�����ַ���
	 * 
	 * @param date
	 * @return str
	 */
	public static String DateToStr(Date date) {
		String str = sdf1.format(date);
		return str;
	}

	/**
	 * �ַ���ת��������
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
	 * �Ƚ�ʱ����ϢcomparisonTime(sdf1.parse("2015-08-17 12:10"),sdf1.parse(
	 * "2015-08-17 11:12"),sdf1.parse("2015-08-17 12:11"));
	 * ��time��startTime��endTimeʱ�����,�򷵻���,���򷵻ؼ�
	 * 
	 * @param time
	 *            �Աȵ�ʱ��
	 * @param startTime
	 *            ��ʼʱ��
	 * @param endTime
	 *            ����ʱ��
	 * @param methodName
	 *            ���ô˷�����method��
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
	 * �Ƚ�ʱ��,����Сʱ��
	 * @param time Ҫ�뵱ǰϵͳʱ��Աȵ�ʱ��
	 * @return ����ʱ���(����ֵ)
	 */
	public static long comparisonTime(Date time){
		long a=System.currentTimeMillis();
		long b=time.getTime();
		return Math.abs(((a-b)/1000)/3600);
	}
}
