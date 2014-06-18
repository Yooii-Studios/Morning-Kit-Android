package com.yooiistudios.morningkit.alarm.model.notification;

import android.app.NotificationManager;
import android.content.Context;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;

import java.util.ArrayList;

public class MNAlarmNotificationChecker {
	public static void checkNotificationState(ArrayList<MNAlarm> alarmList, Context context) {
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
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1234);
        } else {
            // 하나라도 알람이 커져있을 경우 가장 빠른 시간의 알람으로 notification 생성
            MNAlarmOngoingNotificationMaker.make(fastestAlarm, context);
        }
    }
}
