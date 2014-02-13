package com.yooiistudios.morningkit.setting.theme;

import lombok.Getter;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 7.
 *
 * MNInfoItemType
 */
public enum MNThemeItemType {
    THEME(0), LANGUAGE(1), PANEL_MATRIX(2), SOUND_EFFECTS(3);

    @Getter
    private final int index;
    MNThemeItemType(int index) { this.index = index; }

    public static MNThemeItemType valueOf(int index) {

        switch (index) {
            case 0: return THEME;
            case 1: return LANGUAGE;
            case 2: return PANEL_MATRIX;
            case 3: return SOUND_EFFECTS;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
