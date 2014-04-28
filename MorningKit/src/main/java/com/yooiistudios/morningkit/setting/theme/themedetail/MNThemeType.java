package com.yooiistudios.morningkit.setting.theme.themedetail;

import android.content.Context;

import com.yooiistudios.morningkit.R;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNThemeType
 *  모닝키트의 테마를 enum으로 표현
 *  index = 설정 창에서 순서를 표현
 *  uniqueId = 이 테마의 고유 id를 표시
 */
public enum MNThemeType {
    WATER_LILY(0, 0),
    TRANQUILITY_BACK_CAMERA(1, 1),
    REFLECTION_FRONT_CAMERA(2, 2),
    PHOTO(3, 3),
    MODERNITY_WHITE(4, 4),
    SLATE_GRAY(5, 5),
    CELESTIAL_SKY_BLUE(6, 6),
    PASTEL_GREEN(7, 7);

    @Getter private final int index;
    @Getter private final int uniqueId;

    MNThemeType(int index, int uniqueId) {
        this.index = index;
        this.uniqueId = uniqueId;
    }

    public static MNThemeType valueOf(int index) {
        switch (index) {
            case 0: return WATER_LILY;
            case 1: return TRANQUILITY_BACK_CAMERA;
            case 2: return REFLECTION_FRONT_CAMERA;
            case 3: return PHOTO;
            case 4: return MODERNITY_WHITE;
            case 5: return SLATE_GRAY;
            case 6: return CELESTIAL_SKY_BLUE;
            case 7: return PASTEL_GREEN;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static MNThemeType valueOfUniqueId(int uniqueId) {
        switch (uniqueId) {
            case 0: return WATER_LILY;
            case 1: return TRANQUILITY_BACK_CAMERA;
            case 2: return REFLECTION_FRONT_CAMERA;
            case 3: return PHOTO;
            case 4: return MODERNITY_WHITE;
            case 5: return SLATE_GRAY;
            case 6: return CELESTIAL_SKY_BLUE;
            case 7: return PASTEL_GREEN;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static String toString(int position, Context context) {
        switch (position) {
            case 0: return context.getString(R.string.store_item_water_lily);
            case 1: return context.getString(R.string.setting_theme_scenery);
            case 2: return context.getString(R.string.setting_theme_mirror);
            case 3: return context.getString(R.string.setting_theme_photo);
            case 4: return context.getString(R.string.setting_theme_color_classic_white);
            case 5: return context.getString(R.string.setting_theme_color_classic_gray);
            case 6: return context.getString(R.string.setting_theme_color_skyblue);
            case 7: return context.getString(R.string.setting_theme_color_pastel_green);
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
