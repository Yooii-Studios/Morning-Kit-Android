package com.yooiistudios.morningkit;

import android.app.Application;
import android.content.res.Configuration;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.yooiistudios.morningkit.common.locale.MNLocaleUtils;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import java.util.HashMap;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNApplication
 *  로케일 설정에 관련된 클래스. 모든 액티비티 설정에 관여한다
 */
public class MNApplication extends Application {
    private Locale locale = null;
    private static final String PROPERTY_ID = "UA-50855812-11";

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getApplicationContext().getResources().updateConfiguration(newConfig,
                    getApplicationContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        Configuration config = getApplicationContext().getResources().getConfiguration();

        MNLocaleUtils.saveDefaultLocaleIfNecessary(getApplicationContext(), Locale.getDefault());

        // load language from MNLanguage
        MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(getApplicationContext());

        // update locale to current language
        locale = new Locale(currentLanguageType.getCode(), currentLanguageType.getRegion());
        Locale.setDefault(locale);
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config,
                getApplicationContext().getResources().getDisplayMetrics());

        // https://gist.github.com/benelog/5954649
        // Activity나 Application 등 UI스레드 아래와 같이 AsyncTask를 한번 호출합니다.
        // 메인스레드에서 단순히 클래스 로딩을 한번만 해도 AsyncTask내의 static 멤버 변수가 정상적으로 초기화됩니다.
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
