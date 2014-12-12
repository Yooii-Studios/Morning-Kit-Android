package com.yooiistudios.morningkit.alarm.model.wake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public static boolean isAlarmReservedInPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SKAlarmManager.PREFS_ALARM_BUFFER,
                Context.MODE_PRIVATE);
        int alarmId = prefs.getInt(SKAlarmManager.ALARM_ID, -1);
        return alarmId != -1;
    }

    /**
     * 기존 AlarmManager 에 알람을 등록하면 intent 에서 알람이 있는지 최초로 확인 필요
     * 따라서 최초로 intent 에서 alarmId를 꺼내서 유효한 id가 있다면 액티비티가 새로 실행되도록 true 반환
     *
     * 이유:
     * 특정 상황에서 모닝키트를 실행할 경우 intent 에 알람 정보가 없어야 하지만 안드로이드의 알 수 없는 원인으로 인해
     * 이전에 울렸던 알람 정보가 있는 intent 가 반환되기 때문에 이 로직을 고안
     *
     * 특정 상황:
     * 반복 알람으로 인해 메인 액티비티가 실행되고, dismiss 이후 홈 버튼을 눌러 나간 이후 여러 활동으로 인해
     * OS 에서 모닝키트 메인 액티비티를 메모리에서 제거할 경우, 최근의 intent 가 캐싱이 되어 다시 모닝키트를 실행할 때
     * 알람 정보가 들어 있는 intent 가 반환이 되어 비정상적인 알람이 작동되게 됨
     * ps) 100% 재연을 위해 개발자 옵션에서 'do not keep activity' 를 켜면 확실하게 확인 가능
     */
    public static boolean isAlarmReservedByIntent(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(SKAlarmManager.PREFS_ALARM_BUFFER,
                Context.MODE_PRIVATE);
        int alarmId = intent.getIntExtra(SKAlarmManager.ALARM_ID, -1);
        if (alarmId != -1) {
            // 새로 실행될 액티비티에서 알람을 울리기 위해 저장
            prefs.edit().putInt(SKAlarmManager.ALARM_ID,
                    intent.getIntExtra(SKAlarmManager.ALARM_ID, -1)).apply();
            return true;
        } else {
            return false;
        }
    }

    public static void invokeAlarm(Context context) throws IOException {
        SharedPreferences prefs = context.getSharedPreferences(SKAlarmManager.PREFS_ALARM_BUFFER,
                Context.MODE_PRIVATE);
        int alarmId = prefs.getInt(SKAlarmManager.ALARM_ID, -1);

        MNAlarm alarm = MNAlarmListManager.findAlarmById(alarmId, context);
        if (alarm != null) {
            // 알람이 울렸으므로 알람 아이디는 prefs 에서 제거
            prefs.edit().remove(SKAlarmManager.ALARM_ID).apply();

            // 알람 울리기
            MNAlarmWakeCustomDialog.show(alarm, context);
            SKAlarmSoundPlayer.playAlarmSound(alarm.getAlarmSound(), alarm.getAlarmVolume(), context);

            // 진동 추가 구현
            if (alarm.isVibrateOn()) {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {500, 1000, 500, 1000, 500};         // 진동, 무진동, 진동 무진동 순으로 시간을 설정한다.
                vibrator.vibrate(pattern, 0);                           // 패턴을 지정하고 반복횟수를 지정
            }
        }
    }
}
