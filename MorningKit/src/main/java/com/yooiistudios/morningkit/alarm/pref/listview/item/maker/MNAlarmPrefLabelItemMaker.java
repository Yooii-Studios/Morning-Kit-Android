package com.yooiistudios.morningkit.alarm.pref.listview.item.maker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private MNAlarmPrefLabelItemMaker() { throw new AssertionError("You MUST NOT create this class"); }

    /**
     * Methods
     */
    public static View makeLabelItem(final Context context, ViewGroup parent, final MNAlarm alarm) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        LabelItemViewHolder viewHolder = new LabelItemViewHolder(convertView);
        convertView.setTag(viewHolder);

        // Check for default alarm label
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
                final LabelDialogLayoutHolder dialogLayoutHolder = new LabelDialogLayoutHolder(dialogLayout, alarm);

                // AlertDialog
                AlertDialog alertDialog = makeLabelAlertDialog(context, dialogLayout, dialogLayoutHolder);
                alertDialog.show();
            }
        });
        return convertView;
    }

    public static AlertDialog makeLabelAlertDialog(final Context context, FrameLayout dialogLayout, final LabelDialogLayoutHolder dialogLayoutHolder) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // to MNAlarmPreferecnceListAdapter
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
        return alertDialog;
    }

    /**
     * ViewHolder
     */
    public static class LabelItemViewHolder extends MNAlarmPrefItemMaker.MNAlarmPrefDefaultItemViewHolder {
        public LabelItemViewHolder(View view) {
            super(view);
        }
    }

    /**
     * Dialog ViewHolder
     */
    public static class LabelDialogLayoutHolder {
        @Getter @InjectView(R.id.alarm_pref_label_dialog_editText)      EditText    labelEditText;
        @Getter @InjectView(R.id.alarm_pref_label_dialog_clear_button)  Button      clearButton;

        public LabelDialogLayoutHolder(View view, MNAlarm alarm) {
            ButterKnife.inject(this, view);

            // Label EditText
            if (alarm.getAlarmLabel().equals("Alarm")) {
                labelEditText.setText(R.string.alarm_default_label);
            } else {
                labelEditText.setText(alarm.getAlarmLabel());
            }
            labelEditText.setSelection(labelEditText.length());
            labelEditText.setTag(MNAlarmPrefListItemType.LABEL);

            // Clear Button
            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    labelEditText.setText("");
                }
            });
        }
    }
}
