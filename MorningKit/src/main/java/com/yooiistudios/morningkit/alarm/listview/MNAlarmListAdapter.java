package com.yooiistudios.morningkit.alarm.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EView;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.listview.item.MNAlarmItemScrollView;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;

enum MNAlarmListAdapterType { MAIN, CONFIGURE; }

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 11. 27.
 *
 * MNAlarmListAdapter
 *  메인화면 + 설정화면의 알람 리스트뷰에 적용 가능한 리스트 어댑터
 */
public class MNAlarmListAdapter extends BaseAdapter {
    private static final String TAG = "MNAlarmListAdapter";
    private Context context;
    private View.OnClickListener alarmItemClickListener;
//    private MNAlarmListAdapterType type;

    private MNAlarmListAdapter() {}
    public MNAlarmListAdapter(Context context, View.OnClickListener alarmItemClickListener) {
//        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.alarmItemClickListener = alarmItemClickListener;
    }

    @Override
    public int getCount() {
        return MNAlarmListManager.getAlarmList(context).size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < MNAlarmListManager.getAlarmList(context).size()) {
            try {
                return MNAlarmListManager.getAlarmList(context).get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (position < MNAlarmListManager.getAlarmList(context).size()) {
            try {
                MNAlarm alarm = MNAlarmListManager.getAlarmList(context).get(position);
                return alarm.getAlarmId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position < MNAlarmListManager.getAlarmList(context).size()) {
            MNAlarm alarm = null;
            try {
                alarm = MNAlarmListManager.getAlarmList(context).get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // changed code to 'Butter Knife' code
//            convertView = mLayoutInflater.inflate(R.layout.alarm_item, parent, false);
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_item, parent, false);
            if (convertView != null && alarm != null) {
                convertView.setOnClickListener(alarmItemClickListener);
                convertView.setLongClickable(false);
                convertView.setTag(alarm);

                // MNAlarmItemViewHolder
                MNAlarmItemViewHolder alarmViewHolder = new MNAlarmItemViewHolder(convertView);

                // Alarm Switch Button
//                final ImageButton alarmSwitchButton = (ImageButton) convertView.findViewById(R.id.alarm_item_switch_imagebutton);
//                final ImageButton alarmSwitchButton = ButterKnife.findById(convertView, R.id.alarm_item_switch_imagebutton);
                final ImageButton alarmSwitchButton = alarmViewHolder.alarmSwitchImageButton;
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
            return MNAlarmItemScrollView.newInstance(context, position, convertView);

        }else{
            // change to 'Butter Knife' code
//            convertView = mLayoutInflater.inflate(R.layout.alarm_create_item, parent, false);
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_create_item, parent, false);
            if (convertView != null) {
                convertView.setLongClickable(false);
                convertView.setOnClickListener(alarmItemClickListener);
                convertView.setTag(-1);

                // MNAlarmCreateItemViewHolder
                MNAlarmCreateItemViewHolder alarmCreateItemViewHolder = new MNAlarmCreateItemViewHolder(convertView);
            }
            return convertView;
        }
    }

    static class MNAlarmItemViewHolder {
//        @ViewById(R.id.alarm_item_outer_layout)           RelativeLayout  outerLayout;
//        @ViewById(R.id.alarm_item_inner_layout)           RelativeLayout  innerLayout;
//        @ViewById(R.id.alarm_item_time_textview)          TextView        timeTextView;
//        @ViewById(R.id.alarm_item_repeat_textview)        TextView        repeatTextView;
//        @ViewById(R.id.alarm_item_alarm_label_textview)   TextView        alarmLabelTextView;
//        @ViewById(R.id.alarm_item_dividing_bar_imageview) ImageView       dividingBarImageView;
//        @ViewById(R.id.alarm_item_switch_imagebutton)     ImageButton     alarmSwitchImageButton;

        RelativeLayout  outerLayout;
        RelativeLayout  innerLayout;
        TextView        timeTextView;
        TextView        repeatTextView;
        TextView        alarmLabelTextView;
        ImageView       dividingBarImageView;
        ImageButton     alarmSwitchImageButton;

        public MNAlarmItemViewHolder(View view) {
//            ButterKnife.inject(this, view);

            outerLayout = (RelativeLayout) view.findViewById(R.id.alarm_item_outer_layout);
            innerLayout = (RelativeLayout) view.findViewById(R.id.alarm_item_inner_layout);
            timeTextView = (TextView) view.findViewById(R.id.alarm_item_time_textview);
            repeatTextView = (TextView) view.findViewById(R.id.alarm_item_repeat_textview);
            alarmLabelTextView = (TextView) view.findViewById(R.id.alarm_item_alarm_label_textview);
            dividingBarImageView = (ImageView) view.findViewById(R.id.alarm_item_dividing_bar_imageview);
            alarmSwitchImageButton = (ImageButton) view.findViewById(R.id.alarm_item_switch_imagebutton);
        }
    }

    static class MNAlarmCreateItemViewHolder {
//        @ViewById(R.id.alarm_create_outer_layout)                 RelativeLayout  outerLayout;
//        @ViewById(R.id.alarm_create_inner_layout)                 RelativeLayout  innerLayout;
//        @ViewById(R.id.alarm_create_item_text)                    TextView        createAlarmTextView;
//        @ViewById(R.id.alarm_create_item_dividing_bar_image_view) ImageView       dividingBarImageView;
//        @ViewById(R.id.alarm_create_item_plus_image_view)         ImageView       plusImageView;

        RelativeLayout  outerLayout;
        RelativeLayout  innerLayout;
        TextView        createAlarmTextView;
        ImageView       dividingBarImageView;
        ImageView       plusImageView;

        public MNAlarmCreateItemViewHolder(View view) {
//            ButterKnife.inject(this, view);
            outerLayout = (RelativeLayout) view.findViewById(R.id.alarm_create_outer_layout);
            innerLayout = (RelativeLayout) view.findViewById(R.id.alarm_create_inner_layout);
            createAlarmTextView = (TextView) view.findViewById(R.id.alarm_create_item_textview);
            dividingBarImageView = (ImageView) view.findViewById(R.id.alarm_create_item_dividing_bar_image_view);
            plusImageView = (ImageView) view.findViewById(R.id.alarm_create_item_plus_image_view);
        }
    }
}
