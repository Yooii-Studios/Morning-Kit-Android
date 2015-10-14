package com.yooiistudios.morningkit.main;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 10.
 *
 * MNMainScrollViewTest
 *  메인 스크롤뷰의 높이와 컨텐츠 높이를 테스트
 */
@RunWith(AndroidJUnit4.class)
public class MNMainScrollViewTest extends ActivityInstrumentationTestCase2<MNMainActivity> {
    MNMainActivity mainActivity;

    public MNMainScrollViewTest() {
        super(MNMainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        // 알람을 비우고 새로 리스트를 만든다
        mainActivity = getActivity();
        MNAlarmListManager.removeAlarmList(mainActivity);
    }

    @Test
    public void testScrollViewShouldNotBeNull() throws Exception {
        assertThat(mainActivity.getScrollView(), notNullValue());
    }

    @Test
    public void checkScrollViewHeightOnPortrait() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        final Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.onConfigurationChanged(newConfig);

                RelativeLayout.LayoutParams scrollViewParams =
                        (RelativeLayout.LayoutParams) mainActivity.getScrollView().getLayoutParams();

                // bottomMargin 은 0, 최하단까지 MATCH_PARENT
                // ABOVE 룰 취소
                assertThat(scrollViewParams.bottomMargin, is(0));
                int[] layoutRules = scrollViewParams.getRules();
                assertThat(layoutRules[RelativeLayout.ABOVE], is(0));
            }
        });
    }

    @Test
    public void checkScrollViewHeightOnLandscape() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);

        // bottomMargin = outer 마진 - inner 마진
        int expectedBottomMargin = (int) (mainActivity.getResources().getDimension(R.dimen.margin_outer) - mainActivity.getResources().getDimension(R.dimen.margin_inner));

        RelativeLayout.LayoutParams scrollViewParams = (RelativeLayout.LayoutParams) mainActivity.getScrollView().getLayoutParams();
        assertThat(scrollViewParams, notNullValue());
        assertThat(scrollViewParams.bottomMargin, is(expectedBottomMargin));
        // ABOVE 는 button layout 에 적용되에 상단위 위치한다.
        int[] layoutRules = scrollViewParams.getRules();
        assertThat(layoutRules[RelativeLayout.ABOVE], is(R.id.main_button_layout));
    }
}
