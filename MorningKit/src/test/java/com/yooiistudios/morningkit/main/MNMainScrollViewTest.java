package com.yooiistudios.morningkit.main;

import android.content.res.Configuration;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 10.
 *
 * MNMainScrollViewTest
 *  메인 스크롤뷰의 높이와 컨텐츠 높이를 테스트
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = { AdWebViewShadow.class })
public class MNMainScrollViewTest {
    private static final String TAG = "MNMainScrollViewTest";
    MNMainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
        // 알람을 비우고 새로 리스트를 만든다
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
        MNAlarmListManager.removeAlarmList(mainActivity);
    }

    @Test
    public void testScrollViewShouldNotBeNull() throws Exception {
        assertThat(mainActivity.getScrollView(), notNullValue());
        assertThat(mainActivity.getScrollContentLayout(), notNullValue());
    }

    @Test
    @Config(qualifiers = "port")
    public void checkScrollViewHeightOnPortrait() throws Exception {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.onConfigurationChanged(newConfig);

        RelativeLayout.LayoutParams scrollViewParams = (RelativeLayout.LayoutParams) mainActivity.getScrollView().getLayoutParams();
        // bottomMargin은 0, 최하단까지 MATCH_PARENT
        // ABOVE 룰 취소
        assertThat(scrollViewParams.bottomMargin, is(0));
        int[] layoutRules = scrollViewParams.getRules();
        assertThat(layoutRules[RelativeLayout.ABOVE], is(0));
    }

    @Test
    @Config(qualifiers = "land")
    public void checkScrollViewHeightOnLandscape() throws Exception {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);

        // bottomMargin = outer 마진 - inner 마진
        int expectedBottomMargin = (int) (mainActivity.getResources().getDimension(R.dimen.margin_outer) - mainActivity.getResources().getDimension(R.dimen.margin_inner));

        RelativeLayout.LayoutParams scrollViewParams = (RelativeLayout.LayoutParams) mainActivity.getScrollView().getLayoutParams();
        assertThat(scrollViewParams, notNullValue());
        assertThat(scrollViewParams.bottomMargin, is(expectedBottomMargin));
        // ABOVE는 button layout에 적용되에 상단위 위치한다.
        int[] layoutRules = scrollViewParams.getRules();
        assertThat(layoutRules[RelativeLayout.ABOVE], is(R.id.main_button_layout));
    }
}
