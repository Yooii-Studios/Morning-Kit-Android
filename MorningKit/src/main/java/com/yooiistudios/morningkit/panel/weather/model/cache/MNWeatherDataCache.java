package com.yooiistudios.morningkit.panel.weather.model.cache;

import android.content.Context;

import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherData;

import java.util.List;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 31.
 *
 * MNWeatherDataCache
 *  날씨 데이터들을 캐싱하는 클래스
 */
public abstract class MNWeatherDataCache {
    private static final int CACHE_LIMIT = 10;
    protected  static final int WEATHER_REFRESH_LIMIT_DISTANCE = (35 * 1000);
//    protected static final int WEATHER_REFRESH_LIMIT_TIME_IN_MILLIS = (60 * 60 * 4 * 1000); // second -> 4 hours;
    protected static final int WEATHER_REFRESH_LIMIT_TIME_IN_MILLIS = (30 * 1000); // test: 10 seconds
    protected static final boolean DEBUG_MODE = true;
    protected List<MNWeatherData> weatherDataCacheList;
    protected Context context;

    public MNWeatherDataCache(Context context) {
        this.context = context;
        unarchiveWeatherDataCacheList(context);
    }

    protected abstract void unarchiveWeatherDataCacheList(Context context);

    public abstract MNWeatherData findWeatherCache(double latitude, double longitude);

    public void addWeatherDataToCache(MNWeatherData weatherData, Context context) {
        // remove first one if the size overflow the limit
        if (weatherDataCacheList.size() >= CACHE_LIMIT) {
            weatherDataCacheList.remove(0);
        }
        weatherDataCacheList.add(weatherData);

        archiveWeatherData(weatherData, context);
    }

    protected abstract void archiveWeatherData(MNWeatherData weatherData, Context context);
}
