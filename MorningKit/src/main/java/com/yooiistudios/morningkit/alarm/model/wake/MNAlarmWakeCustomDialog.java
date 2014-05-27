package com.yooiistudios.morningkit.alarm.model.wake;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.common.bus.MNAlarmScrollViewBusProvider;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundPlayer;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 20.
 *
 * MNAlarmWakeDialog
 *  알람이 울릴 때 보여 줄 다이얼로그를 생성
 */
public class MNAlarmWakeCustomDialog {
    private MNAlarmWakeCustomDialog() { throw new AssertionError("You MUST NOT create this class!"); }

    public static void show(MNAlarm alarm, Context context) {
        AlertDialog wakeDialog = makeWakeAlertDialog(alarm, context);
        if (wakeDialog != null) {
            wakeDialog.show();
        }
    }

    protected static AlertDialog makeWakeAlertDialog(final MNAlarm alarm, final Context context) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        // Custom View 추가
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.alarm_wake_custom_dialog, null);

        if (customView != null) {
            final AlertDialog wakeDialog = builder.create();
            wakeDialog.setView(customView, 0, 0, 0, 0);

            final MNAlarmWakeCustomView alarmWakeCustomView = new MNAlarmWakeCustomView(customView);

            // 애니메이션
            if (context.getApplicationContext() != null) {
                Animation vibrateAnimation = AnimationUtils.loadAnimation(context.getApplicationContext(),
                        R.anim.alarm_vibrate);
                if (vibrateAnimation != null) {
                    alarmWakeCustomView.alarmImageView.startAnimation(vibrateAnimation);
                }
            }

            // 레이블
            alarmWakeCustomView.labelTextView.setText(alarm.getAlarmLabel());
            // 시간 - 현재 시간 표시
            SimpleDateFormat simpleDateFormat;
            if (DateFormat.is24HourFormat(context)) {
                simpleDateFormat = new SimpleDateFormat("HH:mm");
            } else {
                simpleDateFormat = new SimpleDateFormat("hh:mm a");
            }
            alarmWakeCustomView.timeTextView.setText(
                    simpleDateFormat.format(DateTime.now().toDate()));
            // 클릭 리스너
            alarmWakeCustomView.dismissTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wakeDialog.dismiss();
                    alarmWakeCustomView.alarmImageView.clearAnimation();
                    SKAlarmSoundPlayer.stop();

                    MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarm.getAlarmId(), context);
                    targetAlarm.stopAlarm(context);
                    if (alarm.isRepeatOn()) {
                        targetAlarm.startAlarm(context);
                    }
                    try {
                        MNAlarmListManager.saveAlarmList(context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MNAlarmScrollViewBusProvider.getInstance().post(context);
                }
            });
            // 다시 알림 옵션이 켜져 있으면 보여 주고, 없으면 알람 끄기 버튼만 보여주기
            if (alarm.isSnoozeOn()) {
                alarmWakeCustomView.buttonMiddleDivider.setVisibility(View.VISIBLE);
                alarmWakeCustomView.snoozeTextView.setVisibility(View.VISIBLE);
                alarmWakeCustomView.snoozeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wakeDialog.dismiss();
                        alarmWakeCustomView.alarmImageView.clearAnimation();
                        SKAlarmSoundPlayer.stop();

                        MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarm.getAlarmId(), context);
                        targetAlarm.snoozeAlarm(context);
                    }
                });
            } else {
                alarmWakeCustomView.buttonMiddleDivider.setVisibility(View.GONE);
                alarmWakeCustomView.snoozeTextView.setVisibility(View.GONE);
                alarmWakeCustomView.snoozeTextView.setOnClickListener(null);
            }

            // 기타 필요한 설정
            wakeDialog.setCancelable(false);
            wakeDialog.setCanceledOnTouchOutside(false);
            return wakeDialog;
        }
        return null;
    }

    static class MNAlarmWakeCustomView {
        @InjectView(R.id.alarm_wake_custom_dialog_image_view)               ImageView   alarmImageView;
        @InjectView(R.id.alarm_wake_custom_dialog_label_text_view)          TextView    labelTextView;
        @InjectView(R.id.alarm_wake_custom_dialog_time_text_view)           TextView    timeTextView;
        @InjectView(R.id.alarm_wake_custom_dialog_dismiss_textview)         TextView    dismissTextView;
        @InjectView(R.id.alarm_wake_custom_dialog_button_middle_divider)    View        buttonMiddleDivider;
        @InjectView(R.id.alarm_wake_custom_dialog_snooze_textview)          TextView    snoozeTextView;

        public MNAlarmWakeCustomView(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
