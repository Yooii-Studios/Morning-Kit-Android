package com.yooiistudios.morningkit.setting.store;

import lombok.Getter;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 7.
 *
 * MNInfoItemType
 */
public enum MNStoreTabType {
    FUNCTIONS(0), PANELS(1), THEMES(2);

    @Getter
    private final int index;
    MNStoreTabType(int index) { this.index = index; }

    public static MNStoreTabType valueOf(int index) {

        switch (index) {
            case 0: return FUNCTIONS;
            case 1: return PANELS;
            case 2: return THEMES;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
