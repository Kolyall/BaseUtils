package by.kolyall.utils;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;

/**
 * Created by User on 20.04.2017.
 */

public class DateUtils {
    public static final String HOUR_FORMAT = "HH";
    public static final String DAY_DATE_FORMAT = "dd";
    public static final String DISPLAY_DATE_FORMAT = "HH:mm, dd.MM.yy";
    public static final String DISPLAY_DATE_WEEK_FORMAT = "dd MMMM";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String TIME_FORMAT = "HH:mm";

    public static String getForwardYearDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

    @Nullable
    public static Date toDate(String format,String dateString) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            date = dateFormat.parse(dateString);
            return date;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return date;
    }

    @Nullable
    public static String getFormatedDate(String dateFormat, Date date) {
        DateFormat writeFormat = new SimpleDateFormat(dateFormat); //MM в HH:mm
        String formattedDate = "";
        if (date != null) {
            formattedDate = writeFormat.format(date);
        }
        return formattedDate;
    }

    public static String getFormatedDate(Date date) {
        return getFormatedDate(DISPLAY_DATE_FORMAT,date);
    }

    public static Calendar setCalendarToMin(Calendar calendar) {
        if (calendar != null) {
            Date zeroDate = DateUtils.toDate(DateUtils.TIME_FORMAT, "00:00");
            calendar.setTime(zeroDate);
        }
        return calendar;
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static boolean isCurrentDay(Date date) {
        return Days.daysBetween(new LocalDate(date), new LocalDate(new Date())).getDays()==0;
    }
}
