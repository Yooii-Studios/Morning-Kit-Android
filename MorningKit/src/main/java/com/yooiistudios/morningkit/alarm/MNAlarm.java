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
    // 이 메서드 호출을 방지, 일반적으로 MNAlarmMaker에서 생성해 사용할 것
    private MNAlarm() {}

    public static MNAlarm newInstance() {
        MNAlarm alarm = new MNAlarm();
        alarm.alarmRepeatOnOfWeek = new ArrayList<Boolean>();
        return alarm;
    }

    public void stopAlarm() {

    }

    public void startAlarm() {

    }

    public void snoozeAlarm() {

    }

    // 혹시나 깊은 복사를 사용할 경우를 대비해서 가져옴
    // 아래 주석은 Eclipse에서 그대로 가져옴
//    @SuppressWarnings("unchecked")
    public MNAlarm clone() throws CloneNotSupportedException {
        MNAlarm obj = (MNAlarm)super.clone();
        obj.alarmCalendar = (Calendar) alarmCalendar.clone();
        obj.alarmRepeatOnOfWeek = (ArrayList<Boolean>)alarmRepeatOnOfWeek.clone();

        return obj;
    }
}
