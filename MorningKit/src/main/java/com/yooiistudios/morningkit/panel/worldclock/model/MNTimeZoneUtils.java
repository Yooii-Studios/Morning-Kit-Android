package com.yooiistudios.morningkit.panel.worldclock.model;

import org.joda.time.DateTime;

import java.util.Calendar;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 18.
 *
 * MNTimeZone의 daylight saving time을 체크, 현재 2019년까지 지원
 */
public class MNTimeZoneUtils {
    private MNTimeZoneUtils() { throw new AssertionError("You MUST not create this class!"); }

    public static boolean isDaylightSavingTime(MNTimeZone timeZone, Calendar worldClockCalendar) {
        String timeZoneName = timeZone.getTimeZoneName();

        if (isTimeZoneInNorthAmerica(timeZoneName)) {
            return isDaylightSavingTimeInNorthAmerica(worldClockCalendar);
        } else if (isTimeZoneInEurope(timeZoneName)) {
            return isDaylightSavingTimeInEurope(worldClockCalendar);
        } else if (isTimeZoneInAustralia(timeZoneName)) {
            return isDaylightSavingTimeInAustralia(worldClockCalendar);
        } else if (isTimeZoneInSouthAmerica(timeZoneName)) {
            return isDaylightSavingTimeInSouthAmerica(worldClockCalendar);
        }
        return false;
    }

    // check if the time zone is in specific area
    private static boolean isTimeZoneInNorthAmerica(String timeZoneName) {
        return timeZoneName.equals("Pacific Standard Time") ||
                timeZoneName.equals("Eastern Standard Time") ||
                timeZoneName.equals("Central Standard Time") ||
                timeZoneName.equals("Atlantic Standard Time") ||
                timeZoneName.equals("Mountain Standard Time") ||
                timeZoneName.equals("Alaskan Standard Time") ||
                timeZoneName.equals("Hawaii-Aleutian Standard Time") ||
                timeZoneName.equals("Newfoundland Standard Time") ||
                timeZoneName.contains("Canada Central") ||
                timeZoneName.contains("Mexico");
    }

    private static boolean isTimeZoneInEurope(String timeZoneName) {
        return timeZoneName.contains("Europe") ||
                timeZoneName.contains("Romance") ||
                timeZoneName.contains("GMT");
    }

    private static boolean isTimeZoneInAustralia(String timeZoneName) {
        return timeZoneName.contains("Cen. Australia") || timeZoneName.contains("AUS Eastern");
    }

    private static boolean isTimeZoneInSouthAmerica(String timeZoneName) {
        return timeZoneName.contains("E. South");
    }

    // check if the specific time zone is daylight saving time
    private static boolean isDaylightSavingTimeInEurope(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        if (year == 2014) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2014, 3, 30, 1, 0), new DateTime(2014, 10, 26, 2, 0), true);
        } else if (year == 2015) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2015, 3, 29, 1, 0), new DateTime(2015, 10, 25, 2, 0), true);
        } else if (year == 2016) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2016, 3, 27, 1, 0), new DateTime(2016, 10, 30, 2, 0), true);
        } else if (year == 2017) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2017, 3, 26, 1, 0), new DateTime(2017, 10, 29, 2, 0), true);
        } else if (year == 2018) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2018, 3, 25, 1, 0), new DateTime(2018, 10, 28, 2, 0), true);
        } else if (year == 2019) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2019, 3, 31, 1, 0), new DateTime(2019, 10, 27, 2, 0), true);
        }
        return false;
    }

    private static boolean isDaylightSavingTimeInAustralia(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        if (year == 2014) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2014, 10, 5, 3, 0), new DateTime(2014, 4, 6, 2, 0), false);
        } else if (year == 2015) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2015, 10, 4, 3, 0), new DateTime(2015, 4, 5, 2, 0), false);
        } else if (year == 2016) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2016, 10, 2, 3, 0), new DateTime(2016, 4, 3, 2, 0), false);
        } else if (year == 2017) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2017, 10, 1, 3, 0), new DateTime(2017, 4, 2, 2, 0), false);
        } else if (year == 2018) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2018, 10, 7, 3, 0), new DateTime(2018, 4, 1, 2, 0), false);
        } else if (year == 2019) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2019, 10, 6, 3, 0), new DateTime(2019, 4, 7, 2, 0), false);
        }
        return false;
    }

    private static boolean isDaylightSavingTimeInNorthAmerica(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        if (year == 2014) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2014, 3, 9, 2, 0), new DateTime(2014, 11, 2, 2, 0), true);
        } else if (year == 2015) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2015, 3, 8, 2, 0), new DateTime(2015, 11, 1, 2, 0), true);
        } else if (year == 2016) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2016, 3, 13, 2, 0), new DateTime(2016, 11, 6, 2, 0), true);
        } else if (year == 2017) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2017, 3, 12, 2, 0), new DateTime(2017, 11, 5, 2, 0), true);
        } else if (year == 2018) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2018, 3, 11, 2, 0), new DateTime(2018, 11, 4, 2, 0), true);
        } else if (year == 2019) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2019, 3, 10, 2, 0), new DateTime(2019, 11, 3, 2, 0), true);
        }
        return false;
    }

    private static boolean isDaylightSavingTimeInSouthAmerica(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        if (year == 2014) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2014, 10, 18, 0, 0), new DateTime(2014, 2, 16, 0, 0), false);
        } else if (year == 2015) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2015, 10, 18, 0, 0), new DateTime(2015, 2, 22, 0, 0), false);
        } else if (year == 2016) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2016, 10, 16, 0, 0), new DateTime(2016, 2, 21, 0, 0), false);
        } else if (year == 2017) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2017, 10, 15, 0, 0), new DateTime(2017, 2, 19, 0, 0), false);
        } else if (year == 2018) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2018, 10, 21, 0, 0), new DateTime(2018, 2, 18, 0, 0), false);
        } else if (year == 2019) {
            return isCalendarInDaylightSeason(calendar, new DateTime(2019, 10, 20, 0, 0), new DateTime(2019, 2, 17, 0, 0), false);
        }
        return false;
    }

    // check target calendar between two calendar
    private static boolean isCalendarInDaylightSeason(Calendar targetCalendar, DateTime startDateTime,
                                                             DateTime endDateTime, boolean isNorth) {
        // Joda-Time 을 활용해서 시간 비교
        DateTime targetDateTime = new DateTime(targetCalendar.get(Calendar.YEAR),
                targetCalendar.get(Calendar.MONTH) + 1, targetCalendar.get(Calendar.DAY_OF_MONTH),
                targetCalendar.get(Calendar.HOUR), targetCalendar.get(Calendar.MINUTE),
                targetCalendar.get(Calendar.SECOND));

//        MNLog.now("startDateTime: " + startDateTime);
//        MNLog.now("targetDateTime: " + targetDateTime);
//        MNLog.now("endDateTime: " + endDateTime);

        if (isNorth) {
            return targetDateTime.isAfter(startDateTime) && targetDateTime.isBefore(endDateTime);
        } else {
            return targetDateTime.isAfter(startDateTime) || targetDateTime.isBefore(endDateTime);
        }
    }
}
