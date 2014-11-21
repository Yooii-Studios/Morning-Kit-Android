package com.yooiistudios.morningkit.common.ad;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.dp.DipToPixel;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeInfo;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.co.garage.onesdk.Constants;
import jp.co.garage.onesdk.DGService;
import jp.co.garage.onesdk.OneSDK;

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

    public static void checkFullScreenAdCount(Context context, Activity activity) {
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
                            fullScreenAdView.show();
                        }
                    });
                    AdRequest fullAdRequest = new AdRequest.Builder().build();
                    fullScreenAdView.loadAd(fullAdRequest);
                    
                    /*
                    MNLanguageType currentLangunageType = MNLanguage.getCurrentLanguageType(context);
                    if (currentLangunageType != MNLanguageType.JAPANESE) {

                    } else {
                        // 일본어 = Digital Garage
                        OneSDK sdk = OneSDK.getInstance(activity);

                        // Open service
                        // 4820 = publisher ID = Yooii Studios
                        // 20 = App ID = Morning Kit
                        // 8 = Sketch Kit, 테스트용
                        DGService dgService = sdk.OpenService(4820, 8, 20, Constants.ServiceCategories.INTERSTITIAL, activity);

                        // 리스너 테스트용, 나중에 배너에서 활용
//                        dgService.setOneSDKListeners(new OneSDKListeners() {
//                            @Override
//                            public void startLoad(int i) {
//                                MNLog.now("DG Ad startLoad");
////                                adView.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void finishLoad(int i) {
//                                MNLog.now("DG Ad finishLoad");
////                                adView.setVisibility(View.INVISIBLE);
//                            }
//                        });

                        // 중앙 계산
                        int x = (MNDeviceSizeInfo.getDeviceWidth(activity) - DipToPixel.getPixel(activity, 320)) / 2;
                        int y = (MNDeviceSizeInfo.getDeviceHeight(activity) - DipToPixel.getPixel(activity, 250)) / 2 -
                                activity.getResources().getDimensionPixelSize(R.dimen.main_button_layout_height);

                        // Show
                        if (dgService != null) {
                            String paramstr = "{ \"x\" : \"" + x + "\",\"y\" : \"" + y + "\"}";
                            try {
                                JSONObject json = new JSONObject(paramstr);
                                dgService.Request(json);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    */
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
