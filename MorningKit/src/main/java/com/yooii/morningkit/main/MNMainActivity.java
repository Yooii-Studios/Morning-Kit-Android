package com.yooii.morningkit.main;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yooii.morningkit.R;
import com.yooii.morningkit.common.MNDeviceSizeChecker;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

public class MNMainActivity extends Activity
{
    private static final String TAG = "MNMainActivity";

    @InjectView(R.id.main_widget_window_layout) MNWidgetWindowLayout mWidgetWindowLayout;
    @InjectView(R.id.main_alarm_list_view) MNMainAlarmListView mAlarmListView;
    @InjectView(R.id.main_button_layout) RelativeLayout mButtonLayout;
    @InjectView(R.id.main_admob_layout) RelativeLayout mAdmobLayout;

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

        mWidgetWindowLayout.initWithWidgetMatrix();

        // 1. Portrait
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*
        ViewTreeObserver viewTreeObserver = mWidgetWindowLayout.getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Log.i(TAG, "widgetWinodwLayout portrait height: " + mWidgetWindowLayout.getHeight());
                    Log.i(TAG, "widgetWinodwLayout portrait width: " + mWidgetWindowLayout.getWidth());

                    ViewTreeObserver obs = mWidgetWindowLayout.getViewTreeObserver();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this);
                    } else {
                        obs.removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }

        // 2. Landscape
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        viewTreeObserver = mWidgetWindowLayout.getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Log.i(TAG, "widgetWinodwLayout landscape height: " + mWidgetWindowLayout.getHeight());
                    Log.i(TAG, "widgetWinodwLayout landscape width: " + mWidgetWindowLayout.getWidth());
                    Log.i(TAG, "widgetWinodwLayout dimens: " + getResources().getDimension(R.dimen.main_widget_window_layout_height));

                    ViewTreeObserver obs = mWidgetWindowLayout.getViewTreeObserver();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this);
                    } else {
                        obs.removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }
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

                // 애드몹
                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mAdmobLayout.getLayoutParams();
                admobLayoutParams.height = (int)getResources().getDimension(R.dimen.main_admob_layout_height);
                mAdmobLayout.setLayoutParams(admobLayoutParams);
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {

                // 버튼
                RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) mButtonLayout.getLayoutParams();
                buttonLayoutParams.height =
                        (int)(getResources().getDimension(R.dimen.main_button_layout_height) + getResources().getDimension(R.dimen.margin_inner)*2);
                mButtonLayout.setLayoutParams(buttonLayoutParams);

                // 애드몹
                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) mAdmobLayout.getLayoutParams();
                admobLayoutParams.height = 0;
                mAdmobLayout.setLayoutParams(admobLayoutParams);
                break;
            }
        }

        Log.i(TAG, "widgetWindowLayout height:" + mWidgetWindowLayout.getHeight());
        Log.i(TAG, "alarmListView height:" + mAlarmListView.getHeight());
        Log.i(TAG, "buttonLayout height:" + mButtonLayout.getHeight());
        Log.i(TAG, "admobLayout height:" + mAdmobLayout.getHeight());
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
}
