package com.yooii.morningkit.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.yooii.morningkit.R;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

public class MNMainActivity extends Activity
{
    private static final String TAG = "MNMainActivity";

    @InjectView(R.id.main_widget_window_view) MNWidgetWindowView mWidgetWindowView;
    @InjectView(R.id.main_alarm_list_view) MNMainAlarmListView mAlarmListView;
    @InjectView(R.id.main_button_layout) RelativeLayout mRelativeLayout;
    @InjectView(R.id.main_admob_layout) RelativeLayout mAdmobLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Activity start, Load the xml layout

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 기존의 레거시 코드 대신에 이 한줄로 findViewById를 모두 대체
        Views.inject(this);
//        mWidgetWindowView = (MNWidgetWindowView) findViewById(R.id.main_widget_window_view);
//        mAlarmListView = (MNMainAlarmListView) findViewById(R.id.main_alarm_list_view);
//        mRelativeLayout = (RelativeLayout) findViewById(R.id.main_button_layout);
//        mAdmobLayout = (RelativeLayout) findViewById(R.id.main_admob_layout);

        mWidgetWindowView.initWithWidgetMatrix();
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
     * @category OnClick
     */
    @OnClick(R.id.main_refresh_image) void refreshButtonClicked() {
        Log.i(TAG, "refreshButtonClicked");
    }

    @OnClick(R.id.main_configure_image) void configureButtonClicked() {
        Log.i(TAG, "configureButtonClicked");
    }

    /**
     * @category Getter
     */
    public MNWidgetWindowView getWidgetWindowView() {
        return mWidgetWindowView;
    }
    public MNMainAlarmListView getAlarmListView() { return mAlarmListView; }
}
