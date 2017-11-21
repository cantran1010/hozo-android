package vn.tonish.hozo.utils;

import android.content.Context;
import android.text.format.DateUtils;

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
import java.util.concurrent.TimeUnit;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 4/21/2017.
 */
public class DateTimeUtils {

    private static final String TAG = DateTimeUtils.class.getSimpleName();

    public static String getOnlyDateFromIso(String input) {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
    }

    public static String getOnlyHourFromIso(String input) {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
    }

    public static String getPaymentTime(String input) {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(date);
    }

    public static String getDateBirthDayFromIso(String input) {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
    }

    public static int getAgeFromIso(String birthday) {
        Calendar calendar = GregorianCalendar.getInstance();
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(birthday);

            calendar.setTime(date);

            Calendar today = Calendar.getInstance();

            int curYear = today.get(Calendar.YEAR);
            int dobYear = calendar.get(Calendar.YEAR);

            return curYear - dobYear;
        } catch (ParseException e) {
            e.printStackTrace();
            return 20;
        }
    }


    public static String getOnlyIsoFromDate(String input) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }


    public static String getHourMinuteFromIso(String input) {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
    }

    private static final String DATE_FORMAT = "dd/MM/yyyy";

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
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .format(date);
    }

    /**
     * Transform Calendar to ISO 8601 string.
     */
    public static String fromCalendarIso(final Calendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf
                .format(date);
    }

    /**
     * Get current date and time formatted as ISO 8601 string.
     */
    public static String now() {
        return fromCalendarIso(GregorianCalendar.getInstance());
    }

    /**
     * Transform Calendar to ISO 8601 string.
     */
    public static String fromCalendarIsoCreateTask(final Calendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * Transform Calendar to ISO 8601 string.
     */
    public static String fromCalendarToDate(final Calendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * Transform Calendar to ISO 8601 string.
     */
    public static String fromCalendarToTime(final Calendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date);
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
        //noinspection deprecation
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return formatter.format(tme);
    }

    private static final long SECOND_MILLIS = 1000;
    private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long MONTH_MILLIS = 30 * DAY_MILLIS;
    private static final long YEAR_MILLIS = 12 * MONTH_MILLIS;

    public static String getTimeAgo(String date, Context context) {
        String result = "";
        long time;
        try {
            time = toCalendar(date).getTimeInMillis();
//            if (time < 1000000000000L) {
//                // if timestamp given in seconds, convert to millis
//                time *= 1000;
//            }
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            if (time > now || time <= 0) {
                return context.getResources().getString(R.string.just_now);
            }
            long diff = now - time;
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
                result = DateTimeUtils.getOnlyDateFromIso(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getTimeAgo(long time, Context context) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateStr = sdf
                .format(date);

        return getTimeAgo(dateStr, context);
    }

    public static String getTimeChat(long time, Context context) {
        if(DateUtils.isToday(time)){
            Date date1 = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sdf.format(date1);
        }else {
            Date date = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.getDefault());
            return sdf.format(date);
        }
    }

    public static long hoursBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toHours(Math.abs(end - start));
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();

        LogUtils.d(TAG, "daysBetween : " + TimeUnit.DAYS.toDays(Math.abs(end - start)));
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

    public static long minutesBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();

        if (end < start) return 0;
        return TimeUnit.MILLISECONDS.toMinutes(Math.abs(end - start));
    }

}
