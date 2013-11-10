package com.yooiistudios.morningkit.main;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.MNDeviceSizeChecker;
import com.yooiistudios.morningkit.common.MNViewSizeMeasure;
import com.yooiistudios.morningkit.main.layout.MNMainLayout;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

public class MNMainActivity extends Activity implements AdListener
{
    private static final String TAG = "MNMainActivity";

    @InjectView(R.id.main_scroll_view) ScrollView mScrollView;
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
                // 스크롤뷰
                MNMainLayout.adjustScrollViewLayoutParamsOnOrientation(mScrollView, newConfig.orientation);

                // 위젯윈도우 레이아웃
                MNMainLayout.adjustWidgetLayoutParamsOnOrientation(mWidgetWindowLayout, newConfig.orientation);

                // 알람 리스트뷰
                mAlarmListView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams alarmListViewLayoutParams = (LinearLayout.LayoutParams) mAlarmListView.getLayoutParams();
                if (alarmListViewLayoutParams != null) {
                    float alarmListViewHeight = MNDeviceSizeChecker.getDeviceHeight(this) - mWidgetWindowLayout.getLayoutParams().height;
                    alarmListViewLayoutParams.height = (int)alarmListViewHeight;
                }

                // 버튼 레이아웃 
                final RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) mButtonLayout.getLayoutParams();
                if (buttonLayoutParams != null) {
                    buttonLayoutParams.height = (int)getResources().getDimension(R.dimen.main_button_layout_height);
                }

                // 애드몹 레이아웃
                final RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mAdmobLayout.getLayoutParams();
                if (admobLayoutParams != null) {
                    admobLayoutParams.height = (int)getResources().getDimension(R.dimen.main_admob_layout_height);
                }

                // 애드몹
                // 버튼 레이아웃에 광고가 있을 경우 애드몹 레이아웃으로 옮기기
                if (mButtonLayout.findViewById(R.id.adView) != null) {
                    mButtonLayout.removeView(mAdView);
                    mAdmobLayout.addView(mAdView);
                }

                // 애드몹레이아웃의 width를 체크해 버튼레이아웃과 맞추어주기
                MNViewSizeMeasure.setViewSizeObserver(mAdmobLayout, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
                    @Override
                    public void onLayoutLoad() {
                        AdSize adSize = AdSize.createAdSize(AdSize.BANNER, getBaseContext());
                        if (mAdmobLayout.getWidth() > adSize.getWidthInPixels(getBaseContext())) {
//                            Log.i(TAG, "AdMobLayout.getWidth() is bigger than adSize.getWidth()");
                            // 1. 버튼 레이아웃 width 보다 광고뷰 width가 더 짧을 경우는 버튼 레이아웃에 맞추어줌
                            if (adSize.getWidthInPixels(getBaseContext()) <= mButtonLayout.getWidth()) {
//                                Log.i(TAG, "adSize.getWidth() is shorter than buttonLayout.getWidth()");
                                if (admobLayoutParams != null && buttonLayoutParams != null) {
                                    admobLayoutParams.leftMargin = buttonLayoutParams.leftMargin;
                                    admobLayoutParams.rightMargin= buttonLayoutParams.rightMargin;
                                }
                            }
                            // 2. 더 넓을 경우는 match_parent 그대로 놔두어야 할듯(기본)
                        }
                    }
                });
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {

                // 스크롤뷰
                MNMainLayout.adjustScrollViewLayoutParamsOnOrientation(mScrollView, newConfig.orientation);

                // 위젯윈도우 레이아웃
                MNMainLayout.adjustWidgetLayoutParamsOnOrientation(mWidgetWindowLayout, newConfig.orientation);

                // 알람 리스트뷰
                // Gone: 안보이고 차지한 공간도 사라짐
                // INVISIBLE: 안보이지만 공간은 차지함
                mAlarmListView.setVisibility(View.GONE);

                // 애드몹 레이아웃
                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mAdmobLayout.getLayoutParams();
                if (admobLayoutParams != null) {
                    admobLayoutParams.height = 0;
                }

                // 버튼
                RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) mButtonLayout.getLayoutParams();
                if (buttonLayoutParams != null) {
                    buttonLayoutParams.height =
                            (int)(getResources().getDimension(R.dimen.main_button_layout_height) + getResources().getDimension(R.dimen.margin_outer)*2);
                }

                // 애드몹
                // Landscape 모드에서 버튼 레이아웃으로 광고 옮기기
                if (mAdmobLayout.findViewById(R.id.adView) != null) {
                    mAdmobLayout.removeView(mAdView);
                    mButtonLayout.addView(mAdView);
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
    public ScrollView getMainScrollView() { return mScrollView; }

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
