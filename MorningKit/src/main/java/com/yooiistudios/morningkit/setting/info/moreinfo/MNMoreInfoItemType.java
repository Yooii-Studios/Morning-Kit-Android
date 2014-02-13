package com.yooiistudios.morningkit.setting.info.moreinfo;

import lombok.Getter;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 7.
 *
 * MNInfoItemType
 */
public enum MNMoreInfoItemType {
    YOOII_STUDIOS(0), MORNING_KIT_HELP(1), LICENSE(2), VERSION(3);

    @Getter
    private final int index;
    MNMoreInfoItemType(int index) { this.index = index; }

    public static MNMoreInfoItemType valueOf(int index) {

        switch (index) {
            case 0: return YOOII_STUDIOS;
            case 1: return MORNING_KIT_HELP;
            case 2: return LICENSE;
            case 3: return VERSION;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
