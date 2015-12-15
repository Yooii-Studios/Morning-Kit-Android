package com.yooiistudios.stevenkim.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 19.
 *
 * MNAlarmManager
 *  Handling logic for AlarmManager
 */
public class SKAlarmManager {
    public static final String PREFS_ALARM_BUFFER = "PREFS_ALARM_BUFFER";
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
                PendingIntent.getActivity(context, alarmUniqueId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Over Marshmallow, you must use 'setAlarmClock' to avoid DOZE mode
        // Over KitKat, you must use 'setExact' to invoke alarm on exact time
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // showIntent argument of AlarmClockInfo tells the OS what to do when the user
            // taps on the alarm clock info in the notification drawer or the lock screen.
            // use 'null' when you don't need one.
            AlarmManager.AlarmClockInfo alarmClockInfo =
                    new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null);
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * Cancel an alarm from AlarmManager
     *
     * @param alarmUniqueId  unique Id to distinguish between alarms
     * @param context used to get AlarmManager
     * @param activity Class to insert into Intent and used as Context
     */
    public static void cancelAlarm(int alarmUniqueId, Context context, Class activity) {
        Intent intent = new Intent(context, activity);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, alarmUniqueId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = SKAlarmManager.getAlarmManager(context);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    /**
     * Check the Calendar instance and add one day if this is set before now
     * 캘린더 인스턴스를 체크해서 지금 시간보다 이전으로 설정되어 있으면 하루를 더 더해주기
     *
     * @param calendar Calendar to check
     * @return Calendar
     */
    public static Calendar adjustCalendar(Calendar calendar) {

        final boolean ALARM_DEBUG = false;

        // 알람 캘린더의 날짜는 오늘(같은 날)로 맞추고 비교
        Calendar todayCalendar = Calendar.getInstance();

        if (ALARM_DEBUG) {
            // 테스트용으로 무조건 3초 뒤 울리게 구현
            todayCalendar.add(Calendar.SECOND, 3);
            return todayCalendar;
        }

        calendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, todayCalendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
//        Log.i("SKAlarmManager", "todayCalendar: " + dateFormat.format(todayCalendar.getTime()) + "/" + todayCalendar.getTimeInMillis());
//        Log.i("SKAlarmManager", "alarmCalendar: " + dateFormat.format(calendar.getTime()) + "/" + calendar.getTimeInMillis());

        // 같은 날의 시간을 비교해서 시간이 더 앞이라면 1일을 더해주기
        if (!calendar.after(todayCalendar)) {
//            Log.i("SKAlarmManager", "plus 1 day");
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
            newCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
            newCalendar.set(Calendar.SECOND, 0);
            newCalendar.set(Calendar.MILLISECOND, 0);
            newCalendar.add(Calendar.DATE, 1);
            return newCalendar;
        } else {
            return calendar;
        }
    }
}
