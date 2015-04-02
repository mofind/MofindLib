package utility.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	// 获取当天日期
	public static String getToDayDate() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String hehe = dateFormat.format(now);
		return hehe;
	}

	// 获取当天日期2
	public static String getToDayDate2() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		String hehe = dateFormat.format(now);
		return hehe;
	}

	// 获取当天日期3
	public static String getToDayDate3() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	// 获取时间日期字符串
	public static String getDateTimeStr(long currentTimeMillis) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTimeMillis));
	}

	/**
	 * 获取指定时间对应的毫秒数
	 */
	public static long getTimeMillis(String dateStr) {
		try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
			long temp = date1.getTime();// JAVA的时间戳长度是13位
			return temp;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 将时间格式化
	 */
	public static String timeFormat(int time) {
		return time >= 10 ? "" + time : "0" + time;
	}

	/**
	 * 取得当前的时间字符串
	 */

	public static String getCurrentTimeString() {
		return getCurrentTimeString("-", " ", ":");
	}

	public static String getCurrentTimeString(String dateSp, String sp, String timeSp) {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR) + dateSp + timeFormat(c.get(Calendar.MONTH) + 1) + dateSp + timeFormat(c.get(Calendar.DAY_OF_MONTH)) + sp
				+ timeFormat(c.get(Calendar.HOUR_OF_DAY)) + timeSp + timeFormat(c.get(Calendar.MINUTE)) + timeSp + timeFormat(c.get(Calendar.SECOND));
	}

	/**
	 * 处理时间
	 */
	public static String fixTime(String timestamp) {
		if (timestamp == null || "".equals(timestamp)) {
			return "";
		}

		try {
			long _timestamp = Long.parseLong(timestamp) * 1000;
			if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
				return "刚刚";
			} else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
				return ((System.currentTimeMillis() - _timestamp) / 1000 / 60) + "分钟前";
			} else {
				Calendar now = Calendar.getInstance();
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(_timestamp);
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
					SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
					return sdf.format(c.getTime());
				}
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
					SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
					return sdf.format(c.getTime());
				} else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
					SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
					return sdf.format(c.getTime());
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss");
					return sdf.format(c.getTime());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String fixTime(long _timestamp) {
		try {
			if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
				return "刚刚";
			} else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
				return ((System.currentTimeMillis() - _timestamp) / 1000 / 60) + "分钟前";
			} else {
				Calendar now = Calendar.getInstance();
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(_timestamp);
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
					SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
					return sdf.format(c.getTime());
				}
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
					SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
					return sdf.format(c.getTime());
				} else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
					SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
					return sdf.format(c.getTime());
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss");
					return sdf.format(c.getTime());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(long milliseconds) {
		boolean b = false;
		Date time = new Date(milliseconds);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
}
