package com.yooiistudios.morningkit.theme;

import android.content.Context;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.font.MNTranslucentFont;
import com.yooiistudios.morningkit.theme.font.MNTranslucentFontType;

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

            case COOL_NAVY:
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

            case COOL_NAVY:
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

    // World Clock
    public static int getWorldClockAMBase(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case CELESTIAL_SKY_BLUE:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.clock_base_am_gray;
                } else {
                    return R.drawable.clock_base_pm_white;
                }

            case SLATE_GRAY:
                return R.drawable.clock_base_am_gray;

            case WATER_LILY:
            case MODERNITY_WHITE:
                return R.drawable.clock_base_am_white;

            case PASTEL_GREEN:
                return R.drawable.clock_base_am_pastel_green;

            case COOL_NAVY:
                return R.drawable.clock_base_am_cool_navy;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getWorldClockPMBase(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case CELESTIAL_SKY_BLUE:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.clock_base_am_gray;
                } else {
                    return R.drawable.clock_base_pm_white;
                }

            case SLATE_GRAY:
                return R.drawable.clock_base_pm_gray;

            case WATER_LILY:
            case MODERNITY_WHITE:
                return R.drawable.clock_base_pm_white;

            case PASTEL_GREEN:
                return R.drawable.clock_base_pm_pastel_green;

            case COOL_NAVY:
                return R.drawable.clock_base_pm_cool_navy;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getWorldClockAMHourHand(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case CELESTIAL_SKY_BLUE:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.clock_hand_hour_gray;
                } else {
                    return R.drawable.clock_hand_hour_white;
                }

            case SLATE_GRAY:
                return R.drawable.clock_hand_hour_gray;

            case WATER_LILY:
            case MODERNITY_WHITE:
                return R.drawable.clock_hand_hour_white;

            case PASTEL_GREEN:
                return R.drawable.clock_hand_hour_am_pastel_green;

            case COOL_NAVY:
                return R.drawable.clock_hand_hour_am_cool_navy;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getWorldClockPMHourHand(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case CELESTIAL_SKY_BLUE:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.clock_hand_hour_gray;
                } else {
                    return R.drawable.clock_hand_hour_white;
                }

            case SLATE_GRAY:
                return R.drawable.clock_hand_hour_gray;

            case WATER_LILY:
            case MODERNITY_WHITE:
                return R.drawable.clock_hand_hour_white;

            case PASTEL_GREEN:
                return R.drawable.clock_hand_hour_pm_pastel_green;

            case COOL_NAVY:
                return R.drawable.clock_hand_hour_pm_cool_navy;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getWorldClockAMMinuteHand(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case CELESTIAL_SKY_BLUE:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.clock_hand_minute_gray;
                } else {
                    return R.drawable.clock_hand_minute_white;
                }

            case SLATE_GRAY:
                return R.drawable.clock_hand_minute_gray;

            case WATER_LILY:
            case MODERNITY_WHITE:
                return R.drawable.clock_hand_minute_white;

            case PASTEL_GREEN:
                return R.drawable.clock_hand_minute_am_pastel_green;

            case COOL_NAVY:
                return R.drawable.clock_hand_minute_am_cool_navy;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getWorldClockPMMinuteHand(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case CELESTIAL_SKY_BLUE:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.clock_hand_minute_gray;
                } else {
                    return R.drawable.clock_hand_minute_white;
                }

            case SLATE_GRAY:
                return R.drawable.clock_hand_minute_gray;

            case WATER_LILY:
            case MODERNITY_WHITE:
                return R.drawable.clock_hand_minute_white;

            case PASTEL_GREEN:
                return R.drawable.clock_hand_minute_pm_pastel_green;

            case COOL_NAVY:
                return R.drawable.clock_hand_minute_pm_cool_navy;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getWorldClockAMSecondHand(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case CELESTIAL_SKY_BLUE:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.clock_hand_second_gray;
                } else {
                    return R.drawable.clock_hand_second_white;
                }

            case SLATE_GRAY:
                return R.drawable.clock_hand_second_gray;

            case WATER_LILY:
            case MODERNITY_WHITE:
                return R.drawable.clock_hand_second_white;

            case PASTEL_GREEN:
                return R.drawable.clock_hand_second_am_pastel_green;

            case COOL_NAVY:
                return R.drawable.clock_hand_second_am_cool_navy;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getWorldClockPMSecondHand(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
            case CELESTIAL_SKY_BLUE:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.clock_hand_second_gray;
                } else {
                    return R.drawable.clock_hand_second_white;
                }

            case SLATE_GRAY:
                return R.drawable.clock_hand_second_gray;

            case WATER_LILY:
            case MODERNITY_WHITE:
                return R.drawable.clock_hand_second_white;

            case PASTEL_GREEN:
                return R.drawable.clock_hand_second_pm_pastel_green;

            case COOL_NAVY:
                return R.drawable.clock_hand_second_pm_cool_navy;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    /**
     * Alarm
     */
    public static int getAddAlarmDividingBarResourceId(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.alarm_dividing_bar_on_skyblue;
                } else {
                    return R.drawable.alarm_dividing_bar_on_translucent_black;
                }

            case WATER_LILY:
                return R.drawable.alarm_dividing_bar_off_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.alarm_dividing_bar_on_classic_white;

            case SLATE_GRAY:
            case COOL_NAVY:
                return R.drawable.alarm_dividing_bar_on_skyblue;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.alarm_dividing_bar_off_skyblue;

            case PASTEL_GREEN:
                return R.drawable.alarm_dividing_bar_add_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmDividingBarOnResourceId(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.alarm_dividing_bar_on_skyblue;
                } else {
                    return R.drawable.alarm_dividing_bar_on_translucent_black;
                }

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

            case COOL_NAVY:
                return R.drawable.alarm_dividing_bar_on_cool_navy;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmDividingBarOffResourceId(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.alarm_dividing_bar_off_classic_white;
                } else {
                    return R.drawable.alarm_dividing_bar_off_translucent_black;
                }

            case WATER_LILY:
                return R.drawable.alarm_dividing_bar_off_translucent_black;

            case MODERNITY_WHITE:
                return R.drawable.alarm_dividing_bar_off_classic_white;

            case SLATE_GRAY:
                return R.drawable.alarm_dividing_bar_off_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.alarm_dividing_bar_off_skyblue;

            case PASTEL_GREEN:
            case COOL_NAVY:
                return R.drawable.alarm_dividing_bar_off_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmPlusResourceId(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.alarm_plus_classic_gray;
                } else {
                    return R.drawable.alarm_plus_classic_white;
                }

            case WATER_LILY:
                return R.drawable.alarm_plus_classic_white;

            case MODERNITY_WHITE:
                return R.drawable.alarm_plus_classic_white;

            case SLATE_GRAY:
            case COOL_NAVY:
                return R.drawable.alarm_plus_classic_gray;

            case CELESTIAL_SKY_BLUE:
                return R.drawable.alarm_plus_skyblue;

            case PASTEL_GREEN:
                return R.drawable.alarm_plus_pastel_green;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static int getAlarmSwitchButtonSelectorResourceId(MNThemeType themeType, Context context) {
        switch (themeType) {
            case TRANQUILITY_BACK_CAMERA:
            case REFLECTION_FRONT_CAMERA:
            case PHOTO:
                if (MNTranslucentFont.getCurrentFontType(context) == MNTranslucentFontType.WHITE) {
                    return R.drawable.alarm_switch_button_selector_translucent_white;
                } else {
                    return R.drawable.alarm_switch_button_selector_translucent_black;
                }
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

            case COOL_NAVY:
                return R.drawable.alarm_switch_button_selector_cool_navy;

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

            case COOL_NAVY:
                return R.drawable.shape_rounded_view_cool_navy;

            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
