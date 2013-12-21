package com.yooiistudios.morningkit.alarm.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.alarm.model.string.MNAlarmToast;
import com.yooiistudios.morningkit.alarm.model.wake.MNAlarmManager;
import com.yooiistudios.morningkit.main.MNMainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim on 2013. 11. 11..
 *
 * MNAlarm
 *  알람 자료구조
 */
public class MNAlarm implements Serializable, Cloneable {
    private static final String TAG = "MNAlarm";

    /**
     * Variables
     */
    @Getter @Setter private boolean             isAlarmOn;
    @Getter @Setter private boolean             isSnoozeOn;
    @Getter @Setter private boolean             isRepeatOn;

    @Getter @Setter private ArrayList<Boolean>  alarmRepeatList;

    @Getter @Setter private String              alarmLabel;

    // 한 알람당 8개 할당. n+0번 ~ n+6번: 미반복/월(0번이 월요일이거나 미반복) ~ 일, n+7번: 스누즈
    @Getter @Setter private int                 alarmId;

    @Getter @Setter private Calendar            alarmCalendar;

    // 사운드

    /**
     * Methods
     */
    // 이 메서드 호출을 방지, 일반적으로 MNAlarmMaker에서 생성해 사용할 것
    private MNAlarm() {}

    public static MNAlarm newInstance() {
        MNAlarm alarm = new MNAlarm();
        alarm.alarmRepeatList = new ArrayList<Boolean>(7);
        return alarm;
    }

    private void adjustAlarmCalendar() {
        Calendar newAlarmCalendar = Calendar.getInstance();
        newAlarmCalendar.set(Calendar.HOUR_OF_DAY, alarmCalendar.get(Calendar.HOUR_OF_DAY));
        newAlarmCalendar.set(Calendar.MINUTE, alarmCalendar.get(Calendar.MINUTE));
        newAlarmCalendar.set(Calendar.SECOND, 0);
        alarmCalendar = newAlarmCalendar;

        if (alarmCalendar.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
            alarmCalendar.add(Calendar.DATE, 1);
        }
    }

    public void stopAlarm(Context context) {
        isAlarmOn = false;

        Intent intent = new Intent(context, MNMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarmId, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = MNAlarmManager.getAlarmManager(context);
        alarmManager.cancel(pendingIntent);
    }

    public void startAlarm(Context context) {
        isAlarmOn = true;

        adjustAlarmCalendar();

        if (isRepeatOn) {
            startRepeatAlarm(context);
        } else {
            startNonRepeatAlarm(context);
            MNAlarmToast.show(context, alarmCalendar);
        }
        // 현재 시간과 비교하여 오늘, 내일 판단하기
        /*
        Calendar currentTimeCalendar = Calendar.getInstance();
        if (alarmCalendar.getTimeInMillis() > currentTimeCalendar.getTimeInMillis()) {
        }else{
            alarmCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        */
    }

    private void startNonRepeatAlarm(Context context) {
        AlarmManager alarmManager = MNAlarmManager.getAlarmManager(context);

        Intent intent = new Intent(context, MNMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MN.alarm.ALARM_ID, alarmId);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarmId, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
    }

    private void startRepeatAlarm(Context context) {

    }

    public void snoozeAlarm() {

    }

    // 혹시나 깊은 복사를 사용할 경우를 대비해서 가져옴
    // 아래 주석은 Eclipse에서 그대로 가져옴
    @SuppressWarnings("unchecked")
    public MNAlarm clone() throws CloneNotSupportedException {
        MNAlarm obj = (MNAlarm)super.clone();
        obj.alarmCalendar = (Calendar) alarmCalendar.clone();
        obj.alarmRepeatList = (ArrayList<Boolean>) alarmRepeatList.clone();

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
