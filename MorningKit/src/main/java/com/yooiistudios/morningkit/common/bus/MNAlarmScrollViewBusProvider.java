package com.yooiistudios.morningkit.common.bus;

import com.squareup.otto.Bus;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 9.
 *
 * MNAlarmScrollViewBusProvider
 *  알람 삭제에 관한 메시지를 주고받는 버스
 */
public class MNAlarmScrollViewBusProvider {
    /**
     * Singleton
     */
    private volatile static Bus instance;
    private MNAlarmScrollViewBusProvider() {}
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
