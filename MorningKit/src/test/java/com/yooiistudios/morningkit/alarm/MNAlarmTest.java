package com.yooiistudios.morningkit.alarm;

import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 11. 11..
 */
@RunWith(RobolectricGradleTestRunner.class)
public class MNAlarmTest {

    MNAlarm testAlarm;

    @Before
    public void setUp() {
        testAlarm = new MNAlarm();
    }

    @Test
    // 필요한 정보들이 null값이 아닌지 테스트
    public void alarmConstructorTest() {
        assertThat(testAlarm, notNullValue());
        assertThat(testAlarm.isAlarmOn, is(true));
        assertThat(testAlarm.isSnoozeOn, is(false));
        assertThat(testAlarm.isRepeatOn, is(false));
        assertThat(testAlarm.alarmLabel, is("Alarm"));
        assertThat(testAlarm.alarmID, is(not(-1)));
        assertThat(testAlarm.alarmCalendar, notNullValue());

        for (int i = 0; i < testAlarm.alarmRepeatOnOfWeek.size(); i++) {
            Boolean alarmRepeatOnSpecificWeekday = testAlarm.alarmRepeatOnOfWeek.get(i);
            assertThat(alarmRepeatOnSpecificWeekday, is(false));
        }
    }
}
