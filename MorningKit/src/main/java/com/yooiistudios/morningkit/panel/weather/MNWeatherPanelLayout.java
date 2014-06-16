package com.yooiistudios.morningkit.panel.weather;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.tutorial.MNTutorialManager;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.weather.model.LocationUtils;
import com.yooiistudios.morningkit.panel.weather.model.cache.MNWeatherDataCurrentLocationCache;
import com.yooiistudios.morningkit.panel.weather.model.cache.MNWeatherDataSearchCityCache;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherData;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherLocationInfo;
import com.yooiistudios.morningkit.panel.weather.model.parser.MNWeatherWWOAsyncTask;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 19.
 *
 * MNWeatherPanelLayout
 */
public class MNWeatherPanelLayout extends MNPanelLayout implements
        MNWeatherWWOAsyncTask.OnWeatherWWOAsyncTaskListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "MNWeatherPanelLayout";

    protected static final String WEATHER_DATA_IS_USING_CURRENT_LOCATION = "WEATHER_IS_USING_CURRENT_LOCATION";
    protected static final String WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME = "WEATHER_INDICATE_LOCAL_TIME";
    protected static final String WEATHER_DATA_TEMP_CELSIUS = "WEATHER_TEMP_CELSIUS";
    protected static final String WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO = "WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO";

    // UI
    private RelativeLayout innerContentLayout;
    private RelativeLayout upperContentLayout;
    private RelativeLayout upperTempContentLayout;
    private RelativeLayout upperTempInnerContentLayout;
    private ImageView weatherConditionImageView;
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

    // LocalTime
    private boolean isClockRunning = false;

    // Current Location
    private LocationClient locationClient;
    private LocationRequest mLocationRequest; // A request to connect to Location Services

    // Cache
    private MNWeatherDataSearchCityCache searchCityWeatherDataCache;
    private MNWeatherDataCurrentLocationCache currentLocationWeatherDataCache;

    public MNWeatherPanelLayout(Context context) {
        super(context);
    }
    public MNWeatherPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        initUI();
        initLocationRequest();
        initWeatherDataCache();
    }

    private void initLocationRequest() {
        // Create a new global location parameters object
        mLocationRequest = LocationRequest.create();

        // Set the update interval
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
    }

    private void initWeatherDataCache() {
        searchCityWeatherDataCache = new MNWeatherDataSearchCityCache(getContext());
        currentLocationWeatherDataCache = new MNWeatherDataCurrentLocationCache(getContext());
    }

    private void initUI() {
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
        weatherConditionImageView = new ImageView(getContext());
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
        currentTempTextView.setGravity(Gravity.BOTTOM);
        currentTempTextView.setSingleLine();
        currentTempTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_weather_current_temp_text_size));
        currentTempTextView.setTypeface(currentTempTextView.getTypeface(), Typeface.BOLD);
        RelativeLayout.LayoutParams currentTempParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        currentTempParams.addRule(CENTER_HORIZONTAL);
        currentTempTextView.setLayoutParams(currentTempParams);
        upperTempInnerContentLayout.addView(currentTempTextView);

        // lowHigh temp
        lowHighTempTextView = new TextView(getContext());
        lowHighTempTextView.setId(38417324);
        lowHighTempTextView.setGravity(Gravity.TOP);
        lowHighTempTextView.setSingleLine();
        lowHighTempTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_weather_low_high_temp_text_size));
        lowHighTempTextView.setTypeface(lowHighTempTextView.getTypeface(), Typeface.BOLD);
        RelativeLayout.LayoutParams lowHighParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lowHighParams.addRule(BELOW, currentTempTextView.getId());
        lowHighParams.addRule(CENTER_HORIZONTAL);
        lowHighTempTextView.setLayoutParams(lowHighParams);
        upperTempInnerContentLayout.addView(lowHighTempTextView);

        // city name
        cityNameTextView = new TextView(getContext());
        cityNameTextView.setId(4311474);
        cityNameTextView.setGravity(Gravity.CENTER);
        cityNameTextView.setSingleLine();
        cityNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_weather_city_name_local_time_text_size));
        cityNameTextView.setTypeface(cityNameTextView.getTypeface(), Typeface.BOLD);
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
        localTimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_weather_city_name_local_time_text_size));
        localTimeTextView.setTypeface(localTimeTextView.getTypeface(), Typeface.BOLD);
        RelativeLayout.LayoutParams localTimeParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        localTimeParams.addRule(CENTER_HORIZONTAL);
        localTimeParams.addRule(BELOW, cityNameTextView.getId());
        localTimeParams.leftMargin = marginOuter;
        localTimeParams.rightMargin = marginOuter;
        localTimeTextView.setLayoutParams(localTimeParams);
        innerContentLayout.addView(localTimeTextView);

        // test
//        weatherConditionImageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(),
//                BitmapFactory.decodeResource(getResources(), R.drawable.m_icon_weather_07_tornado)));
//        currentTempTextView.setText("14°T");
//        lowHighTempTextView.setText("7°/19°T");
//        cityNameTextView.setText("Daegu Test");
//        localTimeTextView.setText("14:11:56 Test");

        if (DEBUG_UI) {
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
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        // recycle imageview
        if (MNBitmapUtils.recycleImageView(weatherConditionImageView)) {
            MNLog.i(TAG, "weather condition imageview recycled");
        }

        // get data from panelDataObject
        loadPanelDataObject();

        // cancel the previous task first
        if (weatherWWOAsyncTask != null) {
            weatherWWOAsyncTask.cancel(true);
        }

        if (locationClient == null) {
            locationClient = new LocationClient(getContext(), this, this);
        } else {
            locationClient.disconnect();
        }

        // get weather data from server
        if (isUsingCurrentLocation) {
            // 현재 위치는 locationClient에서 위치를 받아와 콜백 메서드에서 로직을 진행
            locationClient.connect();
        } else {
            // find previous data from cache
            MNWeatherData cachedWeatherData = searchCityWeatherDataCache.findWeatherCache(
                    selectedLocationInfo.getLatitude(), selectedLocationInfo.getLongitude());

            if (cachedWeatherData != null) {
                // use cache if exist
                weatherData = cachedWeatherData;
                updateUI();
            } else {
                // get weather data from server if cache doesn't exist
                weatherWWOAsyncTask = new MNWeatherWWOAsyncTask(selectedLocationInfo, getContext(), true, this);
                weatherWWOAsyncTask.execute();
            }
        }

        // 플러리
        Map<String, String> params = new HashMap<String, String>();
        params.put(MNFlurry.WEATHER, isUsingCurrentLocation ? "Using current location" : "Not using current location");
        FlurryAgent.logEvent(MNFlurry.PANEL, params);
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
            // set image resource
            Bitmap weatherConditionBitmap = BitmapFactory.decodeResource(
                    getContext().getApplicationContext().getResources(),
                    weatherData.weatherCondition.getConditionResourceId(), MNBitmapUtils.getDefaultOptions());

            weatherConditionImageView.setImageDrawable(
                    new BitmapDrawable(getContext().getApplicationContext().getResources(),
                            weatherConditionBitmap));
//            weatherConditionImageView.setImageResource(weatherData.weatherCondition.getConditionResourceId());

            // convert to bitmap with color
            applyTheme();
        }

        // temperature
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

        // 로컬 시계 사용 여부에 따라 UI 변경
        if (isDisplayingLocaltime) {
            localTimeTextView.setVisibility(View.VISIBLE);
            startClock();

            // 도시 텍스트뷰 마진 설정
            RelativeLayout.LayoutParams cityNameParams =
                    (RelativeLayout.LayoutParams) cityNameTextView.getLayoutParams();
            cityNameParams.topMargin = 0;
        } else {
            localTimeTextView.setVisibility(View.GONE);
            stopClock();

            // 도시 텍스트뷰 마진 설정
            RelativeLayout.LayoutParams cityNameParams =
                    (RelativeLayout.LayoutParams) cityNameTextView.getLayoutParams();
            cityNameParams.topMargin = getResources().getDimensionPixelSize(R.dimen.panel_detail_padding_inner);
        }
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

        // add weather data to cache
        if (isUsingCurrentLocation) {
            currentLocationWeatherDataCache.addWeatherDataToCache(weatherData, getContext());
        } else {
            searchCityWeatherDataCache.addWeatherDataToCache(weatherData, getContext());
        }
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
                    LocalDateTime localDateTime;
                    try {
                        localDateTime = LocalDateTime.now(DateTimeZone.forOffsetMillis((int) weatherData.timeOffsetInMillis));
                    } catch (IllegalArgumentException e) {
                        // Millis out of range 에러 방지: 허니콤에서 발생. 최소한 죽지는 않게 조치
                        localDateTime = LocalDateTime.now();
                    }
                    timeString = localDateTime.toString("HH:mm:ss");
                }

                // UI갱신
                // 튜토리얼때는 updateUI가 메인 쓰레드가 끊기지 않게 하기 위해 사용하지 않음
                if (MNTutorialManager.isTutorialShown(getContext().getApplicationContext())
                        || localTimeTextView.length() == 0) {
                    localTimeTextView.setText(timeString);
                }

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
        clockHandler.sendEmptyMessageDelayed(0, 0); // 첫 시작은 딜레이 없이 호출
    }

    private void stopClock() {
        if (!isClockRunning) {
            return;
        }
        isClockRunning = false;
        clockHandler.removeMessages(0); // 기존 핸들러 메시지 삭제(1개만 유지하기 위함)
    }

    /**
     * Location Client handler
     */

    /**
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        // lastLocation can't have a recent location, so must call
        // requestLocationUpdates()
        if (servicesConnected()) {
            locationClient.requestLocationUpdates(mLocationRequest, this);
        }
    }

    /**
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Location Fail 메시지 보여주기
        showCoverLayout(getResources().getString(R.string.weather_choose_your_city));
    }

    /**
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
    }

    @Override
    public void onLocationChanged(Location location) {
        locationClient.disconnect();

        // 현재위치를 사용할 때만 진행
        if (location != null && isUsingCurrentLocation) {
            // find previous data from cache
            MNWeatherData cachedWeatherData = currentLocationWeatherDataCache.findWeatherCache(
                    location.getLatitude(), location.getLongitude());

            if (cachedWeatherData != null) {
                // update UI using cache weather data
                weatherData = cachedWeatherData;
                updateUI();
            } else {
                // WWO using current location
                MNWeatherLocationInfo currentLocationInfo = new MNWeatherLocationInfo();

                currentLocationInfo.setLatitude(location.getLatitude());
                currentLocationInfo.setLongitude(location.getLongitude());
                weatherWWOAsyncTask = new MNWeatherWWOAsyncTask(currentLocationInfo, getContext(), false, this);
                weatherWWOAsyncTask.execute();
            }
        } else {
            // Location Fail 메시지 보여주기
            MNLog.now("weatherPanel/onConnected: no last location");
        }
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
//            MNLog.i(TAG, "play services available");

            // Continue
            return true;

        // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)getContext(), 0);
            if (dialog != null) {
                Toast.makeText(getContext(), dialog.toString(), Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getContext());

        // weather condition image view
        int highlightColor = MNMainColors.getWeatherConditionColor(currentThemeType, getContext());
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor,
                PorterDuff.Mode.SRC_ATOP);
        weatherConditionImageView.setColorFilter(colorFilter);

        // text views
        currentTempTextView.setTextColor(MNMainColors.getMainFontColor(currentThemeType, getContext()));
        lowHighTempTextView.setTextColor(MNMainColors.getWeatherLowHighTextColor(currentThemeType, getContext()));
        cityNameTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, getContext()));
        localTimeTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, getContext()));
    }

    // 뷰가 붙을 때 아날로그 시계뷰 재가동
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startClock();
    }

    // 뷰가 사라질 때 아날로그 시계뷰 핸들러 중지
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopClock();
    }
}
