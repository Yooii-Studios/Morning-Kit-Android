package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;

import java.util.Calendar;

/**
 * Created by StevenKim on 2013. 11. 11..
 *
 * MNAlarmMaker (유틸리티 클래스)
 *  알람을 생성해주는 Factory 클래스
 *  1. 초기화된 현재 시간의 알람 생성
 *  2. 1번 + 특정 시간의 알람 생성
 */
public class MNAlarmMaker {
    private static final String TAG = "MNAlarmMaker";

    private MNAlarmMaker() { throw new AssertionError(); } // You must not create instance

    public static MNAlarm makeAlarm(Context context) {
        MNAlarm alarm = MNAlarm.newInstance();

        if (alarm != null) {
            alarm.setAlarmOn(true);
            alarm.setSnoozeOn(false);
            alarm.setRepeatOn(false);
            alarm.setAlarmLabel("Alarm");
            alarm.setAlarmCalendar(Calendar.getInstance());
            alarm.getAlarmCalendar().set(Calendar.SECOND, 0);

            for (int i=0; i<7; i++) {
                alarm.getAlarmRepeatOnOfWeek().add(Boolean.FALSE);
            }

            alarm.setAlarmId(MNAlarmIdMaker.getValidAlarmID(context));
        }

        return alarm;
    }

    public static MNAlarm makeAlarmWithTime(Context context, int hourOfDay, int minute) {
        MNAlarm alarm = MNAlarmMaker.makeAlarm(context);
        if (alarm != null) {
            alarm.setAlarmOn(false);

            alarm.getAlarmCalendar().set(Calendar.HOUR_OF_DAY, hourOfDay);
            alarm.getAlarmCalendar().set(Calendar.MINUTE, minute);
        }
        return alarm;
    }
}
