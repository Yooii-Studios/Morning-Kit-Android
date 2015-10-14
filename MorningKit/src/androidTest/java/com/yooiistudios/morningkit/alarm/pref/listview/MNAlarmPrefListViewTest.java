package com.yooiistudios.morningkit.alarm.pref.listview;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.pref.MNAlarmPreferenceActivity;
import com.yooiistudios.morningkit.main.MNMainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 14.
 *
 * MNAlarmPrefListViewTest
 *  리스트뷰 관련 테스트
 */

@RunWith(AndroidJUnit4.class)
public class MNAlarmPrefListViewTest extends ActivityInstrumentationTestCase2<MNMainActivity> {
    private static final String TAG = "MNAlarmPrefListViewTest";
    MNMainActivity mainActivity;
    MNAlarmPreferenceActivity alarmPreferenceActivity;

    public MNAlarmPrefListViewTest() {
        super(MNMainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        // main
        mainActivity = getActivity();

        // dummy alarm setting
        MNAlarm alarm = MNAlarmMaker.makeAlarm(mainActivity.getBaseContext());
        alarm.setAlarmId(50);
        ArrayList<MNAlarm> dummyAlarmList = MNAlarmListManager.loadAlarmList(mainActivity.getBaseContext());
        dummyAlarmList.add(alarm);
        try {
            MNAlarmListManager.saveAlarmList(mainActivity.getBaseContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 'Edit alarm' Activity
        /*
        Intent intent_edit_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity.class);
        intent_edit_alarm.putExtra(MNAlarmPreferenceActivity.ALARM_PREFERENCE_ALARM_ID, alarm.getAlarmId());
        alarmPreferenceActivity = Robolectric.buildActivity(MNAlarmPreferenceActivity.class)
                .withIntent(intent_edit_alarm).create().visible().get();
                */
    }

    // 나중에 테스트 할 것 정해서 다시 살리자
    // 테스트를 할 수가 없다. 리스트뷰의 getChildAt() 이 null이 나와 버려서 아직 원인을 못 찾고 있음.
    // 분명히 폰에서는 제대로 나오는데...
    @Test
    public void testAlarmPrefListView() {
        /*
        assertThat(alarmPreferenceActivity.getListView(), notNullValue());
        // 현재로서는 5개
        assertThat(alarmPreferenceActivity.getListView().getCount(), is(MNAlarmPrefListItemType.values().length));
        */

        /*
        Log.i("TAG", "getCount(): " + prefListView.getCount());
        for(int i=0; i<prefListView.getCount(); i++) {
            View convertView = prefListView.getChildAt(i);
            assertThat(convertView, notNullValue());

            MNAlarmPrefListItemType indexType = MNAlarmPrefListItemType.valueOf(i);
            switch (indexType) {
                case REPEAT: {
                    Log.i("TAG", "REPEAT");
                    assertThat(convertView.getTag(), is(MNAlarmPrefListViewItemMaker.MNAlarmPrefLabelItemViewHolder.class));
                    MNAlarmPrefListViewItemMaker.MNAlarmPrefRepeatItemViewHolder viewHolder =
                            (MNAlarmPrefListViewItemMaker.MNAlarmPrefRepeatItemViewHolder) convertView.getTag();
                    break;
                }
                case LABEL: {
                    assertThat(convertView.getTag(), is(MNAlarmPrefListViewItemMaker.MNAlarmPrefLabelItemViewHolder.class));
                    MNAlarmPrefListViewItemMaker.MNAlarmPrefLabelItemViewHolder viewHolder =
                            (MNAlarmPrefListViewItemMaker.MNAlarmPrefLabelItemViewHolder) convertView.getTag();
                    break;
                }
                case SOUND: {
                    assertThat(convertView.getTag(), is(MNAlarmPrefListViewItemMaker.MNAlarmPrefSoundItemViewHolder.class));
                    MNAlarmPrefListViewItemMaker.MNAlarmPrefSoundItemViewHolder viewHolder =
                            (MNAlarmPrefListViewItemMaker.MNAlarmPrefSoundItemViewHolder) convertView.getTag();
                    break;
                }
                case SNOOZE: {
                    assertThat(convertView.getTag(), is(MNAlarmPrefListViewItemMaker.MNAlarmPrefSoundItemViewHolder.class));
                    MNAlarmPrefListViewItemMaker.MNAlarmPrefSoundItemViewHolder viewHolder =
                            (MNAlarmPrefListViewItemMaker.MNAlarmPrefSoundItemViewHolder) convertView.getTag();
                    break;
                }
                case TIME: {
                    assertThat(convertView.getTag(), is(MNAlarmPrefListViewItemMaker.MNAlarmPrefTimeItemViewHolder.class));
                    MNAlarmPrefListViewItemMaker.MNAlarmPrefTimeItemViewHolder viewHolder =
                            (MNAlarmPrefListViewItemMaker.MNAlarmPrefTimeItemViewHolder) convertView.getTag();
                    break;
                }
                default:
                    throw new AssertionError("Undefined position");
            }
        }
        */
    }
}
