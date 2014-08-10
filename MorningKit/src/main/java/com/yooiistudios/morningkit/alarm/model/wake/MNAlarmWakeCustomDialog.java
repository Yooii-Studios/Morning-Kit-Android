package com.yooiistudios.morningkit.alarm.model.wake;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private MNAlarmWakeCustomDialog() {
        throw new AssertionError("You MUST NOT create this class!");
    }

    public static void show(MNAlarm alarm, Context context) {
        AlertDialog wakeDialog = makeWakeAlertDialog(alarm, context);
        if (wakeDialog != null) {
            wakeDialog.show();

            // 5분 후 dismiss 자동으로 되게 구현
            Message msg = Message.obtain(alarmWakeDialogHandler, alarm.getAlarmId(), wakeDialog);
//            alarmWakeDialogHandler.sendMessageDelayed(msg, 4 * 1 * 1000); // for test
            alarmWakeDialogHandler.sendMessageDelayed(msg, 5 * 60 * 1000);
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
                    alarmWakeCustomView.layout.setAnimationCacheEnabled(true);
                    alarmWakeCustomView.layout.setDrawingCacheEnabled(true);
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
                    // 5분 후 자동으로 꺼지는 핸들러 취소
                    alarmWakeDialogHandler.removeMessages(alarm.getAlarmId());

                    wakeDialog.dismiss();
                    alarmWakeCustomView.alarmImageView.clearAnimation();
                    SKAlarmSoundPlayer.stop();

                    // stop vibrator
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.cancel();

                    MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarm.getAlarmId(), context);
                    if (targetAlarm != null) {
                        targetAlarm.stopAlarm(context);
                        if (targetAlarm.isRepeatOn()) {
                            targetAlarm.startAlarm(context);
                        }
                        try {
                            MNAlarmListManager.saveAlarmList(context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    MNAlarmScrollViewBusProvider.getInstance().post(context);       // 리스트 어댑터, UI 갱신
                    MNAlarmScrollViewBusProvider.getInstance().post(wakeDialog);    // 메인, SCREEN_ON 해제
                }
            });
            // 다시 알림 옵션이 켜져 있으면 보여 주고, 없으면 알람 끄기 버튼만 보여주기
            if (alarm.isSnoozeOn()) {
                alarmWakeCustomView.buttonMiddleDivider.setVisibility(View.VISIBLE);
                alarmWakeCustomView.snoozeTextView.setVisibility(View.VISIBLE);
                alarmWakeCustomView.snoozeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 5분 후 자동으로 꺼지는 핸들러 취소
                        alarmWakeDialogHandler.removeMessages(alarm.getAlarmId());

                        wakeDialog.dismiss();
                        alarmWakeCustomView.alarmImageView.clearAnimation();
                        SKAlarmSoundPlayer.stop();

                        // stop vibrator
                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.cancel();

                        MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarm.getAlarmId(), context);
                        targetAlarm.snoozeAlarm(context);

                        MNAlarmScrollViewBusProvider.getInstance().post(context);
                        MNAlarmScrollViewBusProvider.getInstance().post(wakeDialog);
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
        @InjectView(R.id.alarm_wake_custom_dialog_layout)                   RelativeLayout layout;
        @InjectView(R.id.alarm_wake_custom_dialog_image_view)               ImageView alarmImageView;
        @InjectView(R.id.alarm_wake_custom_dialog_label_text_view)          TextView labelTextView;
        @InjectView(R.id.alarm_wake_custom_dialog_time_text_view)           TextView timeTextView;
        @InjectView(R.id.alarm_wake_custom_dialog_dismiss_textview)         TextView dismissTextView;
        @InjectView(R.id.alarm_wake_custom_dialog_button_middle_divider)    View buttonMiddleDivider;
        @InjectView(R.id.alarm_wake_custom_dialog_snooze_textview)          TextView snoozeTextView;

        public MNAlarmWakeCustomView(View view) {
            ButterKnife.inject(this, view);
        }
    }

    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    // 5분 동안 반응이 없으면 강제로 dismiss 시키기
    private static MNAlarmWakeDialogHandler alarmWakeDialogHandler = new MNAlarmWakeDialogHandler();

    private static class MNAlarmWakeDialogHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // get values
            int alarmId = msg.what;
            AlertDialog wakeDialog = (AlertDialog) msg.obj;

            if (wakeDialog != null) {
                Context context = wakeDialog.getContext();

                if (context != null) {
                    // stop vibrator
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null) {
                        vibrator.cancel();
                    }

                    // manipulate target alarm
                    MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarmId, context);
                    if (targetAlarm != null) {
                        targetAlarm.stopAlarm(context);
                        if (targetAlarm.isRepeatOn()) {
                            targetAlarm.startAlarm(context);
                        }

                        // save alarm
                        try {
                            MNAlarmListManager.saveAlarmList(context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    // bus message
                    MNAlarmScrollViewBusProvider.getInstance().post(context);       // 리스트 어댑터, UI 갱신
                    MNAlarmScrollViewBusProvider.getInstance().post(wakeDialog);    // 메인, SCREEN_ON 해제
                }

                // 따라서 모델부터 처리 후 UI를 제일 마지막에 처리
                // stop alarm sound
                SKAlarmSoundPlayer.stop();

                // clear animation and wakeDialog
                if (wakeDialog != null && wakeDialog.isShowing()) {
                    try {
                        ImageView alarmImageView =
                                (ImageView) wakeDialog.findViewById(R.id.alarm_wake_custom_dialog_image_view);
                        alarmImageView.clearAnimation();
                        wakeDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}