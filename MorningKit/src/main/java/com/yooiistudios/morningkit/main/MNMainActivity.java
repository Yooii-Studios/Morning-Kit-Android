package com.yooiistudios.morningkit.main;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.MNDeviceSizeChecker;
import com.yooiistudios.morningkit.common.MNViewSizeMeasure;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

public class MNMainActivity extends Activity implements AdListener
{
    private static final String TAG = "MNMainActivity";

    @InjectView(R.id.main_widget_window_layout) MNWidgetWindowLayout mWidgetWindowLayout;
    @InjectView(R.id.main_alarm_list_view) MNMainAlarmListView mAlarmListView;
    @InjectView(R.id.main_button_layout) RelativeLayout mButtonLayout;
    @InjectView(R.id.main_admob_layout) RelativeLayout mAdmobLayout;
    @InjectView(R.id.adView) AdView mAdView;

    /**
     * Lifecycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Activity start, Load the xml layout

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 기존의 레거시 코드 대신에 이 한줄로 findViewById를 모두 대체
        Views.inject(this);
//        mWidgetWindowView = (MNWidgetWindowLayout) findViewById(R.id.main_widget_window_view);
//        mAlarmListView = (MNMainAlarmListView) findViewById(R.id.main_alarm_list_view);
//        mRelativeLayout = (RelativeLayout) findViewById(R.id.main_button_layout);
//        mAdmobLayout = (RelativeLayout) findViewById(R.id.main_admob_layout);

        // 위젯 윈도우
        mWidgetWindowLayout.initWithWidgetMatrix();

        // 알람

        // 애드몹
//        mAdView = new AdView(this, AdSize.BANNER, MN.ads.ADMOB_ID);
//        mAdmobLayout.addView(mAdView);
//        mAdView.loadAd(new AdRequest());


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
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    /**
     * Rotation
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                // 위젯
                LinearLayout.LayoutParams widgetWindowLayoutParams = (LinearLayout.LayoutParams) mWidgetWindowLayout.getLayoutParams();
                widgetWindowLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                float widgetWindowHeight = getResources().getDimension(R.dimen.widget_height) * 2
                        + getResources().getDimension(R.dimen.margin_outer)
                        + getResources().getDimension(R.dimen.margin_outer)
                        + getResources().getDimension(R.dimen.margin_inner);
                widgetWindowLayoutParams.height = (int)widgetWindowHeight;
                mWidgetWindowLayout.setLayoutParams(widgetWindowLayoutParams);

                // 로그 테스트
//                mWidgetWindowLayout.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i(TAG, "widgetWindowLayout height:" + mWidgetWindowLayout.getHeight());
//                    }
//                });

                // 알람
                LinearLayout.LayoutParams alarmListViewLayoutParams = (LinearLayout.LayoutParams) mAlarmListView.getLayoutParams();
                alarmListViewLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                float alarmListViewHeight = MNDeviceSizeChecker.getDeviceHeight(this) - widgetWindowHeight;
                alarmListViewLayoutParams.height = (int)alarmListViewHeight;
                mAlarmListView.setLayoutParams(alarmListViewLayoutParams);

                // 버튼
                RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) mButtonLayout.getLayoutParams();
                buttonLayoutParams.height = (int)getResources().getDimension(R.dimen.main_button_layout_height);
                mButtonLayout.setLayoutParams(buttonLayoutParams);

                // 애드몹 레이아웃
                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mAdmobLayout.getLayoutParams();
                admobLayoutParams.height = (int)getResources().getDimension(R.dimen.main_admob_layout_height);
                mAdmobLayout.setLayoutParams(admobLayoutParams);

                // 애드몹
                // 버튼 레이아웃에 광고가 있을 경우 애드몹 레이아웃으로 옮기기
                if (mButtonLayout.findViewById(R.id.adView) != null) {
                    mButtonLayout.removeView(mAdView);
                    mAdmobLayout.addView(mAdView);
                }

                // 애드몹레이아웃의 width를 체크
                MNViewSizeMeasure.setViewSizeObserver(mAdmobLayout, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
                    @Override
                    public void onLayoutLoad() {
                        Log.i(TAG, "mButtonLayout size: " + mAdmobLayout.getWidth());
                        Log.i(TAG, "mAdmobLayout size: " + mAdmobLayout.getWidth());
                        AdSize adSize = AdSize.createAdSize(AdSize.BANNER, getBaseContext());
                        Log.i(TAG, "adSize: " + adSize.getWidthInPixels(getBaseContext()));
                        if (mAdmobLayout.getWidth() > adSize.getWidth()) {
                            Log.i(TAG, "AdMobLayout.getWidth() is bigger than adSize.getWidth()");
                            // 1. (버튼 레이아웃 - 마진*2)보다 광고뷰 width가 더 짧을 경우는 버튼 레이아웃에 맞추어줌
                            Log.i(TAG, "mButtonLayout width: " + (mButtonLayout.getWidth() - getResources().getDimension(R.dimen.margin_main_button_layout) * 2));
                            if (adSize.getWidthInPixels(getBaseContext()) <= (mButtonLayout.getWidth() - getResources().getDimension(R.dimen.margin_main_button_layout) * 2)) {
                                Log.i(TAG, "adSize.getWidth() is shorter than buttonLayout.getWidth()");
                                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mAdmobLayout.getLayoutParams();
                                admobLayoutParams.leftMargin = ((RelativeLayout.LayoutParams) mButtonLayout.getLayoutParams()).leftMargin;
                                admobLayoutParams.rightMargin= ((RelativeLayout.LayoutParams) mButtonLayout.getLayoutParams()).rightMargin;
                                mAdmobLayout.setLayoutParams(admobLayoutParams);
                            }
                        }
                    }
                });
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {

                // 애드몹 레이아웃
                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mAdmobLayout.getLayoutParams();
                admobLayoutParams.height = 0;
                mAdmobLayout.setLayoutParams(admobLayoutParams);

                // 버튼
                RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) mButtonLayout.getLayoutParams();
                buttonLayoutParams.height =
                        (int)(getResources().getDimension(R.dimen.main_button_layout_height) + getResources().getDimension(R.dimen.margin_outer)*2);
                mButtonLayout.setLayoutParams(buttonLayoutParams);

                // 애드몹
                // Landscape 모드에서 버튼 레이아웃으로 광고 옮기기
                Log.i(TAG, mAdView.getRootView().toString());
                if (mAdmobLayout.findViewById(R.id.adView) != null) {
                    mAdmobLayout.removeView(mAdView);
                    mButtonLayout.addView(mAdView);
                    Log.i(TAG, mAdView.getRootView().toString());
                }
                if (mAdmobLayout.findViewById(R.id.adView) != null) {
                    Log.i(TAG, "adview is in admob Layout");
                }else{
                    Log.i(TAG, "adview is in button Layout");
                }
                break;
            }
        }

//        Log.i(TAG, "widgetWindowLayout height:" + mWidgetWindowLayout.getHeight());
//        Log.i(TAG, "alarmListView height:" + mAlarmListView.getHeight());
//        Log.i(TAG, "buttonLayout height:" + mButtonLayout.getHeight());
//        Log.i(TAG, "admobLayout height:" + mAdmobLayout.getHeight());
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
     * Getter
     */
    public RelativeLayout getButtonLayout() { return mButtonLayout; }
    public RelativeLayout getAdmobLayout() { return mAdmobLayout; }
    public MNWidgetWindowLayout getWidgetWindowLayout() {
        return mWidgetWindowLayout;
    }
    public MNMainAlarmListView getAlarmListView() { return mAlarmListView; }
    public AdView getAdView() { return mAdView; }

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
