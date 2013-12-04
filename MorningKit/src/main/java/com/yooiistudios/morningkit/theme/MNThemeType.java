package com.yooiistudios.morningkit.theme;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNThemeType
 *  모닝키트의 테마를 enum으로 표현
 */
public enum MNThemeType {
    WATER_LILY(0),
    TRANQUILITY_BACK_CAMERA(1),
    REFLECTION_FRONT_CAMERA(2),
    PHOTO(3),
    MODERNITY_WHITE(4),
    SLATE_GRAY(5),
    CELESTIAL_SKY_BLE(6);

    @Getter private final int index;
    MNThemeType(int index) { this.index = index; }
}
