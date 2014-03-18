package com.yooiistudios.morningkit.panel.worldclock.model;

import org.joda.time.LocalDate;

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
    @Getter Calendar clockCalendar;
    private MNTimeZone timeZone;
    private String cityName;
    private boolean isDaylightSavingTime = false;

    public MNWorldClock() {
        clockCalendar = Calendar.getInstance();
    }

    // 현재 시간을 1초마다 계산
    public void tick() {
        clockCalendar = Calendar.getInstance();

        // Daylight Saving Time 체크
        int hour = timeZone.getOffsetHour();
        if (timeZone.isDaylightSavingTime()) {
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

        clockCalendar.setTimeZone(TimeZone.getTimeZone(offset));
    }

    public void setTimeZone(MNTimeZone timeZone) {
        // cityName
        if (timeZone.getSearchedLocalizedName()  != null) {
            cityName = timeZone.getSearchedLocalizedName();
        } else {
            cityName = timeZone.getName();
        }
        this.timeZone = timeZone;
        tick();
    }

    public String getUpperCasedTimeZoneString() {
        return cityName.substring(0, 1).toUpperCase() + cityName.substring(1);
    }

    public int getDayDifferences() {
        int localYear = Calendar.getInstance().get(Calendar.YEAR);
        int localMonth = Calendar.getInstance().get(Calendar.MONTH);
        int localDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


        int clockYear = clockCalendar.get(Calendar.YEAR);
        int clockMonth = clockCalendar.get(Calendar.MONTH);
        int clockDay = clockCalendar.get(Calendar.DAY_OF_MONTH);

        // Joda-Time 이용
        LocalDate localDate = new LocalDate(localYear, localMonth, localDay);
        LocalDate clockDate = new LocalDate(clockYear, clockMonth, clockDay);

        // -1 = 어제 / 0 = 같은 날 / 1 = 내일
        if (clockDate.isEqual(localDate)) {
            return 0;
        } else if (clockDate.isAfter(localDate)) {
            return 1;
        } else if (clockDate.isBefore(localDate)) {
            return -1;
        }

        // 문제가 있는 경우 0을 반환
        return 0;

        /*
        // 기존 코드

        // 년 비교
        if (clockYear > localYear) {
            dayDifference = 1;
        }else if(clockYear < localYear) {
            dayDifference = -1;
        }else{
            // 월 비교
            if (clockMonth > localMonth) {
                dayDifference = 1;
            }else if (clockMonth < localMonth) {
                dayDifference = -1;
            }else{
                // 일 비교
                if (clockDay > localDay) {
                    dayDifference = 1;
                }else if(clockDay < localDay) {
                    dayDifference = -1;
                }else{
                    dayDifference = 0;
                }
            }
        }
        */
    }
}
