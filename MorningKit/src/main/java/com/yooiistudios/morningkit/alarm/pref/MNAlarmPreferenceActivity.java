package com.yooiistudios.morningkit.alarm.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.squareup.otto.Subscribe;
import com.yooiistudios.morningkit.MNApplication;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.pref.listview.MNAlarmPreferenceListAdapter;
import com.yooiistudios.morningkit.common.analytic.MNAnalyticsUtils;
import com.yooiistudios.morningkit.common.bus.MNAlarmPrefBusProvider;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundFactory;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundManager;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundType;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 3.
 *
 * MNAlarmPreferenceActivity
 *  알람을 추가, 수정하는 액티비티
 */
public class MNAlarmPreferenceActivity extends ActionBarActivity {

    public static final String ALARM_PREFERENCE_ALARM_ID = "ALARM_PREFERENCE_ALARM_ID";
    public static final String ALARM_SHARED_PREFS = "ALARM_SHARED_PREFS";
    public static final String ALARM_SHARED_PREFS_ALARM_SNOOZE_ON = "ALARM_SHARED_PREFS_ALARM_SNOOZE_ON";
    public static final String ALARM_SHARED_PREFS_ALARM_VIBRATE_ON = "ALARM_SHARED_PREFS_ALARM_VIBRATE_ON";
    public static final String ALARM_SHARED_PREFS_ALARM_VOLUME = "ALARM_SHARED_PREFS_ALARM_VOLUME";
    private static final String TAG = "AlarmPreferenceActivity";

    @Getter private int alarmId;
    @Getter @Setter private MNAlarm alarm;
    @Getter private MNAlarmPreferenceType alarmPreferenceType;
    @Getter @InjectView(R.id.alarm_pref_listview) ListView listView;
    @Getter Menu actionBarMenu;

    // Admob
    @InjectView(R.id.alarm_pref_adview) AdView adView;
    private View footerView;

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
        MNAlarmPrefBusProvider.getInstance().register(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            alarmId = extras.getInt(ALARM_PREFERENCE_ALARM_ID, -1);
            if (alarmId != -1) {
                alarmPreferenceType = MNAlarmPreferenceType.EDIT;
                alarm = MNAlarmListManager.findAlarmById(alarmId, getBaseContext());
            } else {
                alarmPreferenceType = MNAlarmPreferenceType.ADD;
                alarm = MNAlarmMaker.makeAlarm(getApplicationContext());
                alarm.getAlarmCalendar().add(Calendar.MINUTE, 1); // 추가시에는 1분을 추가해 주기

                // 알람 추가일 경우에는 최근 스누즈 사용 여부를 적용, 볼륨, 진동 사용 여부도.
                SharedPreferences prefs = getSharedPreferences(ALARM_SHARED_PREFS, MODE_PRIVATE);
                alarm.setSnoozeOn(prefs.getBoolean(ALARM_SHARED_PREFS_ALARM_SNOOZE_ON, true));
                alarm.setAlarmVolume(prefs.getInt(ALARM_SHARED_PREFS_ALARM_VOLUME, 85));
                alarm.setVibrateOn(prefs.getBoolean(ALARM_SHARED_PREFS_ALARM_VIBRATE_ON, true));
            }
            if ((alarm.getAlarmSound().getAlarmSoundType() == SKAlarmSoundType.MUSIC ||
                    alarm.getAlarmSound().getAlarmSoundType() == SKAlarmSoundType.RINGTONE) &&
                    !SKAlarmSoundManager.validateAlarmSound(alarm.getAlarmSound().getSoundPath(), this)) {
                alarm.setAlarmSound(SKAlarmSoundFactory.makeDefaultAlarmSound(this));
            }
        } else {
            throw new AssertionError("no extras in MNAlarmPreferenceActivity!");
        }
        initTitle();
        initListView();
        initAdView();
        MNAnalyticsUtils.startAnalytics((MNApplication) getApplication(), TAG);
    }

    // getSupportActionBar로 충분하나 테스트를 위해서 이렇게 작성
    private void initTitle() {
        if (getSupportActionBar() != null) {
            switch (alarmPreferenceType) {
                case ADD:
                    getSupportActionBar().setTitle(R.string.add_an_alarm);
                    break;

                case EDIT:
                    getSupportActionBar().setTitle(R.string.edit_alarm);
                    break;
            }
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    protected void initListView() {
        // 애드몹 대응을 위한 FooterView, setAdapter 전에 호출 필요
        footerView = LayoutInflater.from(this).inflate(R.layout.alarm_pref_list_footer_view, null, false);
        listView.addFooterView(footerView);

        listView.setAdapter(new MNAlarmPreferenceListAdapter(this, alarm));
//        MNAlarmPrefActivityBusProvider.getInstance().register(this);
    }

    private void initAdView() {
        List<String> ownededSkus = SKIabProducts.loadOwnedIabProducts(this.getApplicationContext());
        // 풀버전은 NO_ADS 포함
        if (ownededSkus.contains(SKIabProducts.SKU_NO_ADS)) {
            adView.setVisibility(View.GONE);
            if (footerView != null) {
                listView.removeFooterView(footerView);
            }
        } else {
            adView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.loadAd(adRequest);
        }
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
                applyAlarmPreferences();
                finish();
                return true;

            case R.id.pref_action_cancel:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void applyAlarmPreferences() {
        alarm.startAlarm(this);

        // 알람 저장하기 전에 스누즈 여부, 볼륨, 진동을 저장
        SharedPreferences prefs = getSharedPreferences(ALARM_SHARED_PREFS, MODE_PRIVATE);
        prefs.edit().putBoolean(ALARM_SHARED_PREFS_ALARM_SNOOZE_ON, alarm.isSnoozeOn()).apply();
        prefs.edit().putInt(ALARM_SHARED_PREFS_ALARM_VOLUME, alarm.getAlarmVolume()).apply();
        prefs.edit().putBoolean(ALARM_SHARED_PREFS_ALARM_VIBRATE_ON, alarm.isVibrateOn()).apply();

        // 알람 저장
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
     * Otto: MNAlarmPrefBusProvider
     */
    @Subscribe
    public void setActionBarMenuToVisible(Context context) {
        actionBarMenu.findItem(R.id.pref_action_ok).setVisible(true);
        actionBarMenu.findItem(R.id.pref_action_cancel).setVisible(true);
    }

    @Subscribe
    public void setActionBarMenuToInvisible(View view) {
        actionBarMenu.findItem(R.id.pref_action_ok).setVisible(false);
        actionBarMenu.findItem(R.id.pref_action_cancel).setVisible(false);
    }

    @Override
    protected void onStart() {
        // Activity visible to user
        super.onStart();
        FlurryAgent.onStartSession(this, MNFlurry.KEY);
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // Activity no longer visible
        super.onStop();
        FlurryAgent.onEndSession(this);
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
