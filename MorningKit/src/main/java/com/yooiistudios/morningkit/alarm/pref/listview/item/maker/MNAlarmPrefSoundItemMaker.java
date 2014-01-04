package com.yooiistudios.morningkit.alarm.pref.listview.item.maker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.common.bus.MNAlarmPrefBusProvider;
import com.yooiistudios.stevenkim.alarmsound.OnAlarmSoundClickListener;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundDialog;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 17.
 *
 * MNAlarmPrefSoundItemMaker
 */
public class MNAlarmPrefSoundItemMaker {

    private static final String TAG = "MNAlarmPrefRepeatItemMaker";
    private MNAlarmPrefSoundItemMaker() { throw new AssertionError("You MUST NOT create this class"); }

    // ringotns, music, app music, none
    public static View makeSoundItem(final Context context, ViewGroup parent, final MNAlarm alarm,
                                     final OnAlarmSoundClickListener alarmSoundClickListener) {
        final View convertView =
                LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);

        SoundItemViewHolder viewHolder = new SoundItemViewHolder(convertView);
        convertView.setTag(viewHolder);
        viewHolder.titleTextView.setText(R.string.alarm_pref_sound_type);
        viewHolder.detailTextView.setText(alarm.getAlarmSound().getSoundTitle());
        viewHolder.detailTextView.setSelected(true);

        // ClickListener
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ActionBar Menu
                MNAlarmPrefBusProvider.getInstance().post(convertView);

                // from SKAlarmSound
                SKAlarmSoundDialog.makeSoundDialog(context, alarm.getAlarmSound(),
                        alarmSoundClickListener).show();
            }
        });

        return convertView;
    }

    public static class SoundItemViewHolder extends MNAlarmPrefItemMaker.MNAlarmPrefDefaultItemViewHolder {
        public SoundItemViewHolder(View view) {
            super(view);
        }
    }
}
