package com.yooiistudios.stevenkim.alarmsound;

import android.content.Context;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 3.
 *
 * MNAlarmAppMusicManager
 *  알람 앱 음악 로직을 관리
 */
public class MNAlarmAppMusicManager {
    private MNAlarmAppMusicManager() { throw new AssertionError("You MUST not create this class!"); }

    public static int getRawIntFromIndex(int index) {
        int rawInt = -1;
        switch (index) {
            case 0:
                rawInt = R.raw.alarm_dream;
                break;
        }

        return rawInt;
    }

    /*
    public static String getTitleFromRawInt(Context context, int rawInt) {
        String soundName = "";
        if (rawInt == R.raw.alarm_dream) {
            soundName = context.getString(R.string.alarm_sound_string_app_music_dream);
        }
        return soundName;
    }
    */
}
