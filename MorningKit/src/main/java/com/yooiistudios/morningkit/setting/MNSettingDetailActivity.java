package com.yooiistudios.morningkit.setting;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;

// 반복작업을 피하기 위한 액티비티
public class MNSettingDetailActivity extends ActionBarActivity {
    private static final String TAG = "MNSettingDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Theme
        setTheme(R.style.MNSettingActionBarTheme_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_detail);

        // 반짝임을 없애기 위해 프래그먼트와 같은 배경을 사용해야함
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.setting_detail_container);
        layout.setBackgroundColor(0xff4444dd);

        // Force no anim for entering activity, hold for exiting activity
        this.overridePendingTransition(0, R.anim.activity_hold);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle("Setting");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.icon_setting_on_s3);
    }

    @Override
    public void finish() {
        super.finish();
        // Force no animation during transition
        this.overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (MNSound.isSoundOn(this)) {
                MNSoundEffectsPlayer.play(R.raw.effect_view_close, this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Catch the back button and make fragment animate
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (MNSound.isSoundOn(this)) {
                MNSoundEffectsPlayer.play(R.raw.effect_view_close, this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
