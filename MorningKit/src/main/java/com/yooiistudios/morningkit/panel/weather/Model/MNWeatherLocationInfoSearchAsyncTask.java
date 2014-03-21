package com.yooiistudios.morningkit.panel.weather.Model;

import android.content.Context;

import com.stevenkim.waterlily.bitmapfun.util.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 21.
 *
 * MNWeatherLocationInfoSearchAsyncTask
 */
public class MNWeatherLocationInfoSearchAsyncTask extends AsyncTask<String, Void, List<MNWeatherLocationInfo>> {

    private Context context;
    private List<MNWeatherLocationInfo> weatherLocationInfoList;
    private MNWeatherLocationInfoSearchAsyncTaskListener listener;

    public interface MNWeatherLocationInfoSearchAsyncTaskListener {
        public void OnSearchFinished(List<MNWeatherLocationInfo> filteredWeatherLocationInfoList);
    }

    public MNWeatherLocationInfoSearchAsyncTask(Context context,
                                                List <MNWeatherLocationInfo> weatherLocationInfoList,
                                                MNWeatherLocationInfoSearchAsyncTaskListener listener) {
        this.context = context;
        this.weatherLocationInfoList = weatherLocationInfoList;
        this.listener = listener;
    }

    @Override
    protected List<MNWeatherLocationInfo> doInBackground(String... params) {

        if (params.length > 0) {
            CharSequence s = params[0];

            String searchingString = s.toString().toLowerCase();

            if (!searchingString.isEmpty()) {
                ArrayList<MNWeatherLocationInfo> startWithLocationInfoList = new ArrayList<MNWeatherLocationInfo>();
                ArrayList<MNWeatherLocationInfo> containsWithLocationInfoList = new ArrayList<MNWeatherLocationInfo>();

                // 로딩이 된 후 사용을 해야만 함
                if (weatherLocationInfoList != null) {
                    for (MNWeatherLocationInfo locationInfo : weatherLocationInfoList) {
                        if (locationInfo.startsWith(searchingString)) {
                            startWithLocationInfoList.add(locationInfo);
                        } else if (locationInfo.contains(searchingString)) {
                            containsWithLocationInfoList.add(locationInfo);
                        }
                    }
                }

                startWithLocationInfoList.addAll(containsWithLocationInfoList);
                return startWithLocationInfoList;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<MNWeatherLocationInfo> filteredWeatherLocationInfoList) {
        super.onPostExecute(filteredWeatherLocationInfoList);

        if (listener != null) {
            listener.OnSearchFinished(filteredWeatherLocationInfoList);
        }
    }
}
