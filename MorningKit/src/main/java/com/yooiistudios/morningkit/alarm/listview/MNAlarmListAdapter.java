package com.yooiistudios.morningkit.alarm.listview;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.listview.item.MNAlarmItemScrollView;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.string.MNAlarmRepeatString;
import com.yooiistudios.morningkit.alarm.model.string.MNAlarmTimeString;
import com.yooiistudios.morningkit.common.bus.MNAlarmScrollViewBusProvider;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
import com.yooiistudios.morningkit.theme.MNColor;

import java.io.IOException;
import java.util.Calendar;

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
                initAmPmTextView(alarm, alarmItemViewHolder);

                // Repeat
                initRepeatTextView(alarmItemViewHolder);

                // Label
                initLabelTextView(alarm, alarmItemViewHolder);

                // Dividing Bar

                // Switch Button
                initSwitchButton(alarm, alarmItemViewHolder);

                // Theme
                initThemeOfAlarmViewHolder(alarm, alarmItemViewHolder);
            }
            return MNAlarmItemScrollView.newInstance(context, position, convertView);

        }else{
            // change to 'Butter Knife' code
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_create_item, parent, false);
            if (convertView != null) {
//                convertView.setTag(-1);
//                convertView.setOnClickListener(alarmItemClickListener);
//                convertView.setLongClickable(false);

                // MNAlarmCreateItemViewHolder
                MNAlarmCreateItemViewHolder alarmCreateItemViewHolder = new MNAlarmCreateItemViewHolder(convertView);

                MNShadowLayoutFactory.changeThemeOfShadowLayout(alarmCreateItemViewHolder.shadowLayout, context);
                alarmCreateItemViewHolder.shadowLayout.setTag(-1);
                alarmCreateItemViewHolder.shadowLayout.setOnClickListener(alarmItemClickListener);
                alarmCreateItemViewHolder.shadowLayout.setLongClickable(false);
            }
            return convertView;
        }
    }

    private void initTimeTextView(MNAlarm alarm, MNAlarmItemViewHolder alarmItemViewHolder) {
        alarmItemViewHolder.timeTextView.setText(MNAlarmTimeString.makeTimeString(alarm.getAlarmCalendar(), context));
    }

    private void initAmPmTextView(MNAlarm alarm, MNAlarmItemViewHolder alarmItemViewHolder) {
        if (DateFormat.is24HourFormat(context)) {
            // 24시간제면 width 를 0으로 조정,
            alarmItemViewHolder.ampmTextView.getLayoutParams().width = 0;
        } else {
            // wrap_content
            alarmItemViewHolder.ampmTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            if (alarm.getAlarmCalendar().get(Calendar.HOUR_OF_DAY) < 12) {
                alarmItemViewHolder.ampmTextView.setText(R.string.alarm_am);
            } else {
                alarmItemViewHolder.ampmTextView.setText(R.string.alarm_pm);
            }
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
        alarmItemViewHolder.labelTextView.setSelected(true);
        alarmItemViewHolder.labelTextView.setTextColor(MNColor.getAlarmSubFontColor());
    }

    private void initSwitchButton(final MNAlarm alarm, final MNAlarmItemViewHolder alarmItemViewHolder) {
        final ImageButton alarmSwitchButton = alarmItemViewHolder.switchImageButton;
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
                    alarm.stopAlarm(context);
                } else {
                    alarmSwitchButton.setSelected(true);
                    alarm.startAlarm(context);
                }

                // refresh theme
                initThemeOfAlarmViewHolder(alarm, alarmItemViewHolder);

                // save alarmList
                try {
                    MNAlarmListManager.saveAlarmList(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initThemeOfAlarmViewHolder(final MNAlarm alarm, MNAlarmItemViewHolder alarmItemViewHolder) {
        if (alarm.isAlarmOn()) {
            alarmItemViewHolder.timeTextView.setTextColor(MNColor.getAlarmMainFontColor());
            alarmItemViewHolder.ampmTextView.setTextColor(MNColor.getAlarmMainFontColor());
            alarmItemViewHolder.repeatTextView.setText(MNAlarmRepeatString.makeShortRepeatString(alarm.getAlarmRepeatList(), context));
        } else {
            alarmItemViewHolder.timeTextView.setTextColor(MNColor.getAlarmSubFontColor());
            alarmItemViewHolder.ampmTextView.setTextColor(MNColor.getAlarmSubFontColor());
            alarmItemViewHolder.repeatTextView.setText("");
        }
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

    @Subscribe // After WakeDialogAction
    public void refreshListAdaptor(Context context) {
        MNAlarmListManager.loadAlarmList(context);
        notifyDataSetChanged();
    }

    static class MNAlarmItemViewHolder {
        @InjectView(R.id.alarm_item_outer_layout)           RelativeLayout  outerLayout;
        @InjectView(R.id.alarm_item_inner_layout)           RelativeLayout  innerLayout;
        @InjectView(R.id.alarm_item_time_textview)          TextView        timeTextView;
        @InjectView(R.id.alarm_item_ampm_textview)          TextView        ampmTextView;
        @InjectView(R.id.alarm_item_repeat_textview)        TextView        repeatTextView;
        @InjectView(R.id.alarm_item_label_textview)   TextView        labelTextView;
        @InjectView(R.id.alarm_item_dividing_bar_imageview) ImageView       dividingBarImageView;
        @InjectView(R.id.alarm_item_switch_imagebutton)     ImageButton     switchImageButton;

        public MNAlarmItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
    static class MNAlarmCreateItemViewHolder {
        @InjectView(R.id.alarm_create_outer_layout)                 RelativeLayout  outerLayout;
        @InjectView(R.id.alarm_create_inner_layout)                 RelativeLayout  innerLayout;
        @InjectView(R.id.alarm_create_shadow_layout)                RoundShadowRelativeLayout shadowLayout;
        @InjectView(R.id.alarm_create_item_textview)                TextView        createAlarmTextView;
        @InjectView(R.id.alarm_create_item_dividing_bar_image_view) ImageView       dividingBarImageView;
        @InjectView(R.id.alarm_create_item_plus_image_view)         ImageView       plusImageView;

        public MNAlarmCreateItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
