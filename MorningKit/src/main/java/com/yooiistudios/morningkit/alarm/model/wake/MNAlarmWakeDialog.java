package com.yooiistudios.morningkit.alarm.model.wake;

import android.app.AlertDialog;
import android.content.Context;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 20.
 *
 * MNAlarmWakeDialog
 *  알람이 울릴 때 보여 줄 다이얼로그를 생성
 */
public class MNAlarmWakeDialog {
    private MNAlarmWakeDialog() { throw new AssertionError("You MUST NOT create this class!"); }

    public static void show(MNAlarm alarm, Context context) {
        AlertDialog wakeDialog = new AlertDialog.Builder(context).create();
        wakeDialog.setTitle("Test Title");
        wakeDialog.setMessage("Test Message");
        wakeDialog.show();
    }
}
