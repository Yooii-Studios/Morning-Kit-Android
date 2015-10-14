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
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 20.
 *
 * MNAlarmWakeUtils
 *  메인 액티비티의 Intent 를 확인해 알람이 있는지 확인하고, 있으면 알람을 울리게 만들기
 */
public class MNAlarmWakeUtils {
    private MNAlarmWakeUtils() { throw new AssertionError("You MUST NOT create this class!"); }

    /**
     * 알람이 앱을 켤 때 울리거나, 알람이 안울리는 현상을 해결하기 위해 알람 로직을 계속 개선
     *
     * 이유:
     * 특정 상황에서 모닝키트를 실행할 경우 intent 에 알람 정보가 없어야 하지만 OS 에서 여러 이유로 인해 강제로
     * 액티비티를 종료하고 새로 실행하는 경우, 최근의 intent를 새 액티비티에 다시 넣어주기 때문
     *
     * 특정 상황:
     * 반복 알람으로 인해 메인 액티비티가 실행되고, dismiss 이후 홈 버튼을 눌러 나간 이후 여러 활동으로 인해
     * OS 에서 모닝키트 메인 액티비티를 메모리에서 제거할 경우, 최근의 intent 가 캐싱이 되어 다시 모닝키트를 실행할 때
     * 알람 정보가 들어 있는 intent 가 반환이 되어 비정상적인 알람이 작동되게 됨
     * ps) 100% 재연을 위해 개발자 옵션에서 'do not keep activity' 를 켜면 확실하게 확인 가능
     */
    public static boolean isAlarmReservedByIntent(Intent intent) {
        return intent.getIntExtra(SKAlarmManager.ALARM_ID, -1) != -1;
    }

    public static void invokeAlarm(Context context, Intent intent) throws IOException {
        int alarmId = intent.getIntExtra(SKAlarmManager.ALARM_ID, -1);
        MNAlarm alarm = MNAlarmListManager.findAlarmById(alarmId, context);
        if (alarm != null) {
            // 알람 울리기
            MNAlarmWakeCustomDialog.show(alarm, context);
            SKAlarmSoundPlayer.playAlarmSound(context, alarm.getAlarmSound(), alarm.getAlarmVolume());

            // 진동 추가 구현
            if (alarm.isVibrateOn()) {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {800, 1300, 800, 1300, 800};         // 진동, 무진동, 진동 무진동 순으로 시간을 설정한다.
                vibrator.vibrate(pattern, 0);                           // 패턴을 지정하고 반복횟수를 지정
            }
        }
    }
}
