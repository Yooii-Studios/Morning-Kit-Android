package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.model.string.MNAlarmTimeString;
import com.yooiistudios.morningkit.main.MNMainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 18.
 *
 * MNAlarmTimeStringTest
 *  시, 분 -> String 으로 변환해주는 유틸리티 클래스 테스트
 */
@RunWith(AndroidJUnit4.class)
public class MNAlarmTimeStringTest extends ActivityInstrumentationTestCase2<MNMainActivity> {
    Context mContext;

    public MNAlarmTimeStringTest() {
        super(MNMainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mContext = getActivity();
    }

    // 내가 가정한 각종 시간과 상황에 대해서 확인을 할 것
    @Test
    public void testAlarmTimeString_0_0() throws Exception {
        MNAlarm alarm_0_0 = MNAlarmMaker.makeAlarmWithTime(mContext, 0, 0);
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_0_0.getAlarmCalendar(), mContext, false), is("12:00"));
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_0_0.getAlarmCalendar(), mContext, true), is("00:00"));
    }
    @Test
    public void testAlarmTimeString_8_30() throws Exception {
        MNAlarm alarm_8_30 = MNAlarmMaker.makeAlarmWithTime(mContext, 8, 30);
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_8_30.getAlarmCalendar(), mContext, false), is("08:30"));
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_8_30.getAlarmCalendar(), mContext, true), is("08:30"));
    }
    @Test
    public void testAlarmTimeString_12_01() throws Exception {
        MNAlarm alarm_12_01 = MNAlarmMaker.makeAlarmWithTime(mContext, 12, 1);
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_12_01.getAlarmCalendar(), mContext, false), is("12:01"));
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_12_01.getAlarmCalendar(), mContext, true), is("12:01"));
    }
    @Test
    public void testAlarmTimeString_14_59() throws Exception {
        MNAlarm alarm_14_59 = MNAlarmMaker.makeAlarmWithTime(mContext, 14, 59);
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_14_59.getAlarmCalendar(), mContext, false), is("02:59"));
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_14_59.getAlarmCalendar(), mContext, true), is("14:59"));
    }
    @Test
    public void testAlarmTimeString_22_45() throws Exception {
        MNAlarm alarm_22_45 = MNAlarmMaker.makeAlarmWithTime(mContext, 22, 45);
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_22_45.getAlarmCalendar(), mContext, false), is("10:45"));
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_22_45.getAlarmCalendar(), mContext, true), is("22:45"));
    }
}
