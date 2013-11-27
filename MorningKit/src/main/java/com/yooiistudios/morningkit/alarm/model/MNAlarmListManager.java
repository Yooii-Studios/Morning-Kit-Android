package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.common.serialize.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by StevenKim on 2013. 11. 11..
 */
public class MNAlarmListManager {
    private static final String TAG = "MNAlarmListManager";

    /**
     * Singleton
     */
    private volatile static MNAlarmListManager instance;
    private volatile SharedPreferences prefs;
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
     * Static Method
     */

    @SuppressWarnings("unchecked")
    public static ArrayList<MNAlarm> loadAlarmList(Context context) {
        ArrayList<MNAlarm> alarmList = null;

        try {
            String alarmListDataString = MNAlarmListManager.getInstance(context).prefs.getString(MN.alarm.ALARM_LIST, null);
            if (alarmListDataString != null) {
                alarmList = (ArrayList<MNAlarm>) ObjectSerializer.deserialize(alarmListDataString);

//                alarmList = (ArrayList<MNAlarm>) ObjectSerializer
//                        .deserialize(prefs.getString(MN.alarm.ALARM_LIST,
//                                ObjectSerializer
//                                        .serialize(new ArrayList<MNAlarm>())));
            }else{
                alarmList = new ArrayList<MNAlarm>();

                MNAlarm firstAlarm = MNAlarmMaker.makeAlarmWithTime(context, 6, 30);
                MNAlarm secondAlarm = MNAlarmMaker.makeAlarmWithTime(context, 7, 0);

                alarmList.add(firstAlarm);
                alarmList.add(secondAlarm);
                MNAlarmListManager.saveAlarmList(alarmList, context);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return alarmList;
    }

    public static void saveAlarmList(ArrayList<MNAlarm> alarmList, Context context) throws IOException {
        SharedPreferences.Editor editor = MNAlarmListManager.getInstance(context).prefs.edit();
        if (alarmList != null) {
            editor.putString(MN.alarm.ALARM_LIST, ObjectSerializer.serialize(alarmList));
        }else{
            editor.remove(MN.alarm.ALARM_LIST);
        }
        editor.commit();
    }

    public static ArrayList<MNAlarm> sortAlarmList(ArrayList<MNAlarm> alarmList, Context context) {
        ArrayList<MNAlarm> sortedAlarmList = null;

        return sortedAlarmList;
    }
}
