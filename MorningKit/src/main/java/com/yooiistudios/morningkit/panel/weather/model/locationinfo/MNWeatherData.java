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
    }

    public MNWeatherLocationInfo weatherLocationInfo;
//    String currentTemp;
//    String todayTemp;
    public String currentCelsiusTemp;
    public String lowHighCelsiusTemp;

    public String currentFahrenheitTemp;
    public String lowHighFahrenheitTemp;

    public String timeString;

    public long timeStampInMillis; // 날씨가 로딩되었을 당시의 시간 timeInMillis
}
