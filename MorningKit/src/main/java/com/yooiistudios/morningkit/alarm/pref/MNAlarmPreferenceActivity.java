package com.yooiistudios.morningkit.alarm.pref;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.MNAlarmMaker;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 3.
 *
 * MNAlarmPreferenceActivity
 * 알람을 추가, 수정하는 액티비티
 */
public class MNAlarmPreferenceActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "MNAlarmPreferenceActivity";
    private Bus mBus;
    private MNAlarm mAlarm;

    /**
     * Lifecycle
     */
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_pref);

        mBus = new Bus();
        mBus.register(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int alarmId = extras.getInt(MN.alarm.ALARM_PREFERENCE_ALARM_ID, -1);
            Log.i(TAG, "alarmId: " + alarmId);
            if (alarmId != -1) {
                mAlarm = MNAlarmListManager.findAlarmById(alarmId, getBaseContext());
            }else{
                mAlarm = MNAlarmMaker.makeAlarm(this.getBaseContext());
            }
        }

        getSupportActionBar().setTitle(R.string.add_an_alarm);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBus == null) {
            mBus = new Bus();
        }
        mBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mBus != null) {
            mBus.unregister(this);
        }
    }

    @Subscribe
    public void initAlarm(MNAlarm alarm) {
        Log.i(TAG, "bus: initAlarm");
        mAlarm = alarm;
    }

    private void initListView() {

    }

    @Override
    public void onClick(View v) {

    }
}
