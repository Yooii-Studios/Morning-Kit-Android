package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;

import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 20.
 *
 * MNDummyAlarmMaker
 *  테스트용 더미 알람 생성 클래스
 */
public class MNDummyAlarmMaker {

    /**
     * Making dummy alarms
     */
    public static MNAlarm makeNonRepeatAlarm(Context context) {
        MNAlarm nonRepeatAlarm = MNAlarmMaker.makeAlarm(context);
        for (int i = 0; i < nonRepeatAlarm.getAlarmRepeatList().size(); i++) {
            nonRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
        }
        return nonRepeatAlarm;
    }

    // 월금토
    public static MNAlarm makeSeveralRepeatAlarm(Context context) {
        MNAlarm severalRepeatAlarm = MNAlarmMaker.makeAlarm(context);
        for (int i = 0; i < severalRepeatAlarm.getAlarmRepeatList().size(); i++) {
            severalRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
            if (i == 0 || i == 3 || i == 5) {
                severalRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
            }
        }
        return severalRepeatAlarm;
    }

    public static MNAlarm makeWeekdaysRepeatAlarm(Context context) {
        MNAlarm weekdaysRepeatAlarm = MNAlarmMaker.makeAlarm(context);
        for (int i = 0; i < weekdaysRepeatAlarm.getAlarmRepeatList().size(); i++) {
            weekdaysRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
            if (i == 5 || i == 6) {
                weekdaysRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
            }
        }
        return weekdaysRepeatAlarm;
    }

    public static MNAlarm makeWeekendsRepeatAlarm(Context context) {
        MNAlarm weekendsRepeatAlarm = MNAlarmMaker.makeAlarm(context);
        for (int i = 0; i < weekendsRepeatAlarm.getAlarmRepeatList().size(); i++) {
            weekendsRepeatAlarm.getAlarmRepeatList().set(i, Boolean.FALSE);
            if (i == 5 || i == 6) {
                weekendsRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
            }
        }
        return weekendsRepeatAlarm;
    }

    public static MNAlarm makeEverydayRepeatAlarm(Context context) {
        MNAlarm everydayRepeatAlarm = MNAlarmMaker.makeAlarm(context);
        for (int i = 0; i < everydayRepeatAlarm.getAlarmRepeatList().size(); i++) {
            everydayRepeatAlarm.getAlarmRepeatList().set(i, Boolean.TRUE);
        }
        return everydayRepeatAlarm;
    }
}
