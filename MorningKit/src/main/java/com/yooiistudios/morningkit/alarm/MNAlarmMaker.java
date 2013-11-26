package com.yooiistudios.morningkit.alarm;

import android.content.Context;

import java.util.Calendar;

/**
 * Created by StevenKim on 2013. 11. 11..
 */
public class MNAlarmMaker {
    private static final String TAG = "MNAlarmMaker";

    public static MNAlarm makeAlarm(Context context) {
        MNAlarm alarm = MNAlarm.newInstance();

        if (alarm != null) {
            alarm.isAlarmOn = true;
            alarm.isSnoozeOn = false;
            alarm.isRepeatOn = false;
            alarm.alarmLabel = "Alarm";
            alarm.alarmCalendar = Calendar.getInstance();

            for (int i=0; i<7; i++) {
                alarm.alarmRepeatOnOfWeek.add(Boolean.FALSE);
            }

            alarm.alarmID = MNAlarmIdMaker.getValidAlarmID(context);
        }

        return alarm;
    }

    public static MNAlarm makeAlarmWithTime(Context context, int hour, int minute) {
        MNAlarm alarm = MNAlarmMaker.makeAlarm(context);
        if (alarm != null) {
            alarm.isAlarmOn = false;

        }
        return alarm;
    }
}
