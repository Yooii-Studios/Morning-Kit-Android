package com.yooiistudios.morningkit.alarm.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.listview.item.MNAlarmItemScrollView;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;

enum MNAlarmListAdapterType {MainAlarmListAdapter, ConfigureAlarmListAdapter; }

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 11. 27.
 * MNAlarmListAdapter
 */
public class MNAlarmListAdapter extends BaseAdapter {
    private static final String TAG = "MNAlarmListAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private View.OnClickListener mAlarmItemClickListener;
//    private MNAlarmListAdapterType type;

    private MNAlarmListAdapter() {}
    public MNAlarmListAdapter(Context context, View.OnClickListener alarmItemClickListener) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mAlarmItemClickListener = alarmItemClickListener;
    }

    @Override
    public int getCount() {
        return MNAlarmListManager.getAlarmList(mContext).size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < MNAlarmListManager.getAlarmList(mContext).size()) {
            try {
                return MNAlarmListManager.getAlarmList(mContext).get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (position < MNAlarmListManager.getAlarmList(mContext).size()) {
            try {
                MNAlarm alarm = MNAlarmListManager.getAlarmList(mContext).get(position);
                return alarm.getAlarmId();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            if (convertView != null && alarm != null) {
                convertView.setOnClickListener(mAlarmItemClickListener);
                convertView.setLongClickable(false);
                convertView.setTag(alarm);

                // Alarm Switch Button
                final ImageButton alarmSwitchButton = (ImageButton) convertView.findViewById(R.id.alarm_item_switch_imagebutton);
                if (alarm.isAlarmOn()) {
                    alarmSwitchButton.setSelected(true);
                }else{
                    alarmSwitchButton.setSelected(false);
                }
                alarmSwitchButton.setTag(alarm);
                alarmSwitchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (alarmSwitchButton.isSelected()) {
                            alarmSwitchButton.setSelected(false);
                        } else {
                            alarmSwitchButton.setSelected(true);
                        }
                    }
                });
            }
//            return alarmItemScrollView;
            return MNAlarmItemScrollView.newInstance(mContext, position, convertView);

        }else{
            convertView = mLayoutInflater.inflate(R.layout.alarm_create_item, parent, false);
            if (convertView != null) {
                convertView.setLongClickable(false);
                convertView.setOnClickListener(mAlarmItemClickListener);
                convertView.setTag(-1);
            }
            return convertView;
        }
    }
}
