package com.yooiistudios.morningkit.alarm.pref.listview.item;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.pref.listview.MNAlarmPrefListItemType;
import com.yooiistudios.morningkit.common.bus.MNAlarmPrefBusProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 15.
 *
 * MNAlarmPrefListViewLabelItemMaker
 */
public class MNAlarmPrefLabelItemMaker {
    private static final String TAG = "MNAlarmPrefListViewLabelItemMaker";
    // REPEAT, LABEL, SOUND_TYPE, SOUND_NAME, SNOOZE, TIME;
    private MNAlarmPrefLabelItemMaker() { throw new AssertionError("You MUST NOT create this class"); }

    /**
     * Methods
     */
    public static View makeLabelItem(final Context context, ViewGroup parent, final MNAlarm alarm) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        LabelItemViewHolder viewHolder = new LabelItemViewHolder(convertView);
        convertView.setTag(viewHolder);

        // Check for default alarm
        viewHolder.titleTextView.setText(R.string.alarm_pref_label);
        if (alarm.getAlarmLabel().equals("Alarm")) {
            viewHolder.detailTextView.setText(R.string.alarm_default_label);
        } else {
            viewHolder.detailTextView.setText(alarm.getAlarmLabel());
        }

        // ClickListener
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout dialogLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_label_item_dialog, null);
                final LabelDialogLayoutHolder dialogLayoutHolder = new LabelDialogLayoutHolder(dialogLayout);

                // Label EditText
                if (alarm.getAlarmLabel().equals("Alarm")) {
                    dialogLayoutHolder.labelEditText.setText(R.string.alarm_default_label);
                } else {
                    dialogLayoutHolder.labelEditText.setText(alarm.getAlarmLabel());
                }
                dialogLayoutHolder.labelEditText.setSelection(dialogLayoutHolder.labelEditText.length());
                dialogLayoutHolder.labelEditText.setTag(MNAlarmPrefListItemType.LABEL);

                // Clear Button
                dialogLayoutHolder.clearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogLayoutHolder.labelEditText.setText("");
                    }
                });

                // AlertDialog
                final AlertDialog alertDialog =
                        new AlertDialog.Builder(context).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(TAG, "positivieButton");
                                MNAlarmPrefBusProvider.getInstance().post(dialogLayoutHolder.labelEditText);
                            }
                        }).setNegativeButton(R.string.cancel, null)
                        .create();
                alertDialog.setTitle(R.string.alarm_pref_label);
                alertDialog.setView(dialogLayout);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(dialogLayoutHolder.labelEditText, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                alertDialog.show();
            }
        });

        return convertView;
    }

    /**
     * ViewHolder
     */
    static class LabelItemViewHolder {
        @InjectView(R.id.alarm_pref_list_default_item_outer_layout)     RelativeLayout  outerLayout;
        @InjectView(R.id.alarm_pref_list_default_item_inner_layout)     RelativeLayout  innerLayout;
        @InjectView(R.id.alarm_pref_list_default_item_title_textview)   TextView        titleTextView;
        @InjectView(R.id.alarm_pref_list_default_item_detail_textview)  TextView        detailTextView;

        public LabelItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    /**
     * Dialog ViewHolder
     */
    public static class LabelDialogLayoutHolder {
        @Getter
        @InjectView(R.id.alarm_pref_label_dialog_editText)              EditText    labelEditText;
        @Getter @InjectView(R.id.alarm_pref_label_dialog_clear_button)  Button      clearButton;

        public LabelDialogLayoutHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
