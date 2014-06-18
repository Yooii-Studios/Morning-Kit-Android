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

    public static void checkFullScreenAdCount(Activity activity) {

        List<String> owndedSkus =  SKIabProducts.loadOwnedIabProducts(activity);

        // 풀버전이나 광고 구매를 하지 않았을 경우만 진행
        if (!owndedSkus.contains(SKIabProducts.SKU_FULL_VERSION) &&
        !owndedSkus.contains(SKIabProducts.SKU_NO_ADS)) {
            // 10회 이상 실행, 5회 카운트 계산
            SharedPreferences prefs = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
            int launchCount = prefs.getInt(LAUNCH_COUNT, 0);
            // 10회 이상 실행부터 계속 5배수 실행 카운트 체크
            if (launchCount >= 4) {
                int eachLaunchCount = prefs.getInt(EACH_LAUNCH_COUNT, 0);
                if (eachLaunchCount == 4) {
                    // 5회 실행시마다 초기화
                    prefs.edit().remove(EACH_LAUNCH_COUNT).commit();

                    // 광고 실행
                    MNLanguageType currentLagunageType = MNLanguage.getCurrentLanguageType(activity);
                    if (currentLagunageType != MNLanguageType.JAPANESE) {
                        // Admob
                        final InterstitialAd fullScreenAdView = new InterstitialAd(activity);
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
                        // 일본어 = Digital Garage
                        OneSDK sdk = OneSDK.getInstance(activity);

                        // Open service
                        // 4820 = publisher ID = Yooii Studios
                        // 19 = App ID = Morning Kit
                        // 8 = Sketch Kit, 테스트용
                        DGService dgService = sdk.OpenService(4820, 8, 2, Constants.ServiceCategories.INTERSTITIAL, activity);

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
}
