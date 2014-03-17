package com.yooiistudios.morningkit.panel.worldclock.model;

import java.util.Calendar;
import java.util.TimeZone;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 17.
 *
 * MNWorldClock
 *  세계 세계에 관한 로직을 담고 있는 클래스
 */
public class MNWorldClock {
    @Getter Calendar calendar;
    private MNTimeZone timeZone;
    private String cityName;
    private boolean isDaylightSavingTime = false;

    public MNWorldClock() {
        calendar = Calendar.getInstance();
    }

    // 현재 시간을 1초마다 계산
    public void tick() {
        calendar = Calendar.getInstance();

        int hour = timeZone.getOffsetHour();
        if (isDaylightSavingTime) {
            hour += 1;
        }

        String hourString;
        int absoluteHour = Math.abs(hour);
        if (absoluteHour < 10 && hour >= 0) {
            hourString = String.format("+0%d", absoluteHour);
        } else if (absoluteHour < 10 && hour < 0) {
            hourString = String.format("-0%d", absoluteHour);
        } else if(hour >= 0) {
            hourString = String.format("+%d", hour);
        } else if(hour < 0) {
            hourString = String.format("-%d", hour);
        } else {
            hourString = String.format("+%d", hour);
        }

        // GMT 시간 설정을 사용해 원하는 시간대의 시간으로 변경
        String offset = String.format("GMT%s:%1d%1d", hourString,
                timeZone.getOffsetMin() / 10, timeZone.getOffsetMin() % 10);

        calendar.setTimeZone(TimeZone.getTimeZone(offset));
    }

    public void setTimeZone(MNTimeZone timeZone) {
        // cityName
        if (timeZone.getSearchedLocalizedName()  != null) {
            cityName = timeZone.getSearchedLocalizedName();
        } else {
            cityName = timeZone.getName();
        }

        // daylight saving time 체크 - 아직 미구현
        checkDaylightSavingTime();

        int hour = timeZone.getOffsetHour();
        if (isDaylightSavingTime) {
            hour += 1;
        }

        this.timeZone = timeZone;
        tick();
    }

    private void checkDaylightSavingTime() {
        isDaylightSavingTime = false;
    }

    public String getUpperCasedTimeZoneString() {
        return cityName.substring(0, 1).toUpperCase() + cityName.substring(1);
    }
}
