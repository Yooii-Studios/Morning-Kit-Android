package com.yooiistudios.morningkit.theme;

import android.content.Context;
import android.graphics.Color;

import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.font.MNTranslucentFont;
import com.yooiistudios.morningkit.theme.font.MNTranslucentFontType;

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

    public static int getButtonLayoutBackgroundColor(MNThemeType themeType) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
            case SLATE_GRAY:
                return Color.parseColor("#CC000000");

            case PASTEL_GREEN:
                return Color.parseColor("#CC5ab38c"); // 아직 더 조정할 필요가 있음

            case CELESTIAL_SKY_BLUE:
                return Color.parseColor("#E5043f4b");

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    // forward 쪽은 이제 selector로 처리
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
    public static int getMainFontColor(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return Color.parseColor("#ffffff");
                } else {
                    return Color.parseColor("#333333");
                }

            case WATER_LILY:
                return Color.parseColor("#333333");

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

    public static int getSubFontColor(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return Color.parseColor("#cccccc");
                } else {
                    return Color.parseColor("#666666");
                }

            case WATER_LILY:
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
    public static int getAlarmMainFontColor(MNThemeType themeType, Context context) {
        return getMainFontColor(themeType, context);
    }

    public static int getAlarmAddTextFontColor(MNThemeType themeType, Context context) {
        if (themeType == MNThemeType.CELESTIAL_SKY_BLUE) {
            return Color.parseColor("#043f4b");
        } else if (themeType == MNThemeType.PASTEL_GREEN) {
            return Color.parseColor("#797979");
        } else {
            return getAlarmMainFontColor(themeType, context);
        }
    }

    public static int getAlarmSubFontColor(MNThemeType themeType, Context context) {
        switch (themeType) {
            case PASTEL_GREEN:
                return Color.parseColor("#c1c1c1");

            default:
                return getSubFontColor(themeType, context);
        }
    }

    /**
     * Panel
     */
    public static int getWeatherLowHighTextColor(MNThemeType themeType, Context context) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return getMainFontColor(themeType, context);

            case PASTEL_GREEN:
                return getSubFontColor(themeType, context);

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getWeatherConditionColor(MNThemeType themeType, Context context) {
        switch (themeType) {
            case WATER_LILY:
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case MODERNITY_WHITE:
            case SLATE_GRAY:
            case CELESTIAL_SKY_BLUE:
                return getMainFontColor(themeType, context);

            case PASTEL_GREEN:
                return getSubFontColor(themeType, context);

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
