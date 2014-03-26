package com.yooiistudios.morningkit.panel.weather;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.stevenkim.waterlily.bitmapfun.util.RecyclingBitmapDrawable;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherData;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherLocationInfo;
import com.yooiistudios.morningkit.panel.weather.model.parser.MNWeatherWWOAsyncTask;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.JSONException;

import java.lang.reflect.Type;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 19.
 *
 * MNWeatherPanelLayout
 */
public class MNWeatherPanelLayout extends MNPanelLayout implements MNWeatherWWOAsyncTask.OnWeatherWWOAsyncTaskListener {

    protected static final String WEATHER_DATA_IS_USING_CURRENT_LOCATION = "WEATHER_IS_USING_CURRENT_LOCATION";
    protected static final String WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME = "WEATHER_INDICATE_LOCAL_TIME";
    protected static final String WEATHER_DATA_TEMP_CELSIUS = "WEATHER_TEMP_CELSIUS";
    protected static final String WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO = "WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO";

    // UI
    private RelativeLayout innerContentLayout;
    private RelativeLayout upperContentLayout;
    private RelativeLayout upperTempContentLayout;
    private RelativeLayout upperTempInnerContentLayout;
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
    private MNWeatherData weatherData;

    // AsyncTask
    private MNWeatherWWOAsyncTask weatherWWOAsyncTask;

    // Local time clock
    private boolean isClockRunning = false;

    public MNWeatherPanelLayout(Context context) {
        super(context);
    }
    public MNWeatherPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        // containers
        innerContentLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams innerLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        innerLayoutParams.addRule(CENTER_IN_PARENT);
        innerContentLayout.setLayoutParams(innerLayoutParams);
        getContentLayout().addView(innerContentLayout);

        // upper layout
        upperContentLayout = new RelativeLayout(getContext());
        upperContentLayout.setId(9123857);
        RelativeLayout.LayoutParams upperLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        upperLayoutParams.addRule(CENTER_HORIZONTAL);
        upperContentLayout.setLayoutParams(upperLayoutParams);
        innerContentLayout.addView(upperContentLayout);

        // image
        weatherConditionImageView = new RecyclingImageView(getContext());
        weatherConditionImageView.setId(8213774);
        int imageSize = getResources().getDimensionPixelSize(R.dimen.panel_weather_condition_image_size);
        RelativeLayout.LayoutParams imageViewParams = new LayoutParams(imageSize, imageSize);
        weatherConditionImageView.setLayoutParams(imageViewParams);
        weatherConditionImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        upperContentLayout.addView(weatherConditionImageView);

        int marginOuter = getResources().getDimensionPixelSize(R.dimen.margin_outer);

        // upper temp layout
        upperTempContentLayout = new RelativeLayout(getContext());
        upperTempContentLayout.setId(1323857);
        RelativeLayout.LayoutParams upperTempLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        upperTempLayoutParams.addRule(RIGHT_OF, weatherConditionImageView.getId());
        upperTempLayoutParams.addRule(ALIGN_TOP, weatherConditionImageView.getId());
        upperTempLayoutParams.addRule(ALIGN_BOTTOM, weatherConditionImageView.getId());
        upperTempLayoutParams.leftMargin = marginOuter;
        upperTempContentLayout.setLayoutParams(upperTempLayoutParams);
        upperContentLayout.addView(upperTempContentLayout);

        // upper temp inner layout
        upperTempInnerContentLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams upperTempInnerLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        upperTempInnerLayoutParams.addRule(CENTER_IN_PARENT);
        upperTempInnerContentLayout.setLayoutParams(upperTempInnerLayoutParams);
        upperTempContentLayout.addView(upperTempInnerContentLayout);

        // current temp
        currentTempTextView = new TextView(getContext());
        currentTempTextView.setId(1384174);
        currentTempTextView.setGravity(Gravity.CENTER);
        currentTempTextView.setSingleLine();
        RelativeLayout.LayoutParams currentTempParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        currentTempParams.addRule(CENTER_HORIZONTAL);
        currentTempTextView.setLayoutParams(currentTempParams);
        upperTempInnerContentLayout.addView(currentTempTextView);

        // lowHigh temp
        lowHighTempTextView = new TextView(getContext());
        lowHighTempTextView.setId(38417324);
        lowHighTempTextView.setGravity(Gravity.CENTER);
        lowHighTempTextView.setSingleLine();
        RelativeLayout.LayoutParams lowHighParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lowHighParams.addRule(BELOW, currentTempTextView.getId());
        lowHighParams.addRule(CENTER_HORIZONTAL);
        lowHighParams.topMargin = marginOuter;
        lowHighTempTextView.setLayoutParams(lowHighParams);
        upperTempInnerContentLayout.addView(lowHighTempTextView);

        // city name
        cityNameTextView = new TextView(getContext());
        cityNameTextView.setId(4311474);
        cityNameTextView.setGravity(Gravity.CENTER);
        cityNameTextView.setSingleLine();
        RelativeLayout.LayoutParams cityNameParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        cityNameParams.addRule(CENTER_HORIZONTAL);
        cityNameParams.addRule(BELOW, upperContentLayout.getId());
        cityNameParams.leftMargin = marginOuter;
        cityNameParams.rightMargin = marginOuter;
        cityNameTextView.setLayoutParams(cityNameParams);
        innerContentLayout.addView(cityNameTextView);

        // local time
        localTimeTextView = new TextView(getContext());
        localTimeTextView.setGravity(Gravity.CENTER);
        localTimeTextView.setSingleLine();
        RelativeLayout.LayoutParams localTimeParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        localTimeParams.addRule(CENTER_HORIZONTAL);
        localTimeParams.addRule(BELOW, cityNameTextView.getId());
        localTimeParams.leftMargin = marginOuter;
        localTimeParams.rightMargin = marginOuter;
        localTimeTextView.setLayoutParams(localTimeParams);
        innerContentLayout.addView(localTimeTextView);

        // test
        weatherConditionImageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.m_icon_weather_07_tornado)));
        currentTempTextView.setText("14°");
        lowHighTempTextView.setText("7°/19°");
        cityNameTextView.setText("Daegu");
        localTimeTextView.setText("14:11:56");

        innerContentLayout.setBackgroundColor(Color.BLUE);
        upperContentLayout.setBackgroundColor(Color.LTGRAY);
        weatherConditionImageView.setBackgroundColor(Color.CYAN);
        upperTempContentLayout.setBackgroundColor(Color.YELLOW);
        upperTempInnerContentLayout.setBackgroundColor(Color.RED);
        currentTempTextView.setBackgroundColor(Color.GREEN);
        lowHighTempTextView.setBackgroundColor(Color.MAGENTA);
        cityNameTextView.setBackgroundColor(Color.RED);
        localTimeTextView.setBackgroundColor(Color.YELLOW);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        // get data from panelDataObject
        loadPanelDataObject();

        // cancel the previous task first
        if (weatherWWOAsyncTask != null) {
            weatherWWOAsyncTask.cancel(true);
        }

        // get weather data from server
        if (isUsingCurrentLocation) {
            // WWO using current location
            // find previous data from cache
            weatherWWOAsyncTask = new MNWeatherWWOAsyncTask(selectedLocationInfo, getContext(), false, this);
        } else {
            // Yahoo using woeid -> iOS 소스를 보니까 전부 WWO를 사용하게 변경이 되었네
            // find previous data from cache
            weatherWWOAsyncTask = new MNWeatherWWOAsyncTask(selectedLocationInfo, getContext(), true, this);
        }
        weatherWWOAsyncTask.execute();
    }

    private void loadPanelDataObject() throws JSONException {
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
        super.updateUI();

        // weather condition
        if (weatherData.weatherCondition != null) {
            weatherConditionImageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(),
                    BitmapFactory.decodeResource(getResources(), weatherData.weatherCondition.getConditionResourceId())));
        }

        // temp
        if (isUsingCelsius) {
            currentTempTextView.setText(weatherData.currentCelsiusTemp);
            lowHighTempTextView.setText(weatherData.lowHighCelsiusTemp);
        } else {
            currentTempTextView.setText(weatherData.currentFahrenheitTemp);
            lowHighTempTextView.setText(weatherData.lowHighFahrenheitTemp);
        }

        // city name
        if (weatherData.weatherLocationInfo.getName() != null) {
            cityNameTextView.setText(capitalize(weatherData.weatherLocationInfo.getName()));
        }

        // local time
    }

    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    // AsyncTask
    @Override
    public void onSucceedLoadingWeatherInfo(MNWeatherData weatherData) {
        this.weatherData = weatherData;
        startClock();
        updateUI();
    }

    @Override
    public void onFailedLoadingWeatherInfo() {
        showNetworkIsUnavailable();
    }

    // Clock
    private Handler clockHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ){
            if (isClockRunning) {

                // tick(계산)
                String timeString = "";
                if (weatherData != null) {
                    LocalDateTime localDateTime = LocalDateTime.now(DateTimeZone.forOffsetMillis((int)weatherData.timeOffsetInMillis));
                    timeString = localDateTime.toString("HH:mm:ss");
                }

                // UI갱신
                localTimeTextView.setText(timeString);

                // tick의 동작 시간을 계산해서 정확히 1초마다 UI 갱신을 요청할 수 있게 구현
                long endMilli = System.currentTimeMillis();
                long delay = endMilli % 1000;

                clockHandler.sendEmptyMessageDelayed(0, 1000 - delay);
            }
        }
    };

    private void startClock() {
        if (isClockRunning) {
            return;
        }
        isClockRunning = true;

        int diffInMilli = (int) System.currentTimeMillis() % 1000;
        clockHandler.sendEmptyMessageDelayed(0, 1000 - diffInMilli);
    }

    private void stopClock() {
        if (!isClockRunning) {
            return;
        }
        isClockRunning = false;
    }

    // 패널이 없어질 때 핸들러 중지
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopClock();
    }
}
