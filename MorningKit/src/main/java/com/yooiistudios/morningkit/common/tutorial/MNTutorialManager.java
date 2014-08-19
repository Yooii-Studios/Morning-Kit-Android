package com.yooiistudios.morningkit.common.tutorial;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 6. 2.
 *
 * MNTutorialManager
 *  튜토리얼 상태를 관리
 */
public class MNTutorialManager {
    private static final String PREFS = "MNTutorialManager";
    private static final String KEY_IS_TUTORIAL_SHOWN = "KEY_IS_TUTORIAL_SHOWN";

    public static boolean isTutorialShown(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences("SKAlarmSoundManager", Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_TUTORIAL_SHOWN, false);
    }

    public static void setTutorialShown(Context context) {
        if (context != null) {
            SharedPreferences prefs =
                    context.getSharedPreferences("SKAlarmSoundManager", Context.MODE_PRIVATE);
            prefs.edit().putBoolean(KEY_IS_TUTORIAL_SHOWN, true).apply();
        }
    }

    public static void resetTutorial(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences("SKAlarmSoundManager", Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_IS_TUTORIAL_SHOWN).apply();
    }
}
