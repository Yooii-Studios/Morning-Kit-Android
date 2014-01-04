package com.yooiistudios.stevenkim.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 19.
 *
 * MNAlarmManager
 *  Handling logic for AlarmManager
 */
public class SKAlarmManager {

    public static final String ALARM_ID = "ALARM_ID";
    public static final String ALARM_UNIQUE_ID = "ALARM_UNIQUE_ID";
    private AlarmManager alarmManager;

    /**
     * Singleton
     */
    private volatile static SKAlarmManager instance;
    private SKAlarmManager() {}
    public static SKAlarmManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SKAlarmManager.class) {
                if (instance == null) {
                    instance = new SKAlarmManager();
                    instance.alarmManager =
                            (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                }
            }
        }
        return instance;
    }
    public static AlarmManager getAlarmManager(Context context) {
        return SKAlarmManager.getInstance(context).alarmManager;
    }

    /**
     * Add an alarm into AlarmManager
     *
     * @param alarmUniqueId  unique Id to distinguish between alarms
     * @param alarmId  this is used for repeated alarms in one alarm to distinguish each others
     * @param calendar Calendar instance to set into an alarm
     * @param context used to get AlarmManager
     * @param activity Class to insert into Intent and used as Context
     */
    public static void setAlarm(int alarmUniqueId, int alarmId, Calendar calendar, Context context, Class activity) {
        AlarmManager alarmManager = SKAlarmManager.getAlarmManager(context);

        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ALARM_UNIQUE_ID, alarmUniqueId);
        intent.putExtra(ALARM_ID, alarmId);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, alarmId, intent, PendingIntent.FLAG_ONE_SHOT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    /**
     * Cancel an alarm from AlarmManager
     *
     * @param alarmId  unique Id to distinguish between alarms
     * @param context used to get AlarmManager
     * @param activity Class to insert into Intent and used as Context
     */
    public static void cancelAlarm(int alarmId, Context context, Class activity) {
        Intent intent = new Intent(context, activity);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, alarmId, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = SKAlarmManager.getAlarmManager(context);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Check the Calendar instance and add one day if this is set before now
     * 캘린더 인스턴스를 체크해서 지금 시간보다 이전으로 설정되어 있으면 하루를 더 더해주기
     *
     * @param calendar Calendar to check
     * @return Calendar
     */
    public static Calendar adjustCalendar(Calendar calendar) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        newCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        newCalendar.set(Calendar.SECOND, 0);

        if (calendar.before(newCalendar)) {
            newCalendar.add(Calendar.DATE, 1);
        } else {
            return calendar;
        }

        return newCalendar;
    }
}
