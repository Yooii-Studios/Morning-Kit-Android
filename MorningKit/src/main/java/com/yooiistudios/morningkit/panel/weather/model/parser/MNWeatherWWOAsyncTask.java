package com.yooiistudios.morningkit.panel.weather.model.parser;

import android.content.Context;
import android.os.AsyncTask;

import com.yooiistudios.morningkit.common.json.MNJsonUtils;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWWOWeatherCondition;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherData;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherLocationInfo;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 5.
 *
 * MNWeatherWWOAsyncTask
 *  현재 위치를 가지고 날씨 정보를 World Weather Online에서 가져오는 AsyncTask
 */
public class MNWeatherWWOAsyncTask extends AsyncTask<Void, Void, MNWeatherData> {

    private static final String TAG = "MNExchangeRatesAsyncTask";
    private static final String WWO_KEY = "k2zbbj8yamvc2rjfpfs6yxhn"; // WWO Premium LIVE Key
//    private static final String WWO_FREE_KEY = "5nz2zqymjhewyhusjsfqw6nu"; // WWO Free Key

    MNWeatherLocationInfo locationInfo;
    Context context;
    OnWeatherWWOAsyncTaskListener listener;
    boolean isTaskForWeatherOnly = false;

    public interface OnWeatherWWOAsyncTaskListener {
        public void onSucceedLoadingWeatherInfo(MNWeatherData weatherData);
        public void onFailedLoadingWeatherInfo();
    }

    public MNWeatherWWOAsyncTask(MNWeatherLocationInfo locationInfo, Context context, boolean isTaskForWeatherOnly,
                                 OnWeatherWWOAsyncTaskListener listener) {
        this.locationInfo = locationInfo;
        this.context = context;
        this.isTaskForWeatherOnly = isTaskForWeatherOnly;
        this.listener = listener;
    }

    @Override
    protected MNWeatherData doInBackground(Void... params) {

        MNWeatherData weatherData = null;

        String queryUrlString = String.format(
                "http://api.worldweatheronline.com/premium/v1/weather.ashx?q=+"
                + locationInfo.getLatitude() + "," + locationInfo.getLongitude()
                + "&format=json&extra=localObsTime&num_of_days=1&date=today"
                + "&includelocation=yes&show_comments=no&key=" + WWO_KEY);

//        MNLog.now("queryUrlString: " + queryUrlString);

        // 1번 블락: 날씨 가져오기
        // 2번 블락: 구글에서 도시 이름 가져오기
        // 3번 블락: 구글에서 현재 언어 도시 가져오기
        // 4번 블락: 세 블락 작업이 끝나고 리턴하기 위해 sync를 사용

        // 아래 두 가지 변경을 하지 않으면 제대로 된 URL로 인식을 하지 못함
        queryUrlString = queryUrlString.replace(",", "%2C");

        JSONObject resultJsonObject = MNJsonUtils.getJsonObjectFromUrl(queryUrlString);
//        MNLog.now("resultJsonObject: " + resultJsonObject);

        if (resultJsonObject != null) {
            try {
                if (resultJsonObject.has("data")) {
                    JSONObject allWeatherData = resultJsonObject.getJSONObject("data");
                    if (allWeatherData != null && allWeatherData.has("current_condition")) {

                        // 정보가 제대로 있다고 판단하고 인스턴스 할당
                        weatherData = new MNWeatherData();
                        if (locationInfo != null) {
                            weatherData.weatherLocationInfo.setLatitude(locationInfo.getLatitude());
                            weatherData.weatherLocationInfo.setLongitude(locationInfo.getLongitude());
                        }

                        // time stamp - 로딩 당시의 초를 기록(캐시 비교용)
                        weatherData.timeStampInMillis = Calendar.getInstance().getTimeInMillis();

                        // 현재 날씨 파악
                        if (allWeatherData.has("current_condition")) {
                            JSONObject currentConditionData = allWeatherData.getJSONArray("current_condition").getJSONObject(0);

                            // weather code
                            if (currentConditionData.has("weatherCode")) {
                                int weatherCode = currentConditionData.getInt("weatherCode");
                                weatherData.weatherCondition = MNWWOWeatherCondition.valueOfWeatherCode(weatherCode);
                            }

                            // temperature
                            if (currentConditionData.has("temp_C")) {
                                weatherData.currentCelsiusTemp = currentConditionData.getString("temp_C") + "°";
                            }
                            if (currentConditionData.has("temp_F")) {
                                weatherData.currentFahrenheitTemp = currentConditionData.getString("temp_F") + "°";
                            }

                            // timeOffset
                            if (currentConditionData.has("localObsDateTime")) {
                                // yyyy-MM-dd hh:mm a
                                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm a");
//                                DateTimeFormatter printFormatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss a");

                                // utc(0초로)
                                DateTime utcNowTime = DateTime.now(DateTimeZone.UTC);
                                utcNowTime = new DateTime(utcNowTime.getYear(), utcNowTime.getMonthOfYear(),
                                        utcNowTime.getDayOfMonth(), utcNowTime.getHourOfDay(), utcNowTime.getMinuteOfHour());
//                                MNLog.now("utcNowTime: " + printFormatter.withLocale(Locale.US).print(utcNowTime));


                                // local time - 원래 0초로 옴
                                String dateString = currentConditionData.getString("localObsDateTime");
                                DateTime localNowTime = formatter.withLocale(Locale.US).parseDateTime(dateString);
//                                MNLog.now("localNowTime: " + printFormatter.withLocale(Locale.US).print(localNowTime));

                                // calculate offset
                                weatherData.timeOffsetInMillis = localNowTime.getMillis() - utcNowTime.getMillis();

                                // offset이 몇 초 차이로 900(15분)으로 나누어 떨어지지 않는다면 1분을 더해 다시 timeOffset을 계산
                                // 시간대는 30분, 45분이 차이가 날 경우도 있기 때문에 15분으로 나누어서 확인할 필요가 있음
                                if ((weatherData.timeOffsetInMillis / 1000) % 900 != 0) {
                                    localNowTime = localNowTime.plusMinutes(1);
                                    weatherData.timeOffsetInMillis = localNowTime.getMillis() - utcNowTime.getMillis();
                                }
                            }
                        }

                        // 오늘 평균 날씨 파악
                        if (allWeatherData.has("weather")) {
                            JSONObject todayConditionData = allWeatherData.getJSONArray("weather").getJSONObject(0);
                            if (todayConditionData.has("mintempC") && todayConditionData.has("maxtempC")) {
                                weatherData.lowHighCelsiusTemp = todayConditionData.getString("mintempC")
                                        + "°/" + todayConditionData.getString("maxtempC") + "°";
                            }
                            if (todayConditionData.has("mintempF") && todayConditionData.has("maxtempF")) {
                                weatherData.lowHighFahrenheitTemp = todayConditionData.getString("mintempF")
                                        + "°/" + todayConditionData.getString("maxtempF") + "°";
                            }
                        }
                        if (isTaskForWeatherOnly) {
                            // 기존 위치 정보가 제대로 있다고 판단, 기존 정보 대입
                            weatherData.weatherLocationInfo.setName(locationInfo.getName());
                            weatherData.weatherLocationInfo.setEnglishName(locationInfo.getEnglishName());
                            weatherData.weatherLocationInfo.setWoeid(locationInfo.getWoeid());
                        } else {
                            // 현재 언어 코드를 가져오기 - 중국일 경우 region 도 제대로 입력 필요
                            MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(context);
                            String languageCode;
                            if (currentLanguageType == MNLanguageType.SIMPLIFIED_CHINESE ||
                                    currentLanguageType == MNLanguageType.TRADITIONAL_CHINESE) {
                                languageCode = currentLanguageType.getCode() + "-" +
                                        currentLanguageType.getRegion();
                            } else {
                                languageCode = currentLanguageType.getCode();
                            }
                            // 지오코딩을 통해 현재 위치의 정보를 로딩
                            String geoCodedCityName = MNReverseGeoCodeParser.getCityNameFromLocation(
                                    locationInfo.getLatitude(), locationInfo.getLongitude(), languageCode);
                            weatherData.weatherLocationInfo.setName(geoCodedCityName);
                        }
                    }
                }

                /*
                "data": {
		            "current_condition": [
			        {
				        "cloudcover": "75",
				        "FeelsLikeC": "12",
				        "FeelsLikeF": "54",
				        "humidity": "44",
				        "localObsDateTime": "2014-03-24 10:37 PM",
				        "observation_time": "03:37 AM",
				        "precipMM": "0.0",
				        "pressure": "1016",
				        "temp_C": "12",
				        "temp_F": "54",
				        "visibility": "16",
				        "weatherCode": "116",
				        "weatherDesc": [
					    {
						    "value": "Partly Cloudy"
					    }
				],
                */
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return weatherData;
    }

    @Override
    protected void onPostExecute(MNWeatherData weatherData) {
        super.onPostExecute(weatherData);
        if (listener != null) {
            if (weatherData != null) {
                listener.onSucceedLoadingWeatherInfo(weatherData);
            } else {
                listener.onFailedLoadingWeatherInfo();
            }
        } else {
            throw new AssertionError("OnExchangeRatesAsyncTaskListener is null!");
        }
    }
}
