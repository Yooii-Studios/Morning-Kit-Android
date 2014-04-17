package com.yooiistudios.morningkit.common.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 11. 28.
 *
 * MNSharedPreferences
 *  모든 SharedPreferences 접근을 이곳에서 관장
 */
public class MNSharedPreferences {
    public static final String SHARED_PREFS_FILE = "SHARED_PREFS_FILE";

    private MNSharedPreferences() { throw new AssertionError(); } // You must not create instance

    static volatile SharedPreferences alarmSharedPrefs;
    public static SharedPreferences getAlarmSharedPrefs(Context context) {
        if (alarmSharedPrefs == null) {
            alarmSharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        }
        return alarmSharedPrefs;
    }
}
