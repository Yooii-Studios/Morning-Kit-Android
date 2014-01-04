package com.yooiistudios.stevenkim.alarmsound;

import java.io.Serializable;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * AlarmSound
 *  Model
 */
public class SKAlarmSound implements Serializable {

    private SKAlarmSoundType alarmSoundType;
    private String soundTitle;
    private String soundPath;

    private SKAlarmSound() {}
    private SKAlarmSound(SKAlarmSoundType alarmSoundType, String soundTitle, String soundPath) {
        setAlarmSoundType(alarmSoundType);
        setSoundTitle(soundTitle);
        setSoundPath(soundPath);
    }

    // construction method
    public static SKAlarmSound newInstance(SKAlarmSoundType alarmSoundType, String soundTitle, String soundPathString) {
        return new SKAlarmSound(alarmSoundType, soundTitle, soundPathString);
    }

    /**
     * Getter & Setter
     */
    public SKAlarmSoundType getAlarmSoundType() {
        return alarmSoundType;
    }

    public void setAlarmSoundType(SKAlarmSoundType alarmSoundType) {
        this.alarmSoundType = alarmSoundType;
    }

    public String getSoundTitle() {
        return soundTitle;
    }

    public void setSoundTitle(String soundTitle) {
        this.soundTitle = soundTitle;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }
}
