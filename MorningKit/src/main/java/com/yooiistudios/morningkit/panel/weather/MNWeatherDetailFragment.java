package com.yooiistudios.morningkit.panel.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout.WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME;
import static com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout.WEATHER_DATA_IS_USING_CURRENT_LOCATION;
import static com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout.WEATHER_DATA_TEMP_CELSIUS;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 19.
 *
 * MNWeatherDetailFragment
 */
public class MNWeatherDetailFragment extends MNPanelDetailFragment {
    private static final String TAG = "MNWeatherDetailFragment";

    @InjectView(R.id.panel_detail_weather_linear_layout) LinearLayout containerLayout;

    @InjectView(R.id.panel_detail_weather_use_current_location_textview) TextView useCurrentLocationTextView;
    @InjectView(R.id.panel_detail_weather_use_current_location_checkbox) CheckBox useCurrentLocationCheckBox;

    @InjectView(R.id.panel_detail_weather_display_local_time_textview) TextView displayLocalTimeTextView;
    @InjectView(R.id.panel_detail_weather_display_local_time_checkbox) CheckBox displayLocalTimeCheckBox;

    @InjectView(R.id.panel_detail_weather_temperature_unit_textView) TextView temperatureUnitTextView;
    @InjectView(R.id.panel_detail_weather_temperature_celsius_checkbox) CheckBox temperatureCelsiusCheckBox;
    @InjectView(R.id.panel_detail_weather_temperature_fahrenheit_checkbox) CheckBox temperatureFahrenheitCheckBox;

    @InjectView(R.id.panel_detail_weather_search_frame_layout) FrameLayout searchEditLayout;
    @InjectView(R.id.panel_detail_weather_search_edit_text) EditText searchEditText;

    @InjectView(R.id.panel_detail_weather_search_listview_frame_layout) FrameLayout searchListViewLayout;
    @InjectView(R.id.panel_detail_weather_search_listview) ListView searchListView;
    @InjectView(R.id.panel_detail_weather_no_search_result_textview) TextView noSearchResultsTextView;

    boolean isUsingCurrentLocation = true;
    boolean isDisplayingLocaltime = true;
    boolean isUsingCelsius = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_weather_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            initPanelDataObject();

            // UI
            initUI();
        }
        return rootView;
    }

    private void initPanelDataObject() {
        if (getPanelDataObject().has(WEATHER_DATA_IS_USING_CURRENT_LOCATION)) {
            try {
                // 기본은 현재위치 사용
                isUsingCurrentLocation = getPanelDataObject().getBoolean(WEATHER_DATA_IS_USING_CURRENT_LOCATION);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (getPanelDataObject().has(WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME)) {
            try {
                // 기본은 로컬 시간 사용
                isDisplayingLocaltime = getPanelDataObject().getBoolean(WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (getPanelDataObject().has(WEATHER_DATA_TEMP_CELSIUS)) {
            try {
                isUsingCelsius = getPanelDataObject().getBoolean(WEATHER_DATA_TEMP_CELSIUS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // 미국만 fahrenheit 사용
            isUsingCelsius = !getResources().getConfiguration().locale.getCountry().equals("US");
        }
    }

    private void initUI() {
        useCurrentLocationCheckBox.setChecked(isUsingCurrentLocation);
        displayLocalTimeCheckBox.setChecked(isDisplayingLocaltime);
        if (isUsingCelsius) {
            temperatureCelsiusCheckBox.setChecked(true);
            temperatureFahrenheitCheckBox.setChecked(false);
        } else {
            temperatureCelsiusCheckBox.setChecked(false);
            temperatureFahrenheitCheckBox.setChecked(true);
        }

        setUseCurrentLocationState();
        initCheckedChangeListners();
        initTheme();
    }

    private void initCheckedChangeListners() {
        useCurrentLocationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isUsingCurrentLocation = b;
                setUseCurrentLocationState();
            }
        });

        displayLocalTimeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isDisplayingLocaltime = b;
            }
        });

        temperatureCelsiusCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isUsingCelsius = b;
                } else {
                    temperatureCelsiusCheckBox.setChecked(true);
                }
                setTemperatureCheckBoxStates();
            }
        });
        temperatureFahrenheitCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isUsingCelsius = !b;
                } else {
                    temperatureFahrenheitCheckBox.setChecked(true);
                }
                setTemperatureCheckBoxStates();
            }
        });
    }

    private void setTemperatureCheckBoxStates() {
        if (isUsingCelsius) {
            temperatureCelsiusCheckBox.setChecked(true);
            temperatureFahrenheitCheckBox.setChecked(false);
        } else {
            temperatureCelsiusCheckBox.setChecked(false);
            temperatureFahrenheitCheckBox.setChecked(true);
        }
    }

    private void setUseCurrentLocationState() {
        if (isUsingCurrentLocation) {
            searchEditLayout.setVisibility(View.GONE);
            searchListViewLayout.setVisibility(View.GONE);
        } else {
            searchEditLayout.setVisibility(View.VISIBLE);
            searchListViewLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initTheme() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
        containerLayout.setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
        useCurrentLocationTextView.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        displayLocalTimeTextView.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        temperatureUnitTextView.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        noSearchResultsTextView.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        temperatureCelsiusCheckBox.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        temperatureFahrenheitCheckBox.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
    }

    @Override
    protected void archivePanelData() throws JSONException {
        getPanelDataObject().put(WEATHER_DATA_IS_USING_CURRENT_LOCATION, isUsingCurrentLocation);
        getPanelDataObject().put(WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME, isDisplayingLocaltime);
        getPanelDataObject().put(WEATHER_DATA_TEMP_CELSIUS, isUsingCelsius);
    }
}
