package com.yooiistudios.morningkit.common.bus;

import com.squareup.otto.Bus;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 9.
 *
 * MNAlarmScrollViewBusProvider
 *  알람 삭제에 관한 메시지를 주고받는 버스 - 메인 액티비티
 *  알람 스누즈, 디스미스 시 알람 리로딩에 관한 메시지로 주고받음 - 알람 리스트뷰
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
