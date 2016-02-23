package com.yooiistudios.stevenkim.alarmsound;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

import com.crashlytics.android.Crashlytics;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundManager
 *  Handle the logic about sound
 */
public class SKAlarmSoundManager {

//    private static final String TAG = "SKAlarmSoundManager";
//    private SKAlarmSoundManager() { throw new AssertionError("You MUST not craete class!"); }
    private static final String ALARM_SOUND_TYPE = "SK_ALARM_SOUND_TYPE";
    private static final String ALARM_SOUND_TITLE = "SK_ALARM_SOUND_TITLE";
    private static final String ALARM_SOUND_PATH = "SK_ALARM_SOUND_PATH";

    /**
     * Singleton
     */
    private volatile static SKAlarmSoundManager instance;
    private SharedPreferences sharedPreferences;
    public static SharedPreferences getSharedPreferences(Context context) { return getInstance(context).sharedPreferences; }
    private SKAlarmSoundManager() {}
    public static SKAlarmSoundManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SKAlarmSoundManager.class) {
                if (instance == null) {
                    instance = new SKAlarmSoundManager();
                    instance.sharedPreferences = context.getSharedPreferences("SKAlarmSoundManager", Context.MODE_PRIVATE);
                }
            }
        }
        return instance;
    }

    /**
     * Validate sound source path if exist.
     * @param path Path to find sound source
     * @param context Context to access the Android
     * @return true when sound is playable
     */
    public static boolean isValidAlarmSoundPath(String path, Context context) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = { MediaStore.MediaColumns._ID };
        try {
            Cursor cur = cr.query(Uri.parse(path), projection, null, null, null);
            if (cur != null) {
                boolean isAlarmSoundExist = cur.getCount() > 0;
                cur.close();
                return isAlarmSoundExist;
            } else {
                // content Uri was invalid or some other error occurred
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            Crashlytics.getInstance().core.logException(e);
            Crashlytics.getInstance().core.log("Sound path: " + path);
            return false;
        }
    }

    /**
     * save latest alarm sound selection
     *
     * @param alarmSound SKAlarmSound to be archived
     * @param context Context to access the Android
     */
    public static void saveLatestAlarmSound(SKAlarmSound alarmSound, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(ALARM_SOUND_TYPE, alarmSound.getAlarmSoundType().getIndex());
        editor.putString(ALARM_SOUND_TITLE, alarmSound.getSoundTitle());
        editor.putString(ALARM_SOUND_PATH, alarmSound.getSoundPath());
        editor.apply();
    }

    /**
     * Return the latest SKAlarmSound instance from the device.
     * If the one doesn't exist, return the one with the system default ringtone.
     * If the one isn't validate, also return the default ringtone
     *
     * @param context Context to access the Android
     * @return SKAlarmSound which is selected recently.
     */
    public static SKAlarmSound loadLatestAlarmSound(Context context) {
        int soundTypeInt = getSharedPreferences(context).getInt(ALARM_SOUND_TYPE, -1);

        if (soundTypeInt != -1) {
            String soundPath, soundTitle;
            soundTitle = getSharedPreferences(context).getString(ALARM_SOUND_TITLE, context.getString(R.string.default_string));
            soundPath = getSharedPreferences(context).getString(ALARM_SOUND_PATH,
                    Settings.System.DEFAULT_RINGTONE_URI.toString());

            try {
                if (isValidAlarmSoundPath(soundPath, context)) {
                    return SKAlarmSound.newInstance(SKAlarmSoundType.fromInteger(soundTypeInt), soundTitle, soundPath);
                } else {
                    return SKAlarmSoundFactory.makeDefaultAlarmSound(context);
                }
            } catch (Exception e) {
                return SKAlarmSoundFactory.makeDefaultAlarmSound(context);
            }
        } else {
            return SKAlarmSoundFactory.makeDefaultAlarmSound(context);
        }
    }

    /**
     * Clear latest alarm sound stuff from SharedPreferences
     *
     * @param context Context to access the Android
     */
//    public static void clearLatestAlarmSound(Context context) {
//        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
//        editor.remove(ALARM_SOUND_TYPE);
//        editor.remove(ALARM_SOUND_TITLE);
//        editor.remove(ALARM_SOUND_PATH);
//        editor.apply();
//    }
}
