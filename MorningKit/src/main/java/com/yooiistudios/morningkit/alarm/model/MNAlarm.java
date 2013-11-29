package com.yooiistudios.morningkit.alarm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by StevenKim on 2013. 11. 11..
 * MNAlarm
 */
public class MNAlarm implements Serializable, Cloneable {
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
    public int                 alarmId;

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

    /**
     * get MNAlarm instance with specific alarmId from alarmList
     * @param alarmId unique identifier for MNAlarm
     * @param alarmList ArrayList that contains MNAlarm
     * @return MNAlarm
     */
    public static MNAlarm getInstance(int alarmId, ArrayList<MNAlarm> alarmList) {
        MNAlarm alarmToFind = null;
        for (MNAlarm alarm : alarmList ) {
            if (alarm.alarmId == alarmId) {
                alarmToFind = alarm;
            }
        }
        return alarmToFind;
    }

    public void stopAlarm() {

    }

    public void startAlarm() {
        // 현재 시간과 비교하여 오늘, 내일 판단하기
        /*
        Calendar currentTimeCalendar = Calendar.getInstance();
        if (alarmCalendar.getTimeInMillis() > currentTimeCalendar.getTimeInMillis()) {
        }else{
            alarmCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        */
    }

    public void snoozeAlarm() {

    }

    // 혹시나 깊은 복사를 사용할 경우를 대비해서 가져옴
    // 아래 주석은 Eclipse에서 그대로 가져옴
    @SuppressWarnings("unchecked")
    public MNAlarm clone() throws CloneNotSupportedException {
        MNAlarm obj = (MNAlarm)super.clone();
        obj.alarmCalendar = (Calendar) alarmCalendar.clone();
        obj.alarmRepeatOnOfWeek = (ArrayList<Boolean>)alarmRepeatOnOfWeek.clone();

        return obj;
    }
    
    @Override
    public String toString() {
        return String.format("alarmId: %d / alarmLabel: %s / on: %s, repeat: %s / ",
                alarmId,
                alarmLabel,
                isAlarmOn ? "Yes" : "No",
                isRepeatOn ? "Yes" : "No");
    }
}
