package com.yooiistudios.morningkit.panel.weather.model.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherData;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 1.
 *
 * MNWeatherDataSearchCityCache
 *  도시를 검색해서 나온 날씨 정보를 캐싱하는 클래스
 */
public class MNWeatherDataSearchCityCache extends MNWeatherDataCache {
    private static final String WEATHER_DATA_SEARCH_CITY_CACHE_PREFS = "WEATHER_DATA_SEARCH_CITY_CACHE_PREFS";
    private static final String WEATHER_DATA_SEARCH_CITY_CACHE_PREFS_KEY = "WEATHER_DATA_SEARCH_CITY_CACHE_PREFS_KEY";

    public MNWeatherDataSearchCityCache(Context context) {
        super(context);
    }

    @Override
    public MNWeatherData findWeatherCache(double latitude, double longitude) {
        for (MNWeatherData weatherData : weatherDataCacheList) {
            if (weatherData.weatherLocationInfo.getLatitude() == latitude &&
                    weatherData.weatherLocationInfo.getLongitude() == longitude) {

                // check time stamp is valid
                if (DateTime.now().getMillis() - weatherData.timeStampInMillis < WEATHER_REFRESH_LIMIT_TIME_IN_MILLIS) {
                    if (DEBUG_MODE) {
                        Toast.makeText(context, "Target city is in cache and show previous " +
                                "weather data because data is got within 4 hours", Toast.LENGTH_SHORT).show();
                    }
                    return weatherData;
                } else {
                    if (DEBUG_MODE) {
                        Toast.makeText(context, "Target city in in cache but invalid, " +
                                "get a new weather data from WWO", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void unarchiveWeatherDataCacheList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(WEATHER_DATA_SEARCH_CITY_CACHE_PREFS,
                Context.MODE_PRIVATE);
        String weatherDataCacheJsonString = prefs.getString(WEATHER_DATA_SEARCH_CITY_CACHE_PREFS_KEY, null);
        if (weatherDataCacheJsonString != null) {
            Type type = new TypeToken<List<MNWeatherData>>() {}.getType();
            weatherDataCacheList = new Gson().fromJson(weatherDataCacheJsonString, type);
        } else {
            weatherDataCacheList = new ArrayList<MNWeatherData>();
        }
    }

    @Override
    protected void archiveWeatherData(MNWeatherData weatherData, Context context) {
        // archive
        SharedPreferences prefs = context.getSharedPreferences(WEATHER_DATA_SEARCH_CITY_CACHE_PREFS,
                Context.MODE_PRIVATE);
        prefs.edit().putString(WEATHER_DATA_SEARCH_CITY_CACHE_PREFS_KEY,
                new Gson().toJson(weatherDataCacheList)).commit();
    }
}
