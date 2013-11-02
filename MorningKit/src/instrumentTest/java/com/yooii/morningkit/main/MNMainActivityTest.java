package com.yooii.morningkit.main;

// necessary import
import android.widget.RelativeLayout;

import com.yooii.morningkit.RobolectricGradleTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import java.lang.Exception;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 10. 31..
 */
// guide for using Robolectric and JUnit
// Class name must be ended with xxxTest in test class
@RunWith(RobolectricGradleTestRunner.class)
public class MNMainActivityTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().get();
    }

    @Test
    public void shouldAlarmListViewAndWidgetWindowViewNotNull() throws Exception {
        assertNotNull(mainActivity);
        assertThat(mainActivity, instanceOf(MNMainActivity.class));

        assertNotNull(mainActivity.getAlarmListView());
        assertThat(mainActivity, instanceOf(MNMainAlarmListView.class));

        assertNotNull(mainActivity.getWidgetWindowView());
        assertThat(mainActivity, instanceOf(MNWidgetWindowView.class));

        assertNotNull(mainActivity.getButtonLayout());
        assertThat(mainActivity, instanceOf(RelativeLayout.class));

        assertNotNull(mainActivity.getAdmobLayout());
        assertThat(mainActivity, instanceOf(RelativeLayout.class));
    }

    @Test
    public void checkWidgetWindowHeight() throws Exception {
        // dimension으로 정의한 특정 높이를 가지고 있어야 함
    }

    @Test
    public void checkAlarmListHeight() throws Exception {
        // 디바이스 높이 - (위젯 윈도우 높이 + 구글 애드몹 높이(풀버전은 높이가 0)인지 확인
    }
}
