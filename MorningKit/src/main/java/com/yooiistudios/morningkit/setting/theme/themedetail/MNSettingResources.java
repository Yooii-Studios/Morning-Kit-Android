package com.yooiistudios.morningkit.setting.theme.themedetail;

import com.yooiistudios.morningkit.R;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 17.
 *
 * MNSettingResources
 *  세팅 화면에서 테마에 적합한 이미지 리소스를 반환
 */
public class MNSettingResources {
    private MNSettingResources() { throw new AssertionError("You MUST not create this class!"); }

    public static int getCheckResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.setting_theme_check_classic_white;

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return R.drawable.setting_theme_check_classic_gray;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getLockResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.setting_theme_lock_classic_white;

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return R.drawable.setting_theme_lock_classic_gray;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getPanelSelectPagerLockResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.panel_lock_icon_classic_white;

            case SLATE_GRAY:
                return R.drawable.panel_lock_icon_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.panel_lock_icon_skyblue;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    /**
     * SettingPanlMatrixItem Resources
     */

    public static int getWeatherResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.widget_cover_weather_black_ipad;

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return R.drawable.widget_cover_weather_white_ipad;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getDateResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.widget_cover_calendar_black_ipad;

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return R.drawable.widget_cover_calendar_white_ipad;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getCalendarResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.widget_cover_reminder_black_ipad;

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return R.drawable.widget_cover_reminder_white_ipad;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getWorldClockResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.widget_cover_worldclock_black_ipad;

            case SLATE_GRAY:
                return R.drawable.widget_cover_worldclock_white_ipad;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.widget_cover_worldclock_skyblue_ipad;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getQuotesResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.widget_cover_quotes_black_ipad;

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return R.drawable.widget_cover_quotes_white_ipad;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getFlickrResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.widget_cover_flickr_black_ipad;

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return R.drawable.widget_cover_flickr_white_ipad;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getExchangeRatesResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.widget_cover_exchangerates_black_ipad;

            case SLATE_GRAY:
                return R.drawable.widget_cover_exchangerates_white_ipad;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.widget_cover_exchangerates_skyblue_ipad;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getMemoResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.widget_cover_memo_black_ipad;

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return R.drawable.widget_cover_memo_white_ipad;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getDateCountdownResourceId(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return R.drawable.widget_cover_datecountdown_black_ipad;

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return R.drawable.widget_cover_datecountdown_white_ipad;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
