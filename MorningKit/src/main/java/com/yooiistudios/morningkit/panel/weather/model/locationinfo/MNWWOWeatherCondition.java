package com.yooiistudios.morningkit.panel.weather.model.locationinfo;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 25.
 *
 * MNWeatherCondition
 *  WWO 날씨 컨디션을 나열
 */
public enum MNWWOWeatherCondition {
    no_data(0),
    heavy_snow_showers(395), // 1
    thundery_showers(392), // 1
    thunderstorms(389), // 4
    patchy_light_rain_in_area_with_thunder(386), // 4
    moderate_or_heavy_showers_of_ice_pellets(377), // 5
    light_showers_of_ice_pellets(374), // 5
    moderate_or_heavy_snow_showers(371), // 1
    light_snow_showers(368), // 1
    moderate_or_heavy_sleet_showers(365), // 2
    light_sleet_showers(362), // 2
    cloudy_with_heavy_rain(359), // 2
    heavy_rain_showers(356), // 2
    light_rain_showers(353), // 2
    ice_pellets(350), // 5
    heavy_snow(338), // 1
    patchy_heavy_snow(335), // 1
    moderate_snow(332), // 1
    patchy_moderate_snow(329), // 1
    light_snow(326), // 1
    patchy_light_snow(323), // 1
    moderate_or_heavy_sleet(320), // 2
    light_sleet(317), // 2
    moderate_or_heavy_freezing_rain(314), // 4
    light_freezing_rain(311), // 4
    heavy_rain(308), // 4
    heavy_rain_at_times(305), // 4
    moderate_rain(302), // 4
    moderate_rain_at_times(299), // 4
    light_rains(296), // 4
    patchy_light_rain(293), // 4
    heavy_freezing_drizzle(284), // 4
    freezing_drizzle(281), // 4
    light_drizzle(266), // 4
    patchy_light_drizzle(263), // 4
    freezing_fog(260), // 8
    fog(248), // 8
    blizzard(230), // 1
    blowing_snow(227), // 1
    thundery_outbreaks_in_nearby(200), // 14
    patchy_freezing_drizzle_nearby(185), // 4
    patchy_sleet_nearby(182), // 4
    patchy_snow_nearby(179), // 1
    patchy_rain_nearby(176), // 4
    mist(143), // 8
    overcast(122), // 12
    cloudy(119), // 12
    partly_cloudy(116), // 9
    clear_sunny(113), // 11
    not_available(3200);

    @Getter private final int id; // 날씨를 구분하는 id

    MNWWOWeatherCondition(int id) {
        this.id = id;
    }
}
