package com.yooiistudios.morningkit.alarm;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by StevenKim on 2013. 11. 11..
 */
public class MNAlarm {
    private static final String TAG = "MNAlarm";

    /**
     * Variables
     */
    public boolean             isAlarmOn;
    public boolean             isSnoozeOn;
    public boolean             isRepeatOn;

    public ArrayList<Boolean>  alarmRepeatOnOfWeek;

    public String              alarmLabel;

    // 한 알람당 8개 할당. n+0번 ~ n+6번: 미반복/월(0번이 월요일이거나 미반복) ~ 일, n+7번: 스누즈
    public int                 alarmID;

    public Calendar            alarmCalendar;

    // 사운드

    /**
     * Methods
     */
    public MNAlarm() {

    }

    public void stopAlarm() {

    }

    public void startAlarm() {

    }

    public void snoozeAlarm() {

    }
}
