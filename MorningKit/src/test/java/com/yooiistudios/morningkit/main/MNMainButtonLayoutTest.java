package com.yooiistudios.morningkit.main;

import android.content.res.Resources;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.ads.AdSize;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by StevenKim on 2013. 11. 4..
 *
 * MNMainButtonLayoutTest
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config (shadows = { AdWebViewShadow.class })
public class MNMainButtonLayoutTest {

    MNMainActivity mainActivity;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        // visible() 이 뷰를 띄울 수 있게 해주는 중요한 메서드
        // 로그 관련이라는데 아직 잘 모르겠다.
//        System.setProperty("robolectric.logging", "stdout");
//        ShadowLog.stream = System.out;

//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().start().resume().visible().get();
//        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().postResume().visible().get();
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
    }


    /**
     * Button
     * @throws Exception
     */
    @Test
    @Config(qualifiers="port")
    public void checkButtonLayoutHeightOnPortrait() throws Exception {

        float expectedHeight = MNMainLayoutSetter.adjustButtonLayoutParamsAtOrientation(mainActivity.getButtonLayout(), mainActivity.getResources().getConfiguration().orientation);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getButtonLayout().measure(widthMeasureSpec, heightMeasureSpec);

        assertThat(mainActivity.getButtonLayout().getMeasuredHeight(), is((int) expectedHeight));
    }

    @Test
    @Config(qualifiers="large-1280x720-land")
    public void checkButtonLayoutHeightOnLandscape() throws Exception {

        // 1. 광고가 있는 경우: SMART_BANNER의 높이 + inner margin * 2가 되어야 한다.
        float expectedHeight = MNMainLayoutSetter.adjustButtonLayoutParamsAtOrientation(mainActivity.getButtonLayout(), mainActivity.getResources().getConfiguration().orientation);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)expectedHeight, View.MeasureSpec.EXACTLY);
        mainActivity.getButtonLayout().measure(widthMeasureSpec, heightMeasureSpec);

        assertThat(mainActivity.getButtonLayout().getMeasuredHeight(), is((int) expectedHeight));

        // 2. 광고가 없는 경우: main_button_layout_height
    }

    /**
     * Adview
     */

    /*
    @Test
    @Config(qualifiers="port")
    // Portrait 모드에서는 ButtonLayout 안에 속해 있어야함
    // 테스트 불가. onConfigurationChanged가 제대로 실행이 되지 않나 봄
    public void checkAdmobViewNotInButtonLayoutOnPortrait() throws Exception {
        assertThat(mainActivity.getButtonLayout().findViewById(R.id.adView), nullValue());
    }
    */
}
