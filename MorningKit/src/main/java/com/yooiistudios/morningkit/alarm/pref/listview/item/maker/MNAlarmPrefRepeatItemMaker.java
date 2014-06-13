package com.yooiistudios.morningkit.alarm.pref.listview.item.maker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.common.bus.MNAlarmPrefBusProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 16.
 *
 * MNAlarmPrefRepeatItemMaker
 */
public class MNAlarmPrefRepeatItemMaker {
    private static final String TAG = "MNAlarmPrefRepeatItemMaker";
    private MNAlarmPrefRepeatItemMaker() { throw new AssertionError("You MUST NOT create this class"); }

    public static View makeRepeatItem(final Context context, ViewGroup parent, final MNAlarm alarm) {
        final View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_repeat_item, parent, false);
        final MNAlarmPrefRepeatItemViewHolder viewHolder = new MNAlarmPrefRepeatItemViewHolder(convertView);
        convertView.setTag(viewHolder);
        viewHolder.titleTextView.setText(R.string.alarm_pref_repeat);

        // ClickListener
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // ActionBar Menu
//                MNAlarmPrefBusProvider.getInstance().post(convertView);
//
//                // AlertDialog
//                AlertDialog alertDialog = makeRepeatAlertDialog(context, alarm);
//                alertDialog.show();
//            }
//        });

        viewHolder.mondayButton.setTag(0);
        viewHolder.tuesdayButton.setTag(1);
        viewHolder.wednesdayButton.setTag(2);
        viewHolder.thursdayButton.setTag(3);
        viewHolder.fridayButton.setTag(4);
        viewHolder.saturdayButton.setTag(5);
        viewHolder.sundayButton.setTag(6);

        initRepeatButtonOnClickListener(viewHolder.mondayButton, alarm);
        initRepeatButtonOnClickListener(viewHolder.tuesdayButton, alarm);
        initRepeatButtonOnClickListener(viewHolder.wednesdayButton, alarm);
        initRepeatButtonOnClickListener(viewHolder.thursdayButton, alarm);
        initRepeatButtonOnClickListener(viewHolder.fridayButton, alarm);
        initRepeatButtonOnClickListener(viewHolder.saturdayButton, alarm);
        initRepeatButtonOnClickListener(viewHolder.sundayButton, alarm);
        return convertView;
    }

    private static void initRepeatButtonOnClickListener(final Button button, final MNAlarm alarm) {
        final int index = (Integer) button.getTag();
        button.setSelected(alarm.getAlarmRepeatList().get(index));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setSelected(!button.isSelected());
                alarm.getAlarmRepeatList().set(index, button.isSelected());

                // 하나라도 켜져 있으면 반복 알람이라고 설정해주기
                if (alarm != null && alarm.getAlarmRepeatList() != null) {
                    alarm.setRepeatOn(false);
                    for (int i = 0; i < alarm.getAlarmRepeatList().size(); i++) {
                        if (alarm.getAlarmRepeatList().get(i)) {
                            alarm.setRepeatOn(true);
                        }
                    }
                } else {
                    throw new AssertionError("alarm must not be null!");
                }
            }
        });
    }

    public static AlertDialog makeRepeatAlertDialog(final Context context, final MNAlarm alarm) {

        final String[] repeatStrings = new String[]{
                context.getString(R.string.every_monday),
                context.getString(R.string.every_tuesday),
                context.getString(R.string.every_wednesday),
                context.getString(R.string.every_thursday),
                context.getString(R.string.every_friday),
                context.getString(R.string.every_saturday),
                context.getString(R.string.every_sunday)};

        final boolean[] repeats = new boolean[7];
        for (int i = 0; i < repeats.length; i++) {
            repeats[i] = alarm.getAlarmRepeatList().get(i);
        }

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        AlertDialog alertDialog = builder.setMultiChoiceItems(repeatStrings, repeats, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                repeats[which] = isChecked;
            }
        }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MNAlarmPrefBusProvider.getInstance().post(context);
                MNAlarmPrefBusProvider.getInstance().post(repeats);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MNAlarmPrefBusProvider.getInstance().post(context);
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MNAlarmPrefBusProvider.getInstance().post(context);
            }
        }).create();
//                .setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    Log.i(TAG, "keycode_back");
//                    MNAlarmPrefBusProvider.getInstance().post(context);
//                }
//                return false;
//            }
//        })
        alertDialog.setTitle(R.string.alarm_pref_repeat);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    public static class MNAlarmPrefRepeatItemViewHolder {
        @Getter @InjectView(R.id.alarm_pref_list_repeat_item_title_textview)    TextView titleTextView;
        @Getter @InjectView(R.id.alarm_pref_list_repeat_item_monday_button)     Button mondayButton;
        @Getter @InjectView(R.id.alarm_pref_list_repeat_item_tuesday_button)    Button tuesdayButton;
        @Getter @InjectView(R.id.alarm_pref_list_repeat_item_wednesday_button)  Button wednesdayButton;
        @Getter @InjectView(R.id.alarm_pref_list_repeat_item_thursday_button)   Button thursdayButton;
        @Getter @InjectView(R.id.alarm_pref_list_repeat_item_friday_button)     Button fridayButton;
        @Getter @InjectView(R.id.alarm_pref_list_repeat_item_saturday_button)   Button saturdayButton;
        @Getter @InjectView(R.id.alarm_pref_list_repeat_item_sunday_button)     Button sundayButton;

        public MNAlarmPrefRepeatItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
