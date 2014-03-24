package com.yooiistudios.morningkit.panel.weather;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.weather.model.MNWeatherLocationInfo;

import org.json.JSONException;

import java.lang.reflect.Type;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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

    // UI
    private RelativeLayout innerContentLayout;
    private RecyclingImageView weatherConditionImageView;
    private TextView currentTempTextView;
    private TextView lowHighTempTextView;
    private TextView cityNameTextView;
    private TextView localTimeTextView;

    // Model
    private boolean isUsingCurrentLocation = true;
    private boolean isDisplayingLocaltime = true;
    private boolean isUsingCelsius = true;
    private MNWeatherLocationInfo selectedLocationInfo;

    public MNWeatherPanelLayout(Context context) {
        super(context);
    }
    public MNWeatherPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        // containers
        innerContentLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams innerLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        innerLayoutParams.addRule(CENTER_IN_PARENT);
        innerContentLayout.setLayoutParams(innerLayoutParams);
        getContentLayout().addView(innerContentLayout);

        // image
        weatherConditionImageView = new RecyclingImageView(getContext());
        weatherConditionImageView.setId(8213774);
        RelativeLayout.LayoutParams imageViewParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        weatherConditionImageView.setLayoutParams(imageViewParams);
        getContentLayout().addView(weatherConditionImageView);

        // current temp
        currentTempTextView = new TextView(getContext());
        currentTempTextView.setId(1384174);
        RelativeLayout.LayoutParams currentTempParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        currentTempParams.addRule(CENTER_HORIZONTAL);
        currentTempParams.addRule(RIGHT_OF, weatherConditionImageView.getId());
        currentTempTextView.setLayoutParams(currentTempParams);
        getContentLayout().addView(currentTempTextView);

        // lowHigh temp
        lowHighTempTextView = new TextView(getContext());
        lowHighTempTextView.setId(38417324);
        RelativeLayout.LayoutParams lowHighParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        lowHighParams.addRule(CENTER_HORIZONTAL);
        lowHighParams.addRule(RIGHT_OF, weatherConditionImageView.getId());
        lowHighParams.addRule(BELOW, lowHighTempTextView.getId());
        lowHighTempTextView.setLayoutParams(lowHighParams);
        getContentLayout().addView(lowHighTempTextView);

        // city name
        cityNameTextView = new TextView(getContext());
        cityNameTextView.setId(4311474);
        RelativeLayout.LayoutParams cityNameParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        cityNameParams.addRule(CENTER_HORIZONTAL);
        lowHighParams.addRule(BELOW, weatherConditionImageView.getId());
        cityNameTextView.setLayoutParams(cityNameParams);
        getContentLayout().addView(cityNameTextView);

        // local time
        localTimeTextView = new TextView(getContext());
        RelativeLayout.LayoutParams localTimeParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        cityNameParams.addRule(CENTER_HORIZONTAL);
        cityNameParams.addRule(BELOW, cityNameTextView.getId());
        localTimeTextView.setLayoutParams(localTimeParams);
        getContentLayout().addView(localTimeTextView);

        // test
    }

    @Override
    protected void processLoading() throws JSONException {
        if (getPanelDataObject().has(WEATHER_DATA_IS_USING_CURRENT_LOCATION)) {
            // 기본은 현재위치 사용
            isUsingCurrentLocation = getPanelDataObject().getBoolean(WEATHER_DATA_IS_USING_CURRENT_LOCATION);
        }
        if (getPanelDataObject().has(WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME)) {
            // 기본은 로컬 시간 사용
            isDisplayingLocaltime = getPanelDataObject().getBoolean(WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME);
        }
        if (getPanelDataObject().has(WEATHER_DATA_TEMP_CELSIUS)) {
            isUsingCelsius = getPanelDataObject().getBoolean(WEATHER_DATA_TEMP_CELSIUS);
        } else {
            // 미국만 fahrenheit 사용
            isUsingCelsius = !getResources().getConfiguration().locale.getCountry().equals("US");
        }
        if (getPanelDataObject().has(WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO)) {
            Type type = new TypeToken<MNWeatherLocationInfo>(){}.getType();
            selectedLocationInfo = new Gson().fromJson(getPanelDataObject().getString(WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO), type);
        }
    }

    @Override
    protected void updateUI() {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
