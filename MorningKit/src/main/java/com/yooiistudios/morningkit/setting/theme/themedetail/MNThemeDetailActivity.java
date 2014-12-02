package com.yooiistudios.morningkit.setting.theme.themedetail;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.yooiistudios.morningkit.MNApplication;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.analytic.MNAnalyticsUtils;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.setting.MNSettingDetailActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MNThemeDetailActivity extends MNSettingDetailActivity {
    private static final String TAG = "ThemeDetailActivity";

    @InjectView(R.id.setting_theme_detail_container) RelativeLayout backgroundLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_theme_detail);
        ButterKnife.inject(this);

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
            transaction.replace(R.id.setting_theme_detail_container, MNThemeDetailFragment.newInstance());

            // Commit the transaction
            transaction.commit();

            MNAnalyticsUtils.startAnalytics((MNApplication) getApplication(), TAG);
        }
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
