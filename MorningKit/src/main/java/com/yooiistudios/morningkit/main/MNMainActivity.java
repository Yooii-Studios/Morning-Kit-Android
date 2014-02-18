package com.yooiistudios.morningkit.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.squareup.otto.Subscribe;
import com.urqa.clientinterface.URQAController;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.wake.MNAlarmWake;
import com.yooiistudios.morningkit.common.bus.MNAlarmScrollViewBusProvider;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.main.layout.MNMainLayoutSetter;
import com.yooiistudios.morningkit.setting.MNSettingActivity;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import lombok.Getter;

/**
 * Created by Steven Kim on 13. 10. 22..
 *
 * MNMainActivity
 *  앱에서 가장 중요한 메인 액티비티
 */
public class MNMainActivity extends Activity implements AdListener
{
    private static final String TAG = "MNMainActivity";

    @Getter @InjectView(R.id.main_container_layout) RelativeLayout containerLayout;
    @Getter @InjectView(R.id.main_scroll_view) MNMainScrollView scrollView;
    @Getter @InjectView(R.id.main_scroll_content_layout) LinearLayout scrollContentLayout;
    @Getter @InjectView(R.id.main_widget_window_layout) MNPanelWindowLayout panelWindowLayout;
    @Getter @InjectView(R.id.main_alarm_list_view) MNMainAlarmListView alarmListView;
    @Getter @InjectView(R.id.main_button_layout) RelativeLayout buttonLayout;
    @Getter @InjectView(R.id.main_refresh_imageview) ImageView refreshImageView;
    @Getter @InjectView(R.id.main_refresh_imageview) ImageView settingImageView;
    @Getter @InjectView(R.id.main_admob_layout) RelativeLayout admobLayout;
    @Getter @InjectView(R.id.adView) AdView adView;

    private int delayMillisec = 90;	// 알람이 삭제되는 딜레이

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 알람이 있을 경우는 화면을 켜주게 구현
        if (MNAlarmWake.isAlarmReserved(getIntent())) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

        setContentView(R.layout.activity_main);

        // UrQA 라이브러리 추가
        URQAController.InitializeAndStartSession(getApplicationContext(), String.valueOf(72369777));
        initMainActivity();
        scrollView.smoothScrollTo(0, 0);
    }

    void initMainActivity() {
        // 기존의 레거시 코드 대신에 이 한줄로 findViewById를 모두 대체
        ButterKnife.inject(this);

//        // 특정 날짜 이후로 앱이 죽게 폭탄 설치 - 현재는 주석 처리
//        AppValidationChecker.validationCheck(this);

        // 위젯 윈도우
        panelWindowLayout.initWithWidgetMatrix();

        // 알람
        alarmListView.initWithListAdapter();

        // 애드몹
//        adView = new AdView(this, AdSize.BANNER, MN.ads.ADMOB_ID);
//        admobLayout.addView(adView);
//        adView.loadAd(new AdRequest());


        // 이전 버전
        // 먼저 세로 모드에서 로딩하고 가로로 돌린다.
        /*
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Handler pauser = new Handler();
        pauser.postDelayed(new Runnable() {
            public void run() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        }, 100);
        */

        // 최초 실행시는 회전 감지를 안하기에, 명시적으로 최초 한번은 해줌
        onConfigurationChanged(getResources().getConfiguration());

        // 알람 체크
        try {
            MNAlarmWake.checkReservedAlarm(getIntent(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart()
    {
        // Activity visible to user

        super.onStart();
    }

    @Override
    protected void onResume() {
        // Activity visible to user
        MNAlarmScrollViewBusProvider.getInstance().register(this);

        // Alarm
        alarmListView.refreshListView();
        MNMainLayoutSetter.adjustAlarmListView(this, getResources().getConfiguration().orientation);

//        onConfigurationChanged(getResources().getConfiguration());

        // 테마와 관련된 작업 실행
//        containerLayout.setBackgroundColor(Color.WHITE);
        panelWindowLayout.applyTheme();

        // 버튼 레이아웃
        GradientDrawable buttonShape = (GradientDrawable) buttonLayout.getBackground();
        if (buttonShape != null) {
            buttonShape.setColor(Color.parseColor("#BB000000"));
        }
        // 애드몹 레이아웃
        admobLayout.setBackgroundColor(Color.parseColor("#BB000000"));
        super.onResume();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onPause()
    {
        // Partially visible

        super.onPause();
    }

    @Override
    protected void onStop()
    {
        // Activity no longer visible
        MNAlarmScrollViewBusProvider.getInstance().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        // Acitivity is destroyed
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    /**
     * Rotation
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        MNLog.i(TAG, "onConfigurationChanged");
        final Configuration config = newConfig;
        containerLayout.post(new Runnable() {
            @Override
            public void run() {
                // 스크롤뷰
                MNMainLayoutSetter.adjustScrollViewLayoutParamsAtOrientation(scrollView, config.orientation);
                // 위젯윈도우 레이아웃
                MNMainLayoutSetter.adjustPanelLayoutParamsAtOrientation(MNMainActivity.this, config.orientation);
                // 버튼 레이아웃
                MNMainLayoutSetter.adjustButtonLayoutParamsAtOrientation(buttonLayout, config.orientation);
                // 애드몹 레이아웃
                MNMainLayoutSetter.adjustAdmobLayoutParamsAtOrientation(admobLayout, config.orientation);
                // 애드뷰 방향에 따라 위치 옮기기
                MNMainLayoutSetter.adjustAdmobViewAtOrientation(MNMainActivity.this, config.orientation);
                // 애드몹 레이아웃 width 체크
                MNMainLayoutSetter.checkAdmobLayoutWidthAndAdjust(admobLayout, buttonLayout, config.orientation);
                // 알람 리스트뷰
                MNMainLayoutSetter.adjustAlarmListView(MNMainActivity.this, config.orientation);
                // 스크롤컨텐트 레이아웃 높이 조절 - 위젯 윈도우만 조절해주어도 됨
//                MNMainLayoutSetter.adjustScrollContentLayoutHeight(MNMainActivity.this, config.orientation);

//                Log.i(TAG, "status bar height: " + MNDeviceSizeInfo.getStatusBarHeight(MNMainActivity.this));
//                Log.i(TAG, "Device height: " + MNDeviceSizeInfo.getDeviceHeight(MNMainActivity.this));
//                Log.i(TAG, "scrollView height: " + scrollView.getHeight());
//                Log.i(TAG, "scrollViewContent height: " + scrollContentLayout.getHeight());
//                Log.i(TAG, "buttonLayout height: " + buttonLayout.getHeight());
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick(R.id.main_refresh_imageview) void refreshButtonClicked() {
        MNLog.i(TAG, "refreshButtonClicked");

        // 기획 변경 -> 리프레시 버튼 누르면 2초 동안은 동작하지 않음
        panelWindowLayout.refreshAllPanels();
    }

    @OnClick(R.id.main_setting_imageview) void settingButtonClicked() {
        startActivity(new Intent(MNMainActivity.this, MNSettingActivity.class));
    }

    /**
     * Admob
     */
    @Override
    public void onDismissScreen(Ad arg0) {
    }

    @Override
    public void onFailedToReceiveAd(Ad arg0, AdRequest.ErrorCode arg1) {
        // Log.i(TAG, "failed to receive ad (" + arg1 + ")");
    }

    @Override
    public void onLeaveApplication(Ad arg0) {
    }

    @Override
    public void onPresentScreen(Ad arg0) {
    }

    @Override
    public void onReceiveAd(Ad arg0) {
    }

    /**
     * MNAlarmItemScrollView
     */
    @Subscribe
    public void removeAlarmById(final MNAlarm alarm) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delayMillisec);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alarm.stopAlarm(MNMainActivity.this);
                            MNAlarmListManager.removeAlarmFromAlarmList(alarm.getAlarmId(), MNMainActivity.this);
                            try {
                                MNAlarmListManager.saveAlarmList(MNMainActivity.this);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            (MNMainActivity.this).getAlarmListView().refreshListView();
                            MNMainLayoutSetter.adjustAlarmListView(MNMainActivity.this, getResources().getConfiguration().orientation);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
