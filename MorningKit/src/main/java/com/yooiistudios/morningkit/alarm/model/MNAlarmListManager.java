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

    @SuppressWarnings("unchecked")
    public static ArrayList<MNAlarm> loadAlarmList(Context context) {
        ArrayList<MNAlarm> alarmList = null;

        SharedPreferences prefs = context.getSharedPreferences(MN.alarm.SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        try {
            String alarmListDataString = prefs.getString(MN.alarm.ALARM_LIST, null);
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
        SharedPreferences prefs = context.getSharedPreferences(MN.alarm.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
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
