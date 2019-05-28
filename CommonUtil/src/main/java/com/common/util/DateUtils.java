package com.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class DateUtils {

	public final static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");

	public final static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public final static SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public final static SimpleDateFormat ymdh = new SimpleDateFormat("yyyy-MM-dd HH");

	public final static SimpleDateFormat mdhm = new SimpleDateFormat("MM-dd HH:mm");

	public final static SimpleDateFormat hm = new SimpleDateFormat("HH:mm");

	public final static SimpleDateFormat hm_system = new SimpleDateFormat("hh:mm");

	public final static SimpleDateFormat mdhm_chinese = new SimpleDateFormat("MM月dd日 HH:mm");

	public final static SimpleDateFormat ymd_chinese = new SimpleDateFormat("yyyy年MM月dd日");

	public final static SimpleDateFormat ym_chinese = new SimpleDateFormat("yyyy年MM月");

	public final static SimpleDateFormat md_chinese = new SimpleDateFormat("MM月dd日");

	public final static SimpleDateFormat ymdhms_order = new SimpleDateFormat("yyyy-MM-dd         HH:mm:ss");

	public final static SimpleDateFormat mm = new SimpleDateFormat("mm");

	public final static SimpleDateFormat year = new SimpleDateFormat("yyyy");

	public final static SimpleDateFormat ymd_channel = new SimpleDateFormat("yyyyMMdd");

	public final static SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");

	public final static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");

	public final static SimpleDateFormat ymd_simple = new SimpleDateFormat("yyyy/MM/dd");

	public final static SimpleDateFormat ymd_simple_with_dot = new SimpleDateFormat("yyyy.MM.dd");

	public static String longStringToDateString(String longString, SimpleDateFormat df) {
		try {
			return dateToString(new Date(Long.parseLong(longString)), df);
		} catch (Exception e) {
		}
		return null;
	}

	public static String dateToString(Date date, SimpleDateFormat df) {
		return df.format(date);
	}

	public static String formatCountTime(long timeMills) {
		long hour = (timeMills / (60 * 60 * 1000)) % 24;
		timeMills = timeMills % (60 * 60 * 1000);
		long minute = timeMills / (60 * 1000);
		timeMills = timeMills % (60 * 1000);
		long second = timeMills / 1000;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	public static String formatRemainCountTime(long remainTimeMills) {
		long day = remainTimeMills / (24 * 60 * 60 * 1000);
		long hour = (remainTimeMills / (60 * 60 * 1000)) % 24;
		remainTimeMills = remainTimeMills % (60 * 60 * 1000);
		long minute = remainTimeMills / (60 * 1000);
		remainTimeMills = remainTimeMills % (60 * 1000);
		long second = remainTimeMills / 1000;
		if (day > 0) {
			return String.format("%02d天:%02d:%02d:%02d", day, hour, minute, second);
		} else {
			return String.format("%02d:%02d:%02d", hour, minute, second);
		}
	}

	public static String getDayCountWithFormat(long timeMills) {
		if (timeMills > 0) {
			return String.format("%02d", timeMills / (24 * 60 * 60 * 1000));
		}
		return "";
	}

	public static int getDayCount(long timeMills) {
		if (timeMills > 0) {
			return (int) (timeMills / (24 * 60 * 60 * 1000));
		}
		return 0;
	}

	public static String getTimeFormat(long time) {
		if (time > 24 * 60 * 60 * 1000) {
			int days = (int) (time / (24 * 60 * 60 * 1000));
			return days + "天";
		} else {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumIntegerDigits(2);
			return nf.format((time / (60 * 60 * 1000)) % 24) + ":" + nf.format(((time / (60 * 1000))) % 60) + ":"
					+ nf.format((time / 1000) % 60);
		}
	}

	public static boolean isDuring(String date, String startDate, String endDate) {
		if (TextUtils.isEmpty(startDate) && TextUtils.isEmpty(endDate)) {
			return true;
		}
		try {
			Date now = new Date();
			if (!TextUtils.isEmpty(date)) {
				now = ymdhms.parse(date);
			}

			Date start;
			Date to;
			if (TextUtils.isEmpty(startDate)) {
				to = ymdhms.parse(endDate);
				return now.getTime() <= to.getTime();
			}

			if (TextUtils.isEmpty(endDate)) {
				start = ymdhms.parse(startDate);
				return now.getTime() >= start.getTime();
			}

			start = ymdhms.parse(startDate);
			to = ymdhms.parse(endDate);
			if (now.getTime() <= to.getTime() && now.getTime() >= start.getTime()) {
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
		return false;
	}

	public static boolean isDuring(Date date, Date startDate, Date endDate) {
		String dateStr = null, startDateStr = null, endDateStr = null;
		if (date != null) {
			dateStr = ymdhms.format(date);
		}
		if (startDate != null) {
			startDateStr = ymdhms.format(startDate);
		}
		if (endDate != null) {
			endDateStr = ymdhms.format(endDate);
		}
		return isDuring(dateStr, startDateStr, endDateStr);
	}

	public static boolean isExpired(long startDate, long curDate, int field, int expiredMonths) {

		Calendar curCal = Calendar.getInstance();
		curCal.setTimeInMillis(curDate);

		curCal.add(field, expiredMonths);
		if (startDate <= curCal.getTimeInMillis()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isOvertime(long oldTime, long curTime, long millisecond) {
		return (curTime - oldTime) > millisecond;
	}

	public static boolean isSameYear(Long first, Long second) {
		if (first == null || second == null) {
			return false;
		}
		return year.format(new Date(first)).equals(year.format(new Date(second)));
	}

	public static boolean isSameYearMonthDay(Date first, Date second) {
		if (first == null || second == null) {
			return false;
		}
		return ymd_chinese.format(first).equals(ymd_chinese.format(second));
	}

	public static String getDateIncomStateToNow(Date targetDate) {
		if (isToday(targetDate)) {
			return "今天";
		} else if (IsYesterday(targetDate)) {
			return "昨天";
		} else {
			return DateUtils.ymd_chinese.format(targetDate);
		}
	}

	public static String compareDateToday(Long currentServerTime, Long createDate) {
		GregorianCalendar calCurrent = new GregorianCalendar(Locale.CHINA);
		calCurrent.set(Calendar.HOUR_OF_DAY, 0);
		calCurrent.set(Calendar.MINUTE, 0);
		calCurrent.set(Calendar.SECOND, 0);
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(createDate);
		int apm = mCalendar.get(Calendar.AM_PM);
		String time = hm_system.format(createDate);
		if (createDate > calCurrent.getTime().getTime()
				&& createDate <= calCurrent.getTime().getTime() + MILLISECOND_PER_DAY) {
			long pastTime = currentServerTime - createDate;
			if (pastTime <= 0 || pastTime > 0 && pastTime < MILLISECOND_PER_MINUTE) {
				return "刚刚";
			} else if (pastTime >= MILLISECOND_PER_MINUTE && pastTime < MILLISECOND_PER_HOUR) {
				return pastTime / MILLISECOND_PER_MINUTE + "分钟前";
			}
		}
		String yearMonthDay = ymd.format(createDate) + " ";
		return yearMonthDay + (apm == 0 ? "上午" + time : "下午" + time);
	}

	public static boolean isToday(Date date) {
		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);

			if (diffDay == 0) {
				return true;
			}
		}
		return false;
	}

	public static boolean IsYesterday(Date date) {
		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);

			if (diffDay == -1) {
				return true;
			}
		}
		return false;
	}

	public static String compareDateToNow(Long currentServerTime, Long createDate) {
		String result = null;
		long pastTime = currentServerTime - createDate;
		if (pastTime <= 0 || pastTime > 0 && pastTime < MILLISECOND_PER_MINUTE) {
			result = "刚刚";
		} else if (pastTime >= MILLISECOND_PER_MINUTE && pastTime < MILLISECOND_PER_HOUR) {
			result = pastTime / MILLISECOND_PER_MINUTE + "分钟前";
		} else if (pastTime >= MILLISECOND_PER_HOUR && pastTime < MILLISECOND_PER_DAY) {
			result = pastTime / MILLISECOND_PER_HOUR + "小时前";
		} else if (pastTime >= MILLISECOND_PER_DAY && pastTime < MILLISECOND_PER_MONTH) {
			result = pastTime / MILLISECOND_PER_DAY + "天前";
		} else if (pastTime >= MILLISECOND_PER_MONTH && pastTime < MILLISECOND_PER_YEAR) {
			result = pastTime / MILLISECOND_PER_MONTH + "月前";
		} else if (pastTime >= MILLISECOND_PER_YEAR) {
			result = pastTime / MILLISECOND_PER_YEAR + "年前";
		}
		return result;
	}

	public final static long MILLISECOND_PER_MINUTE = 60 * 1000;

	public final static long MILLISECOND_PER_HOUR = 60 * 60 * 1000;

	public final static long MILLISECOND_PER_DAY = 24 * 60 * 60 * 1000;

	public final static long MILLISECOND_PER_WEEK = 7 * 24 * 60 * 60 * 1000;

	public final static long MILLISECOND_PER_MONTH = 30L * 24 * 60 * 60 * 1000;

	public final static long MILLISECOND_PER_YEAR = 365L * 24 * 60 * 60 * 1000;

	public static String getDateCompareToNow(Date targetDate, String[] dayOfWeek) {
		GregorianCalendar calCurrent = new GregorianCalendar(Locale.CHINA);
		calCurrent.set(Calendar.HOUR_OF_DAY, 0);
		calCurrent.set(Calendar.MINUTE, 0);
		calCurrent.set(Calendar.SECOND, 0);
		long targetTime = targetDate.getTime();
		if (targetTime > calCurrent.getTime().getTime()
				&& targetTime <= calCurrent.getTime().getTime() + MILLISECOND_PER_DAY) {
			return "今天" + formatDateHM(targetDate);
		} else if (targetTime > calCurrent.getTime().getTime() - MILLISECOND_PER_DAY
				&& targetTime <= calCurrent.getTime().getTime()) {
			return "昨天" + formatDateHM(targetDate);
		} else if (targetTime > calCurrent.getTime().getTime() - MILLISECOND_PER_WEEK
				&& targetTime <= calCurrent.getTime().getTime() - MILLISECOND_PER_DAY) {
			return dayOfWeek[getDayIndexOfWeek(targetDate) - 1] + formatDateHM(targetDate);
		}
		calCurrent.set(Calendar.DAY_OF_YEAR, 1);
		if (targetTime > calCurrent.getTime().getTime()
				&& targetTime <= calCurrent.getTime().getTime() - MILLISECOND_PER_WEEK) {
			return formatDateMDChinese(targetDate) + DateUtils.formatDateHM(targetDate);
		} else {
			return formatDateYMDChinese(targetDate);
		}
	}

	public static String formatDateYMD(Date date) {
		return ymd.format(date);
	}

	public static String formatDateYMDHMS(Date date) {
		return ymdhms.format(date);
	}

	public static String formatDateHM(Date date) {
		return hm.format(date);
	}

	public static String formatDateYMDChinese(Date date) {
		return ymd_chinese.format(date);
	}

	public static String formatDateYMDChinese(long millisecond) {
		Date date = new Date(millisecond);
		return ymd_chinese.format(date);
	}

	public static String formatDateMDChinese(Date date) {
		return md_chinese.format(date);
	}

	public static int getDayIndexOfWeek(Date targetDate) {
		GregorianCalendar cal = new GregorianCalendar(Locale.CHINA);
		cal.setTime(targetDate);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static boolean isAroundTargetHour(int targetHour, long intervalMinutes) {
		long curTime = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, targetHour);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long targetTime = cal.getTimeInMillis();
		return Math.abs(curTime - targetTime) < intervalMinutes;
	}

	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}

	public static String formatNewsDate(Long createDate) {
		if (createDate == null) {
			return "";
		}
		String result = null;
		long pastTime = System.currentTimeMillis() - createDate;
		if (pastTime <= 0 || pastTime > 0 && pastTime < MILLISECOND_PER_MINUTE) {
			result = "刚刚";
		} else if (pastTime >= MILLISECOND_PER_MINUTE && pastTime < MILLISECOND_PER_HOUR) {
			result = pastTime / MILLISECOND_PER_MINUTE + "分钟前";
		} else if (pastTime >= MILLISECOND_PER_HOUR && pastTime < MILLISECOND_PER_DAY) {
			result = pastTime / MILLISECOND_PER_HOUR + "小时前";
		} else if (pastTime >= MILLISECOND_PER_DAY && pastTime < MILLISECOND_PER_MONTH) {
			result = md_chinese.format(new Date(createDate));
		}
		return result;
	}

	public static String formatNewsDateWithSeconds(Long seconds) {
		if (seconds == null) {
			return "";
		}
		return formatNewsDate(seconds * 1000);
	}

	public static long parseDateToMillis(String dateString) {
		return parseDateToMillis(ymdhm, dateString);
	}

	public static long parseDateToMillis(SimpleDateFormat format, String dateString) {
		if (format == null || dateString == null) {
			return -1;
		}
		try {
			Date date = format.parse(dateString);
			return date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String parseTimeToMinute(long time) {
		StringBuffer sb = new StringBuffer();
		long day = time / (24 * 60 * 60 * 1000);
		time = time % (24 * 60 * 60 * 1000);
		long hour = time / (60 * 60 * 1000);
		time = time % (60 * 60 * 1000);
		long minute = time / (60 * 1000);
		sb.append(day > 0 ? (day + "天") : "");
		sb.append(hour > 0 ? (hour + "小时") : "");
		sb.append(minute > 0 ? (minute + "分钟") : "");
		return sb.toString();
	}

	public static int getDaysUntilNow(Date date) {
		GregorianCalendar calCurrent = new GregorianCalendar(Locale.CHINA);
		calCurrent.set(Calendar.HOUR_OF_DAY, 0);
		calCurrent.set(Calendar.MINUTE, 0);
		calCurrent.set(Calendar.SECOND, 0);
		long pastTime = calCurrent.getTimeInMillis() - date.getTime();
		if (pastTime <= 0) {
			return 0;
		} else {
			return (int) (pastTime / android.text.format.DateUtils.DAY_IN_MILLIS);
		}
	}
}
