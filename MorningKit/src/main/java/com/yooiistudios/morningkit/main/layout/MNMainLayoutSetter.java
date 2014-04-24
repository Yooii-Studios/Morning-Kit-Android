package com.yooiistudios.morningkit.main.layout;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.ads.AdSize;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeInfo;
import com.yooiistudios.morningkit.main.MNMainActivity;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrix;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrixType;

/**
 * Created by StevenKim on 2013. 11. 10..
 *
 * MNMainLayoutSetter
 *  메인 액티비티의 layout들의 width, height를 계산해주는 유틸리티 클래스
 */
public class MNMainLayoutSetter {
    private final static String TAG = "MNMainLayoutSetter";

    private MNMainLayoutSetter() { throw new AssertionError("You MUST NOT create this class"); }

    public static void adjustScrollViewLayoutParamsAtOrientation(ScrollView scrollView, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                RelativeLayout.LayoutParams scrollViewLayoutParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
                // ABOVE 설정 삭제
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    scrollViewLayoutParams.removeRule(RelativeLayout.ABOVE);
                }else{
                    scrollViewLayoutParams.addRule(RelativeLayout.ABOVE, 0);
                }
                scrollViewLayoutParams.bottomMargin = 0;
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                RelativeLayout.LayoutParams scrollViewLayoutParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
                scrollViewLayoutParams.addRule(RelativeLayout.ABOVE, R.id.main_button_layout);
                // 아래쪽으로 margin_outer - margin_inner 만큼 주어야 윗 마진(margin_outer)과 같아짐
                // -> margin_inner로 panelWindowLayout의 topMargin을 변경
                scrollViewLayoutParams.bottomMargin = (int)(scrollView.getResources().getDimension(R.dimen.margin_inner));
                break;
            }
        }
    }

    public static void adjustPanelLayoutParamsAtOrientation(MNMainActivity mainActivity, int orientation) {
        LinearLayout.LayoutParams panelWindowLayoutParams = (LinearLayout.LayoutParams) mainActivity.getPanelWindowLayout().getLayoutParams();
        panelWindowLayoutParams.height = (int) getPanelWindowLayoutHeight(mainActivity, orientation);

        // 방향에 따른 패널 매트릭스 배열 재정렬
        mainActivity.getPanelWindowLayout().adjustPanelLayoutMatrixAtOrientation(orientation);
    }

    public static void adjustButtonLayoutParamsAtOrientation(RelativeLayout buttonLayout, int orientation) {
        RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
        buttonLayoutParams.height = (int)getButtonLayoutHeight(buttonLayout.getContext(), orientation);
    }

    public static void adjustAdmobLayoutParamsAtOrientation(RelativeLayout admobLayout, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) admobLayout.getLayoutParams();
                if (SKIabProducts.isIabProductBought(SKIabProducts.SKU_NO_ADS, admobLayout.getContext())) {
                    admobLayoutParams.height = 0;
                } else {
                    admobLayoutParams.height = (int) getAdmobLayoutHeightOnPortrait(admobLayout.getContext());
                }
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) admobLayout.getLayoutParams();
                admobLayoutParams.height = 0;
                break;
            }
        }
    }

    public static void adjustAdmobViewAtOrientation(MNMainActivity mainActivity, int orientation) {
        // 구매 여부에 따라 뷰의 표시 여부 결정
        if (SKIabProducts.isIabProductBought(SKIabProducts.SKU_NO_ADS, mainActivity.getApplicationContext())) {
            mainActivity.getAdView().setVisibility(View.GONE);
        } else {
            mainActivity.getAdView().setVisibility(View.VISIBLE);
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
    }

    // 애드몹레이아웃의 width를 체크해 버튼레이아웃과 맞추어주기
    public static void checkAdmobLayoutWidthAndAdjust(final RelativeLayout admobLayout, final RelativeLayout buttonLayout, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                Context context = admobLayout.getContext();
                Resources resources = context.getResources();
//                AdSize adSize = AdSize.createAdSize(AdSize.BANNER, context);
                AdSize adSize = AdSize.BANNER;

                // 고정값을 쓰는 기존 코드에서 마진을 계산해서 두배만큼 버튼 레이아웃의 마진을 주는 새 코드로 변경
                int buttonLayoutMargin = (resources.getDimensionPixelSize(R.dimen.margin_inner) +
                        resources.getDimensionPixelSize(R.dimen.margin_shadow_inner)) * 2;

                int calcaulatedButtonLayoutWidth = MNDeviceSizeInfo.getDeviceWidth(context) -
                        buttonLayoutMargin * 2;
//                (int)(context.getResources().getDimension(R.dimen.margin_main_button_layout) * 2);

                if (adSize.getWidthInPixels(context) <= calcaulatedButtonLayoutWidth) { // buttonLayout.getWidth()) {
                    RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
                    RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) admobLayout.getLayoutParams();
                    if (admobLayoutParams != null && buttonLayoutParams != null) {
                        buttonLayoutParams.leftMargin = buttonLayoutMargin;
                        buttonLayoutParams.rightMargin = buttonLayoutMargin;
                        admobLayoutParams.leftMargin = buttonLayoutParams.leftMargin;
                        admobLayoutParams.rightMargin = buttonLayoutParams.rightMargin;
                    }
                }
                break;
            }
        }
    }

    public static void adjustAlarmListView(MNMainActivity mainActivity, int orientation) {
        LinearLayout.LayoutParams alarmListViewLayoutParams = (LinearLayout.LayoutParams) mainActivity.getAlarmListView().getLayoutParams();

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                // 알람 리스트뷰
                mainActivity.getAlarmListView().setVisibility(View.VISIBLE);

                float contentHeight = getScrollContentHeightExceptAlarmsOnPortrait(mainActivity)
                        + getAlarmListViewHeightOnPortrait(mainActivity);

                int deviceHeight = MNDeviceSizeInfo.getDeviceHeight(mainActivity);
                float bottomLayoutHeight = getBottomLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT);

                // 컨텐츠가 디바이스 높이보다 크거나, 작지만 버튼 레이아웃을 침범할 정도로 높을 때
                if (contentHeight > deviceHeight ||
                        (contentHeight <= deviceHeight && contentHeight > deviceHeight - bottomLayoutHeight)) {
                    // margin_inner를 빼는 이유는 panelWindowLayout에서 topMargin을 주었기에 그만큼 빼주어야 하는 것으로 추정
                    alarmListViewLayoutParams.height = getAlarmListViewHeightOnPortrait(mainActivity) +
                            (int) getBottomLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT) -
                            mainActivity.getResources().getDimensionPixelSize(R.dimen.margin_inner);
                } else {
                    alarmListViewLayoutParams.height = getAlarmListViewHeightOnPortrait(mainActivity);
                }
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                // 알람 리스트뷰
                // Gone: 안보이고 차지한 공간도 사라짐
                // INVISIBLE: 안보이지만 공간은 차지함
                mainActivity.getAlarmListView().setVisibility(View.GONE);
                alarmListViewLayoutParams.height = 0;
                break;
            }
        }
    }

    /**
     * ScrollContentLayout
     * MNAlarmListView의 height만 조절하면 제대로 height 가 자동으로 조절 되어 따로 작업을 하지 않음
     */
    /*
    public static void adjustScrollContentLayoutHeight(MNMainActivity mainActivity, int orientation) {
        LinearLayout scrollContentLayout = mainActivity.getScrollContentLayout();
        Context context = mainActivity.getBaseContext();

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                break;

            case Configuration.ORIENTATION_LANDSCAPE: {
                int deviceHeight = MNDeviceSizeInfo.getDeviceHeight(context);
                int statusBarHeight = MNDeviceSizeInfo.getStatusBarHeight(mainActivity);
                int buttonLayoutHeight = (int)getButtonLayoutHeight(context,
                        Configuration.ORIENTATION_LANDSCAPE);

                Resources resources = mainActivity.getResources();
                int bottomPadding = (int)(resources.getDimension(R.dimen.margin_outer_minus_inner));
                int scrollViewContentHeight = deviceHeight - statusBarHeight - buttonLayoutHeight - bottomPadding;

//                Log.i(TAG, "buttonLayout height: " + mainActivity.getScrollView().getLayoutParams().height);
//                MNLog.i(TAG, "bottomPadding: " + bottomPadding);
//                MNLog.i(TAG, "deviceHeight: " + deviceHeight);
//                MNLog.i(TAG, "buttonLayout height: " + buttonLayoutHeight);
//                MNLog.i(TAG, "scrollViewContentHeight: " + scrollViewContentHeight);

                LinearLayout.LayoutParams panelWindowLayoutParams =
                        (LinearLayout.LayoutParams) mainActivity.getPanelWindowLayout().getLayoutParams();
//                MNLog.i(TAG, "panelWindowLayoutParams.height: " + panelWindowLayoutParams.height);
//                panelWindowLayoutParams.height = scrollViewContentHeight - 20;

//                FrameLayout.LayoutParams scrollContentLayoutParams =
//                        (FrameLayout.LayoutParams) mainActivity.getScrollContentLayout().getLayoutParams();
//                scrollContentLayoutParams.height = scrollViewContentHeight;
//                scrollContentLayoutParams.height = 50;
                break;
            }
        }
    }
    */

    /**
     * Getting height of main layouts & views
     */
    public static float getPanelWindowLayoutHeight(MNMainActivity mainActivity, int orientation) {
        Resources resources = mainActivity.getResources();

        // 높이 조절
        switch (orientation) {
            // 높이 조절을 panel shadow 영역을 포함하게 구현
            case Configuration.ORIENTATION_PORTRAIT:
                // * 2 를 하면 dp 환산 과정에서 제대로 된 결과가 나오지 않기에 일부러
                // int로 환산해서 더해줌
                if (MNPanelMatrix.getCurrentPanelMatrixType(mainActivity.getBaseContext()) ==
                        MNPanelMatrixType.PANEL_MATRIX_2X2) {
                    return (int) resources.getDimension(R.dimen.panel_height) +
                            (int) resources.getDimension(R.dimen.panel_height);
                } else {
                    return (int) resources.getDimension(R.dimen.panel_height) +
                            (int) resources.getDimension(R.dimen.panel_height) +
                            (int) resources.getDimension(R.dimen.panel_height);
                }

            case Configuration.ORIENTATION_LANDSCAPE:
                int deviceHeight = MNDeviceSizeInfo.getDeviceHeight(mainActivity);
                int statusBarHeight = MNDeviceSizeInfo.getStatusBarHeight(mainActivity);
                int bottomLayoutHeight = (int)getBottomLayoutHeight(mainActivity, Configuration.ORIENTATION_LANDSCAPE);
                int marginTop = resources.getDimensionPixelSize(R.dimen.margin_inner);
                return deviceHeight - statusBarHeight - bottomLayoutHeight - marginTop;

            default:
                return -1;
        }
    }

    public static int getAlarmListViewHeightOnPortrait(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.alarm_item_outer_height) *
                (MNAlarmListManager.getAlarmList(context).size() + 1);
    }

    public static float getBottomLayoutHeight(MNMainActivity mainActivity, int orientation) {
        Resources resources = mainActivity.getResources();
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return getButtonLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT) +
                        getAdmobLayoutHeightOnPortrait(mainActivity) +
                        resources.getDimension(R.dimen.margin_inner);

            case Configuration.ORIENTATION_LANDSCAPE:
                return resources.getDimension(R.dimen.margin_outer_minus_inner)
                        + getButtonLayoutHeight(mainActivity, Configuration.ORIENTATION_LANDSCAPE);

            default:
                MNLog.e(TAG, "not expected orientation: " + orientation);
                return -1;
        }
    }

    public static float getAdmobLayoutHeightOnPortrait(Context context) {
        if (SKIabProducts.isIabProductBought(SKIabProducts.SKU_NO_ADS, context)) {
            return 0;
        } else {
            return (int)context.getResources().getDimension(R.dimen.main_admob_layout_height);
        }
    }

    public static float getButtonLayoutHeight(Context context, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return (int) context.getResources().getDimension(R.dimen.main_button_layout_height);

            case Configuration.ORIENTATION_LANDSCAPE:
                if (SKIabProducts.isIabProductBought(SKIabProducts.SKU_NO_ADS, context)) {
                    return (int) context.getResources().getDimension(R.dimen.main_button_layout_height);
                } else {
                    return (int) (context.getResources().getDimension(R.dimen.main_button_layout_height)
                            + context.getResources().getDimension(R.dimen.margin_outer) * 2);
                }

            default: // Test에서 Undefined 사용
                MNLog.e(TAG, "not expected orientation: " + orientation);
                return -1;
        }
    }

    public static float getScrollContentHeightExceptAlarmsOnPortrait(MNMainActivity mainActivity) {
        return getPanelWindowLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT) +
                getBottomLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT);
    }
}
