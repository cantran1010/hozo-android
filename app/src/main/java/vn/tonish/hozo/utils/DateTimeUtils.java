package vn.tonish.hozo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
 * Created by LongBui.
 */
public class DateTimeUtils {

    private static final String DATE_FORMAT_IN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_OUT = "yyyy-MM-dd";

    public static String changeFormatDate(String input) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT_IN,Locale.getDefault()).parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = new SimpleDateFormat(DATE_FORMAT_OUT,Locale.getDefault()).format(date);
        return result;
    }

    private static final String DATE_FORMAT_IN2 = "yyyy-MM-dd";
    private static final String DATE_FORMAT_OUT2 = "yyyy MM dd";

    public static String changeFormatDate2(String input) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT_IN2,Locale.getDefault()).parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = new SimpleDateFormat(DATE_FORMAT_OUT2,Locale.getDefault()).format(date);
        return result;
    }

    public static String changeFormatDate2(Calendar newDate) {
        String myFormat = "yyyy MM dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(newDate.getTime());

    }

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static Date convertToDate(String input) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT,Locale.getDefault()).parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
