package com.yooiistudios.morningkit.main.layout;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.ads.AdSize;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.alarm.model.list.MNAlarmListManager;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeInfo;
import com.yooiistudios.morningkit.main.MNMainActivity;

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
                scrollViewLayoutParams.bottomMargin = (int)(scrollView.getResources().getDimension(R.dimen.margin_outer_minus_inner));
                break;
            }
        }
    }

    public static void adjustWidgetLayoutParamsAtOrientation(MNMainActivity mainActivity, int orientation) {
        LinearLayout.LayoutParams widgetWindowLayoutParams = (LinearLayout.LayoutParams) mainActivity.getWidgetWindowLayout().getLayoutParams();
        widgetWindowLayoutParams.height = (int) getWidgetWindowLayoutHeight(mainActivity, orientation);
    }

    public static void adjustButtonLayoutParamsAtOrientation(RelativeLayout buttonLayout, int orientation) {
        RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
        buttonLayoutParams.height = (int)getButtonLayoutHeight(buttonLayout.getContext(), orientation);
    }

    public static void adjustAdmobLayoutParamsAtOrientation(RelativeLayout admobLayout, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) admobLayout.getLayoutParams();
                admobLayoutParams.height = (int) getAdmobLayoutHeightOnPortrait(admobLayout.getContext());
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
                Context context = admobLayout.getContext();
                AdSize adSize = AdSize.createAdSize(AdSize.BANNER, context);
                int calcaulatedButtonLayoutWidth;
                calcaulatedButtonLayoutWidth = MNDeviceSizeInfo.getDeviceWidth(context) - (int)(context.getResources().getDimension(R.dimen.margin_main_button_layout) * 2);

                if (adSize.getWidthInPixels(context) <= calcaulatedButtonLayoutWidth) { // buttonLayout.getWidth()) {
                    RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
                    RelativeLayout.LayoutParams admobLayoutParams = (RelativeLayout.LayoutParams) admobLayout.getLayoutParams();
                    if (admobLayoutParams != null && buttonLayoutParams != null) {
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

                if (contentHeight > deviceHeight) {
//                    Log.i(TAG, "contentHeight > deviceHeight");
                    alarmListViewLayoutParams.height =
                            (int) (getAlarmListViewHeightOnPortrait(mainActivity) + getBottomLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT));
                } else {
//                    Log.i(TAG, "contentHeight <= deviceHeight");
                    alarmListViewLayoutParams.height = (int) getAlarmListViewHeightOnPortrait(mainActivity);
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
    public static void adjustScrollContentLayoutHeight(MNMainActivity mainActivity, int orientation) {
        LinearLayout scrollContentLayout = mainActivity.getScrollContentLayout();
        /*
        Context context = mainActivity.getBaseContext();

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                break;
            case Configuration.ORIENTATION_LANDSCAPE: {
                int deviceHeight = MNDeviceSizeInfo.getDeviceHeight(context);

                Resources resources = mainActivity.getResources();
                int bottomPadding = (int)(resources.getDimension(R.dimen.margin_outer) - resources.getDimension(R.dimen.margin_inner));

//                Log.i(TAG, "buttonLayout height: " + mainActivity.getScrollView().getLayoutParams().height);
                Log.i(TAG, "bottomPadding: " + bottomPadding);
                Log.i(TAG, "deviceHeight: " + deviceHeight);
                Log.i(TAG, "scrollView height: " + mainActivity.getScrollView().getLayoutParams().height);
                Log.i(TAG, "buttonLayout height: " + (int)adjustButtonLayoutParamsAtOrientation(mainActivity.getButtonLayout(), Configuration.ORIENTATION_LANDSCAPE));
                int scrollViewHeight = deviceHeight - (int)adjustButtonLayoutParamsAtOrientation(mainActivity.getButtonLayout(), Configuration.ORIENTATION_LANDSCAPE) - bottomPadding;
                Log.i(TAG, "scrollViewHeight: " + scrollViewHeight);

                LinearLayout.LayoutParams widgetWindowLayoutParams = (LinearLayout.LayoutParams) mainActivity.getWidgetWindowLayout().getLayoutParams();
                FrameLayout.LayoutParams scrollContentLayoutParams = (FrameLayout.LayoutParams) mainActivity.getScrollContentLayout().getLayoutParams();
                widgetWindowLayoutParams.height = scrollViewHeight;
                scrollContentLayoutParams.height = scrollViewHeight;
                break;
            }
        }
        */
    }

    /**
     * Getting height of main layouts & views
     */
    public static float getWidgetWindowLayoutHeight(MNMainActivity mainActivity, int orientation) {
        Resources resources = mainActivity.getResources();

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return resources.getDimension(R.dimen.widget_height) * 2
                        + resources.getDimension(R.dimen.margin_outer)
                        + resources.getDimension(R.dimen.margin_outer)
                        + resources.getDimension(R.dimen.margin_inner);

            case Configuration.ORIENTATION_LANDSCAPE:
                int deviceHeight = MNDeviceSizeInfo.getDeviceHeight(mainActivity);
                int statusBarHeight = MNDeviceSizeInfo.getStatusBarHeight(mainActivity);
                int bottomLayoutHeight = (int)getBottomLayoutHeight(mainActivity, Configuration.ORIENTATION_LANDSCAPE);
                return deviceHeight - statusBarHeight - bottomLayoutHeight;

            default:
                return -1;
        }
    }

    public static float getAlarmListViewHeightOnPortrait(Context context) {
        return context.getResources().getDimension(R.dimen.alarm_item_outer_height) * (MNAlarmListManager.getAlarmList(context).size() + 1);
    }

    public static float getBottomLayoutHeight(MNMainActivity mainActivity, int orientation) {
        Resources resources = mainActivity.getResources();
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return resources.getDimension(R.dimen.margin_outer_minus_inner)
                        + getButtonLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT)
                        + getAdmobLayoutHeightOnPortrait(mainActivity);

            case Configuration.ORIENTATION_LANDSCAPE:
                return resources.getDimension(R.dimen.margin_outer_minus_inner)
                        + getButtonLayoutHeight(mainActivity, Configuration.ORIENTATION_LANDSCAPE);

            default:
                Log.e(TAG, "not expected orientation: " + orientation);
                return -1;
        }
    }

    public static float getAdmobLayoutHeightOnPortrait(Context context) {
        return (int)context.getResources().getDimension(R.dimen.main_admob_layout_height);
    }

    public static float getButtonLayoutHeight(Context context, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return (int) context.getResources().getDimension(R.dimen.main_button_layout_height);

            case Configuration.ORIENTATION_LANDSCAPE:
                return (int) (context.getResources().getDimension(R.dimen.main_button_layout_height)
                                + context.getResources().getDimension(R.dimen.margin_outer) * 2);

            default: // Test에서 Undefined 사용
                Log.e(TAG, "not expected orientation: " + orientation);
                return -1;
        }
    }

    public static float getScrollContentHeightExceptAlarmsOnPortrait(MNMainActivity mainActivity) {
        return getWidgetWindowLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT)
                + getBottomLayoutHeight(mainActivity, Configuration.ORIENTATION_PORTRAIT);
    }
}
