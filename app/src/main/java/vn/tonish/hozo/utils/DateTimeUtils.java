package vn.tonish.hozo.utils;

import android.content.Context;

import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 4/21/2017.
 */
public class DateTimeUtils {

    private static final String TAG = DateTimeUtils.class.getSimpleName();

    public static String getOnlyDateFromIso(String input) {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
    }

    public static String getHourMinuteFromIso(String input) {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
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

    private static Date convertToDate2(String input) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT2, Locale.getDefault()).parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateFromStringIso(String dateIso) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        Date date = null;
        try {
            date = df.parse(dateIso);
            String newDateString = df.format(date);
            System.out.println(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Transform Calendar to ISO 8601 string.
     */
    public static String fromDateIso(Date date) {
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .format(date);
        return formatted;
    }

    /**
     * Transform Calendar to ISO 8601 string.
     */
    public static String fromCalendarIso(final Calendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formatted = sdf
                .format(date);
        return formatted;
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
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Calendar calendar = GregorianCalendar.getInstance();
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(iso8601string);
        Date localDate = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        calendar.setTime(localDate);
        return calendar;
    }

    public static String getTimeIso8601(String date, String time) {
        Date dateConverted = convertToDate2(date + " " + time);
        return fromDateIso(dateConverted);
    }

    public static String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }

    public static Date localToGMT(Calendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmt = new Date(sdf.format(date));
        return gmt;
    }

    public static Date gmttoLocalDate(Date date) {
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final int MONTH_MILLIS = 30 * DAY_MILLIS;
    private static final int YEAR_MILLIS = 12 * MONTH_MILLIS;

    public static String getTimeAgo(String date, Context context) {
        String result = "";
        long time;
        try {
            time = toCalendar(date).getTimeInMillis();
            if (time < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                time *= 1000;
            }
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            if (time > now || time <= 0) {
                return context.getResources().getString(R.string.just_now);
            }
            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                result = context.getResources().getString(R.string.just_now);
            } else if (diff < 2 * MINUTE_MILLIS) {
                result = context.getResources().getString(R.string.minute_ago);
            } else if (diff < 50 * MINUTE_MILLIS) {
                result = diff / MINUTE_MILLIS + " " + context.getResources().getString(R.string.minutes_ago);
            } else if (diff < 90 * MINUTE_MILLIS) {
                result = context.getResources().getString(R.string.hour_ago);
            } else if (diff < 24 * HOUR_MILLIS) {
                result = diff / HOUR_MILLIS + " " + context.getResources().getString(R.string.hours_ago);
            } else if (diff < 48 * HOUR_MILLIS) {
                result = context.getResources().getString(R.string.yesterday);
            } else if (diff < 30 * DAY_MILLIS) {
                result = diff / DAY_MILLIS + " " + context.getResources().getString(R.string.days_ago);
            } else if (diff < 12 * MONTH_MILLIS) {
                result = diff / MONTH_MILLIS + " " + context.getResources().getString(R.string.month_ago);
            } else {
                result = diff / YEAR_MILLIS + " " + context.getResources().getString(R.string.year_ago);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

}
