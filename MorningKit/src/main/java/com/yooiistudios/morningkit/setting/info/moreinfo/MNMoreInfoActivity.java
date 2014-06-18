package com.yooiistudios.morningkit.setting.info.moreinfo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAgent;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.setting.MNSettingDetailActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MNMoreInfoActivity extends MNSettingDetailActivity {
    @InjectView(R.id.setting_info_moreinfo_container) RelativeLayout backgroundLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_more_info);
        ButterKnife.inject(this);

        // 반짝임을 없애기 위해 프래그먼트와 같은 배경을 사용해야함
//        backgroundLayout.setBackgroundColor(0xff4444dd);
//        backgroundLayout.setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(MNTheme.getCurrentThemeType(this)));

        if (savedInstanceState == null) {
            // http://developer.android.com/guide/components/fragments.html 참고
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // animate - (upstack)incoming enterAnim, (backstack)outgoing exitAnim /
            // in reverse - (backstack)incoming enterAnim, (upstack)outgoing exitAnim
//            transaction.setCustomAnimations(R.anim.fragment_enter, 0);

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.setting_info_moreinfo_container, new MNMoreInfoFragment());

            // Commit the transaction
            transaction.commit();
        }
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
//            removeFragment();
//            finishWithFragmentAnimation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // Catch the back button and make fragment animate
//        if (keyCode == KeyEvent.KEYCODE_BACK ) {
//            removeFragment();
//            finishWithFragmentAnimation();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    /*
    private void removeFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // animate
        transaction.setCustomAnimations(0, R.anim.fragment_exit);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.remove(moreInfoFragment);
        // Commit the transaction
        transaction.commit();
    }

    private void finishWithFragmentAnimation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    */

    @Override
    protected void onStart() {
        // Activity visible to user
        super.onStart();
        FlurryAgent.onStartSession(this, MNFlurry.KEY);
    }

    @Override
    protected void onStop() {
        // Activity no longer visible
        super.onStop();
        FlurryAgent.onEndSession(this);
    }
}
