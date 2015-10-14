package com.yooiistudios.morningkit.common.json;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 5.
 *
 * MNExchangeRatesParser
 *  JSON을 이용해서 환율 정보를 가지고 옴
 */
public class MNJsonUtils {
    private MNJsonUtils() { throw new AssertionError("You MUST not create this class!"); }
    public static JSONObject getJsonObjectFromUrl(String urlString) {
        BufferedInputStream inputStream = null;
        String result;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            inputStream = new BufferedInputStream(conn.getInputStream());

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
