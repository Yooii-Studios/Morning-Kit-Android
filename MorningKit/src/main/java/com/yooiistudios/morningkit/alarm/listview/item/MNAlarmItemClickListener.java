package com.yooiistudios.morningkit.alarm.listview.item;

import android.content.Intent;
import android.view.View;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.pref.MNAlarmPreferenceActivity;
import com.yooiistudios.morningkit.main.MNMainAlarmListView;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 3.
 *
 * MNAlarmItemClickListener
 *  1. 알람 클릭 로직을 담당
 *  2. 생성/수정에 맞추어 알람설정 액티비티 생성 담당
 */
public class MNAlarmItemClickListener implements View.OnClickListener {

    private static final String TAG = "MNAlarmItemClickListener";
    @Getter private MNMainAlarmListView alarmListView;

    private MNAlarmItemClickListener() {}

    public static MNAlarmItemClickListener newInstance(MNMainAlarmListView alarmListView) {
        MNAlarmItemClickListener itemClickListener = new MNAlarmItemClickListener();
        itemClickListener.alarmListView = alarmListView;
        return itemClickListener;
    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent(alarmListView.getContext(), MNAlarmPreferenceActivity.class);

        MNAlarm alarm = null;
        if (v.getTag().getClass() == MNAlarm.class) {
            // Edit alarm: alarmId
            alarm = (MNAlarm)v.getTag();
        } else {
            // Add an alarm : -1
        }

        if (alarmListView.getContext() != null) {
            // Start activity with extra(alarmId)
            if (alarm != null) {
                i.putExtra(MNAlarmPreferenceActivity.ALARM_PREFERENCE_ALARM_ID, alarm.getAlarmId());
            }else{
                i.putExtra(MNAlarmPreferenceActivity.ALARM_PREFERENCE_ALARM_ID, -1);
            }
            alarmListView.getContext().startActivity(i);
        }
    }
}