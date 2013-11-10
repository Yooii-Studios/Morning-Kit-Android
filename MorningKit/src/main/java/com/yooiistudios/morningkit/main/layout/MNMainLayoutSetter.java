package com.yooiistudios.morningkit.main.layout;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.ads.AdView;
import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.main.MNWidgetWindowLayout;

/**
 * Created by StevenKim on 2013. 11. 10..
 */
public class MNMainLayoutSetter {
    private final static String TAG = "MNMainLayoutSetter";

    public static void adjustScrollViewLayoutParamsAtOrientation(ScrollView scrollView, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                RelativeLayout.LayoutParams scrollViewLayoutParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
                if (scrollViewLayoutParams != null) {
                    // ABOVE 설정 삭제
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        scrollViewLayoutParams.removeRule(RelativeLayout.ABOVE);
                    }else{
                        scrollViewLayoutParams.addRule(RelativeLayout.ABOVE, 0);
                    }
                    scrollViewLayoutParams.bottomMargin = 0;
                }else{
                    Log.d(TAG, "scrollViewLayoutParams is null");
                }
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                RelativeLayout.LayoutParams scrollViewLayoutParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
                if (scrollViewLayoutParams != null) {
                    scrollViewLayoutParams.addRule(RelativeLayout.ABOVE, R.id.main_button_layout);
                    // 아래쪽으로 margin_outer - margin_inner 만큼 주어야 윗 마진(margin_outer)과 같아짐
                    if (scrollView.getResources() != null) {
                        scrollViewLayoutParams.bottomMargin = (int)(scrollView.getResources().getDimension(R.dimen.margin_outer) - scrollView.getResources().getDimension(R.dimen.margin_inner));
                    }else{
                        Log.d(TAG, "scrollView.getResources() is null");
                    }
                }
                break;
            }
        }
    }

    public static void adjustWidgetLayoutParamsAtOrientation(MNWidgetWindowLayout widgetWindowLayout, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                LinearLayout.LayoutParams widgetWindowLayoutParams = (LinearLayout.LayoutParams) widgetWindowLayout.getLayoutParams();
                if (widgetWindowLayoutParams != null) {
                    widgetWindowLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    if (widgetWindowLayout.getResources() != null) {
                        float widgetWindowHeight = widgetWindowLayout.getResources().getDimension(R.dimen.widget_height) * 2
                                + widgetWindowLayout.getResources().getDimension(R.dimen.margin_outer)
                                + widgetWindowLayout.getResources().getDimension(R.dimen.margin_outer)
                                + widgetWindowLayout.getResources().getDimension(R.dimen.margin_inner);
                        widgetWindowLayoutParams.height = (int)widgetWindowHeight;
                    }else{
                        Log.e(TAG, "widgetWindowLayout.getResources() is null");
                    }
                }else{
                    Log.e(TAG, "widgetWindowLayoutParams is null");
                }
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                LinearLayout.LayoutParams widgetWindowLayoutParams = (LinearLayout.LayoutParams) widgetWindowLayout.getLayoutParams();
                if (widgetWindowLayoutParams != null) {
                    widgetWindowLayoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
                }else{
                    Log.e(TAG, "widgetWindowLayoutParams is null");
                }
                break;
            }
        }
    }

    public static void adjustButtonLayoutParamsAtOrientation(RelativeLayout buttonLayout, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
                if (buttonLayoutParams != null) {
                    if (buttonLayout.getResources() != null) {
                        buttonLayoutParams.height = (int)buttonLayout.getResources().getDimension(R.dimen.main_button_layout_height);
                    }else{
                        Log.e(TAG, "buttonLayout.getResources() is null");
                    }
                }else{
                    Log.e(TAG, "buttonLayoutParams is null");
                }
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
                if (buttonLayoutParams != null) {
                    if (buttonLayout.getResources() != null) {
                        buttonLayoutParams.height =
                                (int)(buttonLayout.getResources().getDimension(R.dimen.main_button_layout_height)
                                        + buttonLayout.getResources().getDimension(R.dimen.margin_outer) * 2);
                    }else{
                        Log.e(TAG, "buttonLayout.getResources() is null");
                    }
                }else{
                    Log.e(TAG, "buttonLayoutParams is null");
                }
            }
        }
    }

    public static void adjustAdmobLayoutParamsAtOrientation(RelativeLayout admobLayout, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) admobLayout.getLayoutParams();
                if (admobLayoutParams != null) {
                    if (admobLayout.getResources() != null) {
                        admobLayoutParams.height = (int)admobLayout.getResources().getDimension(R.dimen.main_admob_layout_height);
                    }else{
                        Log.e(TAG, "admobLayout.getResources() is null");
                    }
                }else{
                    Log.e(TAG, "admobLayoutParams is null");
                }
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) admobLayout.getLayoutParams();
                if (admobLayoutParams != null) {
                    admobLayoutParams.height = 0;
                }else{
                    Log.e(TAG, "admobLayoutParams is null");
                }
                break;
            }
        }
    }

    public static void adjustAdmobViewAtOrientation(AdView adView, int orientation) {

    }
}
