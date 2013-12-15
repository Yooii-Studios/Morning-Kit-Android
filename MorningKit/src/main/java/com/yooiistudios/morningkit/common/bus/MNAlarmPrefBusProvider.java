package com.yooiistudios.morningkit.common.bus;

import com.squareup.otto.Bus;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 15.
 *
 * MNAlarmPrefBusProvider
 * 다이얼로그와 리스트어댑터 사이의 버스
 */
public class MNAlarmPrefBusProvider {
    /**
     * Singleton
     */
    private volatile static Bus instance;
    private MNAlarmPrefBusProvider() {}
    public static Bus getInstance() {
        if (instance == null) {
            synchronized (MNBusProvider.class) {
                if (instance == null) {
                    instance = new Bus();
                }
            }
        }
        return instance;
    }
}
