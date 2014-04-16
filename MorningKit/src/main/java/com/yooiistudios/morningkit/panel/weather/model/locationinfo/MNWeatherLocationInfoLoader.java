package com.yooiistudios.morningkit.panel.weather.model.locationinfo;

import android.content.Context;

import com.stevenkim.waterlily.bitmapfun.util.AsyncTask;
import com.yooiistudios.morningkit.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 21.
 *
 * MNWeatherLocationInfoLoader
 *  비동기로 날씨 위치정보 리스트를 읽음
 */
public class MNWeatherLocationInfoLoader extends AsyncTask<Void, Void, List<MNWeatherLocationInfo>> {

    private Context context;
    private OnWeatherLocatinInfoLoaderListener listener;

    public interface OnWeatherLocatinInfoLoaderListener {
        public void OnWeatherLocationInfoLoad(List<MNWeatherLocationInfo> weatherLocationInfoList);
    }

    public MNWeatherLocationInfoLoader(Context context, OnWeatherLocatinInfoLoaderListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected List<MNWeatherLocationInfo> doInBackground(Void... params) {
        ArrayList<MNWeatherLocationInfo> weatherLocationInfoList = new ArrayList<MNWeatherLocationInfo>();
        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_us_high_priority_woeid));
        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_us_woeid));
        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_au_woeid));
        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_jp_woeid));
        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_gb_woeid));
        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_cn_woeid));
        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_kr_woeid));
        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_ca_woeid));
        // 퍼포먼스 문제를 생각해서 빼 버리자. 나중에 필요하면 다시 사용해도 좋을듯
//        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_etc_woeid));
//        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_etc_2_woeid));
//        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_etc_3_woeid));
//        weatherLocationInfoList.addAll(loadWeatherLocationInfo(R.raw.cityinfo_etc_4_woeid));

        return weatherLocationInfoList;
    }

    @Override
    protected void onPostExecute(List<MNWeatherLocationInfo> weatherLocationInfoList) {
        super.onPostExecute(weatherLocationInfoList);
        if (listener != null) {
            listener.OnWeatherLocationInfoLoad(weatherLocationInfoList);
        }
    }

    private List<MNWeatherLocationInfo> loadWeatherLocationInfo(int cityInfoRawId) {
        List<MNWeatherLocationInfo> weatherLocationInfoList = new ArrayList<MNWeatherLocationInfo>();
        InputStream file = context.getResources().openRawResource(cityInfoRawId);
        byte[] tB;

        try {
            tB = new byte[file.available()];
            file.read(tB);
            String buffer = new String(tB);
            String[] lines = buffer.split("\n");

            for (int i = 0; i < lines.length; i += 5) {
                MNWeatherLocationInfo weatherLocationInfo = new MNWeatherLocationInfo();

                // name(english)/originalName/otherName1:otherName2:othername3;woeid
                String cityNamesAndWoeid[] = lines[i].split(";");
                if (cityNamesAndWoeid.length == 2) {
                    // city names
                    String allCityNames[] = cityNamesAndWoeid[0].split("/");

                    if (allCityNames.length == 3) {
                        // 본토 표기나 다른 검색어가 있는 경우
                        weatherLocationInfo.name = allCityNames[0];
                        weatherLocationInfo.englishName = weatherLocationInfo.name;
                        if (!allCityNames[1].equals("")) {
                            weatherLocationInfo.originalName = allCityNames[1];
                        } else {
                            weatherLocationInfo.originalName = null;
                        }
                        if (!allCityNames[2].equals("")) {
                            String[] otherNames = allCityNames[2].split(":");
                            Collections.addAll(weatherLocationInfo.otherNames, otherNames);
                        } else {
                            weatherLocationInfo.otherNames = null;
                        }
                    } else if (allCityNames.length == 2) {
                        // 영어, 본토 표기만 있고, 다른 표기가 없는 경우(ex: 서울)
                        weatherLocationInfo.name = allCityNames[0];
                        weatherLocationInfo.englishName = weatherLocationInfo.name;
                        weatherLocationInfo.originalName = allCityNames[1];
                        weatherLocationInfo.otherNames = null;
                    } else if (allCityNames.length == 1) {
                        // 영어 표기만 있을 경우
                        weatherLocationInfo.name = allCityNames[0];
                        weatherLocationInfo.englishName = weatherLocationInfo.name;
                        weatherLocationInfo.originalName = null;
                        weatherLocationInfo.otherNames = null;
                    }
                    // woeid
                    weatherLocationInfo.woeid = Integer.valueOf(cityNamesAndWoeid[1]);
                    weatherLocationInfo.latitude = Float.valueOf(lines[i + 1]);
                    weatherLocationInfo.longitude = Float.valueOf(lines[i + 2]);
                    weatherLocationInfo.countryCode = lines[i + 3];
                    weatherLocationInfo.regionCode = lines[i + 4];

                    weatherLocationInfoList.add(weatherLocationInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherLocationInfoList;
    }
}
