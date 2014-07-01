package com.yooiistudios.morningkit.alarm.model.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;

import java.util.ArrayList;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 7. 1.
 *
 * MNAlarmBroadCastReceiverService
 *  재부팅이 될 때 알람을 다시 켜주는 서비스
 */
public class MNAlarmBroadcastReceiverService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            // 리부팅이 될 때 켜진 알람들은 다시 켜주기
            ArrayList<MNAlarm> alarmList = MNAlarmListManager.loadAlarmList(context);
            for (MNAlarm alarm : alarmList) {
                if (alarm.isAlarmOn()) {
                    alarm.startAlarmWithNoToast(context);
                }
            }
        }
    }
}
