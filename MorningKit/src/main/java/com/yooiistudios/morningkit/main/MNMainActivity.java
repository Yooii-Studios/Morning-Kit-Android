package com.yooiistudios.morningkit.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.otto.Subscribe;
import com.stevenkim.camera.SKCameraThemeView;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.wake.MNAlarmWake;
import com.yooiistudios.morningkit.common.bus.MNAlarmScrollViewBusProvider;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.review.MNReviewUtil;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.common.tutorial.MNTutorialLayout;
import com.yooiistudios.morningkit.common.tutorial.MNTutorialManager;
import com.yooiistudios.morningkit.common.validate.AppValidationChecker;
import com.yooiistudios.morningkit.main.layout.MNMainButtonLayout;
import com.yooiistudios.morningkit.main.layout.MNMainLayoutSetter;
import com.yooiistudios.morningkit.panel.core.MNPanel;
import com.yooiistudios.morningkit.setting.MNSettingActivity;
import com.yooiistudios.morningkit.setting.store.MNStoreActivity;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;
import com.yooiistudios.morningkit.theme.MNMainResources;
import com.yooiistudios.morningkit.theme.font.MNTranslucentFont;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
public class MNMainActivity extends Activity implements MNTutorialLayout.OnTutorialFinishListener {
    private static final String TAG = "MNMainActivity";

    @Getter @InjectView(R.id.main_container_layout)         RelativeLayout containerLayout;
    @Getter @InjectView(R.id.main_scroll_view)              ScrollView scrollView;
    @Getter @InjectView(R.id.main_scroll_content_layout)    LinearLayout scrollContentLayout;
    @Getter @InjectView(R.id.main_widget_window_layout)     MNPanelWindowLayout panelWindowLayout;
    @Getter @InjectView(R.id.main_alarm_list_view)          MNMainAlarmListView alarmListView;
    @Getter @InjectView(R.id.main_button_layout)            MNMainButtonLayout buttonLayout;
    @Getter @InjectView(R.id.main_refresh_imageview)        ImageView refreshImageView;
    @Getter @InjectView(R.id.main_setting_imageview)        ImageView settingImageView;
    @Getter @InjectView(R.id.main_admob_layout)             RelativeLayout admobLayout;
    @Getter @InjectView(R.id.adView) AdView adView;

    @Getter @InjectView(R.id.main_camera_layout)            RelativeLayout cameraLayout;
    SKCameraThemeView cameraThemeView;
    SKThemeImageView photoThemeImageView;

    @Getter @InjectView(R.id.main_dog_ear_image_view)       ImageView dogEarImageView;

    private int delayMillisec = 90;	// 알람이 삭제되는 딜레이

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 미리 정한 날짜가 지나면 앱이 죽게 변경, 출시시에 풀어야함, MNLog것을 써도 무방할듯
        if (MNLog.isDebug) {
            AppValidationChecker.validationCheck(this);
        }

        // 알람이 있을 경우는 화면을 켜주게 구현
        if (MNAlarmWake.isAlarmReserved(getIntent())) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        } else {
            // 켜진 알람이 없을 경우에는 항상 알람이 제대로 동작함을 보장하기 위해서 켜진 알람들은 다시 켜주기
            ArrayList<MNAlarm> alarmList = MNAlarmListManager.loadAlarmList(getApplicationContext());
            for (MNAlarm alarm : alarmList) {
                if (alarm.isAlarmOn()) {
                    alarm.startAlarmWithNoToast(getApplicationContext());
                }
            }
        }

        setContentView(R.layout.activity_main);

        initMainActivity();
        scrollView.smoothScrollTo(0, 0);

        // 튜토리얼 체크
        if (!MNTutorialManager.isTutorialShown(getApplicationContext())) {
            // 튜토리얼 전 세로고정 설정
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            MNTutorialLayout tutorialLayout = new MNTutorialLayout(getApplicationContext(), this);
            containerLayout.addView(tutorialLayout);
        }

        MNReviewUtil.checkRate(this);
    }

    void initMainActivity() {
        // 기존의 레거시 코드 대신에 이 한줄로 findViewById를 모두 대체
        ButterKnife.inject(this);

        // 위젯 윈도우
        panelWindowLayout.initWithPanelMatrix();

        // 알람
        alarmListView.initWithListAdapter();

        // 애드몹
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("TEST_DEVICE_ID")
                .build();
        adView.loadAd(adRequest);
//        adView = new AdView(this, AdSize.BANNER, MN.ads.ADMOB_ID);
//        admobLayout.addView(adView);
//        adView.loadAd(new AdRequest());

        // 알람 체크
        try {
            MNAlarmWake.checkReservedAlarm(getIntent(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        // Activity visible to user

        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MNLog.i(TAG, "onResume");

        int orientation = getResources().getConfiguration().orientation;

        // Activity visible to user
        MNAlarmScrollViewBusProvider.getInstance().register(this);

        // Alarm
        alarmListView.refreshListView();

        // 패널 교체 확인
        panelWindowLayout.checkPanelHadReplcaedAtSetting();

        // 테마와 관련된 작업 실행
        panelWindowLayout.applyTheme();

        // 액티비티가 resume될 경우 패널에서 필요한 처리 수행
        panelWindowLayout.onActivityResume();

        // 애드몹 레이아웃
        admobLayout.setBackgroundColor(Color.parseColor("#BB000000"));
        adView.resume();

        // 테마 적용
        processTheme();

        // 세팅 탭에서 돌아올 경우를 대비해 전체적인 레이아웃 최신화 적용
        onConfigurationChanged(getResources().getConfiguration());

        // 풀버전 구매 확인, 독이어 제거
        if (SKIabProducts.isIabProductBought(SKIabProducts.SKU_FULL_VERSION, getApplicationContext())) {
            dogEarImageView.setVisibility(View.GONE);
        } else {
            dogEarImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        // check photoImageView for preventing OOM
        if (photoThemeImageView != null) {
            photoThemeImageView.setReadyForRecycle(true);
        }

        // 새로 알람이 켜질 때 카메라 자원을 쓰고 있으면 crash가 나기에 보기에는 안좋아도 이렇게 처리.
        if (cameraThemeView != null) {
            cameraThemeView.setVisibility(View.INVISIBLE);
        }

        // 액티비티가 pause될 경우 패널에서 필요한 처리 수행
        panelWindowLayout.onActivityPause();

        // Partially visible
        adView.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        // Activity no longer visible
        MNAlarmScrollViewBusProvider.getInstance().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
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
    public void onConfigurationChanged(final Configuration newConfig) {
        if (MNTutorialManager.isTutorialShown(getApplicationContext())) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        super.onConfigurationChanged(newConfig);
        MNLog.i(TAG, "onConfigurationChanged");
        // 스크롤뷰
        MNMainLayoutSetter.adjustScrollViewLayoutParamsAtOrientation(scrollView, newConfig.orientation);

        // 패널윈도우 레이아웃
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                MNMainLayoutSetter.adjustPanelLayoutParamsAtOrientation(MNMainActivity.this, newConfig.orientation);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                // 가로로 돌아갈 경우 스테이터스바 높이를 잘못 읽어 뷰가 잘못 그려지던 현상 때문에 수정
                containerLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        MNMainLayoutSetter.adjustPanelLayoutParamsAtOrientation(MNMainActivity.this, newConfig.orientation);
                    }
                });
//                MNViewSizeMeasure.setViewSizeObserver(containerLayout, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
//                    @Override
//                    public void onLayoutLoad() {
//                        MNMainLayoutSetter.adjustPanelLayoutParamsAtOrientation(MNMainActivity.this, newConfig.orientation);
//                    }
//                });
                break;
        }
        // 버튼 레이아웃
        MNMainLayoutSetter.adjustButtonLayoutParamsAtOrientation(buttonLayout, newConfig.orientation);
        // 애드몹 레이아웃
        MNMainLayoutSetter.adjustAdmobLayoutParamsAtOrientation(admobLayout, newConfig.orientation);
        // 애드뷰 방향에 따라 위치 옮기기
        MNMainLayoutSetter.adjustAdmobViewAtOrientation(MNMainActivity.this, newConfig.orientation);
        // 애드몹 레이아웃 width 체크
        MNMainLayoutSetter.checkAdmobLayoutWidthAndAdjust(admobLayout, buttonLayout, newConfig.orientation);
        // 알람 리스트뷰
        MNMainLayoutSetter.adjustAlarmListView(MNMainActivity.this, newConfig.orientation);
        // 스크롤뷰 최상단으로 올려주기
        MNViewSizeMeasure.setViewSizeObserver(scrollView, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad() {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });
//        scrollView.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollView.fullScroll(View.FOCUS_UP);
//            }
//        });

        switch (MNTheme.getCurrentThemeType(this)) {
            case WATER_LILY:
                if (photoThemeImageView != null) {
                    photoThemeImageView.setWaterLilyImage(newConfig.orientation);
                }
                break;

            case PHOTO:
                try {
                    photoThemeImageView.setPhotoThemeImage(newConfig.orientation);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * OnClick
     */
    @OnClick(R.id.main_refresh_imageview) void refreshButtonClicked() {
        // 투명 테마 사용 시 폰트 색을 토글시키고 모든 패널 theme 적용
        MNTranslucentFont.toggleFontType(getApplicationContext());
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getApplicationContext());
        if (currentThemeType == MNThemeType.TRANQUILITY_BACK_CAMERA ||
                currentThemeType == MNThemeType.REFLECTION_FRONT_CAMERA ||
                currentThemeType == MNThemeType.PHOTO) {
            panelWindowLayout.applyTheme();
        }

        // 패널 & 알람 리프레시
        panelWindowLayout.refreshAllPanels();
        alarmListView.refreshListView();
    }

    @OnClick(R.id.main_setting_imageview) void settingButtonClicked() {
        startActivity(new Intent(MNMainActivity.this, MNSettingActivity.class));
    }

    @OnClick(R.id.main_dog_ear_image_view) void dogEarImageViewClicked() {
        startActivity(new Intent(MNMainActivity.this, MNStoreActivity.class));
    }

    /**
     * Admob
     */
    /*
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
    */

    /**
     * Panel
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MNPanel.PANEL_DETAIL_ACTIVITY && resultCode == RESULT_OK) {
            if (data.getBooleanExtra(MNPanel.PANEL_CHANGED, false)) {
                panelWindowLayout.replacePanel(data);
            } else {
                panelWindowLayout.refreshPanel(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    /**
     * Theme
     */
    private void processTheme() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(this);

        // Layout
        switch (currentThemeType) {
            case WATER_LILY:
                if (cameraThemeView != null) {
                    cameraLayout.removeAllViews();
                    cameraThemeView = null;
                }
                if (photoThemeImageView == null) {
                    photoThemeImageView = new SKThemeImageView(this);
                    containerLayout.addView(photoThemeImageView, 0);
                    containerLayout.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    photoThemeImageView.clear();
                }
                break;

            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
                containerLayout.removeView(photoThemeImageView);
                if (photoThemeImageView != null) {
                    photoThemeImageView.clear();
                    photoThemeImageView = null;
                }

                // 기존에 생성이 되었다면 방향만 변경
                synchronized (this) {
                    if (cameraThemeView != null) {
                        cameraThemeView.setVisibility(View.GONE);
                        if (currentThemeType == MNThemeType.TRANQUILITY_BACK_CAMERA) {
                            cameraThemeView.setCameraFacingInfo(Camera.CameraInfo.CAMERA_FACING_BACK);
                        } else {
                            cameraThemeView.setCameraFacingInfo(Camera.CameraInfo.CAMERA_FACING_FRONT);
                        }
                        cameraThemeView.setVisibility(View.VISIBLE);
                    }else{
                        if (currentThemeType == MNThemeType.TRANQUILITY_BACK_CAMERA) {
                            cameraThemeView = new SKCameraThemeView(this, Camera.CameraInfo.CAMERA_FACING_BACK);
                        } else {
                            cameraThemeView = new SKCameraThemeView(this, Camera.CameraInfo.CAMERA_FACING_FRONT);
                        }
                        containerLayout.setBackgroundColor(Color.TRANSPARENT);
                        cameraLayout.addView(cameraThemeView);
                    }
                }
                break;

            case PHOTO:
                if (photoThemeImageView == null) {
                    if (photoThemeImageView == null) {
                        photoThemeImageView = new SKThemeImageView(this);
                        containerLayout.addView(photoThemeImageView, 0);
                        containerLayout.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        photoThemeImageView.clear();
                    }
                }
                if (cameraThemeView != null) {
                    cameraLayout.removeAllViews();
                    cameraThemeView = null;
                }
                break;

            case MODERNITY_WHITE:
            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
            case PASTEL_GREEN:
                containerLayout.removeView(photoThemeImageView);
                if (photoThemeImageView != null) {
                    photoThemeImageView.clear();
                    photoThemeImageView = null;
                }
                if (cameraThemeView != null) {
                    cameraLayout.removeAllViews();
                    cameraThemeView = null;
                }
                containerLayout.setBackgroundColor(MNMainColors.getBackwardBackgroundColor(
                        currentThemeType));
                break;
        }

        // Button Layout & Admob Layout
        refreshImageView.setImageResource(MNMainResources.getRefreshButtonSelectorResourceId(currentThemeType));
        settingImageView.setImageResource(MNMainResources.getSettingButtonSelectorResourceId(currentThemeType));
        GradientDrawable buttonShape = (GradientDrawable) buttonLayout.getBackground();
        if (buttonShape != null) {
            buttonShape.setColor(MNMainColors.getButtonLayoutBackgroundColor(currentThemeType));
        }
        if (admobLayout != null) {
            admobLayout.setBackgroundColor(MNMainColors.getButtonLayoutBackgroundColor(currentThemeType));
        }
    }

    @Override
    public void onFinishTutorial() {
        MNTutorialManager.setTutorialShown(getApplicationContext());
        // 튜토리얼 후 회전 가능하게 방향 설정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
}
