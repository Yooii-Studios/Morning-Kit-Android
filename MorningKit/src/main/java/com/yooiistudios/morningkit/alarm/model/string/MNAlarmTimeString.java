package com.yooiistudios.morningkit.alarm.model.string;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 18.
 *
 * MNAlarmTimeString
 *  hourOfDay, minute -> String 으로 변환하는 유틸리티 클래스
 */
public class MNAlarmTimeString {
    private static final String TAG = "MNAlarmTimeString";
    private MNAlarmTimeString() { throw new AssertionError("You MUST NOT create this class!"); }

    public static String makeTimeString(Calendar alarmCalendar, Context context) {
        if (DateFormat.is24HourFormat(context)) {
            return makeTimeStringFor24HourFormat(alarmCalendar, context);
        } else {
            return makeTimeStringFor12HourFormat(alarmCalendar, context);
        }
    }

    /**
     * This is for Unit test. Never use these.
     */
    public static String makeTimeStringForTest(Calendar alarmCalendar, Context context, boolean is24HourFormat) {
        if (is24HourFormat) {
            return makeTimeStringFor24HourFormat(alarmCalendar, context);
        } else {
            return makeTimeStringFor12HourFormat(alarmCalendar, context);
        }
    }

    private static String makeTimeStringFor24HourFormat(Calendar alarmCalendar, Context context) {
        int hourOfDay = alarmCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = alarmCalendar.get(Calendar.MINUTE);

        String hourString, minuteString;
        if (hourOfDay >= 10) {
            hourString = String.valueOf(hourOfDay);
        } else {
            hourString = "0" + hourOfDay;
        }
        if (minute >= 10) {
            minuteString = String.valueOf(minute);
        } else {
            minuteString = "0" + minute;
        }
        return String.format(hourString + ":" + minuteString);
    }

    private static String makeTimeStringFor12HourFormat(Calendar alarmCalendar, Context context) {
        int hourOfDay = alarmCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = alarmCalendar.get(Calendar.MINUTE);

        int hour = hourOfDay;
        if (hour > 12) {
            hour -= 12;
        } else if (hour == 0) {
            hour += 12;
        }

        String hourString, minuteString;
        if (hour >= 10) {
            hourString = String.valueOf(hour);
        } else {
            hourString = "0" + hour;
        }
        if (minute >= 10) {
            minuteString = String.valueOf(minute);
        } else {
            minuteString = "0" + minute;
        }
        return String.format(hourString + ":" + minuteString);
    }
}
