package com.yooiistudios.morningkit.alarm.listview.item;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.flurry.android.FlurryAgent;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.pref.MNAlarmPreferenceActivity;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.common.unlock.MNUnlockActivity;
import com.yooiistudios.morningkit.main.MNMainAlarmListView;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 3.
 *
 * MNAlarmItemClickListener
 *  1. 알람 클릭 로직을 담당
 *  2. 생성/수정에 맞추어 알람설정 액티비티 생성 담당
 */
public class MNAlarmItemClickListener implements View.OnClickListener {

    private static final String TAG = "MNAlarmItemClickListener";
    @Getter private MNMainAlarmListView alarmListView;

    private MNAlarmItemClickListener() {}

    public static MNAlarmItemClickListener newInstance(MNMainAlarmListView alarmListView) {
        MNAlarmItemClickListener itemClickListener = new MNAlarmItemClickListener();
        itemClickListener.alarmListView = alarmListView;
        return itemClickListener;
    }

    @Override
    public void onClick(View v) {

        MNAlarm alarm = null;
        if (v.getTag().getClass() == MNAlarm.class) {
            // Edit alarm: alarmId
            alarm = (MNAlarm)v.getTag();
        } else {
            // Add an alarm : -1
            // 알람이 4개째 추가될 때 알람 추가 아이템 구매가 안되어 있다면 언락 화면 표시
            Activity activityContext = (Activity) alarmListView.getContext();
            List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(activityContext);
            if (alarmListView.getCount() > 3 && !ownedSkus.contains(SKIabProducts.SKU_MORE_ALARM_SLOTS)) {
                Intent intent = new Intent(activityContext, MNUnlockActivity.class);
                intent.putExtra(MNUnlockActivity.PRODUCT_SKU_KEY,
                        SKIabProducts.SKU_MORE_ALARM_SLOTS);
                activityContext.startActivity(intent);
                activityContext.overridePendingTransition(R.anim.activity_modal_up, R.anim.activity_hold);

                // 플러리
                Map<String, String> params = new HashMap<String, String>();
                params.put(MNFlurry.CALLED_FROM, "More Alarm Slots");
                FlurryAgent.logEvent(MNFlurry.UNLOCK, params);
                return;
            }
        }

        Intent i = new Intent(alarmListView.getContext(), MNAlarmPreferenceActivity.class);
        if (alarmListView.getContext() != null) {
            // Start activity with extra(alarmId)
            if (alarm != null) {
                i.putExtra(MNAlarmPreferenceActivity.ALARM_PREFERENCE_ALARM_ID, alarm.getAlarmId());
            }else{
                i.putExtra(MNAlarmPreferenceActivity.ALARM_PREFERENCE_ALARM_ID, -1);
            }
            alarmListView.getContext().startActivity(i);
        }
    }
}