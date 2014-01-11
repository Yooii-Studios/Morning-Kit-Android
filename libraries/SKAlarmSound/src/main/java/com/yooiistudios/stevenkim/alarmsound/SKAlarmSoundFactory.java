package com.yooiistudios.stevenkim.alarmsound;

import android.content.Context;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundFactory
 *  Class that makes preset Alarm Sounds
 */
public class SKAlarmSoundFactory {
    private SKAlarmSoundFactory() { throw new AssertionError("You MUST not craete class!"); }

    /**
     * make the default alarm sound(system default ringtone) from device.
     * @param context Context to access the Android
     * @return default SKAlarmSound
     */
    public static SKAlarmSound makeDefaultAlarmSound(Context context) {
        // get default sound stuff from the device(Context)
        String soundTitle = context.getString(R.string.default_string);
        String soundPath = "content://settings/system/ringtone";

        // init defaultAlarmSound
        return SKAlarmSound.newInstance(SKAlarmSoundType.RINGTONE, soundTitle, soundPath);
    }

    /**
     * make preset mute alarm sound
     * @param context Context to access the Android
     * @return mute alarm sound
     */
    public static SKAlarmSound makeMuteAlarmSound(Context context) {
        return SKAlarmSound.newInstance(SKAlarmSoundType.MUTE, context.getString(R.string.alarm_sound_string_none), null);
    }
}
