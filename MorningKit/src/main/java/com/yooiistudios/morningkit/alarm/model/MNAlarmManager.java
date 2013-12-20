package com.yooiistudios.morningkit.alarm.model;

import android.app.AlarmManager;
import android.content.Context;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 19.
 *
 * MNAlarmManager
 *  알람매니저의 인스턴스를 가진 싱글턴 클래스
 */
public class MNAlarmManager {

    private AlarmManager alarmManager;

    /**
     * Singleton
     */
    private volatile static MNAlarmManager instance;
    private MNAlarmManager() {}
    public static MNAlarmManager getInstance(Context context) {
        if (instance == null) {
            synchronized (MNAlarmManager.class) {
                if (instance == null) {
                    instance = new MNAlarmManager();
                    instance.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                }
            }
        }
        return instance;
    }

    public static AlarmManager getAlarmManager(Context context) { return MNAlarmManager.getInstance(context).alarmManager; }
}
