package com.yooiistudios.morningkit.common.ad;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 6. 17.
 *
 * MNAdChecker
 *  광고를 주기적으로 체크해서 10회 실행 이후부터 5번에 한번씩 전면광고를 실행
 *  일본어의 경우 디지털 가라지의 광고를 보여줄 것
 */
public class MNAdUtils {
    private MNAdUtils() { throw new AssertionError("You MUST not create this class!"); }
    private static final String KEY = "MNAdUtils";
    private static final String LAUNCH_COUNT = "LAUNCH_COUNT";
    private static final String EACH_LAUNCH_COUNT = "EACH_LAUNCH_COUNT";

    public static void checkFullScreenAdCount(Context context) {

        // 10회 이상 실행, 5회 카운트 계산
        SharedPreferences prefs = context.getSharedPreferences("MNAdUtils", Context.MODE_PRIVATE);
        int launchCount = prefs.getInt(LAUNCH_COUNT, 0);
        // 10회 이상 실행부터 계속 5배수 실행 카운트 체크
        if (launchCount >= 5) {
            int eachLaunchCount = prefs.getInt(EACH_LAUNCH_COUNT, 0);
            if (eachLaunchCount == 5) {
                // 5회 실행시마다 초기화
                prefs.edit().remove(EACH_LAUNCH_COUNT).commit();

                // 광고 실행
                MNLanguageType currentLagunageType = MNLanguage.getCurrentLanguageType(context);
                if (currentLagunageType != MNLanguageType.JAPANESE) {
                    // Admob
                    final InterstitialAd fullScreenAdView = new InterstitialAd(context);
                    fullScreenAdView.setAdUnitId("a15278abca8d8ec");
                    fullScreenAdView.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            fullScreenAdView.show();
                        }
                    });
                    AdRequest fullAdRequest = new AdRequest.Builder().build();
                    fullScreenAdView.loadAd(fullAdRequest);
                } else {
                    // Digital Garage
                }
            } else {
                eachLaunchCount++;
                prefs.edit().putInt(EACH_LAUNCH_COUNT, eachLaunchCount).commit();
            }
        } else {
            launchCount++;
            prefs.edit().putInt(LAUNCH_COUNT, launchCount).commit();
        }
    }
}
