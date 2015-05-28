package com.yooiistudios.morningkit.common.ad;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;

import java.util.List;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 15. 5. 26.
 *
 * NewsKitAdUtils
 *  뉴스키트 프로모션을 위해서 최초 설치 or 업데이트 시 5회 & 20회 2회에 걸쳐 뉴스키트 다이얼로그를 보여주기
 */
public class NewsKitAdUtils {
    private static final String KEY = "NewsKitAdUtils";
    private static final String LAUNCH_COUNT = "launch_count";
    private static final String NEWS_KIT_PACKAGE_NAME = "com.yooiistudios.newskit";
    private static final String IN_HOUSE_AD_ID = "ca-app-pub-2310680050309555/1063543828";
    // ca-app-pub-2310680050309555/2209471823
    // 하우스 ca-app-pub-2310680050309555/1063543828

    public static void showPopupAdIfSatisfied(Context context) {
        if (context == null) {
            return;
        }
        List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(context);

//        showNewsKitAd(context);

        // 풀버전 구매 아이템이 없을 경우만 진행, 첫 설치든 업데이트 이후든 똑같은 횟수에 보여주기
        if (!(ownedSkus.contains(SKIabProducts.SKU_FULL_VERSION))) {
            if (shouldShowAd(context)) {
                showNewsKitAd(context);
            }
            increaseLaunchCount(context);
        }
    }

    // AdUtils 에서 광고를 보여주기 전 뉴스키트 광고가 나갈 차례면 모닝키트 광고를 안 보여주게 하기 위해 public 설정
    public static boolean shouldShowAd(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        int launchCount = prefs.getInt(LAUNCH_COUNT, 1);

        // 첫 설치든, 기존 유저 업데이트 이후든 5회 & 20회 실행시 보여주게 구현
        return launchCount == 5 || launchCount == 20;
    }

    private static void increaseLaunchCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        int launchCount = prefs.getInt(LAUNCH_COUNT, 1);
        if (launchCount < 21) {
            launchCount++;
            prefs.edit().putInt(LAUNCH_COUNT, launchCount).apply();
        }
    }

    private static void showNewsKitAd(final Context context) {
        // 전체 광고 표시
        final InterstitialAd interstitialAdView = new InterstitialAd(context);
        interstitialAdView.setAdUnitId(IN_HOUSE_AD_ID);
        interstitialAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (interstitialAdView.isLoaded()) {
                    interstitialAdView.show();
                }
            }
        });
        AdRequest fullAdRequest = new AdRequest.Builder().build();
        interstitialAdView.loadAd(fullAdRequest);

        /*
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.newskit_ad_dialog_layout);

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView titleTextView =
                (TextView) dialog.findViewById(R.id.store_ad_dialog_title_text_view);
        titleTextView.setText(R.string.news_kit_ad_title);
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goToPlayStoreForNewsKit(context);
            }
        });

        AutoFitTextView descriptionTextView =
                (AutoFitTextView) dialog.findViewById(R.id.store_ad_dialog_description_textview);
        descriptionTextView.setAlpha(0.85f);

        ImageView storeImageView = (ImageView) dialog.findViewById(R.id.store_ad_dialog_image_view);
        storeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goToPlayStoreForNewsKit(context);
            }
        });

        TextView storeButtonView = (TextView) dialog.findViewById(R.id.store_ad_dialog_ok_button);
        storeButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goToPlayStoreForNewsKit(context);
            }
        });

        TextView cancelButtonView = (TextView) dialog.findViewById(R.id.store_ad_dialog_cancel_button);
        cancelButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 기타 필요한 설정
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        */
    }

    private static void goToPlayStoreForNewsKit(Context context) {
        Uri uri = Uri.parse("market://details?id=" + NEWS_KIT_PACKAGE_NAME);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Couldn't launch the market", Toast.LENGTH_SHORT).show();
        }
    }
}
