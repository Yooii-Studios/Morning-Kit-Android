package com.yooiistudios.morningkit.alarm.pref;

import android.content.Intent;
import android.view.MenuItem;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.MNMainActivity;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.tester.android.view.TestMenuItem;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNAlarmPreferenceActivityTest
 * 알람설정 액티비티의 테스트 코드
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = { AdWebViewShadow.class }, emulateSdk = 18) //reportSdk = 10) // Gingerbread // , emulateSdk = 18
public class MNAlarmPreferenceActivityTest {

    private static final String TAG = "MNAlarmPreferenceActivityTest";
    MNMainActivity mainActivity;
    MNAlarmPreferenceActivity alarmPreferenceActivity_add_alarm;
    MNAlarmPreferenceActivity alarmPreferenceActivity_edit_alarm;

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;

        // main
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();

        // dummy alarm setting
        MNAlarm alarm = MNAlarmMaker.makeAlarm(mainActivity.getBaseContext());
        alarm.setAlarmId(50);
        ArrayList<MNAlarm> dummyAlarmList = MNAlarmListManager.loadAlarmList(mainActivity.getBaseContext());
        dummyAlarmList.add(alarm);
        MNAlarmListManager.saveAlarmList(mainActivity.getBaseContext());

        // 'Add alarm' Activity
        Intent intent_add_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity.class);
        intent_add_alarm.putExtra(MN.alarm.ALARM_PREFERENCE_ALARM_ID, -1);
        alarmPreferenceActivity_add_alarm = Robolectric.buildActivity(MNAlarmPreferenceActivity.class)
                .withIntent(intent_add_alarm).create().visible().get();

        // 'Edit alarm' Activity
        Intent intent_edit_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity.class);
        intent_edit_alarm.putExtra(MN.alarm.ALARM_PREFERENCE_ALARM_ID, alarm.getAlarmId());
        alarmPreferenceActivity_edit_alarm = Robolectric.buildActivity(MNAlarmPreferenceActivity.class)
                .withIntent(intent_edit_alarm).create().visible().get();
    }

    @Test
    public void variablesShouldBeValidate() {
        assertThat(alarmPreferenceActivity_edit_alarm.getAlarmPreferenceType(), is(MNAlarmPreferenceType.EDIT));
        assertThat(alarmPreferenceActivity_add_alarm.getAlarmPreferenceType(), is(MNAlarmPreferenceType.ADD));
    }

    @Test
    public void alarmStuffShouldBeValidate() {
        // 액티비티의 alarmId = -1, 즉 해당 알람이 존재 x
        // 하지만 알람은 새로 생성해야하고, id는 -1이 아니어야 한다
        assertThat(alarmPreferenceActivity_add_alarm.getAlarmId(), is(-1));
        assertThat(alarmPreferenceActivity_add_alarm.getAlarm(), notNullValue());
        assertThat(alarmPreferenceActivity_add_alarm.getAlarm().getAlarmId(), is(not(-1)));

        // 액티비티의 alarmId != -1, 즉 해당 알람이 존재 ㅇ
        // 알람이 제대로 생성 되었는지 체크, id는 액티비티의 alarmId과 동일해야 한다
        assertThat(alarmPreferenceActivity_edit_alarm.getAlarmId(), is(50));
        assertThat(alarmPreferenceActivity_edit_alarm.getAlarm(), notNullValue());
        assertThat(alarmPreferenceActivity_edit_alarm.getAlarm().getAlarmId(), is(alarmPreferenceActivity_edit_alarm.getAlarmId()));
    }

    @Test
    public void optionItemsSelectedTest() {
        int sizeOfAlarmList = MNAlarmListManager.getAlarmList(mainActivity).size();
        MenuItem cancelItem = new TestMenuItem(R.id.pref_action_cancel);
        MenuItem okItem = new TestMenuItem(R.id.pref_action_ok);

        // 취소 버튼을 눌러도 아무런 변화가 없어야함
        alarmPreferenceActivity_edit_alarm.onOptionsItemSelected(cancelItem);
        assertThat(MNAlarmListManager.getAlarmList(mainActivity).size(), is(sizeOfAlarmList));

        alarmPreferenceActivity_add_alarm.onOptionsItemSelected(cancelItem);
        assertThat(MNAlarmListManager.getAlarmList(mainActivity).size(), is(sizeOfAlarmList));

        // 확인 버튼은 수정/추가가 됨을 확인
        alarmPreferenceActivity_edit_alarm.onOptionsItemSelected(okItem);
        assertThat(MNAlarmListManager.getAlarmList(mainActivity).size(), is(sizeOfAlarmList));
        assertThat(MNAlarmListManager.findAlarmById(alarmPreferenceActivity_edit_alarm.getAlarm().getAlarmId(), mainActivity), notNullValue());

        alarmPreferenceActivity_add_alarm.onOptionsItemSelected(okItem);
        assertThat(MNAlarmListManager.getAlarmList(mainActivity).size(), is(sizeOfAlarmList+1));
        assertThat(MNAlarmListManager.findAlarmById(alarmPreferenceActivity_add_alarm.getAlarm().getAlarmId(), mainActivity), notNullValue());
    }
}
