package com.yooiistudios.morningkit.main;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.ads.AdSize;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeInfo;
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
@Config (shadows = { AdWebViewShadow.class }, emulateSdk = 18) // , reportSdk = 14)
public class MNMainAdmobLayoutTest {

    private static final String TAG = "MNMainAdmobLayoutTest";
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
//    @Config(qualifiers="normal-480x800-port")
//    @Config(qualifiers = "large-720x1280-port-xhdpi")
//    @Config(qualifiers = "port-xhdpi")
    @Config(qualifiers = "port")
    public void testAdmobLayoutWidthOnPortrait() {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.onConfigurationChanged(newConfig);

        int deviceWidth = MNDeviceSizeInfo.getDeviceWidth(mainActivity);
        assertThat(deviceWidth, is(not(0)));

        // 광고가 있을 때만 테스트를 제대로 하면 됨
        // AdSize 계산
        AdSize adViewSize = AdSize.createAdSize(AdSize.BANNER, mainActivity);
        assertThat(adViewSize.getWidth(), is(not(0)));

        // calculatedButtonLayoutWidth 계산
        int calculatedButtonLayoutWidth = deviceWidth - (int)(mainActivity.getResources().getDimension(R.dimen.margin_main_button_layout) * 2);

//        Log.i(TAG, "calculatedButtonLayoutWidth: " + calculatedButtonLayoutWidth);
//        Log.i(TAG, "adViewSize.getWidthInPixels(): " + adViewSize.getWidthInPixels(mainActivity));

        RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mainActivity.getAdmobLayout().getLayoutParams();
        assertThat(admobLayoutParams, notNullValue());
        // 1. buttonLayout 너비가 AdView보다 크다면, admobLayout을 buttonLayout와 같게 맞추어 주고,
        if (calculatedButtonLayoutWidth > adViewSize.getWidthInPixels(mainActivity)) {
//            Log.i(TAG, "buttonLayout's width is wider than Adview's width");
            RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) mainActivity.getButtonLayout().getLayoutParams();
            assertThat(buttonLayoutParams, notNullValue());
            if (admobLayoutParams != null && buttonLayoutParams != null) {
                assertThat(admobLayoutParams.leftMargin, is(buttonLayoutParams.leftMargin));
                assertThat(admobLayoutParams.rightMargin, is(buttonLayoutParams.rightMargin));
            }
        }
        // 2. 그렇지 않다면 무조건 MATCH_PARENT로 가야 한다 = 마진 0
        else {
//            Log.i(TAG, "buttonLayout's width is shorter than Adview's width");
            if (admobLayoutParams != null) {
                assertThat(admobLayoutParams.leftMargin, is(0));
                assertThat(admobLayoutParams.rightMargin, is(0));
            }
        }
    }

    @Test
    @Config(qualifiers="port")
    public void testAdmobLayoutHeightOnPortrait() throws Exception {

        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.onConfigurationChanged(newConfig);

        // 1. 광고가 있을 때
        float expectedHeight = MNMainLayoutSetter.getAdmobLayoutHeightOnPortrait(mainActivity);

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
    public void testAdmobLayoutHeightOnLandscape() throws Exception {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);

        // 가로는 무조건 이 레이아웃이 0이어야만 한다.
        assertThat(mainActivity.getAdmobLayout().getLayoutParams().height, is(0));
    }
}
