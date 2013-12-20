package com.yooiistudios.morningkit.alarm.model;

import android.app.AlarmManager;
import android.content.Context;

import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.MNMainActivity;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 11. 11..
 *
 * MNAlarmTest
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = { AdWebViewShadow.class })
public class MNAlarmTest {

    MNAlarm alarm;
    ArrayList<MNAlarm> alarmList;
    MNMainActivity mainActivity;
    Context context;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        // Alarm
        alarm = MNAlarm.newInstance();

        alarmList = new ArrayList<MNAlarm>();
        MNAlarm testAlarm1 = MNAlarm.newInstance();
        testAlarm1.setAlarmId(30);
        alarmList.add(testAlarm1);

        MNAlarm testAlarm2 = MNAlarm.newInstance();
        testAlarm2.setAlarmId(38);
        alarmList.add(testAlarm2);

        // Main
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
        context = mainActivity;
    }

    @Test
    // 필요한 정보들이 null값이 아닌지 테스트
    public void alarmConstructorTest() {
        assertThat(alarm, notNullValue());
    }

    @Test
    public void startNonRepeatAlarmTest() {
        MNAlarm nonRepeatAlarm = MNDummyAlarmMaker.makeNonRepeatAlarm(context);
        nonRepeatAlarm.startAlarm(context);

        AlarmManager alarmManager = MNAlarmManager.getAlarmManager(context);
        assertThat(alarmManager, notNullValue());


        // Calendar 시간 비교해서 오늘, 내일 제대로 적용 되는지 테스트

        // 시간을 비교해서 하루를 더해주는 부분은 이미 적용이 되어 실제 코드에서 적용이 되어 있으므로
        // 테스트 코드를 다시 작성해야 함
        // alarmCalendar의 hour가 currentTimeCalendar의 hour보다 클 경우: 오늘
        // 작을 경우: 내일
        // 같을 경우:
        // 1. alarmCalendar의 minute가 currentTimeCalendar의 minute보다 클 경우: 오늘
        // 2. 그외: 내일

    }

    @Test
    public void startRepeatAlarmTest() {
        // 7개의 알람이 제대로 등록 되는지 테스트
    }

    @Test
    public void stopNonRepeatAlarmTest() {
        // 미반복 알람 제대로 꺼지는지 테스트
    }

    @Test
    public void stopRepeatAlarmTest() {
        // 7개 알람 제대로 꺼지는지 테스트
    }

    @Test
    public void snoozeAlarmTest() {
        // n+7 alarmId의 알람이 제대로 등록이 되는지 테스트
    }
}
