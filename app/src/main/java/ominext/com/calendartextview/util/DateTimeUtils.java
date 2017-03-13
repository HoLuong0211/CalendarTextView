package ominext.com.calendartextview.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Vinh on 2/14/2017.
 */

public class DateTimeUtils {

    public static final String PATTERN_DATETIME = "yyyy-MM-dd hh:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    public static String formatJapaneseDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.JAPANESE);
        return dateFormat.format(date);
    }

    public static String formatJapaneseTime(Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.JAPANESE);
        return dateFormat.format(date);
    }

    public static String formatServerDateTime(Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.JAPANESE);
        return dateFormat.format(date);
    }

    public static String formatServerDate(Calendar calendar) {
        return formatServerDateTime(calendar.getTime(), PATTERN_DATE);
    }

    public static String formatServerDateTime(Calendar calendar) {
        return formatServerDateTime(calendar.getTime(), PATTERN_DATETIME);
    }

    public static Date parseDateTime(String strDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.JAPAN);
        Date date;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    public static Calendar convertDateTimeToCalendar(String strDate) {
        Calendar calendar = Calendar.getInstance();
        if (strDate != null && !strDate.equals("")) {
            calendar.setTime(parseDateTime(strDate, PATTERN_DATETIME));
        }
        return calendar;
    }

    public static Calendar convertDateToCalendar(String strDate) {
        Calendar calendar = Calendar.getInstance();
        if (strDate != null && !strDate.equals("")) {
            calendar.setTime(parseDateTime(strDate, PATTERN_DATE));
        }
        return calendar;
    }

    public static String formatDate(String date) {
        String format_date = "";
        String[] a = date.split("-");
        String year = a[0];
        String month = a[1];
        String day = a[2];
        format_date = year + "年" + month + "月" + "(" + month + "/1" + "～" + month + "/" + day + ")分 新規作成";
        return format_date;
    }

    public static int days(String date) {
        int format_date;
        String[] a = date.split("-");
        String year = a[0];
        String month = a[1];
        String day = a[2];
        format_date = Integer.parseInt(day);
        return format_date;
    }
}
