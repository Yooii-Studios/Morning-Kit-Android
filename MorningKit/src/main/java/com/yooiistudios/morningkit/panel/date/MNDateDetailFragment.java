package com.yooiistudios.morningkit.panel.date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.date.MNDatePanelLayout.DATE_DATA_DATE_IS_LUNAR_ON;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 12.
 *
 * MNDateDetailFragment
 */
public class MNDateDetailFragment extends MNPanelDetailFragment {
    private static final String TAG = "MNDateCountdownDetailFragment";

    @InjectView(R.id.date_detail_lunar_textview) TextView lunarTextView;
    @InjectView(R.id.date_detail_lunar_checkbox) CheckBox lunarCheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_date_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            if (getPanelDataObject().has(DATE_DATA_DATE_IS_LUNAR_ON)) {
                try {
                    // 기존 정보가 있다면 가져와서 표시
                    lunarCheckBox.setChecked(getPanelDataObject().getBoolean(DATE_DATA_DATE_IS_LUNAR_ON));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                lunarCheckBox.setChecked(false);
            }
        }
        return rootView;
    }

    @Override
    protected void archivePanelData() throws JSONException {
        getPanelDataObject().put(DATE_DATA_DATE_IS_LUNAR_ON, lunarCheckBox.isChecked());
    }
}
