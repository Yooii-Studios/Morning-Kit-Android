package com.yooiistudios.morningkit.panel.weather.model.parser;

import android.os.AsyncTask;

import com.yooiistudios.morningkit.panel.exchangerates.model.MNExchangeRatesParser;

import org.json.JSONObject;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 5.
 *
 * MNWeatherWWOAsyncTask
 *  현재 위치를 가지고 날씨 정보를 World Weather Online에서 가져오는 AsyncTask
 */
public class MNWeatherWWOAsyncTask extends AsyncTask<Void, Void, Double> {

    private static final String TAG = "MNExchangeRatesAsyncTask";

    String base;
    String target;
    OnWeatherWWOAsyncTaskListener listener;

    public interface OnWeatherWWOAsyncTaskListener {
        public void onWeatherInfoLoad(double rates);
    }

    public MNWeatherWWOAsyncTask(String base, String target, OnWeatherWWOAsyncTaskListener listener) {
        this.base = base;
        this.target = target;
        this.listener = listener;
    }
    @Override
    protected Double doInBackground(Void... params) {

        double rates = -1;

        String queryUrlString = String.format("http://query.yahooapis.com/v1/public/" +
                "yql?q=select * from yahoo.finance.xchange where pair in (\"%s%s\")" +
                "&format=json&env=store://datatables.org/alltableswithkeys&callback=",
                base, target);

        // 아래 두 가지 변경을 하지 않으면 제대로 된 URL로 인식을 하지 못함
        queryUrlString = queryUrlString.replace(" ", "%20");
        queryUrlString = queryUrlString.replace("\"", "%22");

        JSONObject resultJsonObject = MNExchangeRatesParser.getJSONObjectFromUrl(queryUrlString);

        if (resultJsonObject != null) {
            try {
                if (resultJsonObject.has("query")) {
                    JSONObject query = resultJsonObject.getJSONObject("query");
                    if (query != null && query.has("results")) {
                        JSONObject results = query.getJSONObject("results");
                        if (results != null && results.has("rate")) {
                            JSONObject rate = results.getJSONObject("rate");
                            if (rate != null && rate.has("Rate")) {
                                rates = rate.getDouble("Rate");
                            }
                        }
                    }
                }
                /*
                "query":{
                    "count":1,
                    "created":"2014-03-05T07:21:21Z",
                    "lang":"ko-kr",
                    "results":{
                        "rate":{
                            "Ask":"0.0057",
                            "Bid":"0.0057",
                            "Date":"3/5/2014",
                            "id":"KRWCNY",
                            "Name":"KRW to CNY",
                            "Rate":"0.0057",
                            "Time":"2:19am"
                        }
                    }
                }
                */
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rates;
    }

    @Override
    protected void onPostExecute(Double rates) {
        super.onPostExecute(rates);

        if (listener != null) {
            listener.onWeatherInfoLoad(rates);
        } else {
            throw new AssertionError("OnExchangeRatesAsyncTaskListener is null!");
        }
    }
}
