package com.yooiistudios.morningkit.alarm.pref.listview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.View;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNDummyAlarmMaker;
import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.string.MNAlarmRepeatString;
import com.yooiistudios.morningkit.alarm.pref.MNAlarmPreferenceActivity;
import com.yooiistudios.morningkit.alarm.pref.listview.item.maker.MNAlarmPrefRepeatItemMaker;
import com.yooiistudios.morningkit.common.RobolectricGradleTestRunner;
import com.yooiistudios.morningkit.common.bus.MNAlarmPrefBusProvider;
import com.yooiistudios.morningkit.main.MNMainActivity;
import com.yooiistudios.morningkit.main.admob.AdWebViewShadow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 16.
 *
 * MNAlarmPrefRepeatItemMakerTest
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = { AdWebViewShadow.class }, emulateSdk = 18) //reportSdk = 10) // Gingerbread // , emulateSdk = 18
public class MNAlarmPrefRepeatItemMakerTest {
    private static final String TAG = "MNAlarmPrefRepeatItemMakerTest";
    private MNAlarmPreferenceActivity alarmPrefActivity_add;
    private MNAlarmPreferenceActivity alarmPrefActivity_edit;

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;

        // main
        MNMainActivity mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();

        // dummy alarm setting
        MNAlarm alarm = MNAlarmMaker.makeAlarm(mainActivity.getBaseContext());
        alarm.setAlarmId(50);
        ArrayList<MNAlarm> dummyAlarmList = MNAlarmListManager.loadAlarmList(mainActivity.getBaseContext());
        dummyAlarmList.add(alarm);
        MNAlarmListManager.saveAlarmList(mainActivity.getBaseContext());

        // 'Add alarm' Activity
        Intent intent_add_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity.class);
        intent_add_alarm.putExtra(MNAlarmPreferenceActivity.ALARM_PREFERENCE_ALARM_ID, -1);
        alarmPrefActivity_add = Robolectric.buildActivity(MNAlarmPreferenceActivity.class)
                .withIntent(intent_add_alarm).create().visible().get();

        // 'Edit alarm' Activity
        Intent intent_edit_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity.class);
        intent_edit_alarm.putExtra(MNAlarmPreferenceActivity.ALARM_PREFERENCE_ALARM_ID, alarm.getAlarmId());
        alarmPrefActivity_edit = Robolectric.buildActivity(MNAlarmPreferenceActivity.class)
                .withIntent(intent_edit_alarm).create().visible().get();
    }

    @Test
    public void testRepeatDetailString() throws Exception {
        // boolean의 ArrayList을 대입하면, 해당한는 반복 String을 반환

        // 반복 없음 -> Never
        MNAlarm nonRepeatAlarm = MNDummyAlarmMaker.makeNonRepeatAlarm(alarmPrefActivity_edit);
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(nonRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Never"));
        testEachRepeatItem(alarmPrefActivity_edit, nonRepeatAlarm);

        // 월, 목, 토 -> Mon Tue Sat 와 같은 식
        MNAlarm severalRepeatAlarm = MNDummyAlarmMaker.makeSeveralRepeatAlarm(alarmPrefActivity_edit);
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(severalRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Mon Thu Sat"));
        testEachRepeatItem(alarmPrefActivity_edit, severalRepeatAlarm);

        // 월화수목금 -> Every Weekday
        MNAlarm weekdaysRepeatAlarm = MNDummyAlarmMaker.makeWeekdaysRepeatAlarm(alarmPrefActivity_edit);
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(weekdaysRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Weekdays"));
        testEachRepeatItem(alarmPrefActivity_edit, weekdaysRepeatAlarm);

        // 토일 -> Every Weekends
        MNAlarm weekendsRepeatAlarm = MNDummyAlarmMaker.makeWeekendsRepeatAlarm(alarmPrefActivity_edit);
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(weekendsRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Weekends"));
        testEachRepeatItem(alarmPrefActivity_edit, weekendsRepeatAlarm);

        // 월~일 -> Everyday
        MNAlarm everdayRepeatAlarm = MNDummyAlarmMaker.makeEverydayRepeatAlarm(alarmPrefActivity_edit);
        assertThat(MNAlarmRepeatString.makeRepeatDetailString(everdayRepeatAlarm.getAlarmRepeatList(), alarmPrefActivity_edit), is("Everyday"));
        testEachRepeatItem(alarmPrefActivity_edit, everdayRepeatAlarm);
    }

    private void testEachRepeatItem(MNAlarmPreferenceActivity alarmPrefActivity, MNAlarm alarm) {
        // 각 반복 아이템을 만들었을 경우 title 텍스튜와 detail 텍스트가 각각 원하는 결과가 나오는지 확인
        // 특히 makeRepeatDetailString이 각각 이상적인 결과가 나오는지 확인, detailTextView에 적용이 되는지 확인
        View convertView = MNAlarmPrefRepeatItemMaker.makeRepeatItem(alarmPrefActivity, null, alarm);
        assertThat(convertView, notNullValue());

        MNAlarmPrefRepeatItemMaker.MNAlarmPrefRepeatItemViewHolder viewHolder = (MNAlarmPrefRepeatItemMaker.MNAlarmPrefRepeatItemViewHolder) convertView.getTag();
        assertThat(viewHolder, notNullValue());
        assertThat(viewHolder, is(MNAlarmPrefRepeatItemMaker.MNAlarmPrefRepeatItemViewHolder.class));
        assertThat(viewHolder.getTitleTextView().getText().toString(), is(alarmPrefActivity.getString(R.string.alarm_pref_repeat)));
        assertThat(viewHolder.getDetailTextView().getText().toString(), is(MNAlarmRepeatString.makeRepeatDetailString(alarm.getAlarmRepeatList(), alarmPrefActivity)));
    }

    @Test
    public void testRepeatDialog() throws Exception {

        Context context = alarmPrefActivity_edit;

        // 각 다이얼로그에서 월~금을 체크해서 확인하면 알람설정 액티비티의 알람 반복 ArrayList가 제대로 설정이 되어 있어야 함
        // 해당 ArrayList로 String을 만들었을 경우 이상적인 결과가 나와야 함

        // Weekends
        alarmPrefActivity_edit.getAlarm().setAlarmRepeatList(MNDummyAlarmMaker.makeNonRepeatAlarm(alarmPrefActivity_edit).getAlarmRepeatList());
        AlertDialog weekendsAlertDialog = MNAlarmPrefRepeatItemMaker.makeRepeatAlertDialog(context, alarmPrefActivity_edit.getAlarm());
        weekendsAlertDialog.show();
        weekendsAlertDialog.getListView().setItemChecked(5, true);
        weekendsAlertDialog.getListView().setItemChecked(6, true);

        boolean[] weekendsRepeats = getRepeatsFromAlertDialog(weekendsAlertDialog);
        MNAlarmPrefBusProvider.getInstance().post(weekendsRepeats);
        String weekendsString = MNAlarmRepeatString.makeRepeatDetailString(alarmPrefActivity_edit.getAlarm().getAlarmRepeatList(), context);
        assertThat(weekendsString, is(context.getString(R.string.alarm_pref_repeat_weekends)));
        weekendsAlertDialog.dismiss();

        // Weekdays
        alarmPrefActivity_edit.getAlarm().setAlarmRepeatList(MNDummyAlarmMaker.makeNonRepeatAlarm(alarmPrefActivity_edit).getAlarmRepeatList());
        AlertDialog weekDaysAlertDialog = MNAlarmPrefRepeatItemMaker.makeRepeatAlertDialog(context, alarmPrefActivity_edit.getAlarm());
        weekDaysAlertDialog.show();
        weekDaysAlertDialog.getListView().setItemChecked(0, true);
        weekDaysAlertDialog.getListView().setItemChecked(1, true);
        weekDaysAlertDialog.getListView().setItemChecked(2, true);
        weekDaysAlertDialog.getListView().setItemChecked(3, true);
        weekDaysAlertDialog.getListView().setItemChecked(4, true);

        boolean[] weekdaysRepeats = getRepeatsFromAlertDialog(weekDaysAlertDialog);
        MNAlarmPrefBusProvider.getInstance().post(weekdaysRepeats);
        String weekdaysString = MNAlarmRepeatString.makeRepeatDetailString(alarmPrefActivity_edit.getAlarm().getAlarmRepeatList(), context);
        assertThat(weekdaysString, is(context.getString(R.string.alarm_pref_repeat_weekdays)));
        weekDaysAlertDialog.dismiss();

        // Everyday
        alarmPrefActivity_edit.getAlarm().setAlarmRepeatList(MNDummyAlarmMaker.makeNonRepeatAlarm(alarmPrefActivity_edit).getAlarmRepeatList());
        AlertDialog everydayAlertDialog = MNAlarmPrefRepeatItemMaker.makeRepeatAlertDialog(context, alarmPrefActivity_edit.getAlarm());
        everydayAlertDialog.show();
        everydayAlertDialog.getListView().setItemChecked(0, true);
        everydayAlertDialog.getListView().setItemChecked(1, true);
        everydayAlertDialog.getListView().setItemChecked(2, true);
        everydayAlertDialog.getListView().setItemChecked(3, true);
        everydayAlertDialog.getListView().setItemChecked(4, true);
        everydayAlertDialog.getListView().setItemChecked(5, true);
        everydayAlertDialog.getListView().setItemChecked(6, true);

        boolean[] everydayRepeats = getRepeatsFromAlertDialog(everydayAlertDialog);
        MNAlarmPrefBusProvider.getInstance().post(everydayRepeats);
        String everydayString = MNAlarmRepeatString.makeRepeatDetailString(alarmPrefActivity_edit.getAlarm().getAlarmRepeatList(), context);
        assertThat(everydayString, is(context.getString(R.string.alarm_pref_repeat_everyday)));
        everydayAlertDialog.dismiss();
    }

    private boolean[] getRepeatsFromAlertDialog(AlertDialog alertDialog) {
        SparseBooleanArray repeatArray = alertDialog.getListView().getCheckedItemPositions();
        boolean[] repeats = new boolean[7];
        for (int i = 0; i < repeats.length; i++) {
            repeats[i] = repeatArray.get(i);
        }
        return repeats;
    }

    @Test
    public void testAddAlarmPreferenceActivity() throws Exception {
        // 알람을 추가할 경우에는 알람의 반복이 비어 있어야 함
        String repeatString = MNAlarmRepeatString.makeRepeatDetailString(alarmPrefActivity_add.getAlarm().getAlarmRepeatList(), alarmPrefActivity_add);
        assertThat(repeatString, is("Never"));
    }
}
