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

    /**
     * Main
     */
    
    public static int getRefreshButtonSelectorResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case WATER_LILY:
                return R.drawable.main_refresh_button_selector_translucent;

            case MODERNITY_WHITE:
                return R.drawable.main_refresh_button_selector_classic_white;

            case SLATE_GRAY:
                return R.drawable.main_refresh_button_selector_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.main_refresh_button_selector_skyblue;

            case PASTEL_GREEN:
                return R.drawable.main_refresh_button_selector_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getSettingButtonSelectorResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case WATER_LILY:
                return R.drawable.main_setting_button_selector_translucent;

            case MODERNITY_WHITE:
                return R.drawable.main_setting_button_selector_classic_white;

            case SLATE_GRAY:
                return R.drawable.main_setting_button_selector_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.main_setting_button_selector_skyblue;

            case PASTEL_GREEN:
                return R.drawable.main_setting_button_selector_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    /**
     * Panel
     */
    public static int getPanelLayoutSelectorResourceId(MNThemeType themeType) {
        return getAlarmItemSelectorResourceId(themeType);
    }

    /**
     * Alarm
     */
    public static int getAddAlarmDividingBarResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return R.drawable.alarm_dividing_bar_off_translucent_black;

            case WATER_LILY:
                return R.drawable.alarm_dividing_bar_off_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.alarm_dividing_bar_on_classic_white;

            case SLATE_GRAY:
                return R.drawable.alarm_dividing_bar_on_skyblue;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.alarm_dividing_bar_off_skyblue;

            case PASTEL_GREEN:
                return R.drawable.alarm_dividing_bar_add_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmDividingBarOnResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return R.drawable.alarm_dividing_bar_on_translucent_black;

            case WATER_LILY:
                return R.drawable.alarm_dividing_bar_on_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.alarm_dividing_bar_on_classic_white;

            case SLATE_GRAY:
                return R.drawable.alarm_dividing_bar_on_skyblue;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.alarm_dividing_bar_on_skyblue;

            case PASTEL_GREEN:
                return R.drawable.alarm_dividing_bar_on_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmDividingBarOffResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return R.drawable.alarm_dividing_bar_off_translucent_black;

            case WATER_LILY:
                return R.drawable.alarm_dividing_bar_off_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.alarm_dividing_bar_off_classic_white;

            case SLATE_GRAY:
                return R.drawable.alarm_dividing_bar_off_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.alarm_dividing_bar_off_skyblue;

            case PASTEL_GREEN:
                return R.drawable.alarm_dividing_bar_off_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmPlusResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return R.drawable.alarm_plus_translucent_black;

            case WATER_LILY:
                return R.drawable.alarm_plus_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.alarm_plus_classic_white;

            case SLATE_GRAY:
                return R.drawable.alarm_plus_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.alarm_plus_skyblue;

            case PASTEL_GREEN:
                return R.drawable.alarm_plus_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmSwitchButtonSelectorResourceId(MNThemeType themeType) {
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

            case PASTEL_GREEN:
                return R.drawable.alarm_switch_button_selector_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmItemSelectorResourceId(MNThemeType themeType) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case WATER_LILY:
                return R.drawable.shape_rounded_view_translucent;

            case MODERNITY_WHITE:
                return R.drawable.shape_rounded_view_classic_white;

            case SLATE_GRAY:
                return R.drawable.shape_rounded_view_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.shape_rounded_view_skyblue;

            case PASTEL_GREEN:
                return R.drawable.shape_rounded_view_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
