package com.yooiistudios.morningkit.alarm;

import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 11. 11..
 */
@RunWith(RobolectricGradleTestRunner.class)
public class MNAlarmListManagerTest {
    ArrayList<MNAlarm> alarmListForTestSave;
    ArrayList<MNAlarm> alarmListForTestLoad;

    @Before
    public void setUp() {
        // 미리 save 테스트용 ArrayList, load 테스트용 ArrayList를 만들어 두자
        alarmListForTestSave = new ArrayList<MNAlarm>();
        alarmListForTestLoad = new ArrayList<MNAlarm>();
    }

    @Test
    public void saveAlarmListTest() {

    }

    @Test
    public void loadAlarmListTest() {

    }
}
