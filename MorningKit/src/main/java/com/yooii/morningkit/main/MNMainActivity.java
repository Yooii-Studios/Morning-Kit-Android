package com.yooii.morningkit.main;

import android.app.Activity;
import android.os.Bundle;

import com.yooii.morningkit.R;

public class MNMainActivity extends Activity
{
    MNWidgetWindowView mWidgetWindowView;
    MNMainAlarmListView mAlarmListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Activity start, Load the xml layout

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWidgetWindowView = (MNWidgetWindowView)findViewById(R.id.widget_window_view);
        mWidgetWindowView.initWithWidgetMatrix();

        mAlarmListView = (MNMainAlarmListView)findViewById(R.id.alarm_list_view);
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
     * @category Getter
     */
    public MNWidgetWindowView getWidgetWindowView() {
        return mWidgetWindowView;
    }
    public MNMainAlarmListView getAlarmListView() { return mAlarmListView; }
}
