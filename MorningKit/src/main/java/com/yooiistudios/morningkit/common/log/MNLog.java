package com.yooiistudios.morningkit.common.log;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 18.
 *
 * MNLog
 *  앱의 전체적인 로그를 관리. 출시 시 끌 수 있음
 */
public class MNLog {
    private MNLog() { throw new AssertionError("You MUST not create this class!"); }
    public static final boolean isDebug = false;
    public static final String PREF_TEST_PREF_LOG = "PREF_TEST_PREF_LOG";
    public static final String KEY_LOG = "KEY_LOG";

    public static void now(String message) {
        if (isDebug) {
            Log.i("MNLog", message);
        }
    }
    public static void i(String TAG, String message) {
        if (isDebug) {
            Log.i(TAG, message);
        }
    }
    public static void e(String TAG, String message) {
        if (isDebug) {
            Log.e(TAG, message);
        }
    }

    public static void addTestPrefLog(Context context, String testLog) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_TEST_PREF_LOG, Context.MODE_PRIVATE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        prefs.edit().putString(KEY_LOG, prefs.getString(KEY_LOG, "") +
                dateFormat.format(Calendar.getInstance().getTime()) + ": " + testLog + "\n").apply();
    }

    public static void removeTestPrefLog(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_TEST_PREF_LOG, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_LOG).apply();
    }
}
