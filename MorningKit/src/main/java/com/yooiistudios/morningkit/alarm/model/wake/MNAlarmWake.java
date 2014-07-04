package com.yooiistudios.morningkit.alarm.model.wake;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

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
//                MNAlarmWakeDialog.show(alarm, context);
                MNAlarmWakeCustomDialog.show(alarm, context);
                SKAlarmSoundPlayer.playAlarmSound(alarm.getAlarmSound(), alarm.getAlarmVolume(), context);

                // 진동 추가 구현
                if (alarm.isVibrateOn()) {
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = { 500, 1000, 500, 1000, 500 };         // 진동, 무진동, 진동 무진동 숫으로 시간을 설정한다.
                    vibrator.vibrate(pattern, 0);                           // 패턴을 지정하고 반복횟수를 지정
                }
            }
            // 이 상황은 있어서는 안되는 상황이지만 이것 때문에 크래시가 나는 경우가 있기에 주석처리해둠
//            else {
//                throw new AssertionError("The target alarm must exist in the list.");
//            }
        }
    }
}
