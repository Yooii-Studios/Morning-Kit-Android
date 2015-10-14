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
 * Created by StevenKim on 2013. 11. 4..
 *
 * MNMainButtonLayoutTest
 */
@RunWith(AndroidJUnit4.class)
public class MNMainButtonLayoutTest extends ActivityInstrumentationTestCase2<MNMainActivity> {
    MNMainActivity mainActivity;

    public MNMainButtonLayoutTest() {
        super(MNMainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
    }


    /**
     * Button
     * @throws Exception
     */
    @Test
    public void checkButtonLayoutHeightOnPortrait() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        float expectedHeight = MNMainLayoutSetter.getButtonLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT);

        int deviceWidth = MNDeviceSizeInfo.getDeviceWidth(mainActivity);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getButtonLayout().measure(widthMeasureSpec, heightMeasureSpec);

        Log.i("MNMainButtonLayoutTest", "port/height: " + expectedHeight);
        assertThat(mainActivity.getButtonLayout().getMeasuredHeight(), is((int) expectedHeight));
    }

    @Test
    public void checkButtonLayoutHeightOnLandscape() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        // 1. 광고가 있는 경우: SMART_BANNER 의 높이 + inner margin * 2가 되어야 한다.
        float expectedHeight = MNMainLayoutSetter.getButtonLayoutHeight(mainActivity, Configuration.ORIENTATION_LANDSCAPE);

        int deviceWidth = MNDeviceSizeInfo.getDeviceWidth(mainActivity);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getButtonLayout().measure(widthMeasureSpec, heightMeasureSpec);

        Log.i("MNMainButtonLayoutTest", "land/height: " + expectedHeight);
        assertThat(mainActivity.getButtonLayout().getMeasuredHeight(), is((int) expectedHeight));

        // 2. 광고가 없는 경우: main_button_layout_height
    }
}
