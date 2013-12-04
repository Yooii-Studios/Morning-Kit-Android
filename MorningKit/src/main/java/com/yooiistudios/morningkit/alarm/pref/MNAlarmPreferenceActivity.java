package com.yooiistudios.morningkit.alarm.pref;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
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

@EActivity(R.layout.activity_alarm_pref)
public class MNAlarmPreferenceActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "MNAlarmPreferenceActivity";
    @Getter private MNAlarm alarm;
    @Getter private MNAlarmPreferenceType alarmPreferenceType;

    @Extra(MN.alarm.ALARM_PREFERENCE_ALARM_ID) int alarmId = -1;

    @AfterViews
    void initAlarmPreferenceActivity() {

        if (alarmId != -1) {
            alarmPreferenceType = MNAlarmPreferenceType.EDIT;
            alarm = MNAlarmListManager.findAlarmById(alarmId, getBaseContext());
            getSupportActionBar().setTitle(R.string.edit_alarm);
        }else{
            alarmPreferenceType = MNAlarmPreferenceType.ADD;
            alarm = MNAlarmMaker.makeAlarm(this.getBaseContext());
            getSupportActionBar().setTitle(R.string.add_an_alarm);
        }

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

    private void initListView() {

    }

    @Override
    public void onClick(View v) {

    }
}
