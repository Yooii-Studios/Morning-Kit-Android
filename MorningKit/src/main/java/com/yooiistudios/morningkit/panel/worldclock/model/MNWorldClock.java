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
    @Getter Calendar worldClockCalendar;
    @Getter MNTimeZone timeZone;
    private String cityName;
    private boolean isDaylightSavingTime = false;

    public MNWorldClock() {
        worldClockCalendar = Calendar.getInstance();
    }

    // 현재 시간을 1초마다 계산
    public void tick() {
        worldClockCalendar = Calendar.getInstance();

        // Daylight Saving Time 체크
        int hourOffset = timeZone.getOffsetHour();
        if (isDaylightSavingTime) {
            hourOffset += 1;
        }

        String hourString;
        int absoluteHour = Math.abs(hourOffset);
        if (absoluteHour < 10 && hourOffset >= 0) {
            hourString = String.format("+0%d", absoluteHour);
        } else if (absoluteHour < 10 && hourOffset < 0) {
            hourString = String.format("-0%d", absoluteHour);
        } else if(hourOffset >= 0) {
            hourString = String.format("+%d", hourOffset);
        } else if(hourOffset < 0) {
            hourString = String.format("-%d", absoluteHour);
        } else {
            hourString = String.format("+%d", hourOffset);
        }

        // GMT 시간 설정을 사용해 원하는 시간대의 시간으로 변경
        String offset = String.format("GMT%s:%1d%1d", hourString,
                timeZone.getOffsetMin() / 10, timeZone.getOffsetMin() % 10);

        worldClockCalendar.setTimeZone(TimeZone.getTimeZone(offset));
    }

      // 빠르게 시계가 잘 돌아가는지 테스트할 때 사용
//    public void testTick() {
//        worldClockCalendar.set(Calendar.SECOND, 0);
//        worldClockCalendar.add(Calendar.MINUTE, 13);
//    }

    public void setTimeZone(MNTimeZone timeZone) {
        // cityName
        if (timeZone.getSearchedLocalizedName()  != null) {
            cityName = timeZone.getSearchedLocalizedName();
        } else {
            cityName = timeZone.getName();
        }
        this.timeZone = timeZone;

        // tick을 진행한 후에 daylight saving time을 다시 확인해야 함. GMT+-X 때문(날짜)
        // -> 버그 발견. DST 적용 후에 tick을 진행해야만 함. 수정(1.0.1)
//        tick();
        isDaylightSavingTime = MNTimeZoneUtils.isDaylightSavingTime(timeZone, worldClockCalendar);
        tick();
    }

    public String getUpperCasedTimeZoneString() {
        if (cityName != null && cityName.length() >= 1) {
            return cityName.substring(0, 1).toUpperCase() + cityName.substring(1);
        } else {
            return "";
        }
    }

    public int getDayDifferences() {
        int localYear = Calendar.getInstance().get(Calendar.YEAR);
        int localMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int localDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        int clockYear = worldClockCalendar.get(Calendar.YEAR);
        int clockMonth = worldClockCalendar.get(Calendar.MONTH) + 1;
        int clockDay = worldClockCalendar.get(Calendar.DAY_OF_MONTH);

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
    }
}
