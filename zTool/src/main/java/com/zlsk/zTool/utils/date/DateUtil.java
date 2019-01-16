package com.zlsk.zTool.utils.date;

import com.zlsk.zTool.utils.string.StringUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {
    private static final String TAG = "DateUtil";
    private static SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private static SimpleDateFormat sf3 = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    private static SimpleDateFormat sf4 = new SimpleDateFormat("yyyy", Locale.CHINA);
    private static SimpleDateFormat sf5 = new SimpleDateFormat("MM", Locale.CHINA);

    /**
     * getCurrentTime 获得当前时间.
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime() {
        String result = "";
        result = sf1.format(new Date());
        return result;

    }

    /**
     * 获得当前时间.
     *
     * @return yyyy-MM-dd
     */
    public static String getCurrentDate() {
        String result = "";
        result = sf2.format(new Date());
        return result;
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy-MM
     */
    public static String getMonthDate() {
        String result = "";
        result = sf3.format(new Date());
        return result;
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy
     */
    public static String getYearDate() {
        String result = "";
        result = sf4.format(new Date());
        return result;
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy-MM-dd
     */
    public static String getDate(String dateStr) {
        String result = "";
        if (!StringUtil.isBlank(dateStr)) {
            try {
                Date date = sf2.parse(dateStr);
                result = sf2.format(date);
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy
     */
    public static String getYearDate(String dateStr) {
        String result = "";
        if (!StringUtil.isBlank(dateStr)) {
            try {
                Date date = sf4.parse(dateStr);
                result = sf4.format(date);
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy-MM
     */
    public static String getMonthYearDate(String dateStr) {
        String result = "";
        if (!StringUtil.isBlank(dateStr)) {
            try {
                Date date = sf3.parse(dateStr);
                result = sf3.format(date);
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 获得当前时间.
     *
     * @return MM
     */
    public static String getMonthDate(String dateStr) {
        String result = "";
        if (!StringUtil.isBlank(dateStr)) {
            try {
                Date date = sf5.parse(dateStr);
                result = sf5.format(date);
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * @return
     */
    public static Date fromDate(String dateStr) {
        Date date = null;

        if (!StringUtil.isBlank(dateStr)) {
            try {
                date = sf2.parse(dateStr);
            } catch (Exception e) {
            }
        }

        return date;
    }

    public static String fromLong(long ltime) {
        Date date = new Date(ltime);
        return sf1.format(date);
    }

    /**
     * 获得当前时间.
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(Date date) {
        String result = "";
        if (null != date) {
            result = sf1.format(date);
        }

        return result;
    }

    /**
     * @param dateString yyyy-MM-dd HH:mm:ss.
     */
    public static String getDateTime(String dateString) {
        String result = "";
        if (!StringUtil.isBlank(dateString)) {
            try {
                Date date = sf1.parse(dateString);
                result = sf1.format(date);
            } catch (ParseException e) {
            }
        }

        return result;
    }

    /**
     * @return yyyy-MM-dd
     */
    public static String getDateStr(Date date) {
        String result = "";
        if (null != date) {
            result = sf2.format(date);
        }

        return result;
    }

    /**
     * @return Date
     */
    public static Date getDate1(String dateStr) {
        Date date = null;

        if (!StringUtil.isBlank(dateStr)) {
            try {
                date = sf1.parse(dateStr);
            } catch (Exception e) {
            }
        }

        return date;
    }

    public static Date getDate2(String dateStr) {
        Date date = null;

        if (!StringUtil.isBlank(dateStr)) {
            try {
                date = sf2.parse(dateStr);
            } catch (Exception e) {
            }
        }

        return date;
    }

    public static Date getBeforeDate(String dateStr, int days) {
        Date date = getDate2(dateStr);

        return getBeforeDate(date, days);
    }

    public static Date getBeforeDate(Date date, int days) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);

        return calendar.getTime();
    }

    public static String changeLongToString(long date) {
        Date dt = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            String str = format.format(dt);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long changeStringToLong(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null;
        try {
            dt = sdf.parse(date);
            return dt.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDateEN() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;
    }

    public static String getOffsetToServiceTime() {
        String date = changeLongToString(offsetToServiceTime());
        if (date == null || date.contains("1970"))
            date = getDateEN();
        return date;
    }

    public static long offsetToServiceTime() {
        long time = WebTimeTask.getInstance().getWebTime();
        String tmpdate = changeLongToString(time);
        if (tmpdate == null || tmpdate.contains("1970")) {
            time = System.currentTimeMillis();
        }
        return time;
    }

    /**
     * 将String类型的time转换成date "yyyy-MM-dd HH:mm:ss"
     *
     * @param strTime
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 计算一个日期型的时间与当前时间相差多少
     *
     * @param startDate 开始日期
     * @param
     * @return
     */
    public static String twoDateDistance(Date startDate) {
        if (startDate == null) {
            return null;
        }
        Date endDate = Calendar.getInstance().getTime();
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000) {
            return timeLong / 1000 + "秒前";
        } else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 30 * 12) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 30;
            return timeLong + "月前";
        } else {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 30 / 12;
            return timeLong + "年前";
        }
    }

    public static String getYearFirstDay(String date) {

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf2.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return sf2.format(cal.getTime());

    }

    public static String getYearLastDay(String date) {

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf2.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        return sf2.format(cal.getTime());

    }

    public static String getMonthFirstDay(String date) {

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf2.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return sf2.format(cal.getTime());

    }

    public static String getMonthLastDay(String date) {

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf2.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return sf2.format(cal.getTime());

    }

    public static List<String> getAllDaysInMonth(int year, int month, final boolean isAscending) {
        month -= 1;
        List<String> days = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        while (calendar.get(Calendar.MONTH) == month) {
            days.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (!isAscending) {
            Collections.reverse(days);
        }

        return days;
    }

    public static String getDaysOfMonth(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sf2.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int days = (calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return String.valueOf(days);
    }

    public static Date convertTimeString2Date(String timeStr) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int daysBetween(Date early, Date late) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                .getTime().getTime() / 1000)) / 3600 / 24;

        return days;
    }

    public static int hoursBetween(Date early, Date late) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间
        calst.set(java.util.Calendar.HOUR_OF_DAY, early.getHours());
        calst.set(java.util.Calendar.MINUTE, early.getMinutes());
        calst.set(java.util.Calendar.SECOND, early.getSeconds());
        caled.set(java.util.Calendar.HOUR_OF_DAY, late.getHours());
        caled.set(java.util.Calendar.MINUTE, late.getMinutes());
        caled.set(java.util.Calendar.SECOND, late.getSeconds());
        //得到两个日期相差的小时
        int hours = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600;

        return hours;
    }

    public static int secondsBetween(Date early, Date late) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间
        calst.set(java.util.Calendar.HOUR_OF_DAY, early.getHours());
        calst.set(java.util.Calendar.MINUTE, early.getMinutes());
        calst.set(java.util.Calendar.SECOND, early.getSeconds());
        caled.set(java.util.Calendar.HOUR_OF_DAY, late.getHours());
        caled.set(java.util.Calendar.MINUTE, late.getMinutes());
        caled.set(java.util.Calendar.SECOND, late.getSeconds());
        //得到两个日期相差的分钟
        int seconds = (int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000);

        return seconds;
    }

    public static int compare_date(String DATE1, String DATE2) {
        try {
            Date dt1 = sf1.parse(DATE1);
            Date dt2 = sf1.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
