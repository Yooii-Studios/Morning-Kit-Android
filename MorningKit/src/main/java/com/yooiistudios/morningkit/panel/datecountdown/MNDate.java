package com.yooiistudios.morningkit.panel.datecountdown;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 28.
 *
 * MNDate
 */
public class MNDate {
    @Getter int year;
    @Getter protected int month;
    @Getter protected int day;

    MNDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
