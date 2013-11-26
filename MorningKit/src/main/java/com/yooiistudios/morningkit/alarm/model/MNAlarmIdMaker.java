package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.yooiistudios.morningkit.MN;

/**
 * Created by StevenKim on 2013. 11. 26..
 */
public class MNAlarmIdMaker {
    public static int getValidAlarmID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                MN.alarm.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        int alarmId = prefs.getInt("alarmId", -1);
        if (alarmId == -1) {
            alarmId = 0;
        } else {
            alarmId += 8;
            if (alarmId >= Integer.MAX_VALUE) {
                alarmId = 0;
            }
        }
        prefs.edit().putInt("alarmId", alarmId).commit();

        return alarmId;
    }
}
