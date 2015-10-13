package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
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
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 11.
 *
 * MNMainAlarmListViewTest
 *  회전시 레이아웃이 원하는 대로 설정되는지 여부를 테스트
 */
@RunWith(AndroidJUnit4.class)
public class MNMainAlarmListViewTest extends ActivityInstrumentationTestCase2<MNMainActivity> {
    private static final String TAG = "MNMainAlarmListViewTest";
    MNMainActivity mainActivity;

    public MNMainAlarmListViewTest() {
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
    // 1. 알람이 적게 있을 때 = 이 전체 높이가 deviceHeight 보다 작다면, 이 높이는 deviceHeight로 할 것
    public void testScrollContainerLayoutHeightWithLessAlarms() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        // 방향 지정
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.onConfigurationChanged(newConfig);

        // 체크할 scrollContainerLayout 의 height 는
        // 위젯윈도우 레이아웃 +
        // (알람아이템 높이 * (알람 갯수 + 1) +
        // (outer_margin - inner_margin) +
        // buttonLayout 높이 +
        // admobLayout 높이
        Context context = mainActivity.getBaseContext();

        // 알람리스트뷰 컨텐츠 높이를 제외한 높이를 구하기
        float scrollContentHeightExceptAlarms =
                MNMainLayoutSetter.getScrollContentHeightExceptAlarmsOnPortrait(mainActivity);
        assertThat((int) scrollContentHeightExceptAlarms, is(not(0)));

        // 알람리스트뷰 높이 구하기
        assertThat(MNAlarmListManager.getAlarmList(context).size(), is(not(0)));
        float alarmListViewContentHeight = MNMainLayoutSetter.getAlarmListViewHeightOnPortrait(context);
        assertThat((int) alarmListViewContentHeight, is(not(0)));

        // 컨텐츠의 합이 디바이스 높이보다 낮다는 것을 확인
        float scrollContentHeight = scrollContentHeightExceptAlarms + alarmListViewContentHeight;
        assertTrue((scrollContentHeight <= MNDeviceSizeInfo.getDeviceHeight(context)));

        // 스크롤뷰컨테이너 레이아웃의 높이가 deviceHeight 인지 확인
        assertThat(mainActivity.getAlarmListView().getLayoutParams(), notNullValue());
        assertThat(mainActivity.getAlarmListView().getLayoutParams().height, is((int)alarmListViewContentHeight));
    }

    @Test
    // 2. 알람이 deviceHeight 보다 넘치진 않지만 buttonLayout 에 가릴 정도일 때
    // = 이 전체 높이가 deviceHeight 보다 크다면, 이 높이를 containerLayout 의 높이로 설정
    public void testScrollContainerLayoutHeightWithSomeAlarms() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        Context context = mainActivity.getBaseContext();

        // 알람 추가 및 갱신
        int sizeOfAlarmList = MNAlarmListManager.getAlarmList(context).size();
        assertThat(sizeOfAlarmList, is(not(0)));
        for (int i = 0; i < 3; i++) {
            MNAlarmListManager.addAlarmToAlarmList(MNAlarmMaker.makeAlarm(context), context);
        }
        assertThat(MNAlarmListManager.getAlarmList(context).size(), is(sizeOfAlarmList + 3));
        MNAlarmListManager.saveAlarmList(mainActivity);

        // 방향 지정(리프레시)
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.getAlarmListView().refreshListView();
        mainActivity.onConfigurationChanged(newConfig);

        // 체크할 scrollContainerLayout 의 height 는
        // 위젯윈도우 레이아웃 +
        // (알람아이템 높이 * (알람 갯수 + 1) +
        // (outer_margin - inner_margin) +
        // buttonLayout 높이 +
        // admobLayout 높이

        // 알람리스트뷰 컨텐츠 높이를 제외한 높이를 구하기
        float scrollContentHeightExceptAlarms =
                MNMainLayoutSetter.getScrollContentHeightExceptAlarmsOnPortrait(mainActivity);
        assertThat((int)scrollContentHeightExceptAlarms, is(not(0)));

        float alarmListViewContentHeight = MNMainLayoutSetter.getAlarmListViewHeightOnPortrait(context);
        assertThat((int) alarmListViewContentHeight, is(not(0)));

        // 컨텐츠의 합이 디바이스 높이보다 낮다는 것을 확인
        float scrollContentHeight = scrollContentHeightExceptAlarms + alarmListViewContentHeight;
        assertTrue(scrollContentHeight <= MNDeviceSizeInfo.getDeviceHeight(context));

        float bottomLayoutHeight = MNMainLayoutSetter.getBottomLayoutHeight(mainActivity,
                Configuration.ORIENTATION_PORTRAIT);

        // 디바이스 높이만큼은 아니지만 버튼 레이아웃을 가릴 정도인지 확인
        Log.i(TAG, "scrollContentHeightExceptAlarms: " + scrollContentHeightExceptAlarms);
        Log.i(TAG, "alarmListViewContentHeight: " + alarmListViewContentHeight);
        Log.i(TAG, "MNDeviceSizeInfo.getDeviceHeight(context): " + MNDeviceSizeInfo.getDeviceHeight(context));
        Log.i(TAG, "bottomLayoutHeight: " + bottomLayoutHeight);
        assertTrue(scrollContentHeightExceptAlarms + alarmListViewContentHeight > MNDeviceSizeInfo.getDeviceHeight(context) - bottomLayoutHeight);

        // 스크롤뷰컨테이너 레이아웃의 높이가 scrollContentHeight 인지 확인
        assertThat(mainActivity.getAlarmListView().getLayoutParams(), notNullValue());
        assertThat(mainActivity.getAlarmListView().getLayoutParams().height, is((int)(alarmListViewContentHeight + bottomLayoutHeight)));
    }

    @Test
    // 3. 알람이 deviceHeight 보다 넘칠 많큼 많을 떄 = 이 전체 높이가 deviceHeight 보다 크다면,
    // 이 높이를 containerLayout 의 높이로 설정
    public void testScrollContainerLayoutHeightWithMoreAlarms() throws Exception {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync(); // 방향 바뀌기를 기다리기

        Context context = mainActivity.getBaseContext();

        // 알람 추가 및 갱신
        int sizeOfAlarmList = MNAlarmListManager.getAlarmList(context).size();
        assertThat(sizeOfAlarmList, is(not(0)));
        for (int i = 0; i < 8; i++) {
            MNAlarmListManager.addAlarmToAlarmList(MNAlarmMaker.makeAlarm(context), context);
        }
        assertThat(MNAlarmListManager.getAlarmList(context).size(), is(sizeOfAlarmList + 8));
        MNAlarmListManager.saveAlarmList(mainActivity);

        // 방향 지정(리프레시)
        Configuration newConfig = new Configuration();
        newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
        mainActivity.getAlarmListView().refreshListView();
        mainActivity.onConfigurationChanged(newConfig);

        // 체크할 scrollContainerLayout 의 height 는
        // 위젯윈도우 레이아웃 +
        // (알람아이템 높이 * (알람 갯수 + 1) +
        // (outer_margin - inner_margin) +
        // buttonLayout 높이 +
        // admobLayout 높이

        // 알람리스트뷰 컨텐츠 높이를 제외한 높이를 구하기
        float scrollContentHeightExceptAlarms =
                MNMainLayoutSetter.getScrollContentHeightExceptAlarmsOnPortrait(mainActivity);
        assertThat((int)scrollContentHeightExceptAlarms, is(not(0)));

        float alarmListViewContentHeight = MNMainLayoutSetter.getAlarmListViewHeightOnPortrait(context);
        assertThat((int) alarmListViewContentHeight, is(not(0)));

        float scrollContentHeight = scrollContentHeightExceptAlarms + alarmListViewContentHeight;
        assertTrue(scrollContentHeight > MNDeviceSizeInfo.getDeviceHeight(context));

        float bottomLayoutHeight = MNMainLayoutSetter.getBottomLayoutHeight(mainActivity,
                Configuration.ORIENTATION_PORTRAIT);

        // 스크롤뷰컨테이너 레이아웃의 높이가 scrollContentHeight 인지 확인
        assertThat(mainActivity.getAlarmListView().getLayoutParams(), notNullValue());
        int expectedAlarmListViewHeight = (int) (alarmListViewContentHeight + bottomLayoutHeight);
        assertThat(mainActivity.getAlarmListView().getLayoutParams().height, is(expectedAlarmListViewHeight));
    }
}
