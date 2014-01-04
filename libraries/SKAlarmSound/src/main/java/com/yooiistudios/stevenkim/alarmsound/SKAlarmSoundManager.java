package com.yooiistudios.stevenkim.alarmsound;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundManager
 *  Handle the logic about sound
 */
public class SKAlarmSoundManager {

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
    public static boolean validateAlarmSound(String path, Context context) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cur = cr.query(Uri.parse(path), projection, null, null, null);
        if(cur != null)
        {
            boolean success = cur.moveToFirst();
            if (success) {
                String filePath = cur.getString(0);
                return new File(filePath).exists();
            }
//		   Log.i(TAG, "soundSource is not available");
            return false;
        } else {
            // content Uri was invalid or some other error occurred
//			Log.i(TAG, "soundSource is not available");
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
        editor.commit();
    }

    /**
     * Return the lastest SKAlarmSound instance from the device.
     * If the one doesn't exist, return the one with the system default ringtone.
     *
     * @param context Context to access the Android
     * @return SKAlarmSound which is selected recently.
     */
    public static SKAlarmSound loadLatestAlarmSound(Context context) {
        int soundTypeInt = getSharedPreferences(context).getInt(ALARM_SOUND_TYPE, -1);

        if (soundTypeInt != -1) {
            String soundPath, soundTitle;
            soundTitle = getSharedPreferences(context).getString(ALARM_SOUND_TITLE, context.getString(R.string.default_string));
            soundPath = getSharedPreferences(context).getString(ALARM_SOUND_PATH, "content://settings/system/ringtone");
            return SKAlarmSound.newInstance(SKAlarmSoundType.fromInteger(soundTypeInt), soundTitle, soundPath);
        } else {
            return SKAlarmSoundFactory.makeDefaultAlarmSound(context);
        }
    }

    /**
     * Clear lastest alarm sound stuff from SharedPreferences
     *
     * @param context Context to access the Android
     */
    public static void clearLatestAlarmSound(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(ALARM_SOUND_TYPE);
        editor.remove(ALARM_SOUND_TITLE);
        editor.remove(ALARM_SOUND_PATH);
        editor.commit();
    }
}
