package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;

import java.util.Calendar;

/**
 * Created by StevenKim on 2013. 11. 11..
 * MNAlarmMaker
 */
public class MNAlarmMaker {
    private static final String TAG = "MNAlarmMaker";

    private MNAlarmMaker() { throw new AssertionError(); } // You shouldn't create instance

    public static MNAlarm makeAlarm(Context context) {
        MNAlarm alarm = MNAlarm.newInstance();

        if (alarm != null) {
            alarm.isAlarmOn = true;
            alarm.isSnoozeOn = false;
            alarm.isRepeatOn = false;
            alarm.alarmLabel = "Alarm";
            alarm.alarmCalendar = Calendar.getInstance();
            alarm.alarmCalendar.set(Calendar.SECOND, 0);

            for (int i=0; i<7; i++) {
                alarm.alarmRepeatOnOfWeek.add(Boolean.FALSE);
            }

            alarm.alarmId = MNAlarmIdMaker.getValidAlarmID(context);
        }

        return alarm;
    }

    public static MNAlarm makeAlarmWithTime(Context context, int hourOfDay, int minute) {
        MNAlarm alarm = MNAlarmMaker.makeAlarm(context);
        if (alarm != null) {
            alarm.isAlarmOn = false;

            alarm.alarmCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            alarm.alarmCalendar.set(Calendar.MINUTE, minute);
        }
        return alarm;
    }
}
