package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmComparator;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.common.sharedpreferences.MNSharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
@RunWith(AndroidJUnit4.class)
public class MNAlarmListManagerTest extends InstrumentationTestCase {
    Context mContext;
    ArrayList<MNAlarm> dummyAlarmList;
    ArrayList<MNAlarm> dummySortAlarmList;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mContext = getInstrumentation().getContext();
        dummyAlarmList = new ArrayList<MNAlarm>();
        dummySortAlarmList = new ArrayList<MNAlarm>();
    }

    /**
     * Save & Load
     */
    @Test
    public void saveAndLoadAlarmListTest() throws IOException {
        // SharedPreference 를 비운다
        SharedPreferences prefs = mContext.getSharedPreferences(
                MNSharedPreferences.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(MNAlarmListManager.ALARM_LIST);
        editor.apply();

        // 무조건 alarmList 를 읽으면 null 은 되면 안된다(초기화 로직도 존재함)
        assertThat(MNAlarmListManager.getAlarmList(mContext), notNullValue());

        // 알람 리스트를 첫 로드할 경우는 알람이 2개가 있어야 함(6:30 / 7:30)
        dummyAlarmList = MNAlarmListManager.loadAlarmList(mContext);
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
        dummyAlarmList.add(MNAlarmMaker.makeAlarm(mContext));
        MNAlarmListManager.saveAlarmList(mContext);

        // 알람을 다시 로드하면 3개가 있어야 함
        dummyAlarmList = MNAlarmListManager.loadAlarmList(mContext);
        assertThat(dummyAlarmList.size(), is(3));
    }

    /**
     * Manipulating Alarm
     */
    @Test
    public void replaceAlarmTest() {
        int sizeOfAlarmList = MNAlarmListManager.getAlarmList(mContext).size();
        assertThat(sizeOfAlarmList, is(not(0)));

        MNAlarm originalAlarm = MNAlarmListManager.getAlarmList(mContext).get(0);
        boolean isAlarmOn = originalAlarm.isAlarmOn();
        originalAlarm.setAlarmOn(!originalAlarm.isAlarmOn());
        MNAlarmListManager.replaceAlarmToAlarmList(originalAlarm, mContext);
        assertThat(MNAlarmListManager.getAlarmList(mContext).size(), is(sizeOfAlarmList));

        // 변경한 isAlarmOn을 체크해서 알람이 교체되었는지 확인 필요
        MNAlarm testAlarm = MNAlarmListManager.findAlarmById(originalAlarm.getAlarmId(), mContext);
        assertThat(testAlarm.isAlarmOn(), is(not(isAlarmOn)));
    }

    @Test
    public void addAlarmTest() {
        int sizeOfAlarmList = MNAlarmListManager.getAlarmList(mContext).size();
        MNAlarm alarm = MNAlarmMaker.makeAlarm(mContext);
        alarm.setAlarmId(500);

        MNAlarmListManager.addAlarmToAlarmList(alarm, mContext);
        assertThat(MNAlarmListManager.getAlarmList(mContext).size(), is(sizeOfAlarmList+1));

        MNAlarm testAlarm = MNAlarmListManager.findAlarmById(500, mContext);
        assertThat(testAlarm, notNullValue());
        assertThat(testAlarm.getAlarmId(), is(alarm.getAlarmId()));
    }

    @Test
    public void findAlarmWithAlarmIDTest() {
        dummyAlarmList = MNAlarmListManager.getAlarmList(mContext);
        assertThat(MNAlarmListManager.getAlarmList(mContext).size(), is(not(0)));
        for (MNAlarm alarm : dummyAlarmList) {
            MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarm.getAlarmId(), mContext);
            assertThat(targetAlarm, is(alarm));
            assertThat(targetAlarm.getAlarmId(), is(alarm.getAlarmId()));
        }
    }

    @Test
    public void removeAlarmTest() {
        int sizeOfAlarmList = MNAlarmListManager.getAlarmList(mContext).size();
        assertThat(sizeOfAlarmList, is(not(0)));

        MNAlarm targetAlarm = MNAlarmListManager.getAlarmList(mContext).get(0);
        MNAlarmListManager.removeAlarmFromAlarmList(targetAlarm.getAlarmId(), mContext);
        assertThat(MNAlarmListManager.findAlarmById(targetAlarm.getAlarmId(), mContext), nullValue());
        assertThat(MNAlarmListManager.getAlarmList(mContext).size(), is(sizeOfAlarmList-1));
    }

    /**
     * Sort
     */
    @Test
    public void sortAlarmListTest() throws Exception {
        // 지우면 6:30 / 7:00 두 개가 있음
        MNAlarmListManager.removeAlarmList(mContext);
        MNAlarm alarm_06_30 = MNAlarmListManager.getAlarmList(mContext).get(0);
        assertThat(MNAlarmComparator.makeComparator(alarm_06_30), is(630));

        MNAlarm alarm_07_00 = MNAlarmListManager.getAlarmList(mContext).get(1);
        assertThat(MNAlarmComparator.makeComparator(alarm_07_00), is(700));

        MNAlarm alarm_14_15 = MNAlarmMaker.makeAlarmWithTime(mContext, 14, 15);
        assertThat(MNAlarmComparator.makeComparator(alarm_14_15), is(1415));
        MNAlarmListManager.addAlarmToAlarmList(alarm_14_15, mContext);

        MNAlarm alarm_00_00 = MNAlarmMaker.makeAlarmWithTime(mContext, 0, 0);
        assertThat(MNAlarmComparator.makeComparator(alarm_00_00), is(0));
        MNAlarmListManager.addAlarmToAlarmList(alarm_00_00, mContext);

        MNAlarm alarm_03_01 = MNAlarmMaker.makeAlarmWithTime(mContext, 3, 1);
        assertThat(MNAlarmComparator.makeComparator(alarm_03_01), is(301));
        MNAlarmListManager.addAlarmToAlarmList(alarm_03_01, mContext);

        // 기존 2개 알람에 3개 추가해 총 5개임을 확인
        assertThat(MNAlarmListManager.getAlarmList(mContext).size(), is(5));

        // 소팅
        MNAlarmListManager.sortAlarmList(mContext);

        // 이상적인 순서: 00:00 / 03:01 / 06:30 / 07:00 / 14:15
        assertThat(MNAlarmListManager.getAlarmList(mContext).get(0).getAlarmId(), is(alarm_00_00.getAlarmId()));
        assertThat(MNAlarmListManager.getAlarmList(mContext).get(1).getAlarmId(), is(alarm_03_01.getAlarmId()));
        assertThat(MNAlarmListManager.getAlarmList(mContext).get(4).getAlarmId(), is(alarm_14_15.getAlarmId()));
    }
}
