package com.yooiistudios.morningkit.setting.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.yooiistudios.morningkit.MNApplication;
import com.yooiistudios.morningkit.MNIabInfo;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.analytic.MNAnalyticsUtils;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.setting.MNSettingDetailActivity;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MNStoreActivity extends MNSettingDetailActivity {
    private static final String TAG = "SoundEffectActivity";

    @InjectView(R.id.store_container) RelativeLayout backgroundLayout;
    // TODO: Naver IAB. 빌드 되게 하기 위해 주석 처리
    /*
    @Getter private SKIabManager iabManager;
    */
    MNStoreFragment storeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ButterKnife.inject(this);

        overridePendingTransition(R.anim.activity_modal_up, R.anim.activity_hold);

        backgroundLayout.setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(MNTheme.getCurrentThemeType(this)));

        if (savedInstanceState == null) {
            // http://developer.android.com/guide/components/fragments.html 참고
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            storeFragment = new MNStoreFragment();
            storeFragment.setFragmentForActivity(true);
            transaction.replace(R.id.store_container, storeFragment);

            // Commit the transaction
            transaction.commit();

            initActionBar();
            initIab();

            MNAnalyticsUtils.startAnalytics((MNApplication) getApplication(), TAG);
        }
    }

    private void initActionBar() {
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.info_store);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setIcon(R.drawable.icon_actionbar_morning);
    }

    private void initIab() {
        boolean isStoreForNaver = MNIabInfo.STORE_TYPE.equals(MNStoreType.NAVER);
        if (!isStoreForNaver) {
            // TODO: Naver IAB. 빌드 되게 하기 위해 주석 처리
            /*
            iabManager = new SKIabManager(this, storeFragment);
            iabManager.loadWithAllItems();
            storeFragment.setIabManager(iabManager);
            */
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: Naver IAB. 빌드 되게 하기 위해 주석 처리
        /*
        if (iabManager != null) {
            if (iabManager.getHelper() == null) return;

            // Pass on the activity result to the helper for handling
            if (!iabManager.getHelper().handleActivityResult(requestCode, resultCode, data)) {
                // not handled, so handle it ourselves (here's where you'd
                // perform any handling of activity results not related to in-app
                // billing...
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, R.anim.activity_modal_down);
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
}
