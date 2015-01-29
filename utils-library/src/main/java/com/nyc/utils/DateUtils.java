package com.nyc.utils;

import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {

    @SuppressWarnings("unused")
    private static final String TAG = DateUtils.class.getSimpleName();
    
    public static final long ONE_HOUR = 60 * 60 * 1000;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long ONE_WEEK = 7 * ONE_DAY;

    /**
     * Compares only the month, day, and year of the provided calendars
     * @param date1
     * @param date2
     * @return -1 if date1 is earlier, 1 if date2 is earlier, 0 otherwise
     */
    public static int compareDates(Calendar date1, Calendar date2) {
        if (date1.get(Calendar.YEAR) < date2.get(Calendar.YEAR)) {
            return -1;
        } else if (date1.get(Calendar.YEAR) > date2.get(Calendar.YEAR)) {
            return 1;
        }
        // Years are equal
        if (date1.get(Calendar.MONTH) < date2.get(Calendar.MONTH)) {
            return -1;
        } else if (date1.get(Calendar.MONTH) > date2.get(Calendar.MONTH)) {
            return 1;
        }
        // Years and months are equal
        if (date1.get(Calendar.DAY_OF_MONTH) < date2.get(Calendar.DAY_OF_MONTH)) {
            return -1;
        } else if (date1.get(Calendar.DAY_OF_MONTH) > date2.get(Calendar.DAY_OF_MONTH)) {
            return 1;
        }
        return 0;
    }
    
    public static boolean isSameDay(Calendar date1, Calendar date2) {
        return compareDates(date1, date2) == 0;
    }
    
    public static boolean isSameDay(long dayMillis1, long dayMillis2) {
        Calendar date1 = getCalendar(dayMillis1);
        Calendar date2 = getCalendar(dayMillis2);
        return compareDates(date1, date2) == 0;
    }
    
    public static boolean isYesterday(long timeMillis) {
        Calendar time = getCalendar(timeMillis);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        return (compareDates(time, yesterday) == 0);
    }
    
    /**
     * Returns a string representation for a date (i.e., throw away time).  This is meant to be used internally
     * and is not localized.
     * @param date
     * @return
     */
    public static String debug_getDateAsString(Calendar date) {
        // Not for display purposes.  Increment month by 1 for debugging purposes, otherwise it's confusing to read
        return date.get(Calendar.YEAR)+"_"+(date.get(Calendar.MONTH)+1)+"_"+date.get(Calendar.DAY_OF_MONTH);
    }
    
    public static String debug_getDateAsString(long dateMillis) {
        Calendar date = getCalendar(dateMillis);
        return debug_getDateAsString(date);
    }
    
    public static long getDateNormalizedToDay(long dateMillis) {
        Calendar date = getCalendar(dateMillis);
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        Calendar normalized = Calendar.getInstance();
        normalized.clear();
        normalized.set(year, month, day);
        return normalized.getTimeInMillis();
    }
    
    public static Calendar getCalendar(long timeInMillis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timeInMillis);
        return date;
    }
    
    public static long getStartOfHour(long timeInMillis) {
        return ONE_HOUR*(int)(timeInMillis/ONE_HOUR);
    }
    
    /**
     * Returns the current timezone offset as an int.  For example EDT would be -400
     * @return
     */
    public static int getTimezoneOffset(long time) {
    	return getTimezoneOffset(time, TimeZone.getDefault());
    }
    
    public static int getTimezoneOffset(long time, TimeZone timezone) {
    	int offsetInMillis = timezone.getOffset(time);
    	int numMinutes = offsetInMillis/(1000*60);
    	int offset = 100*(int)(numMinutes/60) + (numMinutes % 60);
    	return offset;
    }

}
