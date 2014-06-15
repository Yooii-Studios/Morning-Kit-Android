package com.yooiistudios.morningkit.alarm.model.notification;

import android.app.NotificationManager;
import android.content.Context;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;

import java.util.ArrayList;

public class MNAlarmNotificationChecker {
	public static void checkNotificationState(ArrayList<MNAlarm> alarmList, Context context) {
		boolean areAllAlarmsOff = true;

        for (MNAlarm alarm : alarmList) {
            if (alarm.isAlarmOn()) {
                areAllAlarmsOff = false;
            }
        }
		
		if (areAllAlarmsOff) {
			NotificationManager notificationManager =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(1234);
		}
	}
}
