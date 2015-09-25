package com.yooiistudios.morningkit.common.ad;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.yooiistudios.fullscreenad.FullscreenAdUtils;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.store.MNStoreActivity;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;

import java.util.List;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 6. 17.
 *
 * MNAdChecker
 *  광고를 주기적으로 체크해서 10회 실행 이후부터 5번에 한번씩 전면광고를 실행
 *  50회 이상 실행이면 4번에 한번씩 전면광고를 실행
 *
 *  -> 2015. 9. 17 수정(with 이사님)
 *  5회도 뉴스키트 광고가 뜨게 수정, 5-20 뉴스 / 40 모닝 / 이후 60회부터 뉴스 뉴스 모닝 방식
 */
public class AdUtils {
    private AdUtils() { throw new AssertionError("You MUST not create this class!"); }
    private static final String KEY = "MNAdUtils";
    private static final String LAUNCH_COUNT = "LAUNCH_COUNT";
    private static final String EACH_LAUNCH_COUNT = "EACH_LAUNCH_COUNT";
    private static final String EACH_AD_COUNT = "EACH_AD_COUNT";
    private static final String NEWS_KIT_PACKAGE_NAME = "com.yooiistudios.newskit";

    // 원하는 카운트에 실행이 되는지 테스트 용도. 필요할 때 풀고 사용하자
    /*
    public static void resetCounts(Context context) {
        if (context == null) {
            return;
        }
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        prefs.edit().remove(LAUNCH_COUNT).apply();
        prefs.edit().remove(EACH_AD_COUNT).apply();
        prefs.edit().remove(EACH_LAUNCH_COUNT).apply();
    }
    */

    public static void showPopupAdIfSatisfied(Context context) {
        if (context == null) {
            return;
        }
        List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(context);

        // 광고 or 풀버전 구매 아이템이 없을 경우만 진행
        if (!ownedSkus.contains(SKIabProducts.SKU_NO_ADS)) {
            SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
            int launchCount = prefs.getInt(LAUNCH_COUNT, 1);
            if (shouldShowAd(prefs, launchCount)) {

                // 3번째 마다 인하우스 스토어 광고를 보여주게 로직 수정
                int eachAdCount = prefs.getInt(EACH_AD_COUNT, 1);
                if (eachAdCount >= 3) {
                    prefs.edit().remove(EACH_AD_COUNT).apply();
                    showNewsKitAd(context);
                } else if (eachAdCount < 3 || launchCount == 5 || launchCount == 20) {
                    prefs.edit().putInt(EACH_AD_COUNT, ++eachAdCount).apply();
                    showInHouseStoreAd(context);
                }
            }

            // 20회부터 20번 실행마다 광고를 보여주면 되기에 더이상 체크 X
            if (launchCount <= 20) {
                launchCount++;
                prefs.edit().putInt(LAUNCH_COUNT, launchCount).apply();
            }
        }
    }

    private static boolean shouldShowAd(SharedPreferences prefs, final int launchCount) {
        // 일정 카운트(20) 이상부터는 launchCount 는 더 증가시킬 필요가 없음. 실행 횟수만 체크
        if (launchCount > 20) {
            int threshold = 20;

            int eachLaunchCount = prefs.getInt(EACH_LAUNCH_COUNT, 1);
            if (eachLaunchCount >= threshold) {
                // 광고 표시와 동시에 다시 초기화
                prefs.edit().remove(EACH_LAUNCH_COUNT).apply();
                return true;
            } else {
                eachLaunchCount++;
                prefs.edit().putInt(EACH_LAUNCH_COUNT, eachLaunchCount).apply();
            }
        } else if (launchCount == 20 || launchCount == 5) {
            return true;
        }
        return false;
    }

    private static void showNewsKitAd(Context context) {
        // 뉴스키트가 설치된 경우 대신 인하우스 광고 보여주기
        if (!isPackageExisted(context, NEWS_KIT_PACKAGE_NAME)) {
            FullscreenAdUtils.showMorningKitAd(context);
        } else {
            showInHouseStoreAd(context);
        }
    }

    private static boolean isPackageExisted(Context context, String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage)) return true;
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    private static void showInHouseStoreAd(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.store_ad_dialog_layout);

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 다이얼로그 중앙 정렬용 코드였지만 위의 FEATURE_NO_TITLE 설정으로 인해 필요 없게 됨
//        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setGravity(Gravity.CENTER);

        TextView titleTextView =
                (TextView) dialog.findViewById(R.id.store_ad_dialog_title_text_view);
        titleTextView.setText(context.getString(R.string.recommend_app_full_name) + " PRO");
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.startActivity(new Intent(context, MNStoreActivity.class));
            }
        });

        ImageView storeImageView = (ImageView) dialog.findViewById(R.id.store_ad_dialog_image_view);
        storeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.startActivity(new Intent(context, MNStoreActivity.class));
            }
        });

        TextView storeButtonView = (TextView) dialog.findViewById(R.id.store_ad_dialog_ok_button);
        storeButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.startActivity(new Intent(context, MNStoreActivity.class));
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
    }
}
