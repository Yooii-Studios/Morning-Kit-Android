package com.yooiistudios.morningkit.theme;

import android.graphics.Color;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 19.
 *
 * MNColor
 *  각종 컬러값을 얻을 수 있는 클래스
 */
public class MNMainColors {
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

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getPointedFontColor(MNThemeType themeType) {
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

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    /**
     * Panel
     */
}
