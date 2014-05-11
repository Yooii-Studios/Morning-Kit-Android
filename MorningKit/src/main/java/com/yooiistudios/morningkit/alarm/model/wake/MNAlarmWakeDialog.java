package com.yooiistudios.morningkit.alarm.model.wake;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Gravity;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.common.bus.MNAlarmScrollViewBusProvider;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundPlayer;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 20.
 *
 * MNAlarmWakeDialog
 *  알람이 울릴 때 보여 줄 다이얼로그를 생성
 */
public class MNAlarmWakeDialog {
    private MNAlarmWakeDialog() { throw new AssertionError("You MUST NOT create this class!"); }

    public static void show(MNAlarm alarm, Context context) {
        AlertDialog wakeDialog = makeWakeAlertDialog(alarm, context);
        wakeDialog.show();
        // Must show first, setting gravity last.
        ((TextView)wakeDialog.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
    }

    private static AlertDialog makeWakeAlertDialog(final MNAlarm alarm, final Context context) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setPositiveButton(R.string.alarm_wake_dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SKAlarmSoundPlayer.stop();

                MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarm.getAlarmId(), context);
                targetAlarm.stopAlarm(context);
                if (alarm.isRepeatOn()) {
                    targetAlarm.startAlarm(context);
                }
                try {
                    MNAlarmListManager.saveAlarmList(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MNAlarmScrollViewBusProvider.getInstance().post(context);
            }
        });

        // 스누즈 옵션이 켜져 있을 경우에만 사용하기
        if (alarm.isSnoozeOn()) {
            builder.setNegativeButton(R.string.alarm_wake_snooze, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SKAlarmSoundPlayer.stop();

                    MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarm.getAlarmId(), context);
                    targetAlarm.snoozeAlarm(context);
                }
            });
        }
        AlertDialog wakeDialog = builder.create();

        wakeDialog.setCancelable(false);
        wakeDialog.setCanceledOnTouchOutside(false);
        wakeDialog.setTitle(R.string.app_name);
        wakeDialog.setMessage(makeWakeMessage(alarm, context));

        return wakeDialog;
    }

    private static String makeWakeMessage(MNAlarm alarm, Context context) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

        return alarm.getAlarmLabel() + "\n" +
                timeFormat.format(Calendar.getInstance().getTime()) + "\n" +
                dateFormat.format(Calendar.getInstance().getTime());
    }
}
