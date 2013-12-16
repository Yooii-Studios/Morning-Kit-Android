package com.yooiistudios.morningkit.alarm.pref.listview;

import android.content.Intent;
import android.view.View;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.model.MNAlarmRepeatString;
import com.yooiistudios.morningkit.alarm.pref.MNAlarmPreferenceActivity;
import com.yooiistudios.morningkit.alarm.pref.listview.item.maker.MNAlarmPrefRepeatItemMaker;
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
import static org.hamcrest.CoreMatchers.notNullValue;
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
        MNAlarm nonRepeatAlarm = makeNonRepeatAlarm();
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(nonRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Never"));
        testEachRepeatItem(alarmPrefActivity_edit, nonRepeatAlarm);

        // 월, 목, 토 -> Mon Tue Sat 와 같은 식
        MNAlarm severalRepeatAlarm = makeSeveralRepeatAlarm();
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(severalRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Mon Thu Sat"));
        testEachRepeatItem(alarmPrefActivity_edit, severalRepeatAlarm);

        // 월화수목금 -> Every Weekday
        MNAlarm weekdaysRepeatAlarm = makeWeekdaysRepeatAlarm();
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(weekdaysRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Weekdays"));
        testEachRepeatItem(alarmPrefActivity_edit, weekdaysRepeatAlarm);

        // 토일 -> Every Weekends
        MNAlarm weekendsRepeatAlarm = makeWeekendsRepeatAlarm();
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(weekendsRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Weekends"));
        testEachRepeatItem(alarmPrefActivity_edit, weekendsRepeatAlarm);

        // 월~일 -> Everyday
        MNAlarm everdayRepeatAlarm = makeEverydayRepeatAlarm();
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(everdayRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Everyday"));
        testEachRepeatItem(alarmPrefActivity_edit, everdayRepeatAlarm);
    }

    private void testEachRepeatItem(MNAlarmPreferenceActivity alarmPrefActivity, MNAlarm alarm) {
        View convertView = MNAlarmPrefRepeatItemMaker.makeRepeatItem(alarmPrefActivity, null, alarm);
        assertThat(convertView, notNullValue());

        MNAlarmPrefRepeatItemMaker.RepeatItemViewHolder viewHolder = (MNAlarmPrefRepeatItemMaker.RepeatItemViewHolder) convertView.getTag();
        assertThat(viewHolder, notNullValue());
        assertThat(viewHolder, is(MNAlarmPrefRepeatItemMaker.RepeatItemViewHolder.class));
        assertThat(viewHolder.getTitleTextView().getText().toString(), is(alarmPrefActivity.getString(R.string.alarm_pref_repeat)));
        assertThat(viewHolder.getDetailTextView().getText().toString(), is(MNAlarmRepeatString.makeRepeatDetailString(alarm.getAlarmRepeatList(), alarmPrefActivity)));
    }

    @Test
    public void testRepeatDialog() throws Exception {

//        alarmPrefActivity_edit.setAlarm(makeNonRepeatAlarm());
/*
        final String[] repeatStrings = new String[]{
                alarmPrefActivity_edit.getString(R.string.every_monday),
                alarmPrefActivity_edit.getString(R.string.every_tuesday),
                alarmPrefActivity_edit.getString(R.string.every_wednesday),
                alarmPrefActivity_edit.getString(R.string.every_thursday),
                alarmPrefActivity_edit.getString(R.string.every_friday),
                alarmPrefActivity_edit.getString(R.string.every_saturday),
                alarmPrefActivity_edit.getString(R.string.every_sunday)};

        final boolean[] isRepeatOn = new boolean[7];
        for (int i = 0; i < isRepeatOn.length; i++) {
            isRepeatOn[i] = alarmPrefActivity_edit.getAlarm().getAlarmRepeatList().get(i);
        }
        */
    }

    private MNAlarm makeNonRepeatAlarm() {
        MNAlarm nonRepeatAlarm = MNAlarmMaker.makeAlarm(alarmPrefActivity_edit);
        for (int i = 0; i < nonRepeatAlarm.getAlarmRepeatList().size(); i++) {
            nonRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
        }
        return nonRepeatAlarm;
    }

    private MNAlarm makeSeveralRepeatAlarm() {
        MNAlarm severalRepeatAlarm = MNAlarmMaker.makeAlarm(alarmPrefActivity_edit);
        for (int i = 0; i < severalRepeatAlarm.getAlarmRepeatList().size(); i++) {
            severalRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
            if (i == 0 || i == 3 || i == 5) {
                severalRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
            }
        }
        return severalRepeatAlarm;
    }

    private MNAlarm makeWeekdaysRepeatAlarm() {
        MNAlarm weekdaysRepeatAlarm = MNAlarmMaker.makeAlarm(alarmPrefActivity_edit);
        for (int i = 0; i < weekdaysRepeatAlarm.getAlarmRepeatList().size(); i++) {
            weekdaysRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
            if (i == 5 || i == 6) {
                weekdaysRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
            }
        }
        return weekdaysRepeatAlarm;
    }

    private MNAlarm makeWeekendsRepeatAlarm() {
        MNAlarm weekendsRepeatAlarm = MNAlarmMaker.makeAlarm(alarmPrefActivity_edit);
        for (int i = 0; i < weekendsRepeatAlarm.getAlarmRepeatList().size(); i++) {
            weekendsRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
            if (i == 5 || i == 6) {
                weekendsRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
            }
        }
        return weekendsRepeatAlarm;
    }

    private MNAlarm makeEverydayRepeatAlarm() {
        MNAlarm everydayRepeatAlarm = MNAlarmMaker.makeAlarm(alarmPrefActivity_edit);
        for (int i = 0; i < everydayRepeatAlarm.getAlarmRepeatList().size(); i++) {
            everydayRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
        }
        return everydayRepeatAlarm;
    }
}
