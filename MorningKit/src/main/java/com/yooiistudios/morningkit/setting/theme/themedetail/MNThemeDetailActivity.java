package com.yooiistudios.morningkit.setting.theme.themedetail;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.yooiistudios.morningkit.MNApplication;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.analytic.MNAnalyticsUtils;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.common.permission.PermissionUtils;
import com.yooiistudios.morningkit.setting.MNSettingDetailActivity;

import lombok.Getter;
import lombok.Setter;

public class MNThemeDetailActivity extends MNSettingDetailActivity {
    private static final int REQ_PERMISSION_CAMERA = 201;
    private static final String TAG = "ThemeDetailActivity";

    @Getter RelativeLayout rootLayout;
    @Setter MNThemeType pendingTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_theme_detail);
        rootLayout = (RelativeLayout) findViewById(R.id.setting_theme_detail_container);

        if (savedInstanceState == null) {
            // http://developer.android.com/guide/components/fragments.html 참고
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.setting_theme_detail_container, MNThemeDetailFragment.newInstance());
            transaction.commit();
            MNAnalyticsUtils.startAnalytics((MNApplication) getApplication(), TAG);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, MNFlurry.KEY);
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }


    /**
     * 안드로이드 6.0 이후 권한 처리
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void requestCameraPermission() {
        PermissionUtils.requestPermission(this, rootLayout, Manifest.permission.CAMERA,
                R.string.need_permission_camera, REQ_PERMISSION_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQ_PERMISSION_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MNTheme.setThemeType(pendingTheme, this);
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                Snackbar.make(rootLayout, R.string.permission_not_granted, Snackbar.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
