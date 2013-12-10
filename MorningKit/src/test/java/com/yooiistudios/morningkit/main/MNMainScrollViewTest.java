package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.MNAlarmMaker;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
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
import static org.junit.Assert.assertTrue;

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

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();
    }

    @Test
    public void testScrollViewShouldNotBeNull() throws Exception {
        assertThat(mainActivity.getScrollView(), notNullValue());
        assertThat(mainActivity.getScrollContainerLayout(), notNullValue());
    }

    @Test
    @Config(qualifiers = "port")
    public void checkScrollViewHeightOnPortrait() throws Exception {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.onConfigurationChanged(newConfig);

        RelativeLayout.LayoutParams scrollViewParams = (RelativeLayout.LayoutParams) mainActivity.getScrollView().getLayoutParams();
        if (scrollViewParams != null) {
            // bottomMargin은 0, 최하단까지 MATCH_PARENT
            // ABOVE 룰 취소
            assertThat(scrollViewParams.bottomMargin, is(0));
            int[] layoutRules = scrollViewParams.getRules();
            assertThat(layoutRules[RelativeLayout.ABOVE], is(0));
        }
    }

    @Test
    @Config(qualifiers = "land")
    public void checkScrollViewHeightOnLandscape() throws Exception {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);

        if (mainActivity.getResources() != null) {
            // bottomMargin = outer 마진 - inner 마진
            int expectedBottomMargin = (int) (mainActivity.getResources().getDimension(R.dimen.margin_outer) - mainActivity.getResources().getDimension(R.dimen.margin_inner));

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

    @Test
    @Config(qualifiers = "port")
    public void testScrollContainerLayoutParamsOnPortrait() throws Exception {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);
    }

    @Test
    @Config(qualifiers = "land")
    public void testScrollContainerLayoutParamsOnLandscape() throws Exception {
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
        mainActivity.onConfigurationChanged(newConfig);
    }

    @Test
    @Config(qualifiers = "port")
    // 1. 알람이 적게 있을 때 = 이 전체 높이가 deviceHeight 보다 작다면, 이 높이는 deviceHeight로 할 것
    public void testScrollContainerLayoutHeightWithLessAlarms() throws Exception {
        // 체크할 scrollContainerLayout의 height는
        // 위젯윈도우 레이아웃 +
        // (알람아이템 높이 * (알람 갯수 + 1) +
        // (outer_margin - inner_margin) +
        // buttonLayout 높이 +
        // admobLayout 높이
        Context context = mainActivity.getBaseContext();
        assertThat(context, notNullValue());

        // 알람리스트뷰 컨텐츠 높이를 제외한 높이를 구하기
        float scrollContentHeightExceptAlarms = getScrollContentHeightExceptAlarms(context);
        assertThat((int)scrollContentHeightExceptAlarms, is(not(0)));

        // 알람리스트뷰 높이 구하기
        float alarmListViewContentHeight = getAlarmListViewHeight(context);
        assertThat((int)alarmListViewContentHeight, is(not(0)));

        // 확실히 컨텐츠의 합이 디바이스 높이보다 낮다는 것을 확인
        float scrollContentHeight = scrollContentHeightExceptAlarms + alarmListViewContentHeight;
        assertTrue(scrollContentHeight <= MNDeviceSizeChecker.getDeviceHeight(context));

        // 스크롤뷰컨테이너 레이아웃의 높이가 deviceHeight 인지 확인
        assertThat(mainActivity.getScrollContainerLayout().getHeight(), is(MNDeviceSizeChecker.getDeviceHeight(context)));
    }

    @Test
    @Config(qualifiers = "port")
    // 2. 알람이 deviceHeight보다 넘칠 많큼 많을 떄 = 이 전체 높이가 deviceHeight 보다 크다면, 이 높이를 containerLayout의 높이로 설정
    public void testScrollContainerLayoutHeightWithMoreAlarms() throws Exception {
        // 체크할 scrollContainerLayout의 height는
        // 위젯윈도우 레이아웃 +
        // (알람아이템 높이 * (알람 갯수 + 1) +
        // (outer_margin - inner_margin) +
        // buttonLayout 높이 +
        // admobLayout 높이
        Context context = mainActivity.getBaseContext();
        assertThat(context, notNullValue());

        // 알람리스트뷰 컨텐츠 높이를 제외한 높이를 구하기
        float scrollContentHeightExceptAlarms = getScrollContentHeightExceptAlarms(context);
        assertThat((int)scrollContentHeightExceptAlarms, is(not(0)));

        // 알람 추가
        for (int i = 0; i < 5; i++) {
            MNAlarmListManager.addAlarmToAlarmList(MNAlarmMaker.makeAlarm(context), context);
        }
        float alarmListViewContentHeight = getAlarmListViewHeight(context);
        assertThat((int)alarmListViewContentHeight, is(not(0)));

        float scrollContentHeight = scrollContentHeightExceptAlarms + alarmListViewContentHeight;
        assertTrue(scrollContentHeight > MNDeviceSizeChecker.getDeviceHeight(context));

        // 스크롤뷰컨테이너 레이아웃의 높이가 scrollContentHeight 인지 확인
        assertThat(mainActivity.getScrollContainerLayout().getHeight(), is((int)scrollContentHeight));
    }

    /**
     * Utility methods
     */
    float getScrollContentHeightExceptAlarms(Context context) {
        return MNMainLayoutSetter.adjustWidgetLayoutParamsAtOrientation(mainActivity.getWidgetWindowLayout(), Configuration.ORIENTATION_PORTRAIT)
                + context.getResources().getDimension(R.dimen.margin_outer) - context.getResources().getDimension(R.dimen.margin_inner)
                + MNMainLayoutSetter.adjustButtonLayoutParamsAtOrientation(mainActivity.getButtonLayout(), Configuration.ORIENTATION_PORTRAIT)
                + MNMainLayoutSetter.adjustAdmobLayoutParamsAtOrientation(mainActivity.getAdmobLayout(), Configuration.ORIENTATION_PORTRAIT);
    }

    float getAlarmListViewHeight(Context context) {
        return context.getResources().getDimension(R.dimen.alarm_item_outer_height) * (MNAlarmListManager.loadAlarmList(context).size() + 1);
    }
}
