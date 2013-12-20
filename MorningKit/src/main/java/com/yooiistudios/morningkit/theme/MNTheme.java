package com.yooiistudios.morningkit.theme;

import com.squareup.otto.Bus;
import com.yooiistudios.morningkit.common.bus.MNBusProvider;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNTheme (유틸리티 클래스)
 *  테마와 관련된 모든 처리를 관장
 */
public class MNTheme {

    private volatile static MNTheme instance;
    private MNThemeType currentThemeType;

    /**
     * Singleton
     */
    private MNTheme() {}
    public static MNTheme getInstance() {
        if (instance == null) {
            synchronized (MNTheme.class) {
                if (instance == null) {
                    instance = new MNTheme();
                    instance.currentThemeType = MNThemeType.WATER_LILY;
                }
            }
        }
        return instance;
    }

    /**
     * Utility Methods
     */
    public static MNThemeType getCurrentTheme() {
        return MNTheme.getInstance().currentThemeType;
    }
    public static void setCurrentTheme(MNThemeType themeType) {
        MNTheme.getInstance().currentThemeType = themeType;
    }
}
