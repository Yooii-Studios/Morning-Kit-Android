package com.yooiistudios.morningkit.panel.weather.model.parser;

import com.yooiistudios.morningkit.common.json.MNJsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 31.
 *
 * MNReverseGeoCodeParser
 *  구글에서 위도, 경도로 도시 이름을 가져오는 Reverse Coding 유틸리티 클래스
 */
public class MNReverseGeoCodeParser {
    private MNReverseGeoCodeParser() { throw new AssertionError("You MUST not create this class!"); }

    public static String getCityNameFromLocation(double latitude, double longitude, String languageCode) throws JSONException {
        String reverseGeoCodeUrlString = String.format(
                "http://maps.google.com/maps/api/geocode/json?" +
                        "address=" + latitude + "," + longitude + "&sensor=true&" +
                        "language=" + languageCode);

        JSONObject reverseGeoCodeJsonObject = MNJsonUtils.getJsonObjectFromUrl(reverseGeoCodeUrlString);

        if (reverseGeoCodeJsonObject != null && reverseGeoCodeJsonObject.has("status")) {
            if (reverseGeoCodeJsonObject.getString("status").equals("OK")) {
                JSONObject locationData = reverseGeoCodeJsonObject.getJSONArray("results").getJSONObject(0);

                if (locationData != null && locationData.has("address_components")) {
                    JSONArray addressComponents = locationData.getJSONArray("address_components");

                    for (int i = 0; i < addressComponents.length(); i++) {
                        JSONObject addressComponent = addressComponents.getJSONObject(i);
                        if (addressComponent != null && addressComponent.has("types")) {
                            JSONArray types = addressComponent.getJSONArray("types");
                            JSONArray wantedTypes = new JSONArray();
                            wantedTypes.put("locality");
                            wantedTypes.put("political");

                            if (types.equals(wantedTypes)) {
                                if (addressComponent.has("long_name")) {
                                    return addressComponent.getString("long_name");
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
