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
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

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
        // visible() 이 뷰를 띄울 수 있게 해주는 중요한 메서드
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
    }

    @Test
    public void shouldViewsAndLayoutsBeNotNull() throws Exception {
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
    @Config(qualifiers="port")
    public void checkWidgetWindowLayoutHeightOnPortrait() throws Exception {
        // 2 * 2일 경우
        // (위젯 높이 * 2) + outer padding(위쪽) + (inner padding * 2(중앙) * 1) + inner padding(아래쪽)

        // 2 * 1일 경우
        // (위젯 높이 * 2) + outer padding(위쪽) + (inner padding * 2(중앙) * 0) + inner padding(아래쪽)
        assertThat(mainActivity.getWidgetWindowLayout().getHeight(), // mainActivity.getWidgetWindowLayout().getMeasuredHeight()
                is((int)DipToPixel.getPixel(mainActivity,
                        mainActivity.getResources().getDimension(R.dimen.main_widget_window_layout_height))));
    }

    @Test
    @Config(qualifiers="land")
    public void checkWidgetWindowLayoutHeightOnLandscape() throws Exception {
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
