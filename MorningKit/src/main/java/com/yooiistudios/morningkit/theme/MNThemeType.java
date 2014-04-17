package com.yooiistudios.morningkit.theme;

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

    WATER_LILY(0, MNThemeId.WATER_LILY_ID, "WATER_LILY"),
    TRANQUILITY_BACK_CAMERA(1, MNThemeId.TRANQUILITY_BACK_CAMERA_ID, "TRANQUILITY_BACK_CAMERA"),
    REFLECTION_FRONT_CAMERA(2, MNThemeId.REFLECTION_FRONT_CAMERA_ID, "REFLECTION_FRONT_CAMERA"),
    PHOTO(3, MNThemeId.PHOTO_ID, "PHOTO"),
    MODERNITY_WHITE(4, MNThemeId.MODERNITY_WHITE_ID, "MODERNITY_WHITE"),
    SLATE_GRAY(5, MNThemeId.SLATE_GRAY_ID, "SLATE_GRAY"),
    CELESTIAL_SKY_BLE(6, MNThemeId.CELESTIAL_SKY_BLUE_ID, "CELESTIAL_SKY_BLE");

    @Getter private final int index;
    @Getter private final int uniqueId;
    @Getter private final String key;

    public final static int WATER_LILY_ID = 0;

    MNThemeType(int index, int uniqueId, String key) {
        this.index = index;
        this.uniqueId = uniqueId;
        this.key = key;
    }
}

class MNThemeId {
    public final static int WATER_LILY_ID = 0;
    public final static int TRANQUILITY_BACK_CAMERA_ID = 1;
    public final static int REFLECTION_FRONT_CAMERA_ID = 2;
    public final static int PHOTO_ID = 3;
    public final static int MODERNITY_WHITE_ID = 4;
    public final static int SLATE_GRAY_ID = 5;
    public final static int CELESTIAL_SKY_BLUE_ID = 6;
}
