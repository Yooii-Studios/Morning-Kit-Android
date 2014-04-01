package com.yooiistudios.morningkit.panel.weather.model.cache;

import android.content.Context;

import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherData;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 1.
 *
 * MNWeatherDataCurrentLocationCache
 *  현재 위치 정보들로 얻은 날씨 데이터를 캐싱하는 클래스
 */
public class MNWeatherDataCurrentLocationCache extends MNWeatherDataCache {
    public MNWeatherDataCurrentLocationCache(Context context) {
        super(context);
    }

    @Override
    protected void unarchiveWeatherDataCacheList(Context context) {

    }

    @Override
    public MNWeatherData findWeatherCache(double latitude, double longitude) {
        return null;
    }

    @Override
    protected void archiveWeatherData(MNWeatherData weatherData, Context context) {

    }
}
