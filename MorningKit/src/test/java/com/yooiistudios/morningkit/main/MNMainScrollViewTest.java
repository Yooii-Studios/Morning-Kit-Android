package com.yooiistudios.morningkit.main;

import android.content.res.Configuration;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.yooiistudios.morningkit.R;
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
    MNMainActivity mainActivity;
    ScrollView mainScrollView;

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
        mainScrollView = mainActivity.getScrollView();
    }

    @Test
    public void testScrollViewShouldNotBeNull() throws Exception {
        assertThat(mainScrollView, notNullValue());
    }

    @Test
    @Config(qualifiers="port")
    public void checkScrollViewHeightOnPortrait() throws Exception {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.onConfigurationChanged(newConfig);

        if (mainScrollView.getResources() != null) {
            RelativeLayout.LayoutParams scrollViewParams = (RelativeLayout.LayoutParams) mainActivity.getScrollView().getLayoutParams();
            if (scrollViewParams != null) {
                // bottomMargin은 0, 최하단까지 MATCH_PARENT
                // ABOVE 룰 취소
                assertThat(scrollViewParams.bottomMargin, is(0));
                int[] layoutRules = scrollViewParams.getRules();
                assertThat(layoutRules[RelativeLayout.ABOVE], is(0));
            }
        }
    }

    @Test
    @Config(qualifiers="land")
    public void checkScrollViewHeightOnLandscape() throws Exception {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);

        if (mainScrollView.getResources() != null) {
            // bottomMargin = outer 마진 - inner 마진
            int expectedBottomMargin =
                    (int) (mainScrollView.getResources().getDimension(R.dimen.margin_outer)
                            - mainScrollView.getResources().getDimension(R.dimen.margin_inner));

            RelativeLayout.LayoutParams scrollViewParams = (RelativeLayout.LayoutParams) mainActivity.getScrollView().getLayoutParams();
            assertThat(scrollViewParams, notNullValue());
            if (scrollViewParams != null) {
                assertThat(scrollViewParams.bottomMargin, is(expectedBottomMargin));
                // ABOVE는 button layout에 적용되에 상단위 위치한다.
                int[] layoutRules = scrollViewParams.getRules();
                assertThat(layoutRules[RelativeLayout.ABOVE], is(R.id.main_button_layout));
            }
        }
    }
}
