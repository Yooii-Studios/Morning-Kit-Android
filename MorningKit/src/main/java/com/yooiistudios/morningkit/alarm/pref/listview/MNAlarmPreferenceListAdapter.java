package com.yooiistudios.morningkit.alarm.pref.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.pref.listview.item.MNAlarmPrefListViewItemMaker;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 7.
 *
 * MNAlarmPreferenceListAdapter
 *  알람설정 리스트뷰를 초기화하는 어댑터
 */
public class MNAlarmPreferenceListAdapter extends BaseAdapter{

    Context context;

    private MNAlarmPreferenceListAdapter() {}
    public MNAlarmPreferenceListAdapter(Context context) {
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MNAlarmPrefListItemType indexType = MNAlarmPrefListItemType.valueOf(position);
        switch (indexType) {
            case REPEAT:
                convertView = MNAlarmPrefListViewItemMaker.makeRepeatItem(context, parent);
                break;
            case LABEL:
                convertView = MNAlarmPrefListViewItemMaker.makeLabelItem(context, parent);
                break;
            case SOUND_TYPE:
                convertView = MNAlarmPrefListViewItemMaker.makeSoundTypeItem(context, parent);
                break;
            case SOUND_NAME:
                convertView = MNAlarmPrefListViewItemMaker.makeSoundNameItem(context, parent);
                break;
            case SNOOZE:
                convertView = MNAlarmPrefListViewItemMaker.makeSnoozeItem(context, parent);
                break;
            case TIME:
                convertView = MNAlarmPrefListViewItemMaker.makeTimeItem(context, parent);
                break;
            default:
                throw new AssertionError("Undefined position");
        }
        convertView.setTag(indexType);
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return MNAlarmPrefListItemType.values().length;
    }
}
