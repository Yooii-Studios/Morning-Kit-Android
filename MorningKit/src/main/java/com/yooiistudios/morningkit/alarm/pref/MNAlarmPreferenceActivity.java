package com.yooiistudios.morningkit.alarm.pref;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.pref.listview.MNAlarmPreferenceListAdapter;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 3.
 *
 * MNAlarmPreferenceActivity
 *  알람을 추가, 수정하는 액티비티
 */
public class MNAlarmPreferenceActivity extends ActionBarActivity {

    private static final String TAG = "MNAlarmPreferenceActivity";

    @Getter private int alarmId;
    @Getter private MNAlarm alarm;
    @Getter private MNAlarmPreferenceType alarmPreferenceType;
    @Getter @InjectView(R.id.alarm_pref_listview) ListView listView;

    @Getter Menu actionBarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_pref);
        initAlarmPreferenceActivity();
    }

    /**
     * Init
     */
    private void initAlarmPreferenceActivity() {
        ButterKnife.inject(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            alarmId = extras.getInt(MN.alarm.ALARM_PREFERENCE_ALARM_ID, -1);
//            Log.i(TAG, "alarmId: " + alarmId);
            if (alarmId != -1) {
                alarmPreferenceType = MNAlarmPreferenceType.EDIT;
                alarm = MNAlarmListManager.findAlarmById(alarmId, getBaseContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    if (getActionBar() != null) {
                        getActionBar().setTitle(R.string.edit_alarm);
                    }
                }else{
                    getSupportActionBar().setTitle(R.string.edit_alarm); // 추후 구현 다시 하자
                }
            }else{
                alarmPreferenceType = MNAlarmPreferenceType.ADD;
                alarm = MNAlarmMaker.makeAlarm(this.getBaseContext());

            }
        }
        initTitle();
        initListView();
    }

    // getSupportActionBar로 충분하나 테스트를 위해서 이렇게 작성
    private void initTitle() {
        switch (alarmPreferenceType) {
            case ADD:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    if (getActionBar() != null) {
                        getActionBar().setTitle(R.string.add_an_alarm);
                    }
                }else{
                    getSupportActionBar().setTitle(R.string.add_an_alarm); // 추후 구현 다시 하자
                }
                break;

            case EDIT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    if (getActionBar() != null) {
                        getActionBar().setTitle(R.string.edit_alarm);
                    }
                }else{
                    getSupportActionBar().setTitle(R.string.edit_alarm); // 추후 구현 다시 하자
                }
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getActionBar() != null) {
                getActionBar().setDisplayShowHomeEnabled(false); // 추후 구현 다시 하자
            }
        }else{
            getSupportActionBar().setDisplayShowHomeEnabled(false); // 추후 구현 다시 하자
        }
    }

    private void initListView() {
        listView.setAdapter(new MNAlarmPreferenceListAdapter(this, alarm));
//        MNAlarmPrefActivityBusProvider.getInstance().register(this);
    }

    /**
     * Action Bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.pref_actions, menu);
        actionBarMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.pref_action_ok:
                // Add/Edit the alarm
                applyAlarmPreferneces();
                finish();
                return true;

            case R.id.pref_action_cancel:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void applyAlarmPreferneces() {
        switch (alarmPreferenceType) {
            case ADD:
                MNAlarmListManager.addAlarmToAlarmList(alarm, this);
                break;
            case EDIT:
                MNAlarmListManager.replaceAlarmToAlarmList(alarm, this);
                break;
        }
        MNAlarmListManager.sortAlarmList(this);
        try {
            MNAlarmListManager.saveAlarmList(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Otto bus
     */
    /*
    @Subscribe
    public void onAlarmTimePickerChanged(TimePicker timePicker) {
        int hourOfDay = 0, minute = 0;
        hourOfDay = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        Log.i(TAG, "onAlarmTimePickerChanged: " + hourOfDay + ":" + minute);
//        Log.i(TAG, "onAlarmTimePickerChanged: " + hour + ":" + minute);
    }
    */
}
