package com.yooiistudios.morningkit.alarm.pref.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yooiistudios.morningkit.R;

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
        convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_item, parent, false);
        if (convertView != null) {
            convertView.setTag(indexType);
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
        }
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
