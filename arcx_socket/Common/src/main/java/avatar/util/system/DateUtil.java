package avatar.util.system;


import avatar.util.checkParams.ErrorDealUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	// 最小单位为分钟
	private static long MINUTE_MILLIS = 1000 * 60;
	private static long HOUR_MILLIS = MINUTE_MILLIS * 60;
	private static long DAY_MILLIS = HOUR_MILLIS * 24;
	private static long WEEK_MILLIS = DAY_MILLIS * 7;

	// SimpleDateFormat线程不安全, so ThreadLocal
	private static final ThreadLocal<DateFormats> dateFormats = ThreadLocal.withInitial(DateFormats::new);

	public static String formatYMD(Date date) {
		return dateFormats.get().ymd.format(date);
	}

	public static String formatYM(Date date) {
		return dateFormats.get().ym.format(date);
	}

	public static String formatY(Date date) {
		return dateFormats.get().y.format(date);
	}

	public static String formatYYYYMMDD(Date date) {
		return dateFormats.get().yyyymmdd.format(date);
	}

	public static String formatYMDHM(Date date) {
		return dateFormats.get().ymdhm.format(date);
	}

	public static String formatYMDHMS(Date date) {
		return dateFormats.get().ymdhms.format(date);
	}

	public static String formatYMDChinese(Date date) {
		return dateFormats.get().ymdChinese.format(date);
	}

	public static String formatYMDSlash(Date date) {
		return dateFormats.get().ymdSlash.format(date);
	}

	public static Date parseYMD(String dateStr) {
		return parse(dateFormats.get().ymd, dateStr);
	}

	public static Date parseYMDCHIN(String dateStr){return parse(dateFormats.get().ymdChinese,dateStr);}

	public static Date parseYYYYMMDD(String dateStr) {
		return parse(dateFormats.get().yyyymmdd, dateStr);
	}

	public static Date parseYMDHMS(String dateStr) {
		return parse(dateFormats.get().ymdhms, dateStr);
	}

	public static Date parseYMDHM(String dateStr) {
		return parse(dateFormats.get().ymdhm, dateStr);
	}

	public static Date parseHMS(String dateStr) {
		return parse(dateFormats.get().hms, dateStr);
	}

	/**
	 * 将字符串转换为Date类型
	 * @param str
	 * @return
	 */
	public static Date stringToDate(String str) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
		} catch (ParseException e) {
			ErrorDealUtil.printError(e);
		}
		return null;
	}
	/**
	 * 格式化字符串，解决从MySql中查出来的数据后面多了0的问题
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	/**
	 * 返回星期日历的当前毫秒数
	 *
	 * @param dayOfWeek 星期几 hmsStr 12：00：00
	 */
	public static long parseMsDayOfWeekHMS(int dayOfWeek, String hmsStr) {
		String ymdStr = formatYMD(new Date());
		Date date = parse(dateFormats.get().ymdhms, ymdStr + " " + hmsStr);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dw = getDayOfWeek();
		c.add(Calendar.DATE, dayOfWeek - dw);
		return c.getTimeInMillis();

	}

	public static long parseMsYMDHMS(SimpleDateFormat format, String dateStr) {
		try {
			Date d = format.parse(dateStr);
			// check range for mysql
			// mysql date range : '1000-01-01' to '9999-12-31'
			Calendar c = Calendar.getInstance();
			c.setTime(d);

			int year = c.get(Calendar.YEAR);
			if (year >= 1000 && year <= 9999) {
				return -1l;
			} else {
				return c.getTimeInMillis();
			}
		} catch (ParseException ex) {
			return -1l;
		}
	}

	public static Date parse(SimpleDateFormat format, String dateStr) {
		try {
			Date d = format.parse(dateStr);
			// check range for mysql
			// mysql date range : '1000-01-01' to '9999-12-31'
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			int year = c.get(Calendar.YEAR);
			if (year >= 1000 && year <= 9999) {
				return d;
			} else {
				return null;
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前是周几
	 */
	public static int getDayOfWeek() {
		int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		return dayOfWeek;
	}

	/**
	 * 获取当前小时
	 */
	public static int getHourOfDay() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前分钟
	 */
	public static int getMinOfHour() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	public static long minutesDiff(Calendar date1, Calendar date2) {
		return timeDiff(date1, date2, MINUTE_MILLIS);
	}

	public static long minutesDiff(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		return minutesDiff(calendar1, calendar2);
	}

	public static int dayDiff(Calendar date1, Calendar date2) {
		return (int) timeDiff(date1, date2, DAY_MILLIS);
	}

	public static int dayDiff(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		return dayDiff(calendar1, calendar2);
	}

	/**
	 * 两个日期相差的天数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int twoDayDifference(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		int y1 = calendar1.get(Calendar.YEAR);
		int y2 = calendar2.get(Calendar.YEAR);
		int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
		return (y1 - y2) * 365 + (d1 - d2);
	}

	public static long timeDiff(Calendar date1, Calendar date2, long unit) {
		long day1 = (date1.getTimeInMillis() + date1.getTimeZone().getOffset(date1.getTimeInMillis())) / unit;
		long day2 = (date2.getTimeInMillis() + date2.getTimeZone().getOffset(date2.getTimeInMillis())) / unit;
		return day1 - day2;
	}

	public static int todayMinutes() {
		return toMinutes(new Date());
	}

	public static int toMinutes(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return toMinutes(calendar);
	}

	public static int toMinutes(Calendar date) {
		return (int) ((date.getTimeInMillis() + date.getTimeZone().getOffset(date.getTimeInMillis())) / MINUTE_MILLIS);
	}

	public static int todayDays() {
		return toDays(new Date());
	}

	public static int toDays(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return toDays(calendar);
	}

	public static int toDays(Calendar date) {
		return (int) ((date.getTimeInMillis() + date.getTimeZone().getOffset(date.getTimeInMillis())) / DAY_MILLIS);
	}

	/**
	 * 格林威治标准时间 1970 年 1 月 1 日 是星期四
	 * 1969 年 12 月 22 日 到 1969 年 12 月 28 日 返回-1
	 * 1969 年 12 月 29 日 到 1970 年 1 月 4 日 返回1
	 * 1970 年 1 月 5 日 到 1970 年 1 月 11 日 返回2
	 */
	public static short toWeeks(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return toWeeks(calendar);
	}

	/**
	 * 格林威治标准时间 1970 年 1 月 1 日 是星期四
	 * 1969 年 12 月 22 日 到 1969 年 12 月 28 日 返回-1
	 * 1969 年 12 月 29 日 到 1970 年 1 月 4 日 返回1
	 * 1970 年 1 月 5 日 到 1970 年 1 月 11 日 返回2
	 */
	public static short toWeeks(Calendar date) {
		final long offsetMillis = DAY_MILLIS * 3;
		final long realMillis = date.getTimeInMillis() + date.getTimeZone().getOffset(date.getTimeInMillis());
		final long totalMillis = offsetMillis + realMillis;
		short weeks = (short) (totalMillis / WEEK_MILLIS);
		if (totalMillis < 0) {
			weeks -= 1;
		} else {
			weeks += 1;
		}
		return weeks;
	}

	//	public static long getMillisecond(int year, int month, int date, int hourOfDay, int minute, int second) {
	//		Calendar calendar = Calendar.getInstance();
	//		calendar.set(year, month - 1, date, hourOfDay, minute, second);
	//		return calendar.getTimeInMillis();
	//	}

	public static long getTodayMillisecond(int hourOfDay, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return calendar.getTimeInMillis();
	}

	/**
	 * @param newSecondTime 一般是 当前时间
	 * @param oldSecondTime 一般是 保存的旧时间数据
	 * @return
	 */
	public static int secondTimeDiff(int newSecondTime, int oldSecondTime) {
		return newSecondTime - oldSecondTime;
	}

	/**
	 * @param HHmmss 格式为"HH:mm:ss"
	 */
	public static long getTodayMillisecond(String HHmmss) {
		String[] elems = HHmmss.split(":");
		return getTodayMillisecond(Integer.parseInt(elems[0]), Integer.parseInt(elems[1]), Integer.parseInt(elems[2]));
	}

	public static String getNextPublishFriday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		if (calendar.before(Calendar.getInstance())
				|| formatYMD(calendar.getTime()).equals(formatYMD(Calendar.getInstance().getTime()))) {
			// 是星期六 或者 星期五
			calendar.add(Calendar.DAY_OF_YEAR, 7);
		}
		return formatYMD(calendar.getTime());
	}

	/**
	 * 某个毫秒时间 的 那天的零时毫秒数
	 *
	 * @param millisTime
	 * @return
	 */
	public static long getZeroMillisTime(long millisTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millisTime);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int millisecond = calendar.get(Calendar.MILLISECOND);
		return millisTime - hour * 3600 * 1000 - minute * 60 * 1000 - second * 1000 - millisecond;
	}

	private static Date add(Date time, long offset) {
		return new Date(time.getTime() + offset);
	}

	public static Date addMinute(Date time, int m) {
		return add(time, m * MINUTE_MILLIS);
	}

	public static Date addHour(Date time, int h) {
		return add(time, h * HOUR_MILLIS);
	}

	public static Date addDay(Date time, int d) {
		return add(time, d * DAY_MILLIS);
	}

	public static long addDay(long millisTime, int d) {
		long zeroMillisTime = getZeroMillisTime(millisTime);
		return zeroMillisTime + d * DAY_MILLIS;
	}

	/**
	 * @param zeroTime1 为 00:00:00的秒数
	 * @param zeroTime2 为 00:00:00的秒数
	 * @return 两个时间相差的天数
	 */
	public static int dayDifNum(long zeroTime1, long zeroTime2) {
		long abs = Math.abs(zeroTime1 - zeroTime2);
		return (int) (abs / DAY_MILLIS);
	}

	/**
	 * @param newtime
	 * @param oldtime
	 * @return true:newtime > oldtime ; false:time1 <= time2
	 */
	public static boolean afterMillisTime(long newtime, long oldtime) {
		return newtime > oldtime;
	}

	/**
	 * @param newDay
	 * @param oldDay
	 * @return true: newDay >= oldDay ; 两个时间在同一天 为true
	 */
	public static boolean afterDay(Date newDay, Date oldDay) {
		long new_zeroTime = getZeroMillisTime(newDay.getTime());
		long old_zeroTime = getZeroMillisTime(oldDay.getTime());
		return new_zeroTime >= old_zeroTime;
	}

	public static int getNowSecondTime() {
		return (int) Math.ceil((double) (System.currentTimeMillis() / (double) 1000));
	}

	public static int getSecondTimeUtil(Date date) {
		return (int) Math.ceil((double) (date.getTime() / (double) 1000));
	}

	/**
	 * actionTime （毫秒）是否 在 周五0时 到下周四24时 之间
	 *
	 * @param actionTime
	 * @return true:在 false:不在
	 */
	public static boolean isInWeekOfDay(long actionTime) {
		long curMillisTime = System.currentTimeMillis();
		if (actionTime > curMillisTime) {
			return false;
		}
		long zeroMillisTime = getZeroMillisTime(curMillisTime);
		int dayOfWeek = getDayOfWeek();
		int d = 5 - dayOfWeek;
		long big5Time = 0;
		long small5Time = 0;
		if (d > 0) {
			big5Time = addDay(zeroMillisTime, d);
			small5Time = big5Time - 7 * DAY_MILLIS;
		} else {
			small5Time = addDay(zeroMillisTime, d);
			big5Time = small5Time + 7 * DAY_MILLIS;
		}
		return actionTime >= small5Time && actionTime < big5Time;
	}

	/**
	 * If now is in [startDate,endDate), return true
	 */
	public static boolean nowBetween(Date startDate, Date endDate) {
		return between(new Date(), startDate, endDate);
	}

	/**
	 * If cmpDate is in [startDate,endDate), return true
	 */
	public static boolean between(Date cmpDate, Date startDate, Date endDate) {
		if (startDate != null && cmpDate.compareTo(startDate) < 0) {
			return false;
		}
		if (endDate != null && cmpDate.compareTo(endDate) >= 0) {
			return false;
		}
		return true;
	}

	public static boolean after(Date d1, Date d2) {
		return (d1.getTime() - d2.getTime()) > 0;
	}

	public static boolean before(Date d1, Date d2) {
		return (d1.getTime() - d2.getTime()) < 0;
	}

	public static boolean isSameDay(Date date1, Date date2) {
		return dayDiff(date1, date2) == 0;
	}

	/**
	 * true:在本周内  false:不在本周内
	 *
	 * @param cmpDate
	 * @return
	 */
	public static boolean isSameWeekDay(Date cmpDate) {
		Calendar curCal = Calendar.getInstance();
		curCal.setTime(new Date());
		Calendar cmpCal = Calendar.getInstance();
		cmpCal.setTime(cmpDate);
		return curCal.get(Calendar.YEAR) == cmpCal.get(Calendar.YEAR)
				&& curCal.get(Calendar.WEEK_OF_YEAR) == cmpCal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * true:在本月内   false:不在本月内
	 *
	 * @param cmpDate
	 * @return
	 */
	public static boolean isSameMonthDay(Date cmpDate) {
		Calendar curCal = Calendar.getInstance();
		curCal.setTime(new Date());
		Calendar cmpCal = Calendar.getInstance();
		cmpCal.setTime(cmpDate);
		return curCal.get(Calendar.YEAR) == cmpCal.get(Calendar.YEAR)
				&& curCal.get(Calendar.MONTH) == cmpCal.get(Calendar.MONTH);
	}

	public static boolean isSameDay(Date cmpDate) {
		Calendar curCal = Calendar.getInstance();
		curCal.setTime(new Date());
		Calendar cmpCal = Calendar.getInstance();
		cmpCal.setTime(cmpDate);
		return curCal.get(Calendar.YEAR) == cmpCal.get(Calendar.YEAR)
				&& curCal.get(Calendar.DAY_OF_YEAR) == cmpCal.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 是否是同一天的同一小时
	 */
	public static boolean isSameDateForHour(Date date1 , Date date2){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
				&& cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 是否是同一天的同一小时同一分钟
	 */
	public static boolean isSameDateForHourAndMin(Date date1 , Date date2){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
				&& cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY)
				&& cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE)
				;
	}

	/**
	 * 获取过去（或者将来）n天的时间
	 *
	 * @param day 负数表示过去多少天；正数表示将来多少天；0表示今天
	 */
	public static Date day(int day) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date yesterday() {
		return day(-1);
	}

	public static Date today() {
		return day(0);
	}

	public static Date month(Date date, int monthOffset) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, monthOffset);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date month(Date date) {
		return month(date, 0);
	}

	public static Date month(int monthOffset) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, monthOffset);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date curMonth() {
		return month(0);
	}

	/**
	 * @return 周五（周五当天输出的是上周五）
	 */
	public static Date latestPublishDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date targetDate = c.getTime();
		Date today = today();
		if (targetDate.after(today) || targetDate.equals(today)) {
			int date = c.get(Calendar.DATE);
			c.set(Calendar.DATE, date - 7);
		}
		return c.getTime();
	}

	/**
	 * @return 距报告日期最近的周五
	 */
	public static Date latestPublishDate(Date reportDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(reportDate);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.FRIDAY) {
			return c.getTime();
		} else if (dayOfWeek > Calendar.FRIDAY) {
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			return c.getTime();
		} else {
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 7);
			return c.getTime();
		}
	}

	private static final int[] days = new int[] { 6, 7, 1, 2, 3, 4, 5 };

	/**
	 * 获取本周一个周期开始的时间（每周五凌晨开始）
	 */
	public static Date getStartDateOfWeek() {
		int dayOfWeek = DateUtil.getDayOfWeek();
		int n = 0;
		int size = days.length;
		for (int i = 0; i < size; i++) {
			if (days[i] == dayOfWeek) {
				n = i + 1;
				break;
			}
		}
		if (n == size) {
			return DateUtil.day(0);
		} else {
			return DateUtil.day(-n);
		}
	}

	public static void main(String[] args) {
		/*
		 * long zeroMillisTime = getZeroMillisTime(System.currentTimeMillis()); int dayOfWeek = getDayOfWeek(); int d =
		 * 5 - dayOfWeek ; long big5Time = 0; long small5Time = 0; if(d > 0){ big5Time = addDay(zeroMillisTime, d);
		 * small5Time = big5Time - 7 * DAY_MILLIS; }else{ small5Time = addDay(zeroMillisTime, d); big5Time = small5Time + 7 *
		 * DAY_MILLIS; }
		 */
		// 1456974181000
		// 1456889486106
		System.out.println(System.currentTimeMillis());

		// System.out.println(getDayOfWeek());
	}

	private static class DateFormats {

		public final SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		public final SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
		public final SimpleDateFormat y = new SimpleDateFormat("yyyy");
		public final SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		public final SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		public final SimpleDateFormat ymdChinese = new SimpleDateFormat("yyyy年MM月dd");
		public final SimpleDateFormat ymdSlash = new SimpleDateFormat("yyyy/MM/dd");
		public final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
		public final SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");

	}

}

