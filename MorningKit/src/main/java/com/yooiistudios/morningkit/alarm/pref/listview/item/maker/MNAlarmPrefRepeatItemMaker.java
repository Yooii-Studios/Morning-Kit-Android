package com.yooiistudios.morningkit.alarm.pref.listview.item.maker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;

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

    public static View makeRepeatItem(Context context, ViewGroup parent, final MNAlarm alarm) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        RepeatItemViewHolder viewHolder = new RepeatItemViewHolder(convertView);
        convertView.setTag(viewHolder);
        viewHolder.titleTextView.setText(R.string.alarm_pref_repeat);

        return convertView;
    }

    static class RepeatItemViewHolder extends MNAlarmPrefItemMaker.MNAlarmPrefDefaultItemViewHolder {
        public RepeatItemViewHolder(View view) {
            super(view);
        }
    }
}
