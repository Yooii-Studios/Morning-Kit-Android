package com.yooiistudios.morningkit.alarm.pref.listview.item.maker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.common.bus.MNAlarmPrefBusProvider;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 17.
 *
 * MNAlarmPrefSoundItemMaker
 */
public class MNAlarmPrefSoundItemMaker {

    private static final String TAG = "MNAlarmPrefRepeatItemMaker";
    private MNAlarmPrefSoundItemMaker() { throw new AssertionError("You MUST NOT create this class"); }

    // ringotns, music, app music, none
    public static View makeSoundItem(final Context context, ViewGroup parent, final MNAlarm alarm) {
        final View convertView = LayoutInflater.from(context).inflate(R.layout.alarm_pref_list_default_item, parent, false);
        SoundItemViewHolder viewHolder = new SoundItemViewHolder(convertView);
        convertView.setTag(viewHolder);
        viewHolder.titleTextView.setText(R.string.alarm_pref_sound_type);

        // 기본 사운드 소스 - 임시
        String soundSource = "content://settings/system/ringtone";
        Uri ringtoneSource = Uri.parse(soundSource);
        if (!ringtoneSource.toString().isEmpty()) {
            Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneSource);
//            soundType = MN.alarm.ALARM_SOUNDTYPE_RINGTONE;
//            soundName = ringtone.getTitle(context);
            if (ringtone != null) {
                viewHolder.detailTextView.setText(ringtone.getTitle(context));
            } else {
                Log.e(TAG, "unexpected situation! there must be default ringtone!");
                viewHolder.detailTextView.setText(R.string.alarm_sound_string_ringtones);
            }
        }
        viewHolder.detailTextView.setSelected(true);

        // ClickListener
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ActionBar Menu
                MNAlarmPrefBusProvider.getInstance().post(convertView);

                // AlertDialog
                AlertDialog alertDialog = makeSoundAlertDialog(context, alarm);
                alertDialog.show();
            }
        });

        return convertView;
    }

    public static AlertDialog makeSoundAlertDialog(final Context context, final MNAlarm alarm) {
        // SingleChoiceItems
        final String[] soundTypes = new String[] {
                context.getString(R.string.alarm_sound_string_none),
                context.getString(R.string.alarm_sound_string_ringtones),
                context.getString(R.string.alarm_sound_string_music),
                context.getString(R.string.alarm_sound_string_app_music),
        };

        // Builder for each version
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        // Build AlertDialog
        AlertDialog alertDialog = builder.setSingleChoiceItems(soundTypes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Log.i(TAG, "sound_none");
                        MNAlarmPrefBusProvider.getInstance().post(context);
                        break;
                    case 1:
                        Log.i(TAG, "sound_ringtones");
                        AlertDialog ringtoneDialog = makeRingtoneDialog(context, alarm);
                        ringtoneDialog.show();
                        break;
                    case 2:
                        Log.i(TAG, "sound_music");
                        AlertDialog musicDialog = makeMusicDialog(context, alarm);
                        musicDialog.show();
                        break;
                    case 3:
                        Log.i(TAG, "sound_app_music");
                        AlertDialog appMusicDialog = makeAppMusicDialog(context, alarm);
                        appMusicDialog.show();
                        break;
                }
                dialog.dismiss();
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
        alertDialog.setTitle(R.string.alarm_pref_sound_type);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    public static AlertDialog makeRingtoneDialog(final Context context, final MNAlarm alarm) {
        return null;
    }

    public static AlertDialog makeMusicDialog(final Context context, final MNAlarm alarm) {
        return null;
    }

    public static AlertDialog makeAppMusicDialog(final Context context, final MNAlarm alarm) {
        return null;
    }

    public static class SoundItemViewHolder extends MNAlarmPrefItemMaker.MNAlarmPrefDefaultItemViewHolder {
        public SoundItemViewHolder(View view) {
            super(view);
        }
    }
}
