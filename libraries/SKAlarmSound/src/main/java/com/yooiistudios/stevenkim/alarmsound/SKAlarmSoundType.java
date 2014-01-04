package com.yooiistudios.stevenkim.alarmsound;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundType
 */
public enum SKAlarmSoundType {
    MUTE(0, "MUTE"), RINGTONE(1, "RINGTONE"),
    MUSIC(2, "MUSIC"), APP_MUSIC(3, "APP_MUSIC");

    private final int index;
    private final String typeName;
    public int getIndex() { return index; }
    public String toString() { return typeName; }

    SKAlarmSoundType(int index, String typeName) {
        this.index = index;
        this.typeName = typeName;
    }

    public static SKAlarmSoundType fromInteger(int index) {
        switch(index) {
            case 0:
                return MUTE;
            case 1:
                return RINGTONE;
            case 2:
                return MUSIC;
            case 3:
                return APP_MUSIC;
        }
        return null;
    }
}
