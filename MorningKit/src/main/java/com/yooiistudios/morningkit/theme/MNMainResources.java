package com.yooiistudios.morningkit.theme;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 24.
 *
 * MNMainResources
 *  메인 화면에서 사용되는 각종 리소스를 얻는 클래스
 */
public class MNMainResources {
    private MNMainResources() { throw new AssertionError("You MUST not create this class!"); }

    public static int getAlarmDividingBarResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return R.drawable.dividing_bar_off_translucent_black;

            case WATER_LILY:
                return R.drawable.dividing_bar_off_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.dividing_bar_on_classic_white;

            case SLATE_GRAY:
                return R.drawable.dividing_bar_on_skyblue;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.dividing_bar_off_skyblue;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmDividingBarOnResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return R.drawable.dividing_bar_on_translucent_black;

            case WATER_LILY:
                return R.drawable.dividing_bar_on_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.dividing_bar_on_classic_white;

            case SLATE_GRAY:
                return R.drawable.dividing_bar_on_skyblue;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.dividing_bar_on_skyblue;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmDividingBarOffResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return R.drawable.dividing_bar_off_translucent_black;

            case WATER_LILY:
                return R.drawable.dividing_bar_off_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.dividing_bar_off_classic_white;

            case SLATE_GRAY:
                return R.drawable.dividing_bar_gray_s3;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.dividing_bar_off_skyblue;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmPlusResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return R.drawable.plus_translucent_black;

            case WATER_LILY:
                return R.drawable.plus_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.plus_classic_white;

            case SLATE_GRAY:
                return R.drawable.plus_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.plus_skyblue;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmSwitchButtonSelectorResourcesId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return R.drawable.alarm_switch_button_selector_translucent_black;

            case WATER_LILY:
                return R.drawable.alarm_switch_button_selector_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.alarm_switch_button_selector_classic_white;

            case SLATE_GRAY:
                return R.drawable.alarm_switch_button_selector_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.alarm_switch_button_selector_skyblue;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmItemSelectorResourcesId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return R.drawable.shape_rounded_view_classic_gray;

            case WATER_LILY:
                return R.drawable.shape_rounded_view_classic_gray;

            case MODERNITY_WHITE:
                return R.drawable.shape_rounded_view_classic_gray;

            case SLATE_GRAY:
                return R.drawable.shape_rounded_view_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.shape_rounded_view_classic_gray;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
