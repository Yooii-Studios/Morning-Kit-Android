package com.yooiistudios.morningkit.common.ad;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;

import java.util.List;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 6. 17.
 *
 * MNAdChecker
 *  광고를 주기적으로 체크해서 10회 실행 이후부터 5번에 한번씩 전면광고를 실행
 *  50회 이상 실행이면 4번에 한번씩 전면광고를 실행
 */
public class MNAdUtils {
    private MNAdUtils() { throw new AssertionError("You MUST not create this class!"); }
    private static final String KEY = "MNAdUtils";
    private static final String LAUNCH_COUNT = "LAUNCH_COUNT";
    private static final String EACH_LAUNCH_COUNT = "EACH_LAUNCH_COUNT";
    private static final String EACH_AD_COUNT = "EACH_AD_COUNT";

    private static final String INTERSTITIAL_ID = "ca-app-pub-2310680050309555/2209471823";

    public static void showPopupAdIfSatisfied(Context context) {
        if (context == null) {
            return;
        }
        List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(context);

        // 광고 구매 아이템이 없을 경우만 진행(풀버전은 광고 제거 포함)
        if (!ownedSkus.contains(SKIabProducts.SKU_NO_ADS)) {
            SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
            int launchCount = prefs.getInt(LAUNCH_COUNT, 1);
            if (shouldShowAd(prefs, launchCount)) {
                // 3번째 마다 인하우스 스토어 광고를 보여주게 로직 수정
                int eachAdCount = prefs.getInt(EACH_AD_COUNT, 1);
                if (eachAdCount >= 3) {
                    prefs.edit().remove(EACH_AD_COUNT).apply();
                    showInHouseStoreAd(context);
                } else {
                    prefs.edit().putInt(EACH_AD_COUNT, ++eachAdCount).apply();
                    showInterstitialAd(context);
                }
            }
            if (launchCount < 55) {
                launchCount++;
                prefs.edit().putInt(LAUNCH_COUNT, launchCount).apply();
            }
        }
    }

    private static boolean shouldShowAd(SharedPreferences prefs, final int launchCount) {
        // 11회 이상 실행부터 계속 5 or 4배수 실행 카운트 체크 - 10회는 리뷰 남기기 메시지.
        // 일정 카운트(55) 이상부터는 launchCount 는 더 증가시킬 필요가 없음
        if (launchCount >= 7) {
            int threshold;
            if (launchCount < 55) {
                threshold = 5;
            } else {
                threshold = 4;
            }
            int eachLaunchCount = prefs.getInt(EACH_LAUNCH_COUNT, 1);
            if (eachLaunchCount >= threshold) {
                // 광고 표시와 동시에 다시 초기화
                prefs.edit().remove(EACH_LAUNCH_COUNT).apply();
                return true;
            } else {
                eachLaunchCount++;
                prefs.edit().putInt(EACH_LAUNCH_COUNT, eachLaunchCount).apply();
            }
        }
        return false;
    }

    private static void showInterstitialAd(Context context) {
        // 전체 광고 표시
        final InterstitialAd interstitialAdView = new InterstitialAd(context);
        interstitialAdView.setAdUnitId(INTERSTITIAL_ID);
        interstitialAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (interstitialAdView.isLoaded()) {
                    interstitialAdView.show();
                }
            }
        });
        AdRequest fullAdRequest = new AdRequest.Builder()
//                            .addTestDevice("D9XXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
                .build();
        interstitialAdView.loadAd(fullAdRequest);
    }

    private static void showInHouseStoreAd(Context context) {
        showInterstitialAd(context);
    }
}
