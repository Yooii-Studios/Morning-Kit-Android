package com.yooiistudios.morningkit.alarm.model.wake;

import android.content.Context;
import android.content.Intent;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.stevenkim.alarmmanager.SKAlarmManager;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundPlayer;

import java.io.IOException;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 20.
 *
 * MNAlarmReserveChecker
 *  메인 액티비티의 Intent를 확인해 알람이 있는지 확인하고, 있으면 알람을 울리게 만들기
 */
public class MNAlarmWake {
    private MNAlarmWake() { throw new AssertionError("You MUST NOT create this class!"); }

    public static boolean isAlarmReserved(Intent intent) {
        int alarmId = intent.getIntExtra(SKAlarmManager.ALARM_ID, -1);
        return alarmId != -1;
    }

    public static void checkReservedAlarm(Intent intent, Context context) throws IOException {
        int alarmId = intent.getIntExtra(SKAlarmManager.ALARM_ID, -1);
        if (alarmId != -1) {
            int alarmUniqueId = intent.getIntExtra(SKAlarmManager.ALARM_UNIQUE_ID, -1);
            MNAlarm alarm = MNAlarmListManager.findAlarmById(alarmUniqueId, context);
            if (alarm != null) {
                MNAlarmWakeDialog.show(alarm, context);
                SKAlarmSoundPlayer.playAlarmSound(alarm.getAlarmSound(), context);
            } else {
                throw new AssertionError("The target alarm must exist in the list.");
            }
        }
    }
}
