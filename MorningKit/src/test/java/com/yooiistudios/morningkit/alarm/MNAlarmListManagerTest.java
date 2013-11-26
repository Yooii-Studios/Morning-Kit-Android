package com.yooiistudios.morningkit.alarm;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmMaker;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.MNMainActivity;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 11. 11..
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config (shadows = { AdWebViewShadow.class })
public class MNAlarmListManagerTest {
    ArrayList<MNAlarm> alarmListForTestSave;
    ArrayList<MNAlarm> alarmListForTestLoad;
    ArrayList<MNAlarm> dummyAlarmList;

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();

        // 미리 save 테스트용 ArrayList, load 테스트용 ArrayList를 만들어 두자
        alarmListForTestSave = new ArrayList<MNAlarm>();
        alarmListForTestSave.add(MNAlarmMaker.makeAlarm(mainActivity.getBaseContext()));
        alarmListForTestSave.add(MNAlarmMaker.makeAlarm(mainActivity.getBaseContext()));
        alarmListForTestSave.add(MNAlarmMaker.makeAlarm(mainActivity.getBaseContext()));

        alarmListForTestLoad = new ArrayList<MNAlarm>();

        dummyAlarmList = new ArrayList<MNAlarm>();
    }

    /**
     * Save & Load
     */
    @Test
    public void saveAndLoadAlarmListTest() {

        // SharedPreference를 비운다

        // 알람 리스트를 첫 로드할 경우는 알람이 2개가 있어야 함

        // 알람 하나를 추가하고 저장한다

        // 알람을 다시 로드하면 3개가 있어야 함
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
