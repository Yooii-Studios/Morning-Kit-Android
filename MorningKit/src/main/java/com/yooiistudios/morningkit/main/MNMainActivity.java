package com.yooiistudios.morningkit.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.inmobi.commons.InMobi;
import com.squareup.otto.Subscribe;
import com.stevenkim.camera.SKCameraThemeView;
import com.yooiistudios.morningkit.MNApplication;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.alarm.model.wake.MNAlarmWakeUtils;
import com.yooiistudios.morningkit.common.ad.AdUtils;
import com.yooiistudios.morningkit.common.ad.QuitAdDialogFactory;
import com.yooiistudios.morningkit.common.analytic.MNAnalyticsUtils;
import com.yooiistudios.morningkit.common.bus.MNAlarmScrollViewBusProvider;
import com.yooiistudios.morningkit.common.locale.MNLocaleUtils;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.network.InternetConnectionManager;
import com.yooiistudios.morningkit.common.review.MNReviewUtil;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.common.tutorial.MNTutorialLayout;
import com.yooiistudios.morningkit.common.tutorial.MNTutorialManager;
import com.yooiistudios.morningkit.common.validate.AppValidationChecker;
import com.yooiistudios.morningkit.main.layout.MNMainButtonLayout;
import com.yooiistudios.morningkit.main.layout.MNMainLayoutSetter;
import com.yooiistudios.morningkit.panel.core.MNPanel;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.core.MNPanelType;
import com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout;
import com.yooiistudios.morningkit.setting.MNSettingActivity;
import com.yooiistudios.morningkit.setting.store.MNStoreActivity;
import com.yooiistudios.morningkit.setting.store.MNStoreFragment;
import com.yooiistudios.morningkit.setting.store.iab.SKIabManager;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrix;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrixType;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;
import com.yooiistudios.morningkit.theme.MNMainResources;
import com.yooiistudios.morningkit.theme.font.MNTranslucentFont;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public static final String TAG = "MainActivity";

    @Getter @InjectView(R.id.main_container_layout)         RelativeLayout containerLayout;
    @Getter @InjectView(R.id.main_scroll_view)              ScrollView scrollView;
//    @Getter @InjectView(R.id.main_scroll_content_layout)    LinearLayout scrollContentLayout;
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

    // Quit Ad Dialog
    private AdRequest quitAdRequest;
    private AdView quitMediumAdView;
    private AdView quitLargeBannerAdView;

    private int delayMillisec = 90;	// 알람이 삭제되는 딜레이

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 미리 정한 날짜가 지나면 앱이 죽게 변경, 출시에 풀어야함
        if (MNLog.isDebug) {
            AppValidationChecker.validationCheck(this);
        }

        // 강제종료되고 난 후의 실행이 아닌지 && 알람이 있는지 체크
        // OS에 의해 강제종료가 된 후의 실행은 이전의 getIntent()를 받기 때문에 예외처리가 필요
        if (savedInstanceState == null && MNAlarmWakeUtils.isAlarmReservedByIntent(getIntent())) {
            turnOnScreen();
            try {
                MNAlarmWakeUtils.invokeAlarm(this, getIntent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 알람이 울리지 않는다면 리뷰와 전면광고 카운트 체크
            MNReviewUtil.showReviewDialogIfConditionMet(this);
            AdUtils.showPopupAdIfSatisfied(this);
        }
        initIab();

        setContentView(R.layout.activity_main);

        initMainActivity();
        scrollView.smoothScrollTo(0, 0);

        // 튜토리얼 체크
        if (!MNTutorialManager.isTutorialShown(getApplicationContext())) {
            // 네이버 앱스토어에서는 현재 날씨 사용부터 먼저 묻기(반려 사유)
            if (MNStoreFragment.IS_STORE_FOR_NAVER) {
                askUsingCurrentLocationDialog(); // 확인/취소하고 튜토리얼 시작
            } else {
                showTutorialLayout();
            }
        }

        // 플러리, 구글 애널리틱스, 인모비
        sendFlurryAnalytics();
        MNAnalyticsUtils.startAnalytics((MNApplication) getApplication(), TAG);
        InMobi.initialize(this, "2fda5c20a0054c43a454c8027bf2eb83");
    }

    private void initIab() {
        // 구매목록을 항상 새로 확인 - 구글일 경우에만
        if (MNStoreFragment.IS_STORE_FOR_NAVER) {

        } else {
            try {
                SKIabManager iabManager = new SKIabManager(this, null);
                iabManager.loadWithAllItems();
            } catch (Exception ignored) { }
        }
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
//                .addTestDevice("D9XXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
                .build();
        adView.loadAd(adRequest);

        // 애드몹 - Quit Dialog
        quitAdRequest = new AdRequest.Builder().build();
        quitMediumAdView = QuitAdDialogFactory.initAdView(this, AdSize.MEDIUM_RECTANGLE,
                quitAdRequest);
        quitLargeBannerAdView = QuitAdDialogFactory.initAdView(this, AdSize.LARGE_BANNER,
                quitAdRequest);
    }

    private void turnOnScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        adView.resume();

        // 테마 적용
        processTheme();

        // 세팅 탭에서 돌아올 경우를 대비해 전체적인 레이아웃 최신화 적용
        onConfigurationChanged(getResources().getConfiguration());

        // 풀버전 구매 확인, 독이어 제거
        if (SKIabProducts.containsSku(SKIabProducts.SKU_FULL_VERSION, getApplicationContext())) {
            dogEarImageView.setVisibility(View.GONE);
        } else {
            dogEarImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        // onPause 맨 뒤에서 맨 앞으로 보냄 - 시스템이 먼저 잘 처리했으면 함
        super.onPause();

        // onStop에서 onPause로 옮겨옴 - onResume에서 register하는 것과 발을 맞추자
        MNAlarmScrollViewBusProvider.getInstance().unregister(this);

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
    }

    @Override
    protected void onStart() {
        // Activity visible to user
        super.onStart();
        FlurryAgent.onStartSession(this, MNFlurry.KEY);
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // Activity no longer visible
        super.onStop();
        FlurryAgent.onEndSession(this);
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onDestroy() {
        // Activity is destroyed
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

        // 튜토리얼 체크
        if (MNTutorialManager.isTutorialShown(getApplicationContext())) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        super.onConfigurationChanged(newConfig);

        // 회전마다 Locale 을 새로 적용해줌(언어가 바뀌어 버리는 문제 해결)
        MNLocaleUtils.updateLocale(this);

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
        MNAnalyticsUtils.trackMainOrientation((MNApplication)getApplication(), TAG, newConfig.orientation);
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
        overridePendingTransition(R.anim.activity_modal_up, R.anim.activity_hold);

        // 플러리
        Map<String, String> params = new HashMap<String, String>();
        params.put(MNFlurry.CALLED_FROM, "Main - Dog Ear");
        FlurryAgent.logEvent(MNFlurry.STORE, params);
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
     * WakeDialog
     */
    @Subscribe
    public void onDismissAlarmWakeDialog(AlertDialog alertDialog) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
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
            case COOL_NAVY:
            case MINT_PINK:
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

    private void sendFlurryAnalytics() {
        // 쓰레드로 돌려서 UI쓰레드 방해하지 않게 구현
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 풀 버전 체크
                Map<String, String> versionParams = new HashMap<String, String>();
                boolean isFullVersionBought =
                        SKIabProducts.loadOwnedIabProducts(MNMainActivity.this).contains(SKIabProducts.SKU_FULL_VERSION);
                if (isFullVersionBought) {
                    versionParams.put(MNFlurry.VERSION, MNFlurry.FULL_VERSION);
                } else {
                    versionParams.put(MNFlurry.VERSION, MNFlurry.FREE_VERSION);
                }
                FlurryAgent.logEvent(MNFlurry.ON_LAUNCH, versionParams);

                // 풀 버전일 때 따로 2X3 / 2X2 체크
                MNPanelMatrixType currentPanelMatrixType = MNPanelMatrix.getCurrentPanelMatrixType(MNMainActivity.this);
                Map<String, String> panelMatrixParams = new HashMap<String, String>();
                panelMatrixParams.put(MNFlurry.PANEL_MATRIX_TYPE, currentPanelMatrixType.toString());
                if (isFullVersionBought) {
                    FlurryAgent.logEvent(MNFlurry.FULL_VERSION, panelMatrixParams);
                }
                // 전체 2X3 / 2X2 체크 - On Launch 에 그대로 표시
                FlurryAgent.logEvent(MNFlurry.ON_LAUNCH, panelMatrixParams);

                // 언어 체크
                MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(MNMainActivity.this);
                Map<String, String> languageParams = new HashMap<String, String>();
                languageParams.put(MNFlurry.LANGUAGE,
                        MNLanguageType.toEnglishString(currentLanguageType.getIndex()));
                FlurryAgent.logEvent(MNFlurry.ON_LAUNCH, languageParams);

                // 테마 체크
                MNThemeType currentThemeType = MNTheme.getCurrentThemeType(MNMainActivity.this);
                Map<String, String> themeParams = new HashMap<String, String>();
                themeParams.put(MNFlurry.THEME, currentThemeType.toString());
                FlurryAgent.logEvent(MNFlurry.ON_LAUNCH, themeParams);

                // 알람 갯수 체크
                ArrayList<MNAlarm> alarmList = MNAlarmListManager.loadAlarmList(getApplicationContext());
                if (alarmList != null) {
                    Map<String, String> alarmParams = new HashMap<String, String>();
                    alarmParams.put(MNFlurry.NUM_OF_ALARMS, String.valueOf(alarmList.size()));
                    FlurryAgent.logEvent(MNFlurry.ON_LAUNCH, alarmParams);
                }
            }
        }).start();
    }

    private void askUsingCurrentLocationDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setPositiveButton("사용", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 사용시엔 특별히 할 것이 없음
                showTutorialLayout();
            }
        }).setNegativeButton("사용 안함", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 사용 안함 시에는 날씨 패널 오브젝트 설정해주고 리프레시
                MNPanelLayout weatherPanelLayout = panelWindowLayout.getPanelLayouts()[0];
                if (weatherPanelLayout != null && weatherPanelLayout.getPanelType() == MNPanelType.WEATHER &&
                        weatherPanelLayout.getPanelDataObject() != null) {
                    try {
                        weatherPanelLayout.getPanelDataObject().put(
                                MNWeatherPanelLayout.WEATHER_DATA_IS_USING_CURRENT_LOCATION, false);
                        weatherPanelLayout.refreshPanel();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                showTutorialLayout();
            }
        });
        AlertDialog wakeDialog = builder.create();
        wakeDialog.setCancelable(false);
        wakeDialog.setCanceledOnTouchOutside(false);
        wakeDialog.setTitle(R.string.app_name);
        wakeDialog.setMessage("현재 위치를 사용하시겠습니까?");
        wakeDialog.show();
    }

    private void showTutorialLayout() {
        // 튜토리얼 전 세로고정 설정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        MNTutorialLayout tutorialLayout = new MNTutorialLayout(getApplicationContext(), this);
        containerLayout.addView(tutorialLayout);
    }

    @Override
    public void onBackPressed() {
        if (!SKIabProducts.containsSku(SKIabProducts.SKU_NO_ADS, this) &&
                InternetConnectionManager.isNetworkAvailable(this)) {
            AlertDialog adDialog = QuitAdDialogFactory.makeDialog(MNMainActivity.this,
                    quitMediumAdView, quitLargeBannerAdView);
            if (adDialog != null) {
                adDialog.show();
                // make AdView again for next quit dialog
                // prevent child reference
                // 가로 모드는 7.5% 가량 사용하고 있기에 속도를 위해서 광고를 계속 불러오지 않음
                quitMediumAdView = QuitAdDialogFactory.initAdView(this, AdSize.MEDIUM_RECTANGLE,
                        quitAdRequest);
            } else {
                // just finish activity when dialog is null
                super.onBackPressed();
            }
        } else {
            // just finish activity when no ad item is bought
            super.onBackPressed();
        }
    }
}
