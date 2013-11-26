package com.yooiistudios.morningkit.alarm;

import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.MNMainActivity;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 11. 11..
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config (shadows = { AdWebViewShadow.class })
public class MNAlarmMakerTest {

    MNAlarm defaultAlarm;
    MNAlarm customAlarm;
    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();

        defaultAlarm = MNAlarmMaker.makeAlarm(mainActivity.getBaseContext());
        customAlarm = MNAlarmMaker.makeAlarmWithTime(mainActivity.getBaseContext(), 6, 30);
    }

    @Test
    public void makeNewDefaultAlarmTest() {
        assertNotNull(defaultAlarm);

        assertThat(defaultAlarm, notNullValue());
        assertThat(defaultAlarm.isAlarmOn, is(true));
        assertThat(defaultAlarm.isSnoozeOn, is(false));
        assertThat(defaultAlarm.isRepeatOn, is(false));
        assertThat(defaultAlarm.alarmLabel, is("Alarm"));
        // 알람 ID를 제대로 할당했는지 테스트
        assertThat(defaultAlarm.alarmID, is(not(-1)));
        assertThat(defaultAlarm.alarmCalendar, notNullValue());
        // 알람 초는 0초로 설정해야함
        assertThat(defaultAlarm.alarmCalendar.get(Calendar.SECOND), is(0));

        // 초기 설정은 모두 false
        for (int i = 0; i < defaultAlarm.alarmRepeatOnOfWeek.size(); i++) {
            Boolean alarmRepeatOnSpecificWeekday = defaultAlarm.alarmRepeatOnOfWeek.get(i);
            assertThat(alarmRepeatOnSpecificWeekday, is(false));
        }

        // 제대로 된 사운드 타입이 할당되었는지 테스트
        // 제대로 된 사운드 리소스가 대입되었는지 테스트
    }

    @Test
    public void makeNewCustomAlarmTest() {
        assertNotNull(customAlarm);

        // 기본으로 끈 채로 추가
        assertThat(customAlarm.isAlarmOn, is(false));

        // 특정 시간으로 넣었을 때 올해/이번달/오늘 or 내일의 특정 시간으로 나오는지 테스트
        int hour = customAlarm.alarmCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = customAlarm.alarmCalendar.get(Calendar.MINUTE);

        // 시간부터 체크
        assertThat(hour, is(6));
        assertThat(minute, is(30));

        // alarmCalendar가 현재 시간보다 전이면 어제인지, 후면 오늘인지 체크하기
        Calendar currentTimeCalendar = Calendar.getInstance();
        int currentTimeHour = currentTimeCalendar.get(Calendar.HOUR_OF_DAY);
        int currentTimeMinute = currentTimeCalendar.get(Calendar.MINUTE);

        // 시간을 비교해서 하루를 더해주는 부분은 이미 적용이 되어 실제 코드에서 적용이 되어 있으므로
        // 테스트 코드를 다시 작성해야 함
        /*
        if (customAlarm.alarmCalendar.getTimeInMillis() > currentTimeCalendar.getTimeInMillis()) {
            // 오늘인지 확인
            assertThat(customAlarm.alarmCalendar.get(Calendar.DAY_OF_MONTH),
                    is(currentTimeCalendar.get(Calendar.DAY_OF_MONTH)));
        }else{
            // 어제인지 확인
            assertThat(customAlarm.alarmCalendar.get(Calendar.DAY_OF_MONTH),
                    is(currentTimeCalendar.get(Calendar.DAY_OF_MONTH) - 1));
        }
        */
    }
}
