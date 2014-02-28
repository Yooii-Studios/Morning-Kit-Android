package com.yooiistudios.morningkit.panel.datecountdown;

import java.util.Calendar;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 28.
 *
 * MNDefaultDateMaker
 *  내년 1월 1일의 데이트를 넘겨준다
 */
public class MNDefaultDateMaker {
    private MNDefaultDateMaker() { throw new AssertionError("You MUST not create this class!"); }

    public static MNDate getDefaultDate() {
        return new MNDate(Calendar.getInstance().get(Calendar.YEAR) + 1, 0, 1);
    }
}
