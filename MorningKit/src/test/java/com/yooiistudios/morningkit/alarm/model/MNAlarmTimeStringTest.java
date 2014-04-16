package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;

import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.model.string.MNAlarmTimeString;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.MNMainActivity;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 18.
 *
 * MNAlarmTimeStringTest
 *  시, 분 -> String 으로 변환해주는 유틸리티 클래스 테스트
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = { AdWebViewShadow.class }, emulateSdk = 18)
public class MNAlarmTimeStringTest {
    MNMainActivity mainActivity;
    Context context;

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;

        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
        context = mainActivity;
    }

    // 내가 가정한 각종 시간과 상황에 대해서 확인을 할 것
    @Test
    public void testAlarmTimeString_0_0() throws Exception {
        MNAlarm alarm_0_0 = MNAlarmMaker.makeAlarmWithTime(mainActivity, 0, 0);
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_0_0.getAlarmCalendar(), context, false), is("12:00"));
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_0_0.getAlarmCalendar(), context, true), is("00:00"));
    }
    @Test
    public void testAlarmTimeString_8_30() throws Exception {
        MNAlarm alarm_8_30 = MNAlarmMaker.makeAlarmWithTime(mainActivity, 8, 30);
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_8_30.getAlarmCalendar(), context, false), is("08:30"));
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_8_30.getAlarmCalendar(), context, true), is("08:30"));
    }
    @Test
    public void testAlarmTimeString_12_01() throws Exception {
        MNAlarm alarm_12_01 = MNAlarmMaker.makeAlarmWithTime(mainActivity, 12, 1);
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_12_01.getAlarmCalendar(), context, false), is("12:01"));
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_12_01.getAlarmCalendar(), context, true), is("12:01"));
    }
    @Test
    public void testAlarmTimeString_14_59() throws Exception {
        MNAlarm alarm_14_59 = MNAlarmMaker.makeAlarmWithTime(mainActivity, 14, 59);
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_14_59.getAlarmCalendar(), context, false), is("02:59"));
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_14_59.getAlarmCalendar(), context, true), is("14:59"));
    }
    @Test
    public void testAlarmTimeString_22_45() throws Exception {
        MNAlarm alarm_22_45 = MNAlarmMaker.makeAlarmWithTime(mainActivity, 22, 45);
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_22_45.getAlarmCalendar(), context, false), is("10:45"));
        assertThat(MNAlarmTimeString.makeTimeStringForTest(alarm_22_45.getAlarmCalendar(), context, true), is("22:45"));
    }
}
