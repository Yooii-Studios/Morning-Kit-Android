package com.yooiistudios.morningkit.panel.weather.model.locationinfo;

import com.yooiistudios.morningkit.R;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 25.
 *
 * MNWeatherCondition
 *  WWO 날씨 컨디션을 나열
 */
public enum MNWWOWeatherCondition {
    no_data(0, 11),
    heavy_snow_showers(395, 1), // 1
    thundery_showers(392, 1), // 1
    thunderstorms(389, 4), // 4
    patchy_light_rain_in_area_with_thunder(386, 4), // 4
    moderate_or_heavy_showers_of_ice_pellets(377, 5), // 5
    light_showers_of_ice_pellets(374, 5), // 5
    moderate_or_heavy_snow_showers(371, 1), // 1
    light_snow_showers(368, 1), // 1
    moderate_or_heavy_sleet_showers(365, 2), // 2
    light_sleet_showers(362, 2), // 2
    cloudy_with_heavy_rain(359, 2), // 2
    heavy_rain_showers(356, 2), // 2
    light_rain_showers(353, 2), // 2
    ice_pellets(350, 5), // 5
    heavy_snow(338, 1), // 1
    patchy_heavy_snow(335, 1), // 1
    moderate_snow(332, 1), // 1
    patchy_moderate_snow(329, 1), // 1
    light_snow(326, 1), // 1
    patchy_light_snow(323, 1), // 1
    moderate_or_heavy_sleet(320, 2), // 2
    light_sleet(317, 2), // 2
    moderate_or_heavy_freezing_rain(314, 4), // 4
    light_freezing_rain(311, 4), // 4
    heavy_rain(308, 4), // 4
    heavy_rain_at_times(305, 4), // 4
    moderate_rain(302, 4), // 4
    moderate_rain_at_times(299, 4), // 4
    light_rains(296, 4), // 4
    patchy_light_rain(293, 4), // 4
    heavy_freezing_drizzle(284, 4), // 4
    freezing_drizzle(281, 4), // 4
    light_drizzle(266, 4), // 4
    patchy_light_drizzle(263, 4), // 4
    freezing_fog(260, 8), // 8
    fog(248, 8), // 8
    blizzard(230, 1), // 1
    blowing_snow(227, 1), // 1
    thundery_outbreaks_in_nearby(200, 14), // 14
    patchy_freezing_drizzle_nearby(185, 4), // 4
    patchy_sleet_nearby(182, 4), // 4
    patchy_snow_nearby(179, 1), // 1
    patchy_rain_nearby(176, 4), // 4
    mist(143, 8), // 8
    overcast(122, 12), // 12
    cloudy(119, 12), // 12
    partly_cloudy(116, 9), // 9
    clear_sunny(113, 11), // 11
    not_available(3200, 11);

    @Getter private final int weatherCode; // 날씨를 구분하는 id
    @Getter private final int weatherImageId; // 날씨를 구분하는 id

    MNWWOWeatherCondition(int weatherCode, int weatherImageId) {
        this.weatherCode = weatherCode;
        this.weatherImageId = weatherImageId;
    }

    public static MNWWOWeatherCondition valueOfWeatherCode(int weatherCode) {
        switch (weatherCode) {
            case 395: return heavy_snow_showers;
            case 392: return thundery_showers;
            case 389: return thunderstorms;
            case 386: return patchy_light_rain_in_area_with_thunder;
            case 377: return moderate_or_heavy_showers_of_ice_pellets;
            case 374: return light_showers_of_ice_pellets;
            case 371: return moderate_or_heavy_snow_showers;
            case 368: return light_snow_showers;
            case 365: return moderate_or_heavy_sleet_showers;
            case 362: return light_sleet_showers;
            case 359: return cloudy_with_heavy_rain;
            case 356: return heavy_rain_showers;
            case 353: return light_rain_showers;
            case 350: return ice_pellets;
            case 338: return heavy_snow;
            case 335: return patchy_heavy_snow;
            case 332: return moderate_snow;
            case 329: return patchy_moderate_snow;
            case 326: return light_snow;
            case 323: return patchy_light_snow;
            case 320: return moderate_or_heavy_sleet;
            case 317: return light_sleet;
            case 314: return moderate_or_heavy_freezing_rain;
            case 311: return light_freezing_rain;
            case 308: return heavy_rain;
            case 305: return heavy_rain_at_times;
            case 302: return moderate_rain;
            case 299: return moderate_rain_at_times;
            case 296: return light_rains;
            case 293: return patchy_light_rain;
            case 284: return heavy_freezing_drizzle;
            case 281: return freezing_drizzle;
            case 266: return light_drizzle;
            case 263: return patchy_light_drizzle;
            case 260: return freezing_fog;
            case 248: return fog;
            case 230: return blizzard;
            case 227: return blowing_snow;
            case 200: return thundery_outbreaks_in_nearby;
            case 185: return patchy_freezing_drizzle_nearby;
            case 182: return patchy_sleet_nearby;
            case 179: return patchy_snow_nearby;
            case 176: return patchy_rain_nearby;
            case 143: return mist;
            case 122: return overcast;
            case 119: return cloudy;
            case 116: return partly_cloudy;
            case 113: return clear_sunny;
            case 3200: return not_available;

            default: return no_data;
        }
    }

    public int getConditionResourceId() {
        switch (weatherImageId) {
            case 1: return R.drawable.m_icon_weather_01_snow;
            case 2: return R.drawable.m_icon_weather_02_mixed_rain_and_snow;
            case 4: return R.drawable.m_icon_weather_04_showers;
            case 5: return R.drawable.m_icon_weather_05_hail;
            case 6: return R.drawable.m_icon_weather_06_hurricane;
            case 7: return R.drawable.m_icon_weather_07_tornado;
            case 8: return R.drawable.m_icon_weather_08_foggy;
            case 9: return R.drawable.m_icon_weather_09_partly_cloudy;
            case 10: return R.drawable.m_icon_weather_10_windy;
            case 11: return R.drawable.m_icon_weather_11_sunny;
            case 12: return R.drawable.m_icon_weather_12_mostly_cloudy_night;
            case 13: return R.drawable.m_icon_weather_13_mixed_rain_and_hail;
            case 14: return R.drawable.m_icon_weather_14_isolated_thundershowers;
            default: return R.drawable.m_icon_weather_11_sunny;
        }
    }
}
