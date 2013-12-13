package com.yooiistudios.morningkit.alarm.pref.listview.item;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yooiistudios.morningkit.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 13.
 *
 * MNAlarmPrefListViewItemMaker
 *  각 아이템들을 만들어 주는 리스트뷰
 */
public class MNAlarmPrefListViewItemMaker {
    // REPEAT, LABEL, SOUND_TYPE, SOUND_NAME, SNOOZE, TIME;
    private MNAlarmPrefListViewItemMaker() { throw new AssertionError("You MUST NOT create this class"); }

    public static View makeRepeatItem(Context context, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        MNAlarmPrefDefaultItemViewHolder viewHolder = new MNAlarmPrefDefaultItemViewHolder(convertView);
        return convertView;
    }

    public static View makeLabelItem(Context context, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        MNAlarmPrefDefaultItemViewHolder viewHolder = new MNAlarmPrefDefaultItemViewHolder(convertView);
        return convertView;
    }

    public static View makeSoundTypeItem(Context context, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        MNAlarmPrefDefaultItemViewHolder viewHolder = new MNAlarmPrefDefaultItemViewHolder(convertView);
        return convertView;
    }

    public static View makeSoundNameItem(Context context, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        MNAlarmPrefDefaultItemViewHolder viewHolder = new MNAlarmPrefDefaultItemViewHolder(convertView);
        return convertView;
    }

    public static View makeSnoozeItem(Context context, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_snooze_item, parent, false);
        MNAlarmPrefSnoozeItemViewHolder viewHolder = new MNAlarmPrefSnoozeItemViewHolder(convertView);
        return convertView;
    }

    public static View makeTimeItem(Context context, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_time_item, parent, false);
        MNAlarmPrefTimeItemViewHolder viewHolder = new MNAlarmPrefTimeItemViewHolder(convertView);
        return convertView;
    }

    static class MNAlarmPrefDefaultItemViewHolder {
        @InjectView(R.id.alarm_pref_list_default_item_outer_layout)     RelativeLayout  outerLayout;
        @InjectView(R.id.alarm_pref_list_default_item_inner_layout)     RelativeLayout  innerLayout;
        @InjectView(R.id.alarm_pref_list_default_item_title_textview)   TextView        titleTextView;
        @InjectView(R.id.alarm_pref_list_default_item_detail_textview)  TextView        detailTextView;

        public MNAlarmPrefDefaultItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

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
        @InjectView(R.id.alarm_pref_list_time_item_outer_layout)     RelativeLayout  outerLayout;
        @InjectView(R.id.alarm_pref_list_time_item_inner_layout)     RelativeLayout  innerLayout;
        @InjectView(R.id.alarm_pref_list_time_item_picker)           TimePicker      alarmTimePicker;

        public MNAlarmPrefTimeItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
