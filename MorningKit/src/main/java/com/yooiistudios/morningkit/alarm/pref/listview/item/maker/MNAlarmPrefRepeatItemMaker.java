package com.yooiistudios.morningkit.alarm.pref.listview.item.maker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.string.MNAlarmRepeatString;
import com.yooiistudios.morningkit.common.bus.MNAlarmPrefBusProvider;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 16.
 *
 * MNAlarmPrefRepeatItemMaker
 */
public class MNAlarmPrefRepeatItemMaker {
    private static final String TAG = "MNAlarmPrefRepeatItemMaker";
    private MNAlarmPrefRepeatItemMaker() { throw new AssertionError("You MUST NOT create this class"); }

    public static View makeRepeatItem(final Context context, ViewGroup parent, final MNAlarm alarm) {
        final View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        RepeatItemViewHolder viewHolder = new RepeatItemViewHolder(convertView);
        convertView.setTag(viewHolder);
        viewHolder.titleTextView.setText(R.string.alarm_pref_repeat);
        viewHolder.detailTextView.setText(MNAlarmRepeatString.makeRepeatDetailString(alarm.getAlarmRepeatList(), context));
        viewHolder.detailTextView.setSelected(true);

        // ClickListener
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ActionBar Menu
                MNAlarmPrefBusProvider.getInstance().post(convertView);

                // AlertDialog
                AlertDialog alertDialog = makeRepeatAlertDialog(context, alarm);
                alertDialog.show();
            }
        });
        return convertView;
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
                Log.i(TAG, "negativeButton: onClick");
                MNAlarmPrefBusProvider.getInstance().post(context);
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.i(TAG, "onCancel");
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

    public static class RepeatItemViewHolder extends MNAlarmPrefItemMaker.MNAlarmPrefDefaultItemViewHolder {
        public RepeatItemViewHolder(View view) {
            super(view);
        }
    }
}
