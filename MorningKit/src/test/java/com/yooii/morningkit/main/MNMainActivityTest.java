package com.yooii.morningkit.main;

// necessary import
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.widget.RelativeLayout;

import com.yooii.morningkit.R;
import com.yooii.morningkit.RobolectricGradleTestRunner;
import com.yooii.morningkit.common.DipToPixel;
import com.yooii.morningkit.common.MNDeviceSizeChecker;

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
        assertThat(mainActivity.getAlarmListView(), instanceOf(MNMainAlarmListView.class));

        assertNotNull(mainActivity.getWidgetWindowLayout());
        assertThat(mainActivity.getWidgetWindowLayout(), instanceOf(MNWidgetWindowLayout.class));

        assertNotNull(mainActivity.getButtonLayout());
        assertThat(mainActivity.getButtonLayout(), instanceOf(RelativeLayout.class));

        assertNotNull(mainActivity.getAdmobLayout());
        assertThat(mainActivity.getAdmobLayout(), instanceOf(RelativeLayout.class));
    }

    @Test
    public void checkWidgetWindowLayoutHeight() throws Exception {
        // dimension으로 정의한 특정 높이를 가지고 있어야 함

        // 1. Portrait
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assertThat(mainActivity.getWidgetWindowLayout().getHeight(),
                is((int)DipToPixel.getPixel(mainActivity,
                        mainActivity.getResources().getDimension(R.dimen.main_widget_window_layout_height))));
    }

    @Test
    public void checkWidgetWindowLayoutHeightOnLandscape() throws Exception {
        // dimension으로 정의한 특정 높이를 가지고 있어야 함

        // 2. Landscape
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // Device height - buttonLayout height - (outerPadding - innerPadding)를 확인하면 됨
        // 위젯 윈도우뷰의 아래쪽은 innerPadding 만큼만 주기 때문에 (outerPadding - innerPadding)만큼의 공간을 따로 주어야 함
        Resources resource = mainActivity.getResources();
        int expectedHeight = MNDeviceSizeChecker.getDeviceHeight(mainActivity)
                - mainActivity.getButtonLayout().getHeight()
                - (int)(resource.getDimension(R.dimen.padding_outer) - resource.getDimension(R.dimen.padding_inner));
        assertThat(mainActivity.getWidgetWindowLayout().getHeight(), is(expectedHeight));
    }

    @Test
    public void checkAlarmListViewHeight() throws Exception {
        // 1. Portrait
        // 디바이스 높이 - (위젯 윈도우 높이 + 구글 애드몹 높이(풀버전은 높이가 0)인지 확인
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 2. Landscape
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
