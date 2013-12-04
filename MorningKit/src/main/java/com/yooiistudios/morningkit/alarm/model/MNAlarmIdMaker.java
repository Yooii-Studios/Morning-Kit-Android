package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.common.sharedpreferences.MNSharedPreferences;

/**
 * Created by StevenKim on 2013. 11. 26..
 *
 * MNAlarmIdMaker (유틸리티 클래스)
 *  유효한 unique & auto-increase Alarm ID를 생성
 */
public class MNAlarmIdMaker {

    private MNAlarmIdMaker() { throw new AssertionError(); } // You must not create instance

    /**
     * get validate Id automatically. increase 8 for each id.
     * @param context used to get SharedPreferences
     * @return auto-increase id or -1 if error happens.
     */
    public static int getValidAlarmID(Context context) {

        SharedPreferences prefs = MNSharedPreferences.getAlarmSharedPrefs(context);
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
