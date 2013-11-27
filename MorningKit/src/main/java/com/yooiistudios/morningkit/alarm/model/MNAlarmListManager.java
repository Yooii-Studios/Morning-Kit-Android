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
                    instance.alarmList = loadAlarmList(context);
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
        return MNAlarmListManager.getInstance(context).alarmList;
    }

    /**
     * load alarmList(ArrayList<MNAlarm>) from SharedPreferences using ObjectSerializer.
     * @param context used to get SharedPreferences
     * @return ArrayList<MNAlarm>, and if it's first load, two alarms will be added automatically.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<MNAlarm> loadAlarmList(Context context) {
//        ArrayList<MNAlarm> alarmList = null;

        if (MNAlarmListManager.getInstance(context).prefs != null) {
            try {
                String alarmListDataString = MNAlarmListManager.getInstance(context).prefs.getString(MN.alarm.ALARM_LIST, null);
                if (alarmListDataString != null) {
                    MNAlarmListManager.getInstance(context).alarmList = (ArrayList<MNAlarm>) ObjectSerializer.deserialize(alarmListDataString);

//                alarmList = (ArrayList<MNAlarm>) ObjectSerializer
//                        .deserialize(prefs.getString(MN.alarm.ALARM_LIST,
//                                ObjectSerializer
//                                        .serialize(new ArrayList<MNAlarm>())));
                }else{
                    MNAlarmListManager.getInstance(context).alarmList = new ArrayList<MNAlarm>();

                    MNAlarm firstAlarm = MNAlarmMaker.makeAlarmWithTime(context, 6, 30);
                    MNAlarm secondAlarm = MNAlarmMaker.makeAlarmWithTime(context, 7, 0);

                    MNAlarmListManager.getInstance(context).alarmList.add(firstAlarm);
                    MNAlarmListManager.getInstance(context).alarmList.add(secondAlarm);
                    MNAlarmListManager.saveAlarmList(context);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return MNAlarmListManager.getInstance(context).alarmList;
    }

    /**
     * save alarmList(ArrayList<MNAlarm>) to SharedPreferences using ObjectSerializer.
     * @param context used to get SharedPreferences
     * @throws IOException
     */
    public static void saveAlarmList(Context context) throws IOException {
        SharedPreferences.Editor editor = MNAlarmListManager.getInstance(context).prefs.edit();
        if (editor != null) {
            if (MNAlarmListManager.getInstance(context).alarmList != null) {
                editor.putString(MN.alarm.ALARM_LIST, ObjectSerializer.serialize(MNAlarmListManager.getInstance(context).alarmList));
            }else{
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
