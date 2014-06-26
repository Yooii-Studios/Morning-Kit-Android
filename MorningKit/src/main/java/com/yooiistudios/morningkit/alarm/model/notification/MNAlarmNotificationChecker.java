package com.yooiistudios.morningkit.alarm.model.notification;

import android.app.NotificationManager;
import android.content.Context;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.setting.theme.alarmstatusbar.MNAlarmStatusBarIcon;
import com.yooiistudios.morningkit.setting.theme.alarmstatusbar.MNAlarmStatusBarIconType;

import java.util.ArrayList;

public class MNAlarmNotificationChecker {
    private static final int ALARM_NOTIF_ID = 1234;

	public static void checkNotificationState(ArrayList<MNAlarm> alarmList, Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (MNAlarmStatusBarIcon.getCurrentAlarmStatusBarIconType(context) == MNAlarmStatusBarIconType.OFF) {
            // 알람 아이콘을 사용하지 않는 경우 아이콘을 꺼주기
            notificationManager.cancel(ALARM_NOTIF_ID);
        } else {
            boolean areAllAlarmsOff = true;
            MNAlarm fastestAlarm = null;

            for (MNAlarm alarm : alarmList) {
                if (alarm.isAlarmOn()) {
                    areAllAlarmsOff = false;
                    if (fastestAlarm == null) {
                        fastestAlarm = alarm;
                    }
                }
            }
            if (areAllAlarmsOff) {
                // 모든 알람이 꺼져있을 경우는 notification 취소
                notificationManager.cancel(ALARM_NOTIF_ID);
            } else {
                // 하나라도 알람이 커져있을 경우 가장 빠른 시간의 알람으로 notification 생성
                MNAlarmOngoingNotificationMaker.make(fastestAlarm, context);
            }
        }
    }
}
