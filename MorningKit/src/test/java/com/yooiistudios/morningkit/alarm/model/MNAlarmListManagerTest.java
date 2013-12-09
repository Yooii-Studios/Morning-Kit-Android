package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.MNMainActivity;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

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
    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
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
        int sizeOfAlarmList = MNAlarmListManager.getAlarmList(mainActivity).size();
        assertThat(sizeOfAlarmList, is(not(0)));

        MNAlarm originalAlarm = MNAlarmListManager.getAlarmList(mainActivity).get(0);
        boolean isAlarmOn = originalAlarm.isAlarmOn();
        originalAlarm.setAlarmOn(!originalAlarm.isAlarmOn());
        MNAlarmListManager.replaceAlarmToAlarmList(originalAlarm, mainActivity);
        assertThat(MNAlarmListManager.getAlarmList(mainActivity).size(), is(sizeOfAlarmList));

        // 변경한 isAlarmOn을 체크해서 알람이 교체되었는지 확인 필요
        MNAlarm testAlarm = MNAlarmListManager.findAlarmById(originalAlarm.getAlarmId(), mainActivity);
        assertThat(testAlarm.isAlarmOn(), is(not(isAlarmOn)));
    }

    @Test
    public void addAlarmTest() {
        int sizeOfAlarmList = MNAlarmListManager.getAlarmList(mainActivity).size();
        MNAlarm alarm = MNAlarmMaker.makeAlarm(mainActivity);
        alarm.setAlarmId(500);

        MNAlarmListManager.addAlarmToAlarmList(alarm, mainActivity);
        assertThat(MNAlarmListManager.getAlarmList(mainActivity).size(), is(sizeOfAlarmList+1));

        MNAlarm testAlarm = MNAlarmListManager.findAlarmById(500, mainActivity);
        assertThat(testAlarm, notNullValue());
        assertThat(testAlarm.getAlarmId(), is(alarm.getAlarmId()));
    }

    @Test
    public void findAlarmWithAlarmIDTest() {
        dummyAlarmList = MNAlarmListManager.getAlarmList(mainActivity.getBaseContext());
        assertThat(MNAlarmListManager.getAlarmList(mainActivity).size(), is(not(0)));
        for (MNAlarm alarm : dummyAlarmList) {
            MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarm.getAlarmId(), mainActivity.getBaseContext());
            assertThat(targetAlarm, is(alarm));
            assertThat(targetAlarm.getAlarmId(), is(alarm.getAlarmId()));
        }
    }

    @Test
    public void removeAlarmTest() {
        int sizeOfAlarmList = MNAlarmListManager.getAlarmList(mainActivity).size();
        assertThat(sizeOfAlarmList, is(not(0)));

        MNAlarm targetAlarm = MNAlarmListManager.getAlarmList(mainActivity).get(0);
        MNAlarmListManager.removeAlarmFromAlarmList(targetAlarm.getAlarmId(), mainActivity);
        assertThat(MNAlarmListManager.findAlarmById(targetAlarm.getAlarmId(), mainActivity), nullValue());
        assertThat(MNAlarmListManager.getAlarmList(mainActivity).size(), is(sizeOfAlarmList-1));
    }

    /**
     * Sort
     */
    @Test
    public void sortAlarmListTest() {

    }
}
