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
public class MNAlarmMakerTest {

    MNAlarm defaultAlarm;
    MNAlarm customAlarm;

    @Before
    public void setUp() {
        defaultAlarm = MNAlarmMaker.makeAlarm();
        customAlarm = MNAlarmMaker.makeAlarmWithTime(6, 30);
    }

    @Test
    public void makeNewDefaultAlarmTest() {
        assertNotNull(defaultAlarm);

        // 알람 ID를 제대로 할당했는지 테스트
        // 제대로 된 사운드 타입이 할당되었는지 테스트
        // 제대로 된 사운드 리소스가 대입되었는지 테스트

    }

    @Test
    public void makeNewCustomAlarmTest() {
        assertNotNull(customAlarm);

        // 특정 시간으로 넣었을 때 올해/이번달/오늘 or 내일의 특정 시간으로 나오는지 테스트
    }
}
