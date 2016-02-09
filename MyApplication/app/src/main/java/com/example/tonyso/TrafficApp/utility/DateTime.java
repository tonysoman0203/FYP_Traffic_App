package com.example.tonyso.TrafficApp.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by TonySo on 5/2/16.
 */
public class DateTime {
    public static String initDate(Locale locale) {
        Calendar c = Calendar.getInstance();
        return new SimpleDateFormat("EEE , dd MMM yyyy", locale).format(c.getTime());
    }

    public static String getTime() {
        Calendar c = Calendar.getInstance();
        return new SimpleDateFormat("HH:mm:ss", Locale.TRADITIONAL_CHINESE).format(c.getTime());
    }

    public static long getTimeInSec(String time) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date parsedDate = dateFormat.parse(time);
            return parsedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Date getCurrentDate() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    public static String getCurrentDateTime() {
        String t = "Error With text";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm");
            Calendar c = Calendar.getInstance();
            Date d = c.getTime();
            return dateFormat.format(d);
        } catch (Exception e) {//this generic but you can control another types of exception
            e.printStackTrace();
        }
        return t;
    }

}
