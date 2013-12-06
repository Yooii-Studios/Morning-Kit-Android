package com.yooiistudios.morningkit.alarm.pref.listview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 7.
 *
 * MNAlarmPreferenceListAdapter
 *  알람설정 리스트뷰를 초기화하는 어댑터
 */
public class MNAlarmPreferenceListAdapter extends BaseAdapter{

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MNAlarmPrefItemType indexType = MNAlarmPrefItemType.valueOf(position);
        switch (indexType) {
            case REPEAT:
                break;
            case LABEL:
                break;
            case SOUND_TYPE:
                break;
            case SOUND_NAME:
                break;
            case SNOOZE:
                break;
            default:
                throw new AssertionError("Undefined position");
        }
        return null;
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
        return MNAlarmPrefItemType.values().length;
    }
}
