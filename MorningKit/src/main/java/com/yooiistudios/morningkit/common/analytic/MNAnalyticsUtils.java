package com.yooiistudios.morningkit.common.analytic;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yooiistudios.morningkit.MNApplication;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 14. 12. 2.
 *
 * MNAnalyticsUtils
 *  Google Analytics 를 사용하기 위한 클래스
 */
public class MNAnalyticsUtils {
    private MNAnalyticsUtils() { throw new AssertionError("You must not create this class!"); }

    public static void startAnalytics(MNApplication application, String screenName) {
        // Get tracker.
        Tracker t = application.getTracker(MNApplication.TrackerName.APP_TRACKER);

        // it turn on the auto-tracking for post API-V14 devices
        // but you have to write a code on onStart and onStop for pre-V14 devices
        t.enableAutoActivityTracking(true);

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
