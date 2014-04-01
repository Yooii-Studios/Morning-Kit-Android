package com.yooiistudios.morningkit.panel.weather.model.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
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
 * MNWeatherDataCurrentLocationCache
 *  현재 위치 정보들로 얻은 날씨 데이터를 캐싱하는 클래스
 */
public class MNWeatherDataCurrentLocationCache extends MNWeatherDataCache {
    private static final String WEATHER_DATA_CURRENT_LOCATION_CACHE_PREFS = "WEATHER_DATA_CURRENT_LOCATION_CACHE_PREFS";
    private static final String WEATHER_DATA_CURRENT_LOCATION_CACHE_PREFS_KEY = "WEATHER_DATA_CURRENT_LOCATION_CACHE_PREFS_KEY";

    public MNWeatherDataCurrentLocationCache(Context context) {
        super(context);
    }

    @Override
    protected void unarchiveWeatherDataCacheList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(WEATHER_DATA_CURRENT_LOCATION_CACHE_PREFS,
                Context.MODE_PRIVATE);
        String weatherDataCacheJsonString = prefs.getString(WEATHER_DATA_CURRENT_LOCATION_CACHE_PREFS_KEY, null);
        if (weatherDataCacheJsonString != null) {
            Type type = new TypeToken<List<MNWeatherData>>() {}.getType();
            weatherDataCacheList = new Gson().fromJson(weatherDataCacheJsonString, type);
        } else {
            weatherDataCacheList = new ArrayList<MNWeatherData>();
        }
    }

    @Override
    public MNWeatherData findWeatherCache(double latitude, double longitude) {
        MNWeatherData cachedWeatherData = null;
        boolean isCachedButInvalidate = false;

        for (MNWeatherData weatherData : weatherDataCacheList) {

            // calculate distance between two locations
            float distance[] = new float[1];
            Location.distanceBetween(weatherData.weatherLocationInfo.getLatitude(),
                    weatherData.weatherLocationInfo.getLongitude(),
                    latitude, longitude, distance);

            if (distance[0] < WEATHER_REFRESH_LIMIT_DISTANCE) {
                // check time stamp is valid
                if (DateTime.now().getMillis() - weatherData.timeStampInMillis < WEATHER_REFRESH_LIMIT_TIME_IN_MILLIS) {
                    cachedWeatherData = weatherData;
                } else {
                    isCachedButInvalidate = true;
                }
            }
        }

        if (cachedWeatherData != null) {
            if (DEBUG_MODE) {
                Toast.makeText(context, "Current location is in cache and show previous " +
                        "weather data because data is got within 4 hours", Toast.LENGTH_SHORT).show();
            }
            // remove previous cache, because new one will be added
            weatherDataCacheList.remove(cachedWeatherData);
            return cachedWeatherData;
        } else {
            if (DEBUG_MODE) {
                if (isCachedButInvalidate) {
                    Toast.makeText(context, "Current location in in cache but invalid, " +
                            "get a new weather data from WWO", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Current location doesn't exciest in cache, so" +
                            "get a new weather data from WWO", Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }
    }

    @Override
    protected void archiveWeatherData(MNWeatherData weatherData, Context context) {
        // archive
        SharedPreferences prefs = context.getSharedPreferences(WEATHER_DATA_CURRENT_LOCATION_CACHE_PREFS,
                Context.MODE_PRIVATE);
        prefs.edit().putString(WEATHER_DATA_CURRENT_LOCATION_CACHE_PREFS_KEY,
                new Gson().toJson(weatherDataCacheList)).commit();
    }
}
