package com.yooiistudios.morningkit.alarm;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
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

    /**
     * Save & Load
     */
    @Test
    public void saveAlarmListTest() {

    }

    @Test
    public void loadAlarmListTest() {

    }

    /**
     * Manipulating Alarm
     */
    @Test
    public void replaceAlarmTest() {

    }

    @Test
    public void addAlarmTest() {

    }

    @Test
    public void findAlarmWithAlarmIDTest() {

    }

    @Test
    public void removeAlarmTest() {

    }

    /**
     * Sort
     */
    @Test
    public void sortAlarmListTest() {

    }
}
