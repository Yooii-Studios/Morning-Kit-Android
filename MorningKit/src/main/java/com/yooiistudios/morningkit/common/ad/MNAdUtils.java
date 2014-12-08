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
 *  일본어의 경우 디지털 가라지의 광고를 보여줄 것
 */
public class MNAdUtils {
    private MNAdUtils() { throw new AssertionError("You MUST not create this class!"); }
    private static final String KEY = "MNAdUtils";
    private static final String LAUNCH_COUNT = "LAUNCH_COUNT";
    private static final String EACH_LAUNCH_COUNT = "EACH_LAUNCH_COUNT";

    public static void checkFullScreenAdCount(Context context) {
        if (context == null) {
            return;
        }
        List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(context);

        // 광고 구매 아이템이 없을 경우만 진행(풀버전은 광고 제거 포함)
        if (!ownedSkus.contains(SKIabProducts.SKU_NO_ADS)) {
            // 12회 이상 실행, 5회 카운트 계산
            SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
            int launchCount = prefs.getInt(LAUNCH_COUNT, 0);
            // 12회 이상 실행부터 계속 5배수 실행 카운트 체크 - 10회는 리뷰 남기기 메시지.
            // 일정 카운트 이상부터는 launchCount 는 더 증가시킬 필요가 없음
            if (launchCount >= 6) {
                int eachLaunchCount = prefs.getInt(EACH_LAUNCH_COUNT, 0);
                if (eachLaunchCount == 4) {
                    // 5회 실행시마다 초기화
                    prefs.edit().remove(EACH_LAUNCH_COUNT).apply();

                    // 광고 실행
                    // Admob
                    final InterstitialAd fullScreenAdView = new InterstitialAd(context);
                    fullScreenAdView.setAdUnitId("ca-app-pub-2310680050309555/2209471823");
                    fullScreenAdView.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            if (fullScreenAdView.isLoaded()) {
                                fullScreenAdView.show();
                            }
                        }
                    });
                    AdRequest fullAdRequest = new AdRequest.Builder()
//                            .addTestDevice("D9XXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
                            .build();
                    fullScreenAdView.loadAd(fullAdRequest);
                } else {
                    eachLaunchCount++;
                    prefs.edit().putInt(EACH_LAUNCH_COUNT, eachLaunchCount).apply();
                }
            } else {
                launchCount++;
                prefs.edit().putInt(LAUNCH_COUNT, launchCount).apply();
            }
        }
    }
}
