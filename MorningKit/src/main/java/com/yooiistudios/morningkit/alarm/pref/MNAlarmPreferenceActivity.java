package com.yooiistudios.morningkit.alarm.pref;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Subscribe;
import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.MNAlarmMaker;
import com.yooiistudios.morningkit.common.bus.MNBusProvider;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 3.
 *
 * MNAlarmPreferenceActivity
 *  알람을 추가, 수정하는 액티비티
 */
enum MNAlarmPreferenceType { ADD, EDIT; }

public class MNAlarmPreferenceActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "MNAlarmPreferenceActivity";
    @Getter private MNAlarm alarm;
    @Getter private MNAlarmPreferenceType alarmPreferenceType;

    /**
     * Lifecycle
     */
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_pref);

        MNBusProvider.getInstance().register(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int alarmId = extras.getInt(MN.alarm.ALARM_PREFERENCE_ALARM_ID, -1);
            if (alarmId != -1) {
                alarmPreferenceType = MNAlarmPreferenceType.EDIT;
                alarm = MNAlarmListManager.findAlarmById(alarmId, getBaseContext());
            }else{
                alarmPreferenceType = MNAlarmPreferenceType.ADD;
                alarm = MNAlarmMaker.makeAlarm(this.getBaseContext());
            }
            Log.i(TAG, "alarmId: " + alarm.getAlarmId());
            MNBusProvider.getInstance().post(alarm);
        }

        getSupportActionBar().setTitle(R.string.add_an_alarm);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MNBusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MNBusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void initAlarm(MNAlarm alarm) {
        Log.i(TAG, "bus: initAlarm");
        this.alarm = alarm;
    }

    private void initListView() {

    }

    @Override
    public void onClick(View v) {

    }
}
