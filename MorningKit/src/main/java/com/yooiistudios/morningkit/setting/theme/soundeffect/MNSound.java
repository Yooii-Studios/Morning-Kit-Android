package com.yooiistudios.morningkit.setting.theme.soundeffect;

import android.content.Context;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNSound
 *  사운드 설정을 관리
 */
public class MNSound {
    private static final String SOUND_EFFECTS_SHARED_PREFERENCES = "SOUND_EFFECTS_SHARED_PREFERENCES";
    private static final String SOUND_EFFECTS_KEY= "SOUND_EFFECTS_KEY";

    private volatile static MNSound instance;
    private MNSoundType currentSoundType;

    /**
     * Singleton
     */
    private MNSound(){}
    private MNSound(Context context) {
        int uniqueId = context.getSharedPreferences(SOUND_EFFECTS_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .getInt(SOUND_EFFECTS_KEY, MNSoundType.OFF.getUniqueId());

        currentSoundType = MNSoundType.valueOfUniqueId(uniqueId);
    }

    public static MNSound getInstance(Context context) {
        if (instance == null) {
            synchronized (MNSound.class) {
                if (instance == null) {
                    instance = new MNSound(context);
                }
            }
        }
        return instance;
    }

    public static boolean isSoundOn(Context context) {
        return MNSound.getInstance(context).currentSoundType == MNSoundType.ON;
    }

    public static MNSoundType getCurrentSoundType(Context context) { return MNSound.getInstance(context).currentSoundType; }

    public static void setSoundType(MNSoundType newSoundType, Context context) {
        MNSound.getInstance(context).currentSoundType = newSoundType;
        context.getSharedPreferences(SOUND_EFFECTS_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putInt(SOUND_EFFECTS_KEY, newSoundType.getUniqueId()).apply();
    }
}
