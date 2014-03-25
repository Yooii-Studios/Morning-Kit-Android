package com.yooiistudios.morningkit.panel.exchangerates.model;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 5.
 *
 * MNExchangeRatesParser
 *  JSON을 이용해서 환율 정보를 가지고 옴
 */
public class MNExchangeRatesParser {
    private MNExchangeRatesParser() { throw new AssertionError("You MUST not create this class!"); }
    public static JSONObject getJSONObjectFromUrl(String url) {
        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost(url);
        // Depends on your web service
//        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result;
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();

            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();

            // return JSONObject if succeeded loading
            if (result != null) {
                return new JSONObject(result);
            }
        } catch (Exception e) {
            // Oops
        } finally {
            try{
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception ignored) {}
        }
        return null;
    }
}
