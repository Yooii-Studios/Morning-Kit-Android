package com.yooiistudios.morningkit.alarm.model;

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

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 11. 11..
 *
 * MNAlarmMakerTest
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config (shadows = { AdWebViewShadow.class }, emulateSdk = 18)
public class MNAlarmMakerTest {

    MNAlarm defaultAlarm;
    MNAlarm customAlarm;
    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();

        defaultAlarm = MNAlarmMaker.makeAlarm(mainActivity.getBaseContext());
        customAlarm = MNAlarmMaker.makeAlarmWithTime(mainActivity.getBaseContext(), 6, 30);
    }

    @Test
    public void makeNewDefaultAlarmTest() {
        assertNotNull(defaultAlarm);

        assertThat(defaultAlarm, notNullValue());
        assertThat(defaultAlarm.isAlarmOn(), is(true));
        assertThat(defaultAlarm.isSnoozeOn(), is(false));
        assertThat(defaultAlarm.isRepeatOn(), is(false));
        assertThat(defaultAlarm.getAlarmLabel(), is("Alarm"));
        // 알람 ID를 제대로 할당했는지 테스트
        assertThat(defaultAlarm.getAlarmId(), is(not(-1)));
        assertThat(defaultAlarm.getAlarmCalendar(), notNullValue());
        // 알람 초는 0초로 설정해야함
        assertThat(defaultAlarm.getAlarmCalendar().get(Calendar.SECOND), is(0));

        // 초기 설정은 모두 false
        for (int i = 0; i < defaultAlarm.getAlarmRepeatList().size(); i++) {
            Boolean alarmRepeatOnSpecificWeekday = defaultAlarm.getAlarmRepeatList().get(i);
            assertThat(alarmRepeatOnSpecificWeekday, is(false));
        }

        // 제대로 된 사운드 타입이 할당되었는지 테스트
        // 제대로 된 사운드 리소스가 대입되었는지 테스트
    }

    @Test
    public void makeNewCustomAlarmTest() {
        assertNotNull(customAlarm);

        // 기본으로 끈 채로 추가
        assertThat(customAlarm.isAlarmOn(), is(false));

        // 특정 시간으로 넣었을 때 올해/이번달/오늘 or 내일의 특정 시간으로 나오는지 테스트
        int hour = customAlarm.getAlarmCalendar().get(Calendar.HOUR_OF_DAY);
        int minute = customAlarm.getAlarmCalendar().get(Calendar.MINUTE);

        // 시간만 일단 체크(오늘/내일 판단은 startAlarmTest() 에서 구현하기로)
        assertThat(hour, is(6));
        assertThat(minute, is(30));
    }
}
