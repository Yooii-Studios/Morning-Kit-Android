package com.yooiistudios.morningkit.setting.info;

import lombok.Getter;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 7.
 *
 * MNInfoItemType
 */
public enum MNInfoItemType {
    STORE(0), MORNING_KIT_INFO(1), RATE_MORNING_KIT(2), LIKE_US_ON_FACEBOOK(3), CREDITS(4);

    @Getter
    private final int index;
    MNInfoItemType(int index) { this.index = index; }

    public static MNInfoItemType valueOf(int index) {

        switch (index) {
            case 0: return STORE;
            case 1: return RATE_MORNING_KIT;
            case 2: return LIKE_US_ON_FACEBOOK;
            case 3: return CREDITS;
            case 4: return CREDITS;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
