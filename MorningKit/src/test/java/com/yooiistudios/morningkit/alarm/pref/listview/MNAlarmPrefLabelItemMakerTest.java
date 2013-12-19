package com.yooiistudios.morningkit.alarm.pref.listview;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.alarm.pref.MNAlarmPreferenceActivity;
import com.yooiistudios.morningkit.alarm.pref.listview.item.maker.MNAlarmPrefLabelItemMaker;
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
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 15.
 *
 * MNAlarmPrefLabelItemMakerTest
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = { AdWebViewShadow.class }) //reportSdk = 10) // Gingerbread
public class MNAlarmPrefLabelItemMakerTest {
    private static final String TAG = "MNAlarmPrefLabelItemMakerTest";
    MNMainActivity mainActivity;
    MNAlarmPreferenceActivity alarmPrefActivity_add;
    MNAlarmPreferenceActivity alarmPrefActivity_edit;

    private static final String TEST_ALARM_LABEL = "testAlarm";
    private static final String TEST_ALARM_LABEL_AFTER = "testAlarm_after";

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;

        // main
        mainActivity = Robolectric.buildActivity(MNMainActivity.class).create().visible().get();

        // dummy alarm setting
        MNAlarm alarm = MNAlarmMaker.makeAlarm(mainActivity.getBaseContext());
        alarm.setAlarmId(50);
        alarm.setAlarmLabel(TEST_ALARM_LABEL);
        ArrayList<MNAlarm> dummyAlarmList = MNAlarmListManager.loadAlarmList(mainActivity.getBaseContext());
        dummyAlarmList.add(alarm);
        MNAlarmListManager.saveAlarmList(mainActivity.getBaseContext());

        // 'Add alarm' Activity
        Intent intent_add_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity.class);
        intent_add_alarm.putExtra(MN.alarm.ALARM_PREFERENCE_ALARM_ID, -1);
        alarmPrefActivity_add = Robolectric.buildActivity(MNAlarmPreferenceActivity.class)
                .withIntent(intent_add_alarm).create().visible().get();

        // 'Edit alarm' Activity
        Intent intent_edit_alarm = new Intent(mainActivity.getBaseContext(), MNAlarmPreferenceActivity.class);
        intent_edit_alarm.putExtra(MN.alarm.ALARM_PREFERENCE_ALARM_ID, alarm.getAlarmId());
        alarmPrefActivity_edit = Robolectric.buildActivity(MNAlarmPreferenceActivity.class)
                .withIntent(intent_edit_alarm).create().visible().get();
    }

    @Test
    public void testLabelItem() throws Exception {
        testLabelItemForEachActivity(alarmPrefActivity_add);
        testLabelItemForEachActivity(alarmPrefActivity_edit);
    }

    private void testLabelItemForEachActivity(MNAlarmPreferenceActivity alarmPrefActivity) {
        View convertView = MNAlarmPrefLabelItemMaker.makeLabelItem(alarmPrefActivity, null, alarmPrefActivity.getAlarm());
        assertThat(convertView, notNullValue());

        MNAlarmPrefLabelItemMaker.LabelItemViewHolder viewHolder = (MNAlarmPrefLabelItemMaker.LabelItemViewHolder) convertView.getTag();
        assertThat(viewHolder, notNullValue());
        assertThat(viewHolder, is(MNAlarmPrefLabelItemMaker.LabelItemViewHolder.class));
        assertThat(viewHolder.getTitleTextView().getText().toString(), is(alarmPrefActivity.getString(R.string.alarm_pref_label)));

        if (alarmPrefActivity.getAlarm().getAlarmLabel().equals("Alarm")) {
            // 알람 추가에서는 "Alarm"으로 설정 되어 있음 -> 언어에 따른 기본 알람 레이블로 변경이 되는지 테스트
            assertThat(viewHolder.getDetailTextView().getText().toString(), is(alarmPrefActivity.getString(R.string.alarm_default_label)));
        } else {
            // 알람 수정에서는 "testAlarm" -> 표시가 "testAlarm"으로 되는지 테스트
            assertThat(alarmPrefActivity.getAlarm().getAlarmLabel(), is(TEST_ALARM_LABEL));
            assertThat(viewHolder.getDetailTextView().getText().toString(), is(TEST_ALARM_LABEL));
        }
    }

    @Test
    public void testLabelDialog() throws Exception {
        FrameLayout dialogLayout = (FrameLayout) LayoutInflater.from(alarmPrefActivity_edit).inflate(R.layout.alarm_pref_list_label_item_dialog, null);
        assertThat(dialogLayout, notNullValue());

        final MNAlarmPrefLabelItemMaker.LabelDialogLayoutHolder dialogLayoutHolder
                = new MNAlarmPrefLabelItemMaker.LabelDialogLayoutHolder(dialogLayout, alarmPrefActivity_edit.getAlarm());

        // 실행 전 제대로 레이블이 들어가 있는지 확인
        assertThat(dialogLayoutHolder, notNullValue());
        assertThat(dialogLayoutHolder.getLabelEditText().getText().toString(), is(TEST_ALARM_LABEL));

        // 클리어 버튼을 클릭시 동작하는지 확인
        dialogLayoutHolder.getClearButton().performClick();
        assertThat(dialogLayoutHolder.getLabelEditText().getText().toString(), is(""));

        // 레이블을 수정했다고 가정
        dialogLayoutHolder.getLabelEditText().setText(TEST_ALARM_LABEL_AFTER);

        // AlertDialog 생성
        AlertDialog alertDialog = MNAlarmPrefLabelItemMaker.makeLabelAlertDialog(alarmPrefActivity_edit, dialogLayout, dialogLayoutHolder);
        alertDialog.show();

        // 확인 버튼 클릭
        // 액티비티로 post가 가지 않음 -> 어쩔 수 없이 내부 코드로 확인
//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        MNAlarmPrefBusProvider.getInstance().post(dialogLayoutHolder.getLabelEditText().getText().toString());
        assertThat(alarmPrefActivity_edit.getAlarm().getAlarmLabel(), is(TEST_ALARM_LABEL_AFTER));
    }
}
