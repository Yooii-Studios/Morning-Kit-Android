package com.yooiistudios.morningkit.theme;

import com.yooiistudios.morningkit.MN;

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
    WATER_LILY(0, MN.theme.WATER_LILY_ID, "WATER_LILY"),
    TRANQUILITY_BACK_CAMERA(1, MN.theme.TRANQUILITY_BACK_CAMERA_ID, "TRANQUILITY_BACK_CAMERA"),
    REFLECTION_FRONT_CAMERA(2, MN.theme.REFLECTION_FRONT_CAMERA_ID, "REFLECTION_FRONT_CAMERA"),
    PHOTO(3, MN.theme.PHOTO_ID, "PHOTO"),
    MODERNITY_WHITE(4, MN.theme.MODERNITY_WHITE_ID, "MODERNITY_WHITE"),
    SLATE_GRAY(5, MN.theme.SLATE_GRAY_ID, "SLATE_GRAY"),
    CELESTIAL_SKY_BLE(6, MN.theme.CELESTIAL_SKY_BLUE_ID, "CELESTIAL_SKY_BLE");

    @Getter private final int index;
    @Getter private final int uniqueId;
    @Getter private final String key;
    MNThemeType(int index, int uniqueId, String key) {
        this.index = index;
        this.uniqueId = uniqueId;
        this.key = key;
    }
}
