package com.yooiistudios.morningkit.panel.weather.model.parser;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNExchangeRatesParser;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherData;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherLocationInfo;

import org.json.JSONObject;

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

    Location location;
    Context context;
    OnWeatherWWOAsyncTaskListener listener;

    public interface OnWeatherWWOAsyncTaskListener {
        public void onSucceedLoadingWeatherInfo(MNWeatherData weatherData);
        public void onFailedLoadingWeatherInfo();
    }

    public MNWeatherWWOAsyncTask(Location location, Context context, OnWeatherWWOAsyncTaskListener listener) {
        this.location = location;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected MNWeatherData doInBackground(Void... params) {

        MNWeatherData weatherLocationInfo = null;

        String queryUrlString = String.format(
                "http://api.worldweatheronline.com/premium/v1/weather.ashx?q=+"
                + location.getLatitude() + "," + location.getLongitude()
                + "&format=json&extra=localObsTime&num_of_days=1&date=today"
                + "&includelocation=yes&show_comments=no&key=" + WWO_KEY);

        // 1번 블락: 날씨 가져오기
        // 2번 블락: 구글에서 도시 이름 가져오기
        // 3번 블락: 구글에서 현재 언어 도시 가져오기
        // 4번 블락: 세 블락 작업이 끝나고 리턴하기 위해 sync를 사용

        // 아래 두 가지 변경을 하지 않으면 제대로 된 URL로 인식을 하지 못함
        queryUrlString = queryUrlString.replace(",", "%C");

        JSONObject resultJsonObject = MNExchangeRatesParser.getJSONObjectFromUrl(queryUrlString);

        if (resultJsonObject != null) {
            try {
                if (resultJsonObject.has("data")) {
                    JSONObject data = resultJsonObject.getJSONObject("data");
                    if (data != null && data.has("current_condition")) {
                        JSONObject current_condition = data.getJSONObject("current_condition");
                        MNLog.now("current_condition: " + current_condition);

//                        String weatherCondition = current_condition.getString("weatherCode");
//                        String todayTemp = current_condition.getString("todayTemp");

//                        String dateString = current_condition.getString("localObsDateTime");

//                        if (results != null && results.has("rate")) {
//                            JSONObject rate = results.getJSONObject("rate");
//                            if (rate != null && rate.has("Rate")) {
//                                rates = rate.getDouble("Rate");
//                            }
//                        }
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
        return weatherLocationInfo;
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
