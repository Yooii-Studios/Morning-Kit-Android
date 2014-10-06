package com.yooiistudios.morningkit.alarm.model.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.string.MNAlarmTimeString;
import com.yooiistudios.morningkit.main.MNMainActivity;

import java.util.Calendar;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 6. 15.
 *
 * MNAlarmOngoingNotificationMaker
 *  스테이터스바 상단에 표시되는 OngoingNotification을 만드는 클래스
 */
public class MNAlarmOngoingNotificationMaker {
    public static void make(MNAlarm alarm, Context context) {
        NotificationManager notificationManager  = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        String timeString = MNAlarmTimeString.makeTimeString(alarm.getAlarmCalendar(), context);
        String ampmString = "";
        if (!DateFormat.is24HourFormat(context)) {
            if (alarm.getAlarmCalendar().get(Calendar.HOUR_OF_DAY) < 12) {
                ampmString = context.getString(R.string.alarm_am);
            } else {
                ampmString = context.getString(R.string.alarm_pm);
            }
        }

        String title = context.getResources().getString(R.string.app_name);
        String text = timeString + " " + ampmString;

        Intent intent = new Intent(context, MNMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // 다른 PendingIntent 와 겹치지 않게 -20으로 설정
        PendingIntent pendingIntent = PendingIntent.getActivity(context, -20,
                intent, 0);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.status_bar_small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.status_bar_large_icon))
                .setWhen(0)
                .setContentIntent(pendingIntent)
                .build();

//        if (Build.VERSION.SDK_INT > 10) {
//
//        } else {
//            notification = new Notification(R.drawable.status_bar_icon, text, System.currentTimeMillis());
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//                    new Intent(context, MNMainActivity.class), 0);
            notification.setLatestEventInfo(context, title, text, pendingIntent);
//        }

        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(1234, notification);
    }
}
