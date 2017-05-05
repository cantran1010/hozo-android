package vn.tonish.hozo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by LongBui.
 */
public class DateTimeUtils {

    private static final String TAG = DateTimeUtils.class.getSimpleName();

    private static final String DATE_FORMAT_IN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_OUT = "yyyy-MM-dd";

    public static String changeFormatDate(String input) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT_IN, Locale.getDefault()).parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(DATE_FORMAT_OUT, Locale.getDefault()).format(date);
    }

    private static final String DATE_FORMAT_IN2 = "yyyy-MM-dd";
    private static final String DATE_FORMAT_OUT2 = "yyyy MM dd";

    public static String changeFormatDate2(String input) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT_IN2, Locale.getDefault()).parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(DATE_FORMAT_OUT2, Locale.getDefault()).format(date);
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
            date = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static final String DATE_FORMAT2 = "dd-MM-yyyy HH:mm";

    public static Date convertToDate2(String input) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT2, Locale.getDefault()).parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Transform Calendar to ISO 8601 string.
     */
    public static String fromDateIso(Date date) {
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }

    /**
     * Transform Calendar to ISO 8601 string.
     */
    public static String fromCalendarIso(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }

    /**
     * Get current date and time formatted as ISO 8601 string.
     */
    public static String now() {
        return fromCalendarIso(GregorianCalendar.getInstance());
    }

    /**
     * Transform ISO 8601 string to Calendar.
     */
    public static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }

    public static String getTimeIso8601(String date, String time) {
        Date dateConverted = convertToDate2(date + " " + time);
        String nowAsISO = fromDateIso(dateConverted);
        return nowAsISO;
    }

}
