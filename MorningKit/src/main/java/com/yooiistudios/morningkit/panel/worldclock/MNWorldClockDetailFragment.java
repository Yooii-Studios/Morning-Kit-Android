package com.yooiistudios.morningkit.panel.worldclock;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.worldclock.model.MNTimeZone;
import com.yooiistudios.morningkit.panel.worldclock.model.MNTimeZoneLoader;
import com.yooiistudios.morningkit.panel.worldclock.model.MNTimeZoneSearchAsyncTask;
import com.yooiistudios.morningkit.panel.worldclock.model.MNWorldClockTimeZoneAdapter;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.worldclock.MNWorldClockPanelLayout.WORLD_CLOCK_DATA_IS_24_HOUR;
import static com.yooiistudios.morningkit.panel.worldclock.MNWorldClockPanelLayout.WORLD_CLOCK_DATA_IS_ALALOG;
import static com.yooiistudios.morningkit.panel.worldclock.MNWorldClockPanelLayout.WORLD_CLOCK_DATA_TIME_ZONE;
import static com.yooiistudios.morningkit.panel.worldclock.MNWorldClockPanelLayout.WORLD_CLOCK_PREFS;
import static com.yooiistudios.morningkit.panel.worldclock.MNWorldClockPanelLayout.WORLD_CLOCK_PREFS_LATEST_TIME_ZONE;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 12.
 *
 * MNWorldClockDetailFragment
 */
public class MNWorldClockDetailFragment extends MNPanelDetailFragment implements TextWatcher, MNTimeZoneSearchAsyncTask.OnTimeZoneSearchAsyncTaskListener, AdapterView.OnItemClickListener {
    private static final String TAG = "MNWorldClockDetailFragment";

//    @InjectView(R.id.panel_detail_world_clock_linear_layout) LinearLayout containerLayout;

//    @InjectView(R.id.panel_detail_world_clock_clockType_textview) TextView clockTypeTextView;
//    @InjectView(R.id.panel_detail_world_clock_clockType_checkbox_analog) CheckBox analogCheckBox;
//    @InjectView(R.id.panel_detail_world_clock_clockType_checkbox_digital) CheckBox digitalCheckBox;

    @InjectView(R.id.panel_detail_world_clock_use_24_hour_format_layout) RelativeLayout isUsing24HoursLayout;
    @InjectView(R.id.panel_detail_world_clock_use_24_hour_format_check_image_button) ImageButton isUsing24HoursCheckImageButton;
//    @InjectView(R.id.panel_detail_world_clock_use_24_hour_format_textview) TextView isUsing24HoursTextView;

    @InjectView(R.id.panel_detail_world_clock_search_edit_text) EditText searchEditText;
    @InjectView(R.id.panel_detail_world_clock_search_listview) ListView searchListView;
    @InjectView(R.id.panel_detail_world_clock_no_search_result_textview) TextView noSearchResultsTextView;

    @InjectView(R.id.panel_detail_world_clock_analog_layout) RelativeLayout analogCheckLayout;
    @InjectView(R.id.panel_detail_world_clock_analog_check_imagebutton) ImageButton analogCheckImageButton;
    @InjectView(R.id.panel_detail_world_clock_digital_layout) RelativeLayout digitalCheckLayout;
    @InjectView(R.id.panel_detail_world_clock_digital_check_imagebutton) ImageButton digitalCheckImageButton;

    private boolean isClockAnalog = true;
    private boolean isUsing24Hours = false;
    private ArrayList<MNTimeZone> allTimeZones;
    private MNTimeZoneSearchAsyncTask timeZoneSearchAsyncTask;
    private MNWorldClockTimeZoneAdapter timeZoneListAdapter;
    private MNTimeZone selectedTimeZone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_world_clock_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            if (getPanelDataObject().has(WORLD_CLOCK_DATA_IS_ALALOG)) {
                try {
                    // 기본은 아날로그
                    isClockAnalog = getPanelDataObject().getBoolean(WORLD_CLOCK_DATA_IS_ALALOG);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (getPanelDataObject().has(WORLD_CLOCK_DATA_IS_24_HOUR)) {
                try {
                    isUsing24Hours = getPanelDataObject().getBoolean(WORLD_CLOCK_DATA_IS_24_HOUR);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                isUsing24Hours = DateFormat.is24HourFormat(getActivity());
            }

            if (getPanelDataObject().has(WORLD_CLOCK_DATA_TIME_ZONE)) {
                Type type = new TypeToken<MNTimeZone>() {}.getType();
                try {
                    String timeZoneJsonString = getPanelDataObject().getString(WORLD_CLOCK_DATA_TIME_ZONE);
                    selectedTimeZone = new Gson().fromJson(timeZoneJsonString, type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                selectedTimeZone = MNTimeZoneLoader.getDefaultZone(getActivity());
            }

            // 도시 리스트 가져오기
            allTimeZones = MNTimeZoneLoader.loadTimeZone(getActivity());

            // UI
            initUI();
        }
        return rootView;
    }

    private void initUI() {
        updateClockTypeUI();

        // CheckBox
//        analogCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                if (checked) {
//                    isClockAnalog = checked;
//                } else {
//                    analogCheckBox.setChecked(true);
//                }
//                updateClockTypeUI();
//            }
//        });
//
//        digitalCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                if (checked) {
//                    isClockAnalog = !checked;
//                } else {
//                    digitalCheckBox.setChecked(true);
//                }
//                updateClockTypeUI();
//            }
//        });

        // Analog/Digital Check ImageButton
        analogCheckLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isClockAnalog) {
                    isClockAnalog = true;
                    updateClockTypeUI();
                }
            }
        });

        digitalCheckLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClockAnalog) {
                    isClockAnalog = false;
                    updateClockTypeUI();
                }
            }
        });

        // isUsing24Hours Check ImageButton
        isUsing24HoursCheckImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUsing24Hours = !isUsing24Hours;
                updateUsing24HoursUI();
            }
        });

        // EditText
        searchEditText.addTextChangedListener(this);

        // list adapter
        timeZoneListAdapter = new MNWorldClockTimeZoneAdapter(getActivity());
        searchListView.setAdapter(timeZoneListAdapter);
        if (selectedTimeZone != null) {
            searchEditText.setText(selectedTimeZone.getName());
            searchEditText.setSelection(selectedTimeZone.getName().length());
            searchTimeZone(selectedTimeZone.getName());
        }

        // list view
        searchListView.setOnItemClickListener(this);
    }

    private void updateClockTypeUI() {
        if (isClockAnalog) {
            isUsing24HoursLayout.setVisibility(View.GONE);
//            analogCheckBox.setChecked(true);
//            digitalCheckBox.setChecked(false);
            analogCheckImageButton.setBackgroundResource(R.drawable.icon_panel_detail_checkbox_on);
            digitalCheckImageButton.setBackgroundResource(R.drawable.icon_panel_detail_checkbox);
        } else {
            isUsing24HoursLayout.setVisibility(View.VISIBLE);
            updateUsing24HoursUI();
//            analogCheckBox.setChecked(false);
//            digitalCheckBox.setChecked(true);
            analogCheckImageButton.setBackgroundResource(R.drawable.icon_panel_detail_checkbox);
            digitalCheckImageButton.setBackgroundResource(R.drawable.icon_panel_detail_checkbox_on);
        }
    }

    private void updateUsing24HoursUI() {
        if (isUsing24Hours) {
            isUsing24HoursCheckImageButton.setBackgroundResource(R.drawable.icon_panel_detail_checkbox_on);
        } else {
            isUsing24HoursCheckImageButton.setBackgroundResource(R.drawable.icon_panel_detail_checkbox);
        }
    }

    @Override
    protected void archivePanelData() throws JSONException {
        getPanelDataObject().put(WORLD_CLOCK_DATA_IS_ALALOG, isClockAnalog);
        getPanelDataObject().put(WORLD_CLOCK_DATA_IS_24_HOUR, isUsing24Hours);
        String selectedTimeZoneJsonString = new Gson().toJson(selectedTimeZone);
        getPanelDataObject().put(WORLD_CLOCK_DATA_TIME_ZONE, selectedTimeZoneJsonString);

        // shared prefreences에도 저장
        getActivity().getSharedPreferences(WORLD_CLOCK_PREFS, Context.MODE_PRIVATE)
                .edit().putString(WORLD_CLOCK_PREFS_LATEST_TIME_ZONE, selectedTimeZoneJsonString).commit();
    }

    // EditText
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        // 기존엔 검색 결과 clear 후 새로 갱신했지만 iOS는 그대로 보여주기에 해당 코드 삭제함
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        searchTimeZone(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void searchTimeZone(CharSequence searchString) {
        if (timeZoneSearchAsyncTask != null) {
            timeZoneSearchAsyncTask.cancel(true);
        }

        if (searchString.length() > 0) {
            timeZoneSearchAsyncTask = new MNTimeZoneSearchAsyncTask(allTimeZones, this, getActivity());
            timeZoneSearchAsyncTask.execute(searchString.toString());
        } else {
            onTimeZoneSearchFinished(null);
        }
    }

    // AsyncTask Callback Method
    @Override
    public void onTimeZoneSearchFinished(ArrayList<MNTimeZone> filteredTimeZones) {
        if (filteredTimeZones != null && filteredTimeZones.size() > 0) {
            noSearchResultsTextView.setVisibility(View.GONE);
            timeZoneListAdapter.setTimeZones(filteredTimeZones);
            timeZoneListAdapter.notifyDataSetChanged();
        } else {
            timeZoneListAdapter.clear();
            timeZoneListAdapter.notifyDataSetChanged();
            noSearchResultsTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long longPosition) {
        MNTimeZone selectedTimeZone = timeZoneListAdapter.getItem(position);
        if (selectedTimeZone != null) {
            this.selectedTimeZone = selectedTimeZone;
            onActionBarDoneClicked();
        }
    }
}
