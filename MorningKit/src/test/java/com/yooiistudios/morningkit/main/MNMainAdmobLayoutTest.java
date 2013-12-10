package com.yooiistudios.morningkit.main;

import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.ads.AdSize;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.common.dp.DipToPixel;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeChecker;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;
import com.yooiistudios.morningkit.main.layout.MNMainLayoutSetter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim on 2013. 11. 4..
 *
 * MNMainAdmobLayoutTest
 */
@RunWith(RobolectricGradleTestRunner.class)
//@RunWith(RobolectricTestRunner.class)
@Config (shadows = { AdWebViewShadow.class })
public class MNMainAdmobLayoutTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        // visible() 이 뷰를 띄울 수 있게 해주는 중요한 메서드
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
    }

    /**
     * ETC
     */
    @Test
    @Config(qualifiers="normal-480x800-port")
    public void testCalculatingAdmobLayoutWidthOnNormalPhone() {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);

        int deviceWidth = MNDeviceSizeChecker.getDeviceWidth(mainActivity);
        assertThat(deviceWidth, is(not(0)));

        // 광고가 있을 때만 테스트를 제대로 하면 됨
        // AdSize 계산
        AdSize adViewSize = AdSize.createAdSize(AdSize.BANNER, mainActivity);
        assertThat(adViewSize.getWidth(), is(not(0)));

        // buttonLayoutWidth 계산
        int buttonLayoutWidth = deviceWidth - (int)(mainActivity.getResources().getDimension(R.dimen.margin_main_button_layout) * 2);

        Log.i("MNMainAdmobLayoutTest", "buttonLayoutWidth: " + buttonLayoutWidth);
        Log.i("MNMainAdmobLayoutTest", "adViewWidth: " + adViewSize.getWidth());
        Log.i("MNMainAdmobLayoutTest", "DipToPixel.getPixel(mainActivity, adViewSize.getWidth()): " + DipToPixel.getPixel(mainActivity, adViewSize.getWidth()));

        RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mainActivity.getAdmobLayout().getLayoutParams();
        assertThat(admobLayoutParams, notNullValue());
        // 1. buttonLayout 너비가 AdView보다 크다면, admobLayout을 buttonLayout와 같게 맞추어 주고,
        if (buttonLayoutWidth > DipToPixel.getPixel(mainActivity, adViewSize.getWidth())) {
            RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) mainActivity.getButtonLayout().getLayoutParams();
            assertThat(buttonLayoutParams, notNullValue());
            if (admobLayoutParams != null && buttonLayoutParams != null) {
                assertThat(admobLayoutParams.leftMargin, is(buttonLayoutParams.leftMargin));
                assertThat(admobLayoutParams.rightMargin, is(buttonLayoutParams.rightMargin));
            }
        }
        // 2. 그렇지 않다면 무조건 MATCH_PARENT로 가야 한다 = 마진 0
        else {
            if (admobLayoutParams != null) {
                assertThat(admobLayoutParams.leftMargin, is(0));
                assertThat(admobLayoutParams.rightMargin, is(0));
            }
        }
    }

    /*
    @Test
    @Config(qualifiers="large-720x1280-port")
    public void testCalculatingAdmobLayoutWidthOnLargePhone() {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);

        int deviceWidth = MNDeviceSizeChecker.getDeviceWidth(mainActivity);
        assertThat(deviceWidth, is(not(0)));

        // 광고가 있을 때만 테스트를 제대로 하면 됨
        // AdSize 계산
        AdSize adViewSize = AdSize.createAdSize(AdSize.BANNER, mainActivity);
        assertThat(adViewSize.getWidth(), is(not(0)));

        // buttonLayoutWidth 계산
        int buttonLayoutWidth = deviceWidth - (int)(mainActivity.getResources().getDimension(R.dimen.margin_main_button_layout) * 2);

        Log.i("MNMainAdmobLayoutTest", "buttonLayoutWidth: " + buttonLayoutWidth);
        Log.i("MNMainAdmobLayoutTest", "adViewWidth: " + adViewSize.getWidth());
        Log.i("MNMainAdmobLayoutTest", "DipToPixel.getPixel(mainActivity, adViewSize.getWidth()): " + DipToPixel.getPixel(mainActivity, adViewSize.getWidth()));

        RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mainActivity.getAdmobLayout().getLayoutParams();
        assertThat(admobLayoutParams, notNullValue());
        // 1. buttonLayout 너비가 AdView보다 크다면, admobLayout을 buttonLayout와 같게 맞추어 주고,
        if (buttonLayoutWidth > DipToPixel.getPixel(mainActivity, adViewSize.getWidth())) {
            RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) mainActivity.getButtonLayout().getLayoutParams();
            assertThat(buttonLayoutParams, notNullValue());
            if (admobLayoutParams != null && buttonLayoutParams != null) {
                assertThat(admobLayoutParams.leftMargin, is(buttonLayoutParams.leftMargin));
                assertThat(admobLayoutParams.rightMargin, is(buttonLayoutParams.rightMargin));
            }
        }
        // 2. 그렇지 않다면 무조건 MATCH_PARENT로 가야 한다 = 마진 0
        else {
            if (admobLayoutParams != null) {
                assertThat(admobLayoutParams.leftMargin, is(0));
                assertThat(admobLayoutParams.rightMargin, is(0));
            }
        }
    }
    */

//    @Test
//    @Config(qualifiers="port")
//    public void checkAdmobLayoutWidthOnPortrait() throws Exception {
//        MNViewSizeMeasure.setViewSizeObserver(mainActivity.getAdmobLayout(), new MNViewSizeMeasure.OnGlobalLayoutObserver() {
//            @Override
//            public void onLayoutLoad(Point size) {
//                // 1. 광고가 있을 때 기기의 너비가 광고 너비보다 넓다면, layout의 width를 버튼 뷰와 같이 맞추어 주어야 함
//                // -> SMART_BANNER를 사용해서 전체를 광고 레이아웃으로 만드는 것이 좋을 듯
//                AdSize adViewSize = AdSize.createAdSize(AdSize.SMART_BANNER, mainActivity);
//                int expectedWidth = adViewSize.getWidth();
//                assertThat(mainActivity.getAdmobLayout().getWidth(), is(expectedWidth));
//            }
//        });
//    }

    @Test
    @Config(qualifiers="port")
    public void checkAdmobLayoutHeightOnPortrait() throws Exception {

        // 1. 광고가 있을 때
        float expectedHeight = MNMainLayoutSetter.adjustAdmobLayoutParamsAtOrientation(mainActivity.getAdmobLayout(), Configuration.ORIENTATION_PORTRAIT);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getAdmobLayout().measure(widthMeasureSpec, heightMeasureSpec);

//        Log.i("MNMainAdmobLayoutTest", "port/height: " + expectedHeight);
        assertThat(mainActivity.getAdmobLayout().getMeasuredHeight(), is((int) expectedHeight));
//
        // 2. 광고가 없을 때는 높이가 0이어야 함

    }

    @Test
    @Config(qualifiers="land")
    public void checkAdmobLayoutHeightOnLandscape() throws Exception {
        // 가로는 무조건 이 레이아웃이 0이어야만 한다.

        float expectedHeight = MNMainLayoutSetter.adjustAdmobLayoutParamsAtOrientation(mainActivity.getAdmobLayout(), Configuration.ORIENTATION_LANDSCAPE);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getAdmobLayout().measure(widthMeasureSpec, heightMeasureSpec);

//        Log.i("MNMainAdmobLayoutTest", "land/height: " + expectedHeight);
        assertThat(mainActivity.getAdmobLayout().getMeasuredHeight(), is((int) expectedHeight));
    }
}
