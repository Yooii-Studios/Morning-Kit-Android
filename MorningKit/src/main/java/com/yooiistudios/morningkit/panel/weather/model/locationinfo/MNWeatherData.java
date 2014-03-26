package com.yooiistudios.morningkit.panel.weather.model.locationinfo;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 25.
 *
 * MNWeatherData
 *  날씨 정보와 위치를 포함하는 자료 구조
 */
public class MNWeatherData {
    public MNWeatherData() {
        weatherLocationInfo = new MNWeatherLocationInfo();
        weatherCondition = MNWWOWeatherCondition.no_data;
    }

    public MNWeatherLocationInfo weatherLocationInfo;

    public MNWWOWeatherCondition weatherCondition;

//    String currentTemp;
//    String todayTemp;
    public String currentCelsiusTemp;
    public String lowHighCelsiusTemp;

    public String currentFahrenheitTemp;
    public String lowHighFahrenheitTemp;

    public String timeString;

    public long timeStampInMillis; // 날씨가 로딩되었을 당시의 시간 timeInMillis

    public long timeOffsetInMillis; // 현지 시간을 위한 time offset (초)
}
