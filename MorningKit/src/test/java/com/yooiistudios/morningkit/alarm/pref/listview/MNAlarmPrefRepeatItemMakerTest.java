package com.yooiistudios.morningkit.alarm.pref.listview;

import android.content.Intent;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.model.MNAlarmRepeatString;
import com.yooiistudios.morningkit.alarm.pref.MNAlarmPreferenceActivity;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.MNMainActivity;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 16.
 *
 * MNAlarmPrefRepeatItemMakerTest
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = { AdWebViewShadow.class }) //reportSdk = 10) // Gingerbread
public class MNAlarmPrefRepeatItemMakerTest {
    private static final String TAG = "MNAlarmPrefRepeatItemMakerTest";
    private MNAlarmPreferenceActivity alarmPrefActivity_add;
    private MNAlarmPreferenceActivity alarmPrefActivity_edit;

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;

        // main
        MNMainActivity mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();

        // dummy alarm setting
        MNAlarm alarm = MNAlarmMaker.makeAlarm(mainActivity.getBaseContext());
        alarm.setAlarmId(50);
        ArrayList<MNAlarm> dummyAlarmList = MNAlarmListManager.loadAlarmList(mainActivity.getBaseContext());
        dummyAlarmList.add(alarm);
        MNAlarmListManager.saveAlarmList(mainActivity.getBaseContext());

        // 'Add alarm' Activity
        Intent intent_add_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity.class);
        intent_add_alarm.putExtra(MN.alarm.ALARM_PREFERENCE_ALARM_ID, -1);
        alarmPrefActivity_add = Robolectric.buildActivity(MNAlarmPreferenceActivity.class)
                .withIntent(intent_add_alarm).create().visible().get();

        // 'Edit alarm' Activity
        Intent intent_edit_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity.class);
        intent_edit_alarm.putExtra(MN.alarm.ALARM_PREFERENCE_ALARM_ID, alarm.getAlarmId());
        alarmPrefActivity_edit = Robolectric.buildActivity(MNAlarmPreferenceActivity.class)
                .withIntent(intent_edit_alarm).create().visible().get();
    }

    @Test
    public void testRepeatDetailString() throws Exception {
        // boolean의 ArrayList을 대입하면, 해당한는 반복 String을 반환

        // 반복 없음 -> Never
        MNAlarm nonRepeatAlarm = MNAlarmMaker.makeAlarm(alarmPrefActivity_edit);
        for (int i = 0; i < nonRepeatAlarm.getAlarmRepeatList().size(); i++) {
            nonRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
        }
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(nonRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Never"));

        // 월, 목, 토 -> Mon Tue Sat 와 같은 식
        MNAlarm severalRepeatAlarm = MNAlarmMaker.makeAlarm(alarmPrefActivity_edit);
        for (int i = 0; i < severalRepeatAlarm.getAlarmRepeatList().size(); i++) {
            severalRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
            if (i == 0 || i == 3 || i == 5) {
                severalRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
            }
        }
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(severalRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Mon Thu Sat"));

        // 월화수목금 -> Every Weekday
        MNAlarm weekdaysRepeatAlarm = MNAlarmMaker.makeAlarm(alarmPrefActivity_edit);
        for (int i = 0; i < weekdaysRepeatAlarm.getAlarmRepeatList().size(); i++) {
            weekdaysRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
            if (i == 5 || i == 6) {
                weekdaysRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
            }
        }
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(weekdaysRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Weekdays"));

        // 토일 -> Every Weekends
        MNAlarm weekendsRepeatAlarm = MNAlarmMaker.makeAlarm(alarmPrefActivity_edit);
        for (int i = 0; i < weekendsRepeatAlarm.getAlarmRepeatList().size(); i++) {
            weekendsRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
            if (i == 5 || i == 6) {
                weekendsRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
            }
        }
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(weekendsRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Weekends"));

        // 월~일 -> Everyday
        MNAlarm everdayRepeatAlarm = MNAlarmMaker.makeAlarm(alarmPrefActivity_edit);
        for (int i = 0; i < everdayRepeatAlarm.getAlarmRepeatList().size(); i++) {
            everdayRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
        }
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(everdayRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Everyday"));
    }
}
