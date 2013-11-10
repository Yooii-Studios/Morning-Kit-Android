package com.yooiistudios.morningkit.main;

// necessary import
import android.widget.RelativeLayout;

import com.google.ads.AdView;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

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
@Config (shadows = { AdWebViewShadow.class })
public class MNMainActivityTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        // visible() 이 뷰를 띄울 수 있게 해주는 중요한 메서드
//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().start().resume().visible().get();
//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().postResume().get();
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
    }

    @Test
    public void shouldViewsAndLayoutsBeNotNull() throws Exception {
        assertThat(mainActivity, notNullValue());
//        assertNotNull(mainActivity);
        assertThat(mainActivity, instanceOf(MNMainActivity.class));

        assertThat(mainActivity.getAlarmListView(), notNullValue());
//        assertNotNull(mainActivity.getAlarmListView());
        assertThat(mainActivity.getAlarmListView(), instanceOf(MNMainAlarmListView.class));

        assertThat(mainActivity.getWidgetWindowLayout(), notNullValue());
//        assertNotNull(mainActivity.getWidgetWindowLayout());
        assertThat(mainActivity.getWidgetWindowLayout(), instanceOf(MNWidgetWindowLayout.class));

        assertThat(mainActivity.getButtonLayout(), notNullValue());
//        assertNotNull(mainActivity.getButtonLayout());
        assertThat(mainActivity.getButtonLayout(), instanceOf(RelativeLayout.class));

        assertThat(mainActivity.getAdmobLayout(), notNullValue());
//        assertNotNull(mainActivity.getAdmobLayout());
        assertThat(mainActivity.getAdmobLayout(), instanceOf(RelativeLayout.class));

        assertThat(mainActivity.getAdView(), notNullValue());
//        assertNotNull(mainActivity.getAdView());
        assertThat(mainActivity.getAdView(), instanceOf(AdView.class));
    }

    /**
     * Scroll View
     */
    /*
    @Test
    @Config(qualifiers="port")
    public void checkScrollViewHeightOnPortrait() throws Exception {

    }

    @Test
    @Config(qualifiers="land")
    public void checkScrollViewHeightOnLandscape() throws Exception {

    }
    */

    /**
     * Widget Window
     */
    /*
    @Test
    @Config(qualifiers="port")
    public void checkWidgetWindowLayoutHeightOnPortrait() throws Exception {

        // assertTrue(false);
        MNViewSizeMeasure.setViewSizeObserver(mainActivity.getWidgetWindowLayout(), new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad(Point size) {
                // assertTrue(false);
                int widgetMatrix;
                // 2 * 2일 경우
                widgetMatrix = 2;

                // (위젯 높이 * 2) + outer margin(위쪽) + (outer margin * 2(중앙) * 1) + inner margin(아래쪽)
                Resources resources = mainActivity.getResources();
                float expectedHeight = resources.getDimension(R.dimen.widget_height) * 2
                        + resources.getDimension(R.dimen.margin_outer)
                        + resources.getDimension(R.dimen.margin_outer) * (widgetMatrix - 1)
                        + resources.getDimension(R.dimen.margin_inner);
//        assertThat(mainActivity.getWidgetWindowLayout().getHeight(), // mainActivity.getWidgetWindowLayout().getMeasuredHeight()
//                is((int)DipToPixel.getPixel(mainActivity,
//                        mainActivity.getResources().getDimension(R.dimen.main_widget_window_layout_height))));
                assertThat(mainActivity.getWidgetWindowLayout().getHeight(), is((int)expectedHeight));

                // 2 * 1일 경우는 추후 테스트
                widgetMatrix = 1;
                // (위젯 높이 * 2) + outer margin(위쪽) + (inner margin * 2(중앙) * 0) + inner margin(아래쪽)
            }
        });
    }
    */

    /*
    @Test
    @Config(qualifiers="land")
    public void checkWidgetWindowLayoutHeightOnLandscape() throws Exception {
        // Device height - buttonLayout height - (outerPadding - innerPadding)를 확인하면 됨
        // 위젯 윈도우뷰의 아래쪽은 innerPadding 만큼만 주기 때문에 (outerPadding - innerPadding)만큼의 공간을 따로 주어야 함
        Resources resources = mainActivity.getResources();
        int expectedHeight = MNDeviceSizeChecker.getDeviceHeight(mainActivity)
                - mainActivity.getButtonLayout().getHeight()
                - (int)(resources.getDimension(R.dimen.padding_outer) - resources.getDimension(R.dimen.padding_inner));
//        assertThat(mainActivity.getWidgetWindowLayout().getHeight(), is(expectedHeight));
    }
    */

    /**
     * Alarm
     */
    /*
    @Test
    @Config(qualifiers="land")
    public void checkAlarmListViewHeightOnPortrait() throws Exception {
        // 1. Portrait
        // 디바이스 높이 - 위젯 윈도우 높이

        MNViewSizeMeasure.setViewSizeObserver(mainActivity.getAlarmListView(), new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad(Point size) {
                int expectedHeight = MNDeviceSizeChecker.getDeviceHeight(mainActivity)
                        - mainActivity.getWidgetWindowLayout().getHeight();
                assertThat(mainActivity.getAlarmListView().getHeight(), is(expectedHeight));
            }
        });
    }

    @Test
    @Config(qualifiers="land")
    public void checkAlarmListViewHeightOnLandscape() throws Exception {
        // 2. Landscape
        // 일단 높이보다도 안보이는 것이 중요할듯.
    }
    */
}
