package com.yooiistudios.morningkit.alarm.pref.listview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.squareup.otto.Subscribe;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.pref.listview.item.maker.MNAlarmPrefItemMaker;
import com.yooiistudios.morningkit.alarm.pref.listview.item.maker.MNAlarmPrefLabelItemMaker;
import com.yooiistudios.morningkit.alarm.pref.listview.item.maker.MNAlarmPrefRepeatItemMaker;
import com.yooiistudios.morningkit.common.bus.MNAlarmPrefBusProvider;

import java.util.ArrayList;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 7.
 *
 * MNAlarmPreferenceListAdapter
 *  알람설정 리스트뷰를 초기화하는 어댑터
 */
public class MNAlarmPreferenceListAdapter extends BaseAdapter{

    private static final String TAG = "MNAlarmPreferenceListAdapter";
    private Context context;
    private MNAlarm alarm;

    private MNAlarmPreferenceListAdapter() {}
    public MNAlarmPreferenceListAdapter(Context context, MNAlarm alarm) {
        this.context = context;
        this.alarm = alarm;
        MNAlarmPrefBusProvider.getInstance().register(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MNAlarmPrefListItemType indexType = MNAlarmPrefListItemType.valueOf(position);
        switch (indexType) {
            case REPEAT:
                convertView = MNAlarmPrefRepeatItemMaker.makeRepeatItem(context, parent, alarm);
                break;
            case LABEL:
                convertView = MNAlarmPrefLabelItemMaker.makeLabelItem(context, parent, alarm);
                break;
            case SOUND:
                convertView = MNAlarmPrefItemMaker.makeSoundItem(context, parent, alarm);
                break;
            case SNOOZE:
                convertView = MNAlarmPrefItemMaker.makeSnoozeItem(context, parent, alarm);
                break;
            case TIME:
                convertView = MNAlarmPrefItemMaker.makeTimeItem(context, parent, alarm);
                break;
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return MNAlarmPrefListItemType.values().length;
    }

    /**
     * Otto: MNAlarmPrefBusProvider
     */
    @Subscribe
    public void onLabelChanged(String labelString) {
        if (labelString != null) {
            alarm.setAlarmLabel(labelString);
            notifyDataSetChanged();
        } else {
            throw new AssertionError("labelString must be null!");
        }
    }

    @Subscribe
    public void onRepeatChanged(boolean[] repeats) {
        if (alarm != null) {
            for (int i = 0; i < this.alarm.getAlarmRepeatList().size(); i++) {
                this.alarm.getAlarmRepeatList().set(i, repeats[i]);
            }
            notifyDataSetChanged();
        } else {
            throw new AssertionError("alarm must not be null!");
        }
    }
}
