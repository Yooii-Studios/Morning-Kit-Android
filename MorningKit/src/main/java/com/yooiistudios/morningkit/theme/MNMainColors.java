package com.yooiistudios.morningkit.theme;

import android.graphics.Color;

import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 19.
 *
 * MNColor
 *  각종 컬러값을 얻을 수 있는 클래스
 */
public class MNMainColors {
    private MNMainColors() { throw new AssertionError("You MUST not create this class!"); }

    /**
     * Background Color
     */
    public static int getBackwardBackgroundColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                return Color.TRANSPARENT;

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
                return Color.parseColor("#1afffff");

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
                return Color.parseColor("#7fffffff");
//                return Color.parseColor("#7fffffff");

            case MODERNITY_WHITE:
                return Color.parseColor("#dcdcdc");

            case SLATE_GRAY:
                return Color.parseColor("#a1a1a1");

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#03a3c3");

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

            case PASTEL_GREEN:
                return Color.parseColor("#5ab38c");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    /**
     * Alarm
     */

    // 나중에 알람만 다른 색을 쓰게 될 경우를 대비
    public static int getAlarmMainFontColor(MNThemeType themeType) {
        return getMainFontColor(themeType);
    }

    public static int getAlarmAddTextFontColor(MNThemeType themeType) {
        if (themeType == MNThemeType.CELESTIAL_SKY_BLUE) {
            return Color.parseColor("#043f4b");
        } else if (themeType == MNThemeType.PASTEL_GREEN) {
            return Color.parseColor("#797979");
        } else {
            return getAlarmMainFontColor(themeType);
        }
    }

    public static int getAlarmSubFontColor(MNThemeType themeType) {
        return getSubFontColor(themeType);
    }

    /**
     * Panel
     */
    public static int getWeatherLowHighTextColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return getMainFontColor(themeType);

            case PASTEL_GREEN:
                return getSubFontColor(themeType);

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getWeatherConditionColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return getMainFontColor(themeType);

            case PASTEL_GREEN:
                return getSubFontColor(themeType);

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
