package com.yooiistudios.morningkit.alarm.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;

import java.util.ArrayList;

enum MNAlarmListAdaptorType { MainAlarmListAdaptor, ConfigureAlarmListAdaptor; }

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 11. 27.
 * MNAlarmListAdaptor
 */
public class MNAlarmListAdaptor extends BaseAdapter {
    private static final String TAG = "MNAlarmListAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
//    private MNAlarmListAdaptorType type;

    private MNAlarmListAdaptor() {}
    public MNAlarmListAdaptor(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    public int getCount() {
        return MNAlarmListManager.getAlarmList(mContext).size() + 1;
    }

    @Override
    public Object getItem(int position) {
        try {
            return MNAlarmListManager.getAlarmList(mContext).get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        try {
            MNAlarm alarm = MNAlarmListManager.getAlarmList(mContext).get(position);
            return alarm.getAlarmId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position < MNAlarmListManager.getAlarmList(mContext).size()) {
            MNAlarm alarm = null;
            try {
                alarm = MNAlarmListManager.getAlarmList(mContext).get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }

            convertView = mLayoutInflater.inflate(R.layout.alarm_item, parent, false);
            if (convertView != null) {
                convertView.setLongClickable(false);
                convertView.setTag(alarm);
            }
            return convertView;
        }else{
            convertView = mLayoutInflater.inflate(R.layout.alarm_create_item, parent, false);
            if (convertView != null) {
                convertView.setLongClickable(false);
                convertView.setTag("alarm_create_item");
            }
            return convertView;
        }
    }
}
