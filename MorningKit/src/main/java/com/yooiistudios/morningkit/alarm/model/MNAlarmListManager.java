package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.common.serialize.ObjectSerializer;
import com.yooiistudios.morningkit.common.sharedpreferences.MNSharedPreferences;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by StevenKim on 2013. 11. 11..
 *
 * MNAlarmListManager
 *  알람 리스트에 관한 모든 것을 관장하는 유틸리티 클래스,
 *  싱글턴으로 알람 리스트를 가지고 있어서 접근 가능
 */
public class MNAlarmListManager {
    private static final String TAG = "MNAlarmListManager";

    /**
     * Singleton
     */
    private volatile static MNAlarmListManager instance;
    private volatile ArrayList<MNAlarm> alarmList;
    private MNAlarmListManager() {}
    public static MNAlarmListManager getInstance() {
        if (instance == null) {
            synchronized (MNAlarmListManager.class) {
                if (instance == null) {
                    instance = new MNAlarmListManager();
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
    public static ArrayList<MNAlarm> getAlarmList(Context context) {
        if (MNAlarmListManager.getInstance().alarmList == null) {
            MNAlarmListManager.getInstance().alarmList = loadAlarmList(context);
        }
        return MNAlarmListManager.getInstance().alarmList;
    }

    /**
     * load alarmList(ArrayList<MNAlarm>) from SharedPreferences using ObjectSerializer.
     * @param context used to get SharedPreferences
     * @return ArrayList<MNAlarm>, and if it's first load, two alarms will be added automatically.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<MNAlarm> loadAlarmList(Context context) {
        try {
            String alarmListDataString = MNSharedPreferences.getAlarmSharedPrefs(context).getString(MN.alarm.ALARM_LIST, null);
            if (alarmListDataString != null) {
                MNAlarmListManager.getInstance().alarmList = (ArrayList<MNAlarm>) ObjectSerializer.deserialize(alarmListDataString);
            } else {
                MNAlarmListManager.getInstance().alarmList = new ArrayList<MNAlarm>();

                MNAlarm firstAlarm = MNAlarmMaker.makeAlarmWithTime(context, 6, 30);
                MNAlarm secondAlarm = MNAlarmMaker.makeAlarmWithTime(context, 7, 0);

                MNAlarmListManager.getInstance().alarmList.add(firstAlarm);
                MNAlarmListManager.getInstance().alarmList.add(secondAlarm);
                saveAlarmList(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MNAlarmListManager.getInstance().alarmList;
    }

    /**
     * save alarmList(ArrayList<MNAlarm>) to SharedPreferences using ObjectSerializer.
     * @param context used to get SharedPreferences
     * @throws IOException
     */
    public static void saveAlarmList(Context context) throws IOException {
        SharedPreferences.Editor editor = MNSharedPreferences.getAlarmSharedPrefs(context).edit();
        if (editor != null) {
            if (MNAlarmListManager.getInstance().alarmList != null) {
                editor.putString(MN.alarm.ALARM_LIST, ObjectSerializer.serialize(MNAlarmListManager.getInstance().alarmList));
            } else {
                editor.remove(MN.alarm.ALARM_LIST);
            }
            editor.commit();
        }
    }

    /**
     * sort alarmList(ArrayList<MNAlarm>) by time string as ascending
     * @param context used to get SharedPreferences
     */
    public static void sortAlarmList(Context context) {
        ArrayList<MNAlarm> sortedAlarmList = null;
    }

    /**
     * find alarm by specific alarmId in alarmList
     * @param targetAlarmId alarmId to be searched
     * @param context used to get SharedPreferences
     * @return MNAlarm
     */
    public static MNAlarm findAlarmById(int targetAlarmId, Context context) {
        if (targetAlarmId != -1) {
            MNAlarm targetAlarm = null;
            for (MNAlarm alarm : MNAlarmListManager.getAlarmList(context)) {
                if (alarm.getAlarmId() == targetAlarmId) {
                    targetAlarm = alarm;
                }
            }
            return targetAlarm;
        } else {
            return null;
        }
    }
}
