package com.yooiistudios.stevenkim.alarmsound;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundType
 */
public enum SKAlarmSoundType {
    MUSIC(0, "MUSIC"), RINGTONE(1, "RINGTONE"),
    APP_MUSIC(2, "APP_MUSIC"), MUTE(3, "MUTE");

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
                return MUSIC;
            case 1:
                return RINGTONE;
            case 2:
                return APP_MUSIC;
            case 3:
                return MUTE;
        }
        return null;
    }
}
