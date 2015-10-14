package com.yooiistudios.morningkit.main;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;

import com.yooiistudios.morningkit.common.size.MNDeviceSizeInfo;
import com.yooiistudios.morningkit.main.layout.MNMainLayoutSetter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim on 2013. 11. 12..
 *
 * MNMainWidgetWindowTest
 */
@RunWith(AndroidJUnit4.class)
public class MNMainWidgetWindowTest extends ActivityInstrumentationTestCase2<MNMainActivity> {
    MNMainActivity mainActivity;

    public MNMainWidgetWindowTest() {
        super(MNMainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
    }

    /**
     * Widget Window
     */
    @Test
    public void checkWidgetWindowLayoutHeightOnPortrait() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        float expectedHeight = MNMainLayoutSetter.getPanelWindowLayoutHeight(mainActivity,
                Configuration.ORIENTATION_PORTRAIT);

        int deviceWidth = MNDeviceSizeInfo.getDeviceWidth(mainActivity);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getPanelWindowLayout().measure(widthMeasureSpec, heightMeasureSpec);

        Log.i("MNMainWidgetWindowTest", "port/height: " + expectedHeight);
        assertThat(mainActivity.getPanelWindowLayout().getMeasuredHeight(), is((int) expectedHeight));
    }

    @Test
    public void checkWidgetWindowLayoutHeightOnLandscape() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);

        float expectedHeight = MNMainLayoutSetter.getPanelWindowLayoutHeight(mainActivity, Configuration.ORIENTATION_LANDSCAPE);

        // Device height - buttonLayout height - (outerPadding - innerPadding)를 확인하면 됨
        // 위젯 윈도우뷰의 아래쪽은 innerPadding 만큼만 주기 때문에 (outerPadding - innerPadding)만큼의 공간을 따로 주어야 함
        assertThat(mainActivity.getPanelWindowLayout().getLayoutParams().height, is((int)expectedHeight));
    }
}

