package com.yooiistudios.morningkit.setting.theme.themedetail;

import android.graphics.Color;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 16.
 *
 * MNColors
 *  테마에 따른 색깔 값을 얻는 클래스
 */
public class MNSettingColors {
    private MNSettingColors() { throw new AssertionError("You MUST not create this class!"); }

    /**
     * Background Color
     */
    public static int getBackwardBackgroundColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return Color.parseColor("#e8e8e8");

            case MODERNITY_WHITE:
                return Color.parseColor("#e8e8e8");

            case SLATE_GRAY:
                return Color.parseColor("#333333");

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#049ebd");

            case PASTEL_GREEN:
                return Color.parseColor("#ffffff");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getForwardBackgroundColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
//                return 0x20ffffff;
                return Color.parseColor("#e8e8e8");

            case MODERNITY_WHITE:
                return Color.parseColor("#e8e8e8");

            case SLATE_GRAY:
                return Color.parseColor("#434343");

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#01afd2");

            case PASTEL_GREEN:
                return Color.parseColor("#ffffff");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getPressedBackgroundColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return Color.parseColor("#dcdcdc");
//                return Color.parseColor("#7fffffff");

            case MODERNITY_WHITE:
                return Color.parseColor("#dcdcdc");

            case SLATE_GRAY:
                return Color.parseColor("#a1a1a1");

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#03a3c3");

            case PASTEL_GREEN:
                return Color.parseColor("#f0f0f0f");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getLockedBackgroundColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return Color.parseColor("#cfcfcf");

            case SLATE_GRAY:
                return Color.parseColor("#343434");

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#0091ae");

            case PASTEL_GREEN:
                return Color.parseColor("#f0f0f0");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    /**
     * Font Color
     */
    public static int getMainFontColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return Color.parseColor("#252525");

            case MODERNITY_WHITE:
                return Color.parseColor("#252525");

            case SLATE_GRAY:
                return Color.parseColor("#ffffff");

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#ffffff");

            case PASTEL_GREEN:
                return Color.parseColor("#5ab38c");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getSubFontColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return Color.parseColor("#918f8f");

            case MODERNITY_WHITE:
                return Color.parseColor("#a5a5a5");

            case SLATE_GRAY:
                return Color.parseColor("#666666");

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#e4e4e4");

            case PASTEL_GREEN:
                return Color.parseColor("#797979");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getLockedFontColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return Color.parseColor("#797979");

            case SLATE_GRAY:
                return Color.parseColor("#888888");

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#043f4b");

            case PASTEL_GREEN:
                return Color.parseColor("#797979");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getStorePointedFontColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
            case SLATE_GRAY:
                return Color.parseColor("#00cfff");

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#25f1c3");

            case PASTEL_GREEN:
                return Color.parseColor("#00cfff");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    /**
     * Panel
     */
    public static int getDefaultPanelSelectIndicatorColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return Color.parseColor("#c3c3c3");

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#4cffffff");

            case PASTEL_GREEN:
                return Color.parseColor("#c3c3c3");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getCurrentPanelSelectIndicatorColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return Color.parseColor("#989898");

            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#ffffff");

            case PASTEL_GREEN:
                return Color.parseColor("#989898");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getGuidedPanelColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return Color.parseColor("#ffffff");

            case SLATE_GRAY:
                return Color.parseColor("#5b5b5b");

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#21c8ea");

            case PASTEL_GREEN:
                return Color.parseColor("#f0f0f0");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getExchangeRatesForwardColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
                return Color.parseColor("#99cfcfcf");

            case PASTEL_GREEN:
                return Color.parseColor("#f0f0f0");

            default:
                return getForwardBackgroundColor(themeType);
        }
    }
}
