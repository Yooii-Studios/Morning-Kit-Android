package com.yooiistudios.morningkit.panel.weather;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherLocationInfo;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherLocationInfoAdapter;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherLocationInfoLoader;
import com.yooiistudios.morningkit.panel.weather.model.locationinfo.MNWeatherLocationInfoSearchAsyncTask;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout.WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME;
import static com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout.WEATHER_DATA_IS_USING_CURRENT_LOCATION;
import static com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout.WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO;
import static com.yooiistudios.morningkit.panel.weather.MNWeatherPanelLayout.WEATHER_DATA_TEMP_CELSIUS;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 19.
 *
 * MNWeatherDetailFragment
 */
public class MNWeatherDetailFragment extends MNPanelDetailFragment implements AdapterView.OnItemClickListener, TextWatcher, MNWeatherLocationInfoLoader.OnWeatherLocatinInfoLoaderListener, MNWeatherLocationInfoSearchAsyncTask.MNWeatherLocationInfoSearchAsyncTaskListener {
    private static final String TAG = "MNWeatherDetailFragment";

    @InjectView(R.id.panel_detail_weather_use_current_location_check_image_button) ImageButton useCurrentLocationCheckImageButton;

    @InjectView(R.id.panel_detail_weather_display_local_time_checkbox) CheckBox displayLocalTimeCheckBox;

    @InjectView(R.id.panel_detail_weather_temperature_celsius_checkbox) CheckBox temperatureCelsiusCheckBox;
    @InjectView(R.id.panel_detail_weather_temperature_fahrenheit_checkbox) CheckBox temperatureFahrenheitCheckBox;

    @InjectView(R.id.panel_detail_weather_search_frame_layout) FrameLayout searchEditLayout;
    @InjectView(R.id.panel_detail_weather_search_edit_text) EditText searchEditText;

    @InjectView(R.id.panel_detail_weather_search_listview_frame_layout) FrameLayout searchListViewLayout;
    @InjectView(R.id.panel_detail_weather_search_listview) ListView searchListView;
    @InjectView(R.id.panel_detail_weather_no_search_result_textview) TextView noSearchResultsTextView;

    // basic settings
    boolean isUsingCurrentLocation = true;
    boolean isDisplayingLocaltime = true;
    boolean isUsingCelsius = true;

    // search
    MNWeatherLocationInfo selectedLocationInfo;
    MNWeatherLocationInfoLoader locationInfoLoader;
    List<MNWeatherLocationInfo> locationInfoList;
    MNWeatherLocationInfoSearchAsyncTask searchAsyncTask;
    MNWeatherLocationInfoAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_weather_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 모든 위치 정보 로딩
            locationInfoLoader = new MNWeatherLocationInfoLoader(getActivity(), this);
            locationInfoLoader.execute();

            // 패널 데이터 가져오기
            try {
                initPanelDataObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // UI
            initUI();
        }
        return rootView;
    }

    private void initPanelDataObject() throws JSONException {
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

    private void initUI() {
        // local time
        displayLocalTimeCheckBox.setChecked(isDisplayingLocaltime);

        // temperature
        if (isUsingCelsius) {
            temperatureCelsiusCheckBox.setChecked(true);
            temperatureFahrenheitCheckBox.setChecked(false);
        } else {
            temperatureCelsiusCheckBox.setChecked(false);
            temperatureFahrenheitCheckBox.setChecked(true);
        }

        setUseCurrentLocationState();
        initCheckedChangeListners();

        // edit text
        searchEditText.addTextChangedListener(this);

        // list adapter(if city exists)
        listAdapter = new MNWeatherLocationInfoAdapter(getActivity());
        searchListView.setAdapter(listAdapter);
        if (selectedLocationInfo != null) {
            searchEditText.setText(selectedLocationInfo.getName());
            searchEditText.setSelection(selectedLocationInfo.getName().length());
        }

        // list view
        searchListView.setOnItemClickListener(this);

        // theme
        initTheme();
    }

    private void initCheckedChangeListners() {
        useCurrentLocationCheckImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUsingCurrentLocation = !isUsingCurrentLocation;
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
            useCurrentLocationCheckImageButton.setImageResource(R.drawable.icon_panel_detail_checkbox_on);
            searchEditLayout.setVisibility(View.GONE);
            searchListViewLayout.setVisibility(View.GONE);
        } else {
            useCurrentLocationCheckImageButton.setImageResource(R.drawable.icon_panel_detail_checkbox);
            searchEditLayout.setVisibility(View.VISIBLE);
            searchListViewLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initTheme() {
    }

    @Override
    protected void archivePanelData() throws JSONException {
        getPanelDataObject().put(WEATHER_DATA_IS_USING_CURRENT_LOCATION, isUsingCurrentLocation);
        getPanelDataObject().put(WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME, isDisplayingLocaltime);
        getPanelDataObject().put(WEATHER_DATA_TEMP_CELSIUS, isUsingCelsius);
        // 선택 위치 정보가 없다면 명시적으로 삭제
        if (selectedLocationInfo != null) {
            getPanelDataObject().put(WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO,
                    new Gson().toJson(selectedLocationInfo));
        } else {
            getPanelDataObject().put(WEATHER_DATA_IS_USING_CURRENT_LOCATION, true);
            getPanelDataObject().remove(WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO);
        }
    }

    // ListView
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long longPosition) {
        MNWeatherLocationInfo selectedWeatherLocationInfo = listAdapter.getItem(position);
        if (selectedWeatherLocationInfo != null) {
            this.selectedLocationInfo = selectedWeatherLocationInfo;
            onActionBarDoneClicked();
        }
    }

    // Edit Text
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        // 기존엔 검색 결과 clear 후 새로 갱신했지만 iOS는 그대로 보여주기에 해당 코드 삭제함
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        searchCity(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void searchCity(CharSequence searchCharSequence) {
        if (searchAsyncTask != null) {
            searchAsyncTask.cancel(true);
        }
        // 최초 키워드 입력시만 "검색 중..." 표시
        if (listAdapter.getCount() == 0) {
            noSearchResultsTextView.setText(R.string.searching);
        }
        searchAsyncTask = new MNWeatherLocationInfoSearchAsyncTask(getActivity(),
                locationInfoList, this);
        searchAsyncTask.execute(searchCharSequence.toString());
    }

    // MNWeatherLocationInfoLoader listener
    @Override
    public void OnWeatherLocationInfoLoad(List<MNWeatherLocationInfo> weatherLocationInfoList) {
        locationInfoList = weatherLocationInfoList;
        if (selectedLocationInfo != null) {
            searchEditText.setText(selectedLocationInfo.getName());
            searchEditText.setSelection(selectedLocationInfo.getName().length());
            searchCity(selectedLocationInfo.getName());
        }
    }

    // MNWeatherLocationInfoSearchAsyncTask
    @Override
    public void OnSearchFinished(List<MNWeatherLocationInfo> filteredWeatherLocationInfoList) {
        if (filteredWeatherLocationInfoList != null && filteredWeatherLocationInfoList.size() > 0) {
            noSearchResultsTextView.setVisibility(View.GONE);
            listAdapter.setLocationInfoList(filteredWeatherLocationInfoList);
            listAdapter.notifyDataSetChanged();
        } else {
            listAdapter.clear();
            listAdapter.notifyDataSetChanged();
            noSearchResultsTextView.setText(R.string.no_search_result);
            noSearchResultsTextView.setVisibility(View.VISIBLE);
        }
    }
}
