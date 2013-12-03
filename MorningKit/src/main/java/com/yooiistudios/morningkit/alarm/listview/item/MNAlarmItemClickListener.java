package com.yooiistudios.morningkit.alarm.listview.item;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.pref.MNAlarmPreferenceActivity;
import com.yooiistudios.morningkit.common.bus.MNBusProvider;
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

        MNAlarm alarm = null;
        if (v.getTag().getClass() == MNAlarm.class) {
            // Edit alarm: alarmId
            alarm = (MNAlarm)v.getTag();
            Log.i(TAG, "itemClick, alarmId: " + alarm.getAlarmId());
        } else {
            // Add an alarm : -1
            Log.i(TAG, "itemClick: " + v.getTag().toString()); // + position);
        }

        if (mAlarmListView.getContext() != null) {

            // Start activity with extra(alarmId)
            if (alarm != null) {
                i.putExtra(MN.alarm.ALARM_PREFERENCE_ALARM_ID, alarm.getAlarmId());
            }
            mAlarmListView.getContext().startActivity(i);

            // Event bus
            if (alarm != null) {
                Log.i(TAG, "alarm is not null");
//                Bus bus = new Bus();
//                bus.register(this);
//                bus.post(alarm);
                MNBusProvider.getInstance().register(this);
                MNBusProvider.getInstance().post(alarm);


            } else {
                Log.i(TAG, "alarm is null");
            }
        }
    }

    @Subscribe
    public void testAlarmBus(MNAlarm alarm) {
        Log.i(TAG, "testAlarmBus: " + alarm.getAlarmId());
    }

    /**
     * Getter & Setter
     */
    public MNMainAlarmListView getAlarmListView() { return mAlarmListView; }
}