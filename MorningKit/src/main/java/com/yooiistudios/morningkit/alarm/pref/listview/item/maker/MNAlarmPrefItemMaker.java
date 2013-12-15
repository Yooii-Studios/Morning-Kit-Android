package com.yooiistudios.morningkit.alarm.pref.listview.item.maker;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.pref.listview.item.MNAlarmTimePicker;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 13.
 *
 * MNAlarmPrefListViewItemMaker
 *  각 아이템들을 만들어 주는 리스트뷰
 */
public class MNAlarmPrefItemMaker {
    private static final String TAG = "MNAlarmPrefListViewItemMaker";
    // REPEAT, LABEL, SOUND_TYPE, SOUND_NAME, SNOOZE, TIME;
    private MNAlarmPrefItemMaker() { throw new AssertionError("You MUST NOT create this class"); }

    public static View makeTimeItem(Context context, ViewGroup parent, final MNAlarm alarm) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_time_item, parent, false);
        MNAlarmPrefTimeItemViewHolder viewHolder = new MNAlarmPrefTimeItemViewHolder(convertView);
        convertView.setTag(viewHolder);
        viewHolder.alarmTimePicker.setIs24HourView(DateFormat.is24HourFormat(context));
        viewHolder.alarmTimePicker.setCurrentHour(alarm.getAlarmCalendar().get(Calendar.HOUR_OF_DAY));
        viewHolder.alarmTimePicker.setCurrentMinute(alarm.getAlarmCalendar().get(Calendar.MINUTE));
        viewHolder.alarmTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                alarm.getAlarmCalendar().set(Calendar.HOUR_OF_DAY, hourOfDay);
                alarm.getAlarmCalendar().set(Calendar.MINUTE, minute);
            }
        });
        return convertView;
    }

    public static View makeRepeatItem(Context context, ViewGroup parent, final MNAlarm alarm) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        MNAlarmPrefDefaultItemViewHolder viewHolder = new MNAlarmPrefDefaultItemViewHolder(convertView);
        convertView.setTag(viewHolder);
        viewHolder.titleTextView.setText(R.string.alarm_pref_repeat);
        return convertView;
    }

    // ringotns, music, app music, none
    public static View makeSoundItem(Context context, ViewGroup parent, final MNAlarm alarm) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        MNAlarmPrefDefaultItemViewHolder viewHolder = new MNAlarmPrefDefaultItemViewHolder(convertView);
        convertView.setTag(viewHolder);
        viewHolder.titleTextView.setText(R.string.alarm_pref_sound_type);
        return convertView;
    }

    public static View makeSnoozeItem(Context context, ViewGroup parent, final MNAlarm alarm) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_snooze_item, parent, false);
        MNAlarmPrefSnoozeItemViewHolder viewHolder = new MNAlarmPrefSnoozeItemViewHolder(convertView);
        convertView.setTag(viewHolder);
        viewHolder.titleTextView.setText(R.string.alarm_wake_snooze);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            viewHolder.snoozeSwitch.setChecked(alarm.isSnoozeOn());
            viewHolder.snoozeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    alarm.setSnoozeOn(isChecked);
                }
            });
        } else {
            viewHolder.snoozeCheckBox.setChecked(alarm.isSnoozeOn());
            viewHolder.snoozeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    alarm.setSnoozeOn(isChecked);
                }
            });
        }
        return convertView;
    }

    /**
     * ViewHolder
     */
    static class MNAlarmPrefDefaultItemViewHolder {
        @InjectView(R.id.alarm_pref_list_default_item_outer_layout)     RelativeLayout  outerLayout;
        @InjectView(R.id.alarm_pref_list_default_item_inner_layout)     RelativeLayout  innerLayout;
        @InjectView(R.id.alarm_pref_list_default_item_title_textview)   TextView        titleTextView;
        @InjectView(R.id.alarm_pref_list_default_item_detail_textview)  TextView        detailTextView;

        public MNAlarmPrefDefaultItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    /*
    public static class MNAlarmPrefRepeatItemViewHolder extends MNAlarmPrefDefaultItemViewHolder {
        public MNAlarmPrefRepeatItemViewHolder(View view) {
            super(view);
        }
    }

    public static class MNAlarmPrefLabelItemViewHolder extends MNAlarmPrefDefaultItemViewHolder {
        public MNAlarmPrefLabelItemViewHolder(View view) {
            super(view);
        }
    }

    public static class MNAlarmPrefSoundItemViewHolder extends MNAlarmPrefDefaultItemViewHolder {
        public MNAlarmPrefSoundItemViewHolder(View view) {
            super(view);
        }
    }
    */

    static class MNAlarmPrefSnoozeItemViewHolder {
        @InjectView(R.id.alarm_pref_list_snooze_item_outer_layout)      RelativeLayout  outerLayout;
        @InjectView(R.id.alarm_pref_list_snooze_item_inner_layout)      RelativeLayout  innerLayout;
        @InjectView(R.id.alarm_pref_list_snooze_item_title_textview)    TextView        titleTextView;
        @Optional
        @InjectView(R.id.alarm_pref_list_snooze_item_checkbox)          CheckBox        snoozeCheckBox; // < V14
//        @Optional
//        @InjectView(R.id.alarm_pref_list_snooze_item_switch)            Switch          snoozeSwitch; // >= V14
        Switch snoozeSwitch; // >= V14

        public MNAlarmPrefSnoozeItemViewHolder(View view) {
            ButterKnife.inject(this, view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                snoozeSwitch = (Switch) view.findViewById(R.id.alarm_pref_list_snooze_item_switch);
            }
        }
    }

    static class MNAlarmPrefTimeItemViewHolder {
        @InjectView(R.id.alarm_pref_list_time_item_outer_layout)     RelativeLayout     outerLayout;
        @InjectView(R.id.alarm_pref_list_time_item_inner_layout)     RelativeLayout     innerLayout;
        @InjectView(R.id.alarm_pref_list_time_item_picker)
        MNAlarmTimePicker alarmTimePicker;

        public MNAlarmPrefTimeItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
