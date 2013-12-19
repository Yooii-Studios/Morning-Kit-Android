package com.yooiistudios.morningkit.alarm.model;

import java.util.Calendar;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 19.
 *
 * MNAlarmSortingComparator
 */
public class MNAlarmComparator {
    private MNAlarmComparator() { throw new AssertionError("You MUST NOT create this class"); }

    public static int makeComparator(MNAlarm alarm) {
        int hourOfDay = alarm.getAlarmCalendar().get(Calendar.HOUR_OF_DAY);
        int minute = alarm.getAlarmCalendar().get(Calendar.MINUTE);

        return (hourOfDay * 100) + minute;
    }
}
