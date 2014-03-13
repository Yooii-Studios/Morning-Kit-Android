package com.yooiistudios.morningkit.panel.worldclock;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.worldclock.model.MNTimeZone;
import com.yooiistudios.morningkit.panel.worldclock.model.MNTimeZoneLoader;
import com.yooiistudios.morningkit.panel.worldclock.model.MNTimeZoneSearchAsyncTask;
import com.yooiistudios.morningkit.panel.worldclock.model.MNWorldClockTimeZoneAdapter;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.worldclock.MNWorldClockPanelLayout.WORLD_CLOCK_DATA_IS_24_HOUR;
import static com.yooiistudios.morningkit.panel.worldclock.MNWorldClockPanelLayout.WORLD_CLOCK_DATA_IS_ALALOG;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 12.
 *
 * MNWorldClockDetailFragment
 */
public class MNWorldClockDetailFragment extends MNPanelDetailFragment implements TextWatcher, MNTimeZoneSearchAsyncTask.OnTimeZoneSearchAsyncTaskListener {
    private static final String TAG = "MNWorldClockDetailFragment";

    @InjectView(R.id.panel_detail_world_clock_linear_layout) LinearLayout containerLayout;

    @InjectView(R.id.panel_detail_world_clock_clockType_textView) TextView clockTypeTextView;
    @InjectView(R.id.panel_detail_world_clock_clockType_checkbox_analog) CheckBox analogCheckBox;
    @InjectView(R.id.panel_detail_world_clock_clockType_checkbox_digital) CheckBox digitalCheckBox;

    @InjectView(R.id.panel_detail_world_clock_use_24_hour_format_layout) RelativeLayout isUsing24HoursLayout;
    @InjectView(R.id.panel_detail_world_clock_use_24_hour_format_checkbox) CheckBox isUsing24HoursCheckBox;
    @InjectView(R.id.panel_detail_world_clock_use_24_hour_format_textView) TextView isUsing24HoursTextView;

    @InjectView(R.id.panel_detail_world_clock_search_edit_text) EditText searchEditText;
    @InjectView(R.id.panel_detail_world_clock_search_listview) ListView searchListView;
    @InjectView(R.id.panel_detail_world_clock_no_search_result_textview) TextView noSearchResultsTextView;

    private boolean isClockAnalog = true;
    private boolean isUsing24Hours = false;
    private ArrayList<MNTimeZone> allTimeZones;
    private MNTimeZoneSearchAsyncTask timeZoneSearchAsyncTask;
    private MNWorldClockTimeZoneAdapter adapter;

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
        analogCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                isClockAnalog = checked;
                MNWorldClockDetailFragment.this.updateClockTypeUI();
            }
        });

        digitalCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                isClockAnalog = !checked;
                MNWorldClockDetailFragment.this.updateClockTypeUI();
            }
        });

        // EditText
        searchEditText.addTextChangedListener(this);

        // list adapter
        adapter = new MNWorldClockTimeZoneAdapter(getActivity());
        searchListView.setAdapter(adapter);

        // Theme
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
//        searchListView.setDivider(new ColorDrawable(MNSettingColors.getMainFontColor(currentThemeType)));
        containerLayout.setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
        clockTypeTextView.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        isUsing24HoursTextView.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        analogCheckBox.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        digitalCheckBox.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        noSearchResultsTextView.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
    }

    private void updateClockTypeUI() {
        if (isClockAnalog) {
            isUsing24HoursLayout.setVisibility(View.GONE);
            analogCheckBox.setChecked(true);
            digitalCheckBox.setChecked(false);
        } else {
            isUsing24HoursLayout.setVisibility(View.VISIBLE);
            updateUsing24HoursUI();
            analogCheckBox.setChecked(false);
            digitalCheckBox.setChecked(true);
        }
    }

    private void updateUsing24HoursUI() {
        if (isUsing24Hours) {
            isUsing24HoursCheckBox.setChecked(true);
        } else {
            isUsing24HoursCheckBox.setChecked(false);
        }
    }

    @Override
    protected void archivePanelData() throws JSONException {
        getPanelDataObject().put(WORLD_CLOCK_DATA_IS_ALALOG, isClockAnalog);
        getPanelDataObject().put(WORLD_CLOCK_DATA_IS_24_HOUR, isUsing24HoursCheckBox.isChecked());
    }

    // EditText
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        // 기존엔 검색 결과 clear 후 새로 갱신했지만 iOS는 그대로 보여주기에 해당 코드 삭제함
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (timeZoneSearchAsyncTask != null) {
            timeZoneSearchAsyncTask.cancel(true);
        }

        if (charSequence.length() > 0) {
            timeZoneSearchAsyncTask = new MNTimeZoneSearchAsyncTask(allTimeZones, this, getActivity());
            timeZoneSearchAsyncTask.execute(charSequence.toString());
        } else {
            onTimeZoneSearchFinished(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    // AsyncTask Callback Method
    @Override
    public void onTimeZoneSearchFinished(ArrayList<MNTimeZone> filteredTimeZones) {
        if (filteredTimeZones != null && filteredTimeZones.size() > 0) {
            noSearchResultsTextView.setVisibility(View.GONE);
            adapter.setTimeZones(filteredTimeZones);
            adapter.notifyDataSetChanged();
        } else {
            adapter.clear();
            adapter.notifyDataSetChanged();
            noSearchResultsTextView.setVisibility(View.VISIBLE);
        }
    }
}
