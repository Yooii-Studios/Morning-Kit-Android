package com.yooiistudios.morningkit.setting.info;

import lombok.Getter;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 7.
 *
 * MNInfoItemType
 */
public enum MNInfoItemType {
    MORNING_KIT_INFO(0), RATE_MORNING_KIT(1), LIKE_US_ON_FACEBOOK(2), CREDITS(3);

    @Getter
    private final int index;
    MNInfoItemType(int index) { this.index = index; }

    public static MNInfoItemType valueOf(int index) {

        switch (index) {
            case 0: return MORNING_KIT_INFO;
            case 1: return RATE_MORNING_KIT;
            case 2: return LIKE_US_ON_FACEBOOK;
            case 3: return CREDITS;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
