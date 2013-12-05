package com.yooiistudios.morningkit.common.bus;

import com.squareup.otto.Bus;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 3.
 *
 * MNBusProvider (Singleton)
 *  웹에서 BusProvider를 보고 유사하게 만들되 Thread-safe 부분 보강.
 *
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
