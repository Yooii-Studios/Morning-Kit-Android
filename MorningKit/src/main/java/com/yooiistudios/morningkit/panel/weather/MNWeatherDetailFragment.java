package com.yooiistudios.morningkit.panel.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;

import org.json.JSONException;

import butterknife.ButterKnife;

import static com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout.WEATHER_IS_DISPLAYING_LOCAL_TIME;
import static com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout.WEATHER_IS_USING_CURRENT_LOCATION;
import static com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout.WEATHER_TEMP_CELSIUS;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 19.
 *
 * MNWeatherDetailFragment
 */
public class MNWeatherDetailFragment extends MNPanelDetailFragment {
    private static final String TAG = "MNWeatherDetailFragment";

    boolean isUsingCurrentLocation = true;
    boolean isDisplayingLocaltime = true;
    boolean isUsingCelsius = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_world_clock_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            if (getPanelDataObject().has(WEATHER_IS_USING_CURRENT_LOCATION)) {
                try {
                    // 기본은 현재위치 사용
                    isUsingCurrentLocation = getPanelDataObject().getBoolean(WEATHER_IS_USING_CURRENT_LOCATION);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (getPanelDataObject().has(WEATHER_IS_DISPLAYING_LOCAL_TIME)) {
                try {
                    // 기본은 로컬 시간 사용
                    isDisplayingLocaltime = getPanelDataObject().getBoolean(WEATHER_IS_DISPLAYING_LOCAL_TIME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (getPanelDataObject().has(WEATHER_TEMP_CELSIUS)) {
                try {
                    isUsingCelsius = getPanelDataObject().getBoolean(WEATHER_TEMP_CELSIUS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // 미국만 fahrenheit 사용
                isUsingCelsius = !getResources().getConfiguration().locale.getCountry().equals("US");
            }

            // UI
            initUI();
        }
        return rootView;
    }

    private void initUI() {

    }

    @Override
    protected void archivePanelData() throws JSONException {

    }
}
