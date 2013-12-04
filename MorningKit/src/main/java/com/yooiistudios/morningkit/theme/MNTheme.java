package com.yooiistudios.morningkit.theme;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNTheme (유틸리티 클래스)
 *  테마와 관련된 모든 처리를 관장
 */
public class MNTheme {
    private MNTheme() { throw new AssertionError(); } // You must not create instance
    @Getter @Setter private MNThemeType currentThemeType;
}
