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
        // GET_TASKS 퍼미션이 없어서 한번씩 문제가 되는 상황이 생기는 것 같은데 소수라고 판단하고
        // 그 경우에는 광고가 안 뜨도록 하면 될 듯
        try {
            adView.loadAd(adRequest);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
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

        final AlertDialog wakeDialog = builder.create();
        // 세로 전용 앱이라서 동현 파트를 제거할 경우 아래 주석을 해제할 것
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
                ViewParent parentView = tempContentView.getParent();
                if (parentView instanceof ViewGroup) {
                    final ViewGroup contentWrapper = ((ViewGroup)parentView);
                    contentWrapper.removeView(tempContentView);

                    int contentWidth = tempContentView.getWidth();
                    int contentHeight = tempContentView.getHeight();
                    float screenDensity = activity.getResources().getDisplayMetrics().density;

                    if (contentWidth >= 300 * screenDensity && contentHeight >= 250 * screenDensity) {
                        // medium rectangle
                        contentWrapper.addView(mediumAdView);
                        wakeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                contentWrapper.removeView(mediumAdView);
                            }
                        });
                    } else if (contentWidth >= 320 * screenDensity && contentHeight >= 100 * screenDensity) {
                        // large banner
                        contentWrapper.addView(largeBannerAdView);
                        wakeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                contentWrapper.removeView(largeBannerAdView);
                            }
                        });
                    }
                }
            }
        });
        wakeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**
         * 동현 파트 끝
         */

        // 기타 필요한 설정
        wakeDialog.setCanceledOnTouchOutside(false);

        return wakeDialog;
    }
}
