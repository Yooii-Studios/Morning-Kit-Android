package com.yooiistudios.morningkit.main;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.main.layout.MNMainLayoutSetter;

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
    @Getter @InjectView(R.id.main_scroll_view) ScrollView scrollView;
    @Getter @InjectView(R.id.main_widget_window_layout) MNWidgetWindowLayout widgetWindowLayout;
    @Getter @InjectView(R.id.main_alarm_list_view) MNMainAlarmListView alarmListView;
    @Getter @InjectView(R.id.main_button_layout) RelativeLayout buttonLayout;
    @Getter @InjectView(R.id.main_admob_layout) RelativeLayout admobLayout;
    @Getter @InjectView(R.id.adView) AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainActivity();
    }

    void initMainActivity() {
        // 기존의 레거시 코드 대신에 이 한줄로 findViewById를 모두 대체
        ButterKnife.inject(this);

//        // 특정 날짜 이후로 앱이 죽게 폭탄 설치 - 현재는 주석 처리
//        AppValidationChecker.validationCheck(this);

        // 위젯 윈도우
        widgetWindowLayout.initWithWidgetMatrix();

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
    }

    @Override
    protected void onStart()
    {
        // Activity visible to user

        super.onStart();
    }

    @Override
    protected void onResume()
    {
        // Activity visible to user

        // Alarm
        alarmListView.refreshListView();

        // 테마와 관련된 작업 실행
//        containerLayout.setBackgroundColor(Color.WHITE);

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

        // 스크롤뷰
        MNMainLayoutSetter.adjustScrollViewLayoutParamsAtOrientation(scrollView, newConfig.orientation);
        // 위젯윈도우 레이아웃
        MNMainLayoutSetter.adjustWidgetLayoutParamsAtOrientation(widgetWindowLayout, newConfig.orientation);
        // 버튼 레이아웃
        MNMainLayoutSetter.adjustButtonLayoutParamsAtOrientation(buttonLayout, newConfig.orientation);
        // 애드몹 레이아웃
        MNMainLayoutSetter.adjustAdmobLayoutParamsAtOrientation(admobLayout, newConfig.orientation);
        // 애드뷰 방향에 따라 위치 옮기기
        MNMainLayoutSetter.adjustAdmobViewAtOrientation(this, newConfig.orientation);
        // 애드몹 레이아웃 width 체크
        MNMainLayoutSetter.checkAdmobLayoutWidthAndAdjust(admobLayout, buttonLayout, newConfig.orientation);
        // 알람 리스트뷰
        MNMainLayoutSetter.adjustAlarmListView(alarmListView, widgetWindowLayout, newConfig.orientation);
    }

    /**
     * OnClick
     */
    @OnClick(R.id.main_refresh_image) void refreshButtonClicked() {
        Log.i(TAG, "refreshButtonClicked");
    }

    @OnClick(R.id.main_configure_image) void configureButtonClicked() {
        Log.i(TAG, "configureButtonClicked");
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
}
