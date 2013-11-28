package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.yooiistudios.morningkit.MN;

/**
 * Created by StevenKim on 2013. 11. 26..
 * MNAlarmIdMaker
 */
public class MNAlarmIdMaker {

    private static SharedPreferences prefs;
    private MNAlarmIdMaker() { throw new AssertionError(); } // You shouldn't create instance

    /**
     * get validate Id automatically. increase 8 for each id.
     * @param context used to get SharedPreferences
     * @return auto-increase id or -1 if error happens.
     */
    public static int getValidAlarmID(Context context) {

        if (prefs == null) {
            prefs = context.getSharedPreferences(MN.alarm.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        }

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
