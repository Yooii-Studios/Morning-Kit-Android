package com.yooiistudios.morningkit.main.layout;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.ads.AdSize;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeChecker;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.main.MNMainActivity;
import com.yooiistudios.morningkit.main.MNMainAlarmListView;
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

    public static void adjustAdmobViewAtOrientation(MNMainActivity mainActivity, int orientation) {
        switch (orientation) {
            // 버튼 레이아웃에 광고가 있을 경우 애드몹 레이아웃으로 옮기기
            case Configuration.ORIENTATION_PORTRAIT:
                if (mainActivity.getButtonLayout().findViewById(R.id.adView) != null) {
                    mainActivity.getButtonLayout().removeView(mainActivity.getAdView());
                    mainActivity.getAdmobLayout().addView(mainActivity.getAdView());
                }
                break;
            // Landscape 모드에서 버튼 레이아웃으로 광고 옮기기
            case Configuration.ORIENTATION_LANDSCAPE:
                if (mainActivity.getAdmobLayout().findViewById(R.id.adView) != null) {
                    mainActivity.getAdmobLayout().removeView(mainActivity.getAdView());
                    mainActivity.getButtonLayout().addView(mainActivity.getAdView());
                }
                break;
        }
    }

    // 애드몹레이아웃의 width를 체크해 버튼레이아웃과 맞추어주기
    public static void checkAdmobLayoutWidthAndAdjust(final RelativeLayout admobLayout, final RelativeLayout buttonLayout, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                final Context context = admobLayout.getContext();
                MNViewSizeMeasure.setViewSizeObserver(admobLayout, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
                    @Override
                    public void onLayoutLoad() {
                        AdSize adSize = AdSize.createAdSize(AdSize.BANNER, context);
                        if (admobLayout.getWidth() > adSize.getWidthInPixels(context)) {
//                            Log.i(TAG, "AdMobLayout.getWidth() is bigger than adSize.getWidth()");
                            // 1. 버튼 레이아웃 width 보다 광고뷰 width가 더 짧을 경우는 버튼 레이아웃에 맞추어줌
                            if (adSize.getWidthInPixels(context) <= buttonLayout.getWidth()) {
//                                Log.i(TAG, "adSize.getWidth() is shorter than buttonLayout.getWidth()");
                                RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
                                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) admobLayout.getLayoutParams();
                                if (admobLayoutParams != null && buttonLayoutParams != null) {
                                    admobLayoutParams.leftMargin = buttonLayoutParams.leftMargin;
                                    admobLayoutParams.rightMargin = buttonLayoutParams.rightMargin;
                                }
                            }
                            // 2. 더 넓을 경우는 match_parent 그대로 놔두어야 할듯(기본)
                        }
                    }
                });
                break;
            }
        }
    }

    public static void adjustAlarmListView(MNMainAlarmListView alarmListView, MNWidgetWindowLayout widgetWindowLayout, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {

                // 알람 리스트뷰
                alarmListView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams alarmListViewLayoutParams = (LinearLayout.LayoutParams) alarmListView.getLayoutParams();
                if (alarmListViewLayoutParams != null) {
                    float alarmListViewHeight = MNDeviceSizeChecker.getDeviceHeight(alarmListView.getContext()) - widgetWindowLayout.getLayoutParams().height;
                    alarmListViewLayoutParams.height = (int)alarmListViewHeight;
                }
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {

                // 알람 리스트뷰
                // Gone: 안보이고 차지한 공간도 사라짐
                // INVISIBLE: 안보이지만 공간은 차지함
                alarmListView.setVisibility(View.GONE);
                break;
            }
        }
    }
}
