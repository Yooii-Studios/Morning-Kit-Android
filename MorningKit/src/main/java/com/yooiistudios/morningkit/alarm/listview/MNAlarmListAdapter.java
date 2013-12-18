package com.yooiistudios.morningkit.alarm.listview;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.listview.item.MNAlarmItemScrollView;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.string.MNAlarmTimeString;
import com.yooiistudios.morningkit.common.bus.MNAlarmScrollViewBusProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
        this.context = context;
        this.alarmItemClickListener = alarmItemClickListener;
        MNAlarmScrollViewBusProvider.getInstance().register(this);
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

            // recycle convertView - 나중에 필요해지면 사용하자
//            if (convertView == null || convertView.getId() != R.layout.alarm_item) {
//                convertView = LayoutInflater.from(context).inflate(R.layout.alarm_item, parent, false);
//                if (convertView != null && alarm != null) {
//                    convertView.setTag(alarm);
//                    convertView.setId(R.layout.alarm_item);
//                }
//            }else{
//                Log.i(TAG, "recycle convertView");
//            }

            // changed code to 'Butter Knife' code
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_item, parent, false);
            if (convertView != null && alarm != null) {
                convertView.setTag(alarm);
                convertView.setOnClickListener(alarmItemClickListener);
                convertView.setLongClickable(false);

                // MNAlarmItemViewHolder
                MNAlarmItemViewHolder alarmItemViewHolder = new MNAlarmItemViewHolder(convertView);

                // Background

                // Alarm Time
                initTimeTextView(alarm, alarmItemViewHolder);

                // AM / PM
                initAmPmTextView(alarmItemViewHolder);

                // Repeat
                initRepeatTextView(alarmItemViewHolder);

                // Label
                initLabelTextView(alarm, alarmItemViewHolder);

                // Dividing Bar

                // Switch Button
                initSwitchButton(alarm, alarmItemViewHolder);
            }
            return MNAlarmItemScrollView.newInstance(context, position, convertView);

        }else{
            // change to 'Butter Knife' code
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_create_item, parent, false);
            if (convertView != null) {
                convertView.setTag(-1);
                convertView.setOnClickListener(alarmItemClickListener);
                convertView.setLongClickable(false);

                // MNAlarmCreateItemViewHolder
                MNAlarmCreateItemViewHolder alarmCreateItemViewHolder = new MNAlarmCreateItemViewHolder(convertView);
            }
            return convertView;
        }
    }

    private void initTimeTextView(MNAlarm alarm, MNAlarmItemViewHolder alarmItemViewHolder) {
        alarmItemViewHolder.timeTextView.setText(MNAlarmTimeString.makeTimeString(alarm.getAlarmCalendar(), context));
    }

    private void initAmPmTextView(MNAlarmItemViewHolder alarmItemViewHolder) {
        if (DateFormat.is24HourFormat(context)) {
            // 24시간제면 width를 0으로 조정,
            alarmItemViewHolder.ampmTextView.getLayoutParams().width = 0;
        } else {
            // wrap_content
            alarmItemViewHolder.ampmTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

    private void initRepeatTextView(MNAlarmItemViewHolder alarmItemViewHolder) {
        RelativeLayout.LayoutParams repeatTextViewLayoutParams = (RelativeLayout.LayoutParams) alarmItemViewHolder.repeatTextView.getLayoutParams();
        if (DateFormat.is24HourFormat(context)) {
            repeatTextViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.alarm_item_time_textview);
        } else {
            repeatTextViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.alarm_item_ampm_textview);
        }
    }

    private void initLabelTextView(MNAlarm alarm, MNAlarmItemViewHolder alarmItemViewHolder) {
        alarmItemViewHolder.labelTextView.setText(alarm.getAlarmLabel());
    }

    private void initSwitchButton(MNAlarm alarm, MNAlarmItemViewHolder alarmViewHolder) {
        final ImageButton alarmSwitchButton = alarmViewHolder.switchImageButton;
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

    static class MNAlarmItemViewHolder {
        @InjectView(R.id.alarm_item_outer_layout)           RelativeLayout  outerLayout;
        @InjectView(R.id.alarm_item_inner_layout)           RelativeLayout  innerLayout;
        @InjectView(R.id.alarm_item_time_textview)          TextView        timeTextView;
        @InjectView(R.id.alarm_item_ampm_textview)          TextView        ampmTextView;
        @InjectView(R.id.alarm_item_repeat_textview)        TextView        repeatTextView;
        @InjectView(R.id.alarm_item_alarm_label_textview)   TextView        labelTextView;
        @InjectView(R.id.alarm_item_dividing_bar_imageview) ImageView       dividingBarImageView;
        @InjectView(R.id.alarm_item_switch_imagebutton)     ImageButton     switchImageButton;

        public MNAlarmItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
    static class MNAlarmCreateItemViewHolder {
        @InjectView(R.id.alarm_create_outer_layout)                 RelativeLayout  outerLayout;
        @InjectView(R.id.alarm_create_inner_layout)                 RelativeLayout  innerLayout;
        @InjectView(R.id.alarm_create_item_textview)                TextView        createAlarmTextView;
        @InjectView(R.id.alarm_create_item_dividing_bar_image_view) ImageView       dividingBarImageView;
        @InjectView(R.id.alarm_create_item_plus_image_view)         ImageView       plusImageView;

        public MNAlarmCreateItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
