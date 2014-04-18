package com.yooiistudios.morningkit.panel.weather.model.cache;

import android.content.Context;

import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherData;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 31.
 *
 * MNWeatherDataCache
 *  날씨 데이터들을 캐싱하는 클래스
 */
public abstract class MNWeatherDataCache {
    private static final String TAG = "MNWeatherDataCache";
    private static final int CACHE_LIMIT = 10;
    protected  static final int WEATHER_REFRESH_LIMIT_DISTANCE = (35 * 1000);
    protected static final int WEATHER_REFRESH_LIMIT_TIME_IN_MILLIS = (60 * 60 * 4 * 1000); // second -> 4 hours;
//    protected static final int WEATHER_REFRESH_LIMIT_TIME_IN_MILLIS = (30 * 1000); // test: 10 seconds
    protected static final boolean DEBUG_MODE = true;
    protected List<MNWeatherData> weatherDataCacheList;
    protected Context context;

    public MNWeatherDataCache(Context context) {
        this.context = context;
        unarchiveWeatherDataCacheList(context);

        // REFRESH_LIMIT 시간이 지난 데이터는 모두 삭제하고 다시 저장하기
        int previousSize = weatherDataCacheList.size();
        for (int i = 0; i < weatherDataCacheList.size(); i++) {
            MNWeatherData weatherData = weatherDataCacheList.get(i);
            if (DateTime.now().getMillis() - weatherData.timeStampInMillis >= WEATHER_REFRESH_LIMIT_TIME_IN_MILLIS) {
                weatherDataCacheList.remove(i);
            }
        }

        // 변경사항이 있다면 저장
        if (previousSize != weatherDataCacheList.size()) {
            archiveWeatherData(context);
        }
    }

    protected abstract void unarchiveWeatherDataCacheList(Context context);

    public abstract MNWeatherData findWeatherCache(double latitude, double longitude);

    public void addWeatherDataToCache(MNWeatherData weatherData, Context context) {
        // remove first one if the size overflow the limit
        if (weatherDataCacheList.size() >= CACHE_LIMIT) {
            weatherDataCacheList.remove(0);
        }
        weatherDataCacheList.add(weatherData);
        archiveWeatherData(context);
    }

    protected abstract void archiveWeatherData(Context context);

    public List<MNWeatherData> getWeatherDataCacheList() { return weatherDataCacheList; }
}
