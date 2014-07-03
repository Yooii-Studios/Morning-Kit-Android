package com.yooiistudios.morningkit.panel.exchangerates.model;

import android.os.AsyncTask;

import com.yooiistudios.morningkit.common.json.MNJsonUtils;

import org.json.JSONObject;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 5.
 *
 * MNExchangeRatesAsyncTask
 *  환율 정보를 비동기로 가져오는 AsyncTask
 *  환율 서버는 야후 finance로 2014년 1월에 새로 수정했음
 */
public class MNExchangeRatesAsyncTask extends AsyncTask<Void, Void, Double> {

    private static final String TAG = "MNExchangeRatesAsyncTask";

    String baseCountry;
    String targetCountry;
    OnExchangeRatesAsyncTaskListener listener;

    public interface OnExchangeRatesAsyncTaskListener {
        public void onExchangeRatesLoad(double rates);
    }

    public MNExchangeRatesAsyncTask(String base, String target, OnExchangeRatesAsyncTaskListener listener) {
        this.baseCountry = base;
        this.targetCountry = target;
        this.listener = listener;
    }

    @Override
    protected Double doInBackground(Void... params) {

        double rates = getExchangeRates(baseCountry, targetCountry);

        // 여기서 문제가 있음. 만약 0.001 이하의 값이라면 제대로 환율이 적용된 것이라 보기 힘듬.
        // 이경우에는 reversed된 환율을 얻어와서 1/반대환율 을 통해 값을 얻어야 할 것.
        if (rates <= 0.001) {
            double reversedRates = getExchangeRates(targetCountry, baseCountry);
            rates = 1.0f / reversedRates;
        }
        return rates;
    }

    @Override
    protected void onPostExecute(Double rates) {
        super.onPostExecute(rates);

        if (listener != null) {
            listener.onExchangeRatesLoad(rates);
        } else {
            throw new AssertionError("OnExchangeRatesAsyncTaskListener is null!");
        }
    }

    private double getExchangeRates(String baseCountry, String targetCountry) {
        double rates = -1;

        String queryUrlString = String.format("http://query.yahooapis.com/v1/public/" +
                        "yql?q=select * from yahoo.finance.xchange where pair in (\"%s%s\")" +
                        "&format=json&env=store://datatables.org/alltableswithkeys&callback=",
                baseCountry, targetCountry);

        // 아래 두 가지 변경을 하지 않으면 제대로 된 URL로 인식을 하지 못함
        queryUrlString = queryUrlString.replace(" ", "%20");
        queryUrlString = queryUrlString.replace("\"", "%22");

        JSONObject resultJsonObject = MNJsonUtils.getJsonObjectFromUrl(queryUrlString);

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

}
