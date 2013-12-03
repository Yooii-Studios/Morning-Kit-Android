package com.yooiistudios.morningkit.common.bus;

import com.squareup.otto.Bus;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 3.
 *
 * MNBusProvider
 * 웹에서 BusProvider 소스를 보고 따라서 만듬. Singleton는 조금 더 Thread-safe하게 만듬.
 */
public class MNBusProvider {
    /**
     * Singleton
     */
    private volatile static Bus instance;
    private MNBusProvider() {}
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
