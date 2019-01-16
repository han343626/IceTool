package com.zlsk.zTool.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by IceWang on 2018/7/3.
 */

public class TimeUtil {
    public static String timeMill2Date(Long TimeMills) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(TimeMills);
        return dateFormat.format(date);
    }

    public static long timeSub(String start,String end){
        long sub = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);
            sub = Math.abs(endDate.getTime() - startDate.getTime());
        } catch (ParseException e) {
            sub = -1;
        }

        return sub;
    }

    public static long timeSub(String start,long end){
        long sub = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date startDate = dateFormat.parse(start);
            sub = Math.abs(end - startDate.getTime());
        } catch (ParseException e) {
            sub = -1;
        }

        return sub;
    }

    public static String formatTimeStr(String oldPattern,String newPattern,String time){
        String newTime = "";
        SimpleDateFormat oldFormat = new SimpleDateFormat(oldPattern, Locale.CHINA);
        SimpleDateFormat newFormat = new SimpleDateFormat(newPattern, Locale.CHINA);
        try {
            Date date = oldFormat.parse(time);
            newTime = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }
}
