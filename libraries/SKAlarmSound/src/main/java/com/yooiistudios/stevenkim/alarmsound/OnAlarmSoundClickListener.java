package com.yooiistudios.stevenkim.alarmsound;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * OnAlarmSoundClickListener
 *  Interface that makes callback method after selecting alarm sound
 */
public interface OnAlarmSoundClickListener {
    abstract public void onAlarmSoundSelected(SKAlarmSound alarmSound);
    abstract public void onAlarmSoundSelectCanceled();
    abstract public void onAlarmSoundSelectFailedDueToUsbConnection();
}
