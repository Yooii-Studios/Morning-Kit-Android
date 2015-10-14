package com.yooiistudios.morningkit.alarm.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.main.MNMainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim on 2013. 11. 11..
 *
 * MNAlarmTest
 */
@RunWith(AndroidJUnit4.class)
public class MNAlarmTest extends ActivityInstrumentationTestCase2<MNMainActivity> {
    Context mContext;
    MNAlarm alarm;
    ArrayList<MNAlarm> alarmList;

    public MNAlarmTest() {
        super(MNMainActivity.class);
    }

    @Before
    public void setUp() {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mContext = getActivity();

        // Alarm
        alarm = MNAlarm.newInstance();

        alarmList = new ArrayList<MNAlarm>();
        MNAlarm testAlarm1 = MNAlarm.newInstance();
        testAlarm1.setAlarmId(30);
        alarmList.add(testAlarm1);

        MNAlarm testAlarm2 = MNAlarm.newInstance();
        testAlarm2.setAlarmId(38);
        alarmList.add(testAlarm2);
    }

    @Test
    // 필요한 정보들이 null 값이 아닌지 테스트
    public void alarmConstructorTest() {
        assertThat(alarm, notNullValue());
    }

    @SuppressLint("SimpleDateFormat")
    @Test
    public void testAdjustAlarmCalendar() throws Exception {

        Calendar nowCalendar = Calendar.getInstance();
        Log.i("MNAlarmTest", new SimpleDateFormat("MM/dd/HH:mm").format(nowCalendar.getTime()));

        // 시간 정보만 있는 과거의 Calendar 로 변경
        MNAlarm alarm_23_05 = MNAlarmMaker.makeAlarmWithTime(mContext, 23, 5);
        testAdjustAlarmCalendarForEachAlarm(alarm_23_05);

        MNAlarm alarm_03_10 = MNAlarmMaker.makeAlarmWithTime(mContext, 3, 10);
        testAdjustAlarmCalendarForEachAlarm(alarm_03_10);

        MNAlarm alarm_08_30 = MNAlarmMaker.makeAlarmWithTime(mContext, 8, 30);
        testAdjustAlarmCalendarForEachAlarm(alarm_08_30);

        MNAlarm alarm_13_05 = MNAlarmMaker.makeAlarmWithTime(mContext, 13, 5);
        testAdjustAlarmCalendarForEachAlarm(alarm_13_05);

        MNAlarm alarm_17_10 = MNAlarmMaker.makeAlarmWithTime(mContext, 17, 10);
        testAdjustAlarmCalendarForEachAlarm(alarm_17_10);

        MNAlarm alarm_22_50 = MNAlarmMaker.makeAlarmWithTime(mContext, 22, 50);
        testAdjustAlarmCalendarForEachAlarm(alarm_22_50);
    }

    private void testAdjustAlarmCalendarForEachAlarm(MNAlarm alarm) {
        Calendar nowCalendar = Calendar.getInstance();

        // 알람이 현재 시각보다 이전 시간/분 이라면 내일로 설정이 되기에 날짜가 달라야 한다
        if (alarm.getAlarmCalendar().before(nowCalendar)) {
            alarm.startAlarmWithNoToast(mContext);
            assertThat(alarm.getAlarmCalendar().get(Calendar.DATE), is(not(nowCalendar.get(Calendar.DATE))));
        } else {
            alarm.startAlarmWithNoToast(mContext);
            assertThat(alarm.getAlarmCalendar().get(Calendar.DATE), is(nowCalendar.get(Calendar.DATE)));
        }
    }

    // AlarmManager 에서 정보를 가져오는 함수가 없어 테스트가 불가능할듯
    @Test
    public void startNonRepeatAlarmTest() {
        /*
        MNAlarm nonRepeatAlarm = MNDummyAlarmMaker.makeNonRepeatAlarm(mContext);
        nonRepeatAlarm.startAlarm(mContext);

        AlarmManager alarmManager = MNAlarmManager.getAlarmManager(mContext);
        assertThat(alarmManager, notNullValue());

        nonRepeatAlarm.startAlarm(mContext);
        */

        // Calendar 시간 비교해서 오늘, 내일 제대로 적용 되는지 테스트

        // 시간을 비교해서 하루를 더해주는 부분은 이미 적용이 되어 실제 코드에서 적용이 되어 있으므로
        // 테스트 코드를 다시 작성해야 함
        // alarmCalendar 의 hour 가 currentTimeCalendar 의 hour 보다 클 경우: 오늘
        // 작을 경우: 내일
        // 같을 경우:
        // 1. alarmCalendar 의 minute 가 currentTimeCalendar 의 minute 보다 클 경우: 오늘
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
