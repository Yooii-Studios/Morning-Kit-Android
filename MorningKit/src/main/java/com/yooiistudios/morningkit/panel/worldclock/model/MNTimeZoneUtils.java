package com.yooiistudios.morningkit.panel.worldclock.model;

import java.util.Calendar;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 18.
 *
 * MNTimeZone의 daylight saving time을 체크
 */
public class MNTimeZoneUtils {
    private MNTimeZoneUtils() { throw new AssertionError("You MUST not create this class!"); }

    public static boolean isDaylightSavingTime(MNTimeZone timeZone) {
        boolean isTimeZoneDaylightSavingTime = false;

        String timeZoneName = timeZone.getTimeZoneName();
        if (isTimeZoneInNorthAmerica(timeZoneName)) {

        } else if (isTimeZoneInEurope(timeZoneName)) {

        } else if (isTimeZoneInAustralia(timeZoneName)) {

        } else if (isTimeZoneInSouthAmerica(timeZoneName)) {

        }
        return isTimeZoneDaylightSavingTime;
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
        return false;
    }

    private static boolean isDaylightSavingTimeInAustralia(Calendar calendar) {
        return false;
    }

    private static boolean isDaylightSavingTimeInNorthAmerica(Calendar calendar) {
        return false;
    }

    private static boolean isDaylightSavingTimeInSouthAmerica(Calendar calendar) {
        return false;
    }

    // check target calendar between two calendar

}
