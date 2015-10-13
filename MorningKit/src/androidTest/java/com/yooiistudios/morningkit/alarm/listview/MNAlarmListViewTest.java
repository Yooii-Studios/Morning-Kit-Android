package com.yooiistudios.morningkit.alarm.listview;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.yooiistudios.morningkit.alarm.listview.item.MNAlarmItemClickListener;
import com.yooiistudios.morningkit.alarm.listview.item.MNAlarmItemScrollView;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.main.MNMainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim on 2013. 11. 11..

 * MNAlarmListViewTest
 */
@RunWith(AndroidJUnit4.class)
public class MNAlarmListViewTest extends ActivityInstrumentationTestCase2<MNMainActivity> {
    MNMainActivity mainActivity;
    MNAlarmListAdapter alarmListAdaptor;

    public MNAlarmListViewTest() {
        super(MNMainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
        alarmListAdaptor = new MNAlarmListAdapter(mainActivity.getBaseContext(),
                MNAlarmItemClickListener.newInstance(mainActivity.getAlarmListView()));
    }

    @Test
    public void mainAlarmListAdaptorTest() {
        // MNAlarmListAdaptor 의 초기화를 위해서 Context 가 null 이 아닌지 체크
        assertThat(mainActivity.getBaseContext(), notNullValue());
        // null 체크
        assertThat(alarmListAdaptor, notNullValue());

        // alarmListAdaptor 의 뷰 카운트는 알람 갯수 + 1(알람 추가 뷰)
        int alarmCount = MNAlarmListManager.getAlarmList(mainActivity.getBaseContext()).size();
        assertThat(alarmListAdaptor.getCount(), is(alarmCount + 1));
    }

    @Test
    public void alarmItemsTest() {
        assertThat(mainActivity.getAlarmListView().getCount(), is(alarmListAdaptor.getCount()));

        for (int i = 0; i < mainActivity.getAlarmListView().getCount() - 1; i++) {
            MNAlarmItemScrollView alarmItemScrollView =
                    (MNAlarmItemScrollView) mainActivity.getAlarmListView().getChildAt(i);
            assertThat(alarmItemScrollView, notNullValue());
            assertThat(alarmItemScrollView, isA(MNAlarmItemScrollView.class));

            if (alarmItemScrollView != null) {
                // LayoutItems 확인
                assertThat(alarmItemScrollView.getLayoutItems(), notNullValue());
                assertThat(alarmItemScrollView.getLayoutItems().size(), is(3));
                // 알람아이템 스크롤뷰의 태그는 position
                assertThat((Integer) alarmItemScrollView.getTag(), is(i));
                // 알람뷰확인, 태그는 MNAlarm
                assertThat(alarmItemScrollView.getAlarmView(), notNullValue());
                assertThat(alarmItemScrollView.getAlarmView().getTag(), notNullValue());
                // instanceOf로 확인하는 것이 정석인 듯 하나 is()로도 확인이 가능한듯
                assertThat(alarmItemScrollView.getAlarmView().getTag(), is(instanceOf(MNAlarm.class)));
            }
        }

        // 알람 추가 뷰의 태그를 확인한다
        ArrayList<MNAlarm> alarmList = MNAlarmListManager.getAlarmList(mainActivity.getBaseContext());
        View alarmCreateAlarmItemView = mainActivity.getAlarmListView().getChildAt(alarmList.size());
        if (alarmCreateAlarmItemView != null) {
            assertThat((Integer)alarmCreateAlarmItemView.getTag(), is(-1));
        }
    }

    @Test
    public void mainAlarmListViewTest() {
        MNAlarmItemClickListener clickListener =
                mainActivity.getAlarmListView().getAlarmItemClickListener();
        assertThat(clickListener, notNullValue());
        assertThat(clickListener.getAlarmListView(), notNullValue());
    }
}
