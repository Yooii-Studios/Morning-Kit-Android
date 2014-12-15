package com.yooiistudios.morningkit.common.ad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.yooiistudios.morningkit.R;

/**
 * Created by Wooseong Kim in News L from Yooii Studios Co., LTD. on 2014. 10. 16.
 *
 * AdDialogFactory
 *  종료시 애드뷰를 띄워주는 팩토리 클래스
 */
public class QuitAdDialogFactory {
    private QuitAdDialogFactory() { throw new AssertionError("Must not create this class!"); }

    public static final String AD_UNIT_ID = "ca-app-pub-2310680050309555/3689313020";

    public static AdView initAdView(Context context, AdSize adSize,
                                    final com.google.android.gms.ads.AdRequest adRequest) {
        // make AdView again for next quit dialog
        // prevent child reference
        AdView adView = new AdView(context);
        adView.setAdSize(adSize);
        adView.setAdUnitId(QuitAdDialogFactory.AD_UNIT_ID);
        adView.loadAd(adRequest);
        return adView;
    }

    public static AlertDialog makeDialog(final Activity activity, final AdView mediumAdView,
                                         final AdView largeBannerAdView) {
        Context context = activity.getApplicationContext();
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT);
        } else {
            builder = new AlertDialog.Builder(activity);
        }
        builder.setTitle(R.string.quit_ad_dialog_title_text);
        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                activity.finish();
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), null);

        AlertDialog wakeDialog = builder.create();
//        wakeDialog.setView(adView); // Android L 에서 윗 공간이 좀 이상하긴 하지만 기본으로 가야할듯

        /**
         * 동현 파트
         */
        final View tempContentView = new View(activity);
        tempContentView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        wakeDialog.setView(tempContentView);
        wakeDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                AlertDialog alertDialog = (AlertDialog)dialog;

//                View decorView = alertDialog.getWindow().getDecorView();
//				View tempView = decorView.findViewById(R.id.temp);
//                LogUtil.message("mQuitAdView", "decor size : " + decorView.getWidth()+ " X " + decorView.getHeight());
//                LogUtil.message("mQuitAdView", "ad size : " + tempContentView.getWidth()+ " X " + tempContentView.getHeight());

                ViewParent parentView = tempContentView.getParent();
                if (parentView instanceof ViewGroup) {
                    ViewGroup contentWrapper = ((ViewGroup)parentView);
                    contentWrapper.removeView(tempContentView);

                    int contentWidth = tempContentView.getWidth();
                    int contentHeight = tempContentView.getHeight();
                    float screenDensity = activity.getResources().getDisplayMetrics().density;

                    if (contentWidth >= 300 * screenDensity && contentHeight >= 250 * screenDensity) {
                        // medium rectangle
                        contentWrapper.addView(mediumAdView);
//						mediumRectangleAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                    } else if (contentWidth >= 320 * screenDensity && contentHeight >= 100 * screenDensity) {
                        // large banner
                        contentWrapper.addView(largeBannerAdView);
//						mediumRectangleAdView.setAdSize(AdSize.LARGE_BANNER);
                    }
                }
            }
        });
        wakeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 기타 필요한 설정
        wakeDialog.setCanceledOnTouchOutside(false);

        return wakeDialog;
    }
}
