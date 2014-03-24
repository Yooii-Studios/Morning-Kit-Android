package com.yooiistudios.morningkit.panel.weather;

import android.content.Context;
import android.util.AttributeSet;

import com.yooiistudios.morningkit.panel.core.MNPanelLayout;

import org.json.JSONException;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 19.
 *
 * MNWeatherPanelLayout
 */
public class MNWeatherPanelLayout extends MNPanelLayout {

    protected static final String WEATHER_DATA_IS_USING_CURRENT_LOCATION = "WEATHER_IS_USING_CURRENT_LOCATION";
    protected static final String WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME = "WEATHER_INDICATE_LOCAL_TIME";
    protected static final String WEATHER_DATA_TEMP_CELSIUS = "WEATHER_TEMP_CELSIUS";
    protected static final String WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO = "WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO";

    public MNWeatherPanelLayout(Context context) {
        super(context);
    }

    public MNWeatherPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void processLoading() {

    }

    @Override
    protected void updateUI() {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
