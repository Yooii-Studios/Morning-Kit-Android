package com.yooiistudios.morningkit.alarm;

import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

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
        assertNotNull(testAlarm);
    }
}
