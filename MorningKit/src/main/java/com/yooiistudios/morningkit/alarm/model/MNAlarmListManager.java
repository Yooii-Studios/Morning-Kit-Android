package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.common.serialize.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by StevenKim on 2013. 11. 11..
 * MNAlarmListManager
 */
public class MNAlarmListManager {
    private static final String TAG = "MNAlarmListManager";

    /**
     * Singleton
     */
    private volatile static MNAlarmListManager instance;
    private volatile SharedPreferences prefs;
    private volatile ArrayList<MNAlarm> alarmList;
    private MNAlarmListManager() {}
    public static MNAlarmListManager getInstance(Context context) {
        if (instance == null) {
            synchronized (MNAlarmListManager.class) {
                if (instance == null) {
                    instance = new MNAlarmListManager();
                    instance.prefs = context.getSharedPreferences(MN.alarm.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
                }
            }
        }
        return instance;
    }

    /**
     * get Singleton alarmList
     * @param context used to get SharedPreferences
     * @return ArrayList<MNAlarm>
     */
    public static ArrayList<MNAlarm> alarmList(Context context) {
        if (getInstance(context).alarmList == null) {
            getInstance(context).alarmList = loadAlarmList(context);
        }
        return getInstance(context).alarmList;
    }

    /**
     * load alarmList(ArrayList<MNAlarm>) from SharedPreferences using ObjectSerializer.
     * @param context used to get SharedPreferences
     * @return ArrayList<MNAlarm>, and if it's first load, two alarms will be added automatically.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<MNAlarm> loadAlarmList(Context context) {
        if (getInstance(context).prefs != null) {
            try {
                String alarmListDataString = getInstance(context).prefs.getString(MN.alarm.ALARM_LIST, null);
                if (alarmListDataString != null) {
                    getInstance(context).alarmList = (ArrayList<MNAlarm>) ObjectSerializer.deserialize(alarmListDataString);
                } else {
                    getInstance(context).alarmList = new ArrayList<MNAlarm>();

                    MNAlarm firstAlarm = MNAlarmMaker.makeAlarmWithTime(context, 6, 30);
                    MNAlarm secondAlarm = MNAlarmMaker.makeAlarmWithTime(context, 7, 0);

                    getInstance(context).alarmList.add(firstAlarm);
                    getInstance(context).alarmList.add(secondAlarm);
                    saveAlarmList(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getInstance(context).alarmList;
    }

    /**
     * save alarmList(ArrayList<MNAlarm>) to SharedPreferences using ObjectSerializer.
     * @param context used to get SharedPreferences
     * @throws IOException
     */
    public static void saveAlarmList(Context context) throws IOException {
        SharedPreferences.Editor editor = getInstance(context).prefs.edit();
        if (editor != null) {
            if (getInstance(context).alarmList != null) {
                editor.putString(MN.alarm.ALARM_LIST, ObjectSerializer.serialize(getInstance(context).alarmList));
            } else {
                editor.remove(MN.alarm.ALARM_LIST);
            }
            editor.commit();
        }
    }

    public static ArrayList<MNAlarm> sortAlarmList(Context context) {
        ArrayList<MNAlarm> sortedAlarmList = null;

        return sortedAlarmList;
    }
}
