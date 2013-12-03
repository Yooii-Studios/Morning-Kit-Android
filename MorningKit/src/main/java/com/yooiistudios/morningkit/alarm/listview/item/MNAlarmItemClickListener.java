package com.yooiistudios.morningkit.alarm.listview.item;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.pref.MNAlarmPreferenceActivity;
import com.yooiistudios.morningkit.main.MNMainAlarmListView;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 3.
 *
 * MNAlarmItemClickListener
 * 알람 클릭 로직을 담당
 */
public class MNAlarmItemClickListener implements View.OnClickListener {

    private static final String TAG = "MNAlarmItemClickListener";
    private MNMainAlarmListView mAlarmListView;

    private MNAlarmItemClickListener() {}

    public static MNAlarmItemClickListener newInstance(MNMainAlarmListView alarmListView) {
        MNAlarmItemClickListener itemClickListener = new MNAlarmItemClickListener();
        itemClickListener.mAlarmListView = alarmListView;

        return itemClickListener;
    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent(mAlarmListView.getContext(), MNAlarmPreferenceActivity.class);

        if (v.getTag() == MNAlarm.class) {
            // Edit alarm: alarmId
            MNAlarm alarm = (MNAlarm)v.getTag();
//            Log.i(TAG, "itemClick: alarmId: " + alarm.getAlarmId());
        } else {
            // Add an alarm : -1
//            Log.i(TAG, "itemClick: " + v.getTag().toString()); // + position);
        }

        if (mAlarmListView.getContext() != null) {
            mAlarmListView.getContext().startActivity(i);
        }
    }

    /**
     * Getter & Setter
     */
    public MNMainAlarmListView getAlarmListView() { return mAlarmListView; }
}