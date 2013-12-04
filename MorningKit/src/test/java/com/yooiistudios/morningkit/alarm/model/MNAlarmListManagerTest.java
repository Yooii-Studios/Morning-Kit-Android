package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 11. 11..
 *
 * MNAlarmListManagerTest
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config (shadows = { AdWebViewShadow.class })
public class MNAlarmListManagerTest {
    ArrayList<MNAlarm> dummyAlarmList;
    ArrayList<MNAlarm> dummySortAlarmList;
    MNMainActivity_ mainActivity;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        mainActivity = Robolectric.buildActivity(MNMainActivity_.class).create().visible().get();
        dummyAlarmList = new ArrayList<MNAlarm>();
        dummySortAlarmList = new ArrayList<MNAlarm>();
    }

    /**
     * Save & Load
     */
    @Test
    public void saveAndLoadAlarmListTest() throws IOException {
        
        // SharedPreference를 비운다
        SharedPreferences prefs = mainActivity.getSharedPreferences(MN.alarm.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(MN.alarm.ALARM_LIST);
        editor.commit();

        // 무조건 alarmList를 읽으면 null은 되면 안된다(초기화 로직도 존재함)
        assertThat(MNAlarmListManager.getAlarmList(mainActivity.getBaseContext()), notNullValue());

        // 알람 리스트를 첫 로드할 경우는 알람이 2개가 있어야 함(6:30 / 7:30)
        dummyAlarmList = MNAlarmListManager.loadAlarmList(mainActivity.getBaseContext());
        assertThat(dummyAlarmList.size(), is(2));

        // 6:30, 7:00 을 확인
        if (dummyAlarmList.size() == 2) {
            MNAlarm firstAlarm = dummyAlarmList.get(0);
            MNAlarm secondAlarm = dummyAlarmList.get(1);

            assertThat(firstAlarm.getAlarmCalendar().get(Calendar.HOUR_OF_DAY), is(6));
            assertThat(firstAlarm.getAlarmCalendar().get(Calendar.MINUTE), is(30));

            assertThat(secondAlarm.getAlarmCalendar().get(Calendar.HOUR_OF_DAY), is(7));
            assertThat(secondAlarm.getAlarmCalendar().get(Calendar.MINUTE), is(0));
        }

        // 알람 하나를 추가하고 저장한다
        dummyAlarmList.add(MNAlarmMaker.makeAlarm(mainActivity.getBaseContext()));
//        MNAlarmListManager.saveAlarmList(dummyAlarmList, mainActivity.getBaseContext());
        MNAlarmListManager.saveAlarmList(mainActivity.getBaseContext());

        // 알람을 다시 로드하면 3개가 있어야 함
        dummyAlarmList = MNAlarmListManager.loadAlarmList(mainActivity.getBaseContext());
        assertThat(dummyAlarmList.size(), is(3));
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
        dummyAlarmList = MNAlarmListManager.loadAlarmList(mainActivity.getBaseContext());
        for (MNAlarm alarm : dummyAlarmList) {
            MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarm.getAlarmId(), mainActivity.getBaseContext());
            assertThat(targetAlarm, is(alarm));
            assertThat(targetAlarm.getAlarmId(), is(alarm.getAlarmId()));
        }
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
