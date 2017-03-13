package ominext.com.calendartextview.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
}
