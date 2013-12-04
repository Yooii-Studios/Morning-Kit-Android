package com.yooiistudios.morningkit.alarm.pref;

import android.content.Intent;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmMaker;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.MNMainActivity;
import com.yooiistudios.morningkit.main.MNMainActivity_;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNAlarmPreferenceActivityTest
 * 알람설정 액티비티의 테스트 코드
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = { AdWebViewShadow.class })
public class MNAlarmPreferenceActivityTest {

    MNMainActivity_ mainActivity;
    MNAlarmPreferenceActivity_ alarmPreferenceActivity_add_alarm;
    MNAlarmPreferenceActivity_ alarmPreferenceActivity_edit_alarm;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        mainActivity = Robolectric.buildActivity(MNMainActivity_.class).create().visible().get();

        // 'Add alarm' Activity
        Intent intent_add_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity_.class);
        alarmPreferenceActivity_add_alarm = Robolectric.buildActivity(MNAlarmPreferenceActivity_.class)
                .withIntent(intent_add_alarm).create().visible().get();

        // 'Edit alarm' Activity
        Intent intent_edit_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity_.class);
        MNAlarm alarm = MNAlarmMaker.makeAlarm(mainActivity.getBaseContext());
        alarm.setAlarmId(50);
        intent_edit_alarm.putExtra(MN.alarm.ALARM_PREFERENCE_ALARM_ID, alarm.getAlarmId());
        alarmPreferenceActivity_edit_alarm = Robolectric.buildActivity(MNAlarmPreferenceActivity_.class)
                .withIntent(intent_edit_alarm).create().visible().get();
    }

    @Test
    public void testExtra() {
        assertThat(true, is(true));
    }
}
