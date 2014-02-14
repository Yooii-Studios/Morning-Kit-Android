package com.yooiistudios.morningkit.setting.theme.soundeffect;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNSoundType
 *  모닝키트의 사운드타입을 enum으로 표현
 *  index = 설정 창에서 순서를 표현
 *  uniqueId = 이 테마의 고유 id를 표시
 */
public enum MNSoundType {
    ON(0, 0), OFF(1, 1);

    @Getter private final int index;
    @Getter private final int uniqueId;

    MNSoundType(int index, int uniqueId) {
        this.index = index;
        this.uniqueId = uniqueId;
    }

    public static MNSoundType valueOf(int index) {

        switch (index) {
            case 0: return ON;
            case 1: return OFF;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static MNSoundType valueOfUniqueId(int uniqueId) {

        switch (uniqueId) {
            case 0: return ON;
            case 1: return OFF;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
