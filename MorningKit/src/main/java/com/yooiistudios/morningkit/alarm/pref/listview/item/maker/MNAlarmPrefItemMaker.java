package com.yooiistudios.morningkit.alarm.pref.listview.item.maker;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.pref.MNAlarmPreferenceType;
import com.yooiistudios.morningkit.alarm.pref.listview.item.MNAlarmTimePicker;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 13.
 *
 * MNAlarmPrefListViewItemMaker
 *  각 아이템들을 만들어 주는 리스트뷰
 *  -> 현재 타임, 스누즈만 만들어줌
 *  -> 나머지는 각 메이커에서 만듬
 */
public class MNAlarmPrefItemMaker {
    private static final String TAG = "MNAlarmPrefListViewItemMaker";
    // REPEAT, LABEL, SOUND_TYPE, SOUND_NAME, SNOOZE, TIME;
    private MNAlarmPrefItemMaker() { throw new AssertionError("You MUST NOT create this class"); }

    public static View makeTimeItem(Context context, ViewGroup parent, final MNAlarm alarm, MNAlarmPreferenceType alarmPreferenceType) {
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

    public static View makeSnoozeItem(Context context, ViewGroup parent, final MNAlarm alarm) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_snooze_item, parent, false);
        final MNAlarmPrefSnoozeItemViewHolder viewHolder = new MNAlarmPrefSnoozeItemViewHolder(convertView);
        convertView.setTag(viewHolder);

        viewHolder.titleTextView.setText(R.string.alarm_wake_snooze);
        viewHolder.titleTextView.setTextColor(MNSettingColors.getMainFontColor(
                MNTheme.getCurrentThemeType(context.getApplicationContext())));

        if (alarm.isSnoozeOn()) {
            viewHolder.snoozeCheckImageButton.setImageResource(R.drawable.icon_panel_detail_checkbox_on);
        } else {
            viewHolder.snoozeCheckImageButton.setImageResource(R.drawable.icon_panel_detail_checkbox);
        }
        viewHolder.snoozeCheckImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarm.setSnoozeOn(!alarm.isSnoozeOn());
                if (alarm.isSnoozeOn()) {
                    viewHolder.snoozeCheckImageButton.setImageResource(R.drawable.icon_panel_detail_checkbox_on);
                } else {
                    viewHolder.snoozeCheckImageButton.setImageResource(R.drawable.icon_panel_detail_checkbox);
                }
            }
        });

//        viewHolder.snoozeCheckBox.setChecked(alarm.isSnoozeOn());
//        viewHolder.snoozeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                alarm.setSnoozeOn(isChecked);
//            }
//        });

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            viewHolder.snoozeSwitch.setChecked(alarm.isSnoozeOn());
//            viewHolder.snoozeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    alarm.setSnoozeOn(isChecked);
//                }
//            });
//        } else {
//
//        }
        return convertView;
    }

    /**
     * ViewHolder
     */
    static class MNAlarmPrefDefaultItemViewHolder {
        @Getter @InjectView(R.id.alarm_pref_list_default_item_title_textview)   TextView        titleTextView;
        @Getter @InjectView(R.id.alarm_pref_list_default_item_detail_textview)  TextView        detailTextView;

        public MNAlarmPrefDefaultItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class MNAlarmPrefSnoozeItemViewHolder {
        @InjectView(R.id.alarm_pref_list_snooze_item_title_textview)        TextView        titleTextView;
        @InjectView(R.id.alarm_pref_list_snooze_item_check_image_button)    ImageButton     snoozeCheckImageButton;

        //        @Optional
//        @InjectView(R.id.alarm_pref_list_snooze_item_checkbox)          CheckBox        snoozeCheckBox; // < V14
//        @Optional
//        @InjectView(R.id.alarm_pref_list_snooze_item_switch)            Switch          snoozeSwitch; // >= V14
//        Switch snoozeSwitch; // >= V14

        public MNAlarmPrefSnoozeItemViewHolder(View view) {
            ButterKnife.inject(this, view);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                snoozeSwitch = (Switch) view.findViewById(R.id.alarm_pref_list_snooze_item_switch);
//            }
        }
    }

    static class MNAlarmPrefTimeItemViewHolder {
        @InjectView(R.id.alarm_pref_list_time_item_picker)          MNAlarmTimePicker   alarmTimePicker;

        public MNAlarmPrefTimeItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
