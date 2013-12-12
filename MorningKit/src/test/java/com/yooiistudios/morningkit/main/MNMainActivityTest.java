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
import org.robolectric.shadows.ShadowLog;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * Created by StevenKim on 2013. 10. 31..
 *
 * MNMainActivityTest
 */
// guide for using Robolectric and JUnit
// Class name must be ended with xxxTest in test class
@RunWith(RobolectricGradleTestRunner.class)
@Config (shadows = { AdWebViewShadow.class })
public class MNMainActivityTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        // visible() 이 뷰를 띄울 수 있게 해주는 중요한 메서드
//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().start().resume().visible().get();
//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().postResume().get();
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
    }

    @Test
    public void shouldViewsAndLayoutsBeNotNull() throws Exception {
        assertThat(mainActivity, notNullValue());
        assertThat(mainActivity, instanceOf(MNMainActivity.class));

        assertThat(mainActivity.getAlarmListView(), notNullValue());
        assertThat(mainActivity.getAlarmListView(), instanceOf(MNMainAlarmListView.class));

        assertThat(mainActivity.getWidgetWindowLayout(), notNullValue());
        assertThat(mainActivity.getWidgetWindowLayout(), instanceOf(MNWidgetWindowLayout.class));

        assertThat(mainActivity.getButtonLayout(), notNullValue());
        assertThat(mainActivity.getButtonLayout(), instanceOf(RelativeLayout.class));

        assertThat(mainActivity.getAdmobLayout(), notNullValue());
        assertThat(mainActivity.getAdmobLayout(), instanceOf(RelativeLayout.class));

        assertThat(mainActivity.getAdView(), notNullValue());
        assertThat(mainActivity.getAdView(), instanceOf(AdView.class));
    }

    /**
     * Alarm
     */

    @Test
    @Config(qualifiers="land")
    public void checkAlarmListViewHeightOnPortrait() throws Exception {
        // 1. Portrait
        // 디바이스 높이 - 위젯 윈도우 높이
//        int wid
//        mainActivity.getWidgetWindowLayout().measure();
//        int expectedHeight = MNDeviceSizeInfo.getDeviceHeight(mainActivity)
//                - mainActivity.getWidgetWindowLayout().getHeight();
//        assertThat(mainActivity.getAlarmListView().getHeight(), is(expectedHeight));
    }

    /*
    @Test
    @Config(qualifiers="land")
    public void checkAlarmListViewHeightOnLandscape() throws Exception {
        // 2. Landscape
        // 일단 높이보다도 안보이는 것이 중요할듯.
    }
    */
}
