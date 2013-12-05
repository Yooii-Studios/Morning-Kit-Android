package com.yooiistudios.morningkit.alarm.pref;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.MNAlarmMaker;
import com.yooiistudios.morningkit.common.bus.MNBusProvider;

import butterknife.ButterKnife;
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

    @Getter private int alarmId;
    @Getter private MNAlarm alarm;
    @Getter private MNAlarmPreferenceType alarmPreferenceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_pref);
        initAlarmPreferenceActivity();
    }

    void initAlarmPreferenceActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int alarmId = extras.getInt(MN.alarm.ALARM_PREFERENCE_ALARM_ID, -1);
//            Log.i(TAG, "alarmId: " + alarmId);
            if (alarmId != -1) {
                alarmPreferenceType = MNAlarmPreferenceType.EDIT;
                alarm = MNAlarmListManager.findAlarmById(alarmId, getBaseContext());
//                getSupportActionBar().setTitle(R.string.edit_alarm); // 추후 구현 다시 하자
            }else{
                alarmPreferenceType = MNAlarmPreferenceType.ADD;
                alarm = MNAlarmMaker.makeAlarm(this.getBaseContext());
//                getSupportActionBar().setTitle(R.string.add_an_alarm); // 추후 구현 다시 하자
            }
        }
//        getSupportActionBar().setDisplayShowHomeEnabled(false); // 추후 구현 다시 하자
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
