package com.yooiistudios.morningkit.theme.font;

import lombok.Getter;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 5. 7.
 *
 * MNTranslucentFontType
 *  투명 테마에 대응하기 위한 폰트 색 타입
 */
public enum MNTranslucentFontType {
    WHITE(0), BLACK(1);

    @Getter private final int uniqueId;

    MNTranslucentFontType(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public static MNTranslucentFontType valueOfUniqueId(int uniqueId) {
        switch (uniqueId) {
            case 0: return WHITE;
            case 1: return BLACK;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
