package com.yooiistudios.morningkit.main;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.ads.AdSize;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeInfo;
import com.yooiistudios.morningkit.main.layout.MNMainLayoutSetter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim on 2013. 11. 4..
 *
 * MNMainAdmobLayoutTest
 */
@RunWith(AndroidJUnit4.class)
public class MNMainAdmobLayoutTest extends ActivityInstrumentationTestCase2<MNMainActivity> {
    MNMainActivity mainActivity;

    public MNMainAdmobLayoutTest() {
        super(MNMainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
    }

    /**
     * ETC
     */
    @Test
    public void testAdmobLayoutWidthOnPortrait() {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.onConfigurationChanged(newConfig);

        int deviceWidth = MNDeviceSizeInfo.getDeviceWidth(mainActivity);
        assertThat(deviceWidth, is(not(0)));

        // 광고가 있을 때만 테스트를 제대로 하면 됨
        // AdSize 계산
//        AdSize adViewSize = AdSize.createAdSize(AdSize.BANNER, mainActivity);
        assertThat(AdSize.BANNER.getWidthInPixels(mainActivity), is(not(0)));

        // calculatedButtonLayoutWidth 계산
        int calculatedButtonLayoutWidth = deviceWidth - (int)(mainActivity.getResources().getDimension(R.dimen.margin_main_button_layout) * 2);

//        Log.i(TAG, "calculatedButtonLayoutWidth: " + calculatedButtonLayoutWidth);
//        Log.i(TAG, "adViewSize.getWidthInPixels(): " + adViewSize.getWidthInPixels(mainActivity));

        RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mainActivity.getAdmobLayout().getLayoutParams();
        assertThat(admobLayoutParams, notNullValue());
        // 1. buttonLayout 너비가 AdView 보다 크다면, admobLayout 을 buttonLayout 와 같게 맞추어 주고,
        if (calculatedButtonLayoutWidth > AdSize.BANNER.getWidthInPixels(mainActivity)) {
            RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) mainActivity.getButtonLayout().getLayoutParams();
            assertThat(buttonLayoutParams, notNullValue());
            if (admobLayoutParams != null && buttonLayoutParams != null) {
                assertThat(admobLayoutParams.leftMargin, is(buttonLayoutParams.leftMargin));
                assertThat(admobLayoutParams.rightMargin, is(buttonLayoutParams.rightMargin));
            }
        }
        // 2. 그렇지 않다면 무조건 MATCH_PARENT 로 가야 한다 = 마진 0
        else {
            if (admobLayoutParams != null) {
                assertThat(admobLayoutParams.leftMargin, is(0));
                assertThat(admobLayoutParams.rightMargin, is(0));
            }
        }
    }

    @Test
    public void testAdmobLayoutHeightOnPortrait() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.onConfigurationChanged(newConfig);

        // 1. 광고가 있을 때
        float expectedHeight = MNMainLayoutSetter.getAdmobLayoutHeightOnPortrait(mainActivity);

        int deviceWidth = MNDeviceSizeInfo.getDeviceWidth(mainActivity);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getAdmobLayout().measure(widthMeasureSpec, heightMeasureSpec);

//        Log.i("MNMainAdmobLayoutTest", "port/height: " + expectedHeight);
        assertThat(mainActivity.getAdmobLayout().getMeasuredHeight(), is((int) expectedHeight));
//
        // 2. 광고가 없을 때는 높이가 0이어야 함
    }

    @Test
    public void testAdmobLayoutHeightOnLandscape() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);

        // 가로는 무조건 이 레이아웃이 0이어야만 한다.
        assertThat(mainActivity.getAdmobLayout().getLayoutParams().height, is(0));
    }
}
