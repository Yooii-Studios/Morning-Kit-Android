package com.yooiistudios.morningkit.alarm.pref;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.yooiistudios.morningkit.R;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 3.
 *
 * MNAlarmPreferenceActivity
 * 알람을 추가, 수정하는 액티비티
 */
public class MNAlarmPreferenceActivity extends ListActivity implements View.OnClickListener{

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getActionBar().setTitle(R.string.add_an_alarm);
//        getActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    public void onClick(View v) {

    }
}
