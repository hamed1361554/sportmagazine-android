package com.mitranetpars.sportmagazine.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ir.smartlab.persiandatepicker.util.PersianCalendar;
import ir.smartlab.persiandatepicker.util.PersianCalendarUtils;

/**
 * Created by Hamed on 1/13/2017.
 */

public class DateTimeUtils {
    private static SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy MMMM dd HH:mm:ss");

    public static String formatDateTime(Date d) {
        return mFormatter.format(d);
    }

    public static String formatDateTimeToPersian(Date d) {
        PersianCalendar pc = new PersianCalendar(d.getTime());

        int year = pc.getPersianYear();
        int month = pc.getPersianMonth();
        int day = pc.getPersianDay();
        if (month > 6 && month < 12 && day == 31) {
            day = 30;
        } else {
            boolean isLeapYear = PersianCalendarUtils.isPersianLeapYear(year);
            if (isLeapYear && day == 31) {
                day = 30;
            } else if (day > 29) {
                day = 29;
            }
        }



        return String.format("%s/%s/%s %s:%s:%s",
                String.format("%d", year),
                String.format("%d", month),
                String.format("%d", day),
                String.format("%02d", pc.get(Calendar.HOUR_OF_DAY)),
                String.format("%02d", pc.get(Calendar.MINUTE)),
                String.format("%02d", pc.get(Calendar.SECOND)));
    }

    public static String formatDateOnlyToPersian(Date d) {
        PersianCalendar pc = new PersianCalendar(d.getTime());

        int year = pc.getPersianYear();
        int month = pc.getPersianMonth();
        int day = pc.getPersianDay();
        if (month > 6 && month < 12 && day == 31) {
            day = 30;
        } else {
            boolean isLeapYear = PersianCalendarUtils.isPersianLeapYear(year);
            if (isLeapYear && day == 31) {
                day = 30;
            } else if (day > 29) {
                day = 29;
            }
        }

        return String.format("%s/%s/%s",
                String.format("%d", year),
                String.format("%d", month),
                String.format("%d", day));
    }
}
