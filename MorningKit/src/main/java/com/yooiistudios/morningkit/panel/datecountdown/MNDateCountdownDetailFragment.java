package com.yooiistudios.morningkit.panel.datecountdown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.datecountdown.MNDateCountdownDatePicker;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailFragment;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 28.
 *
 * MNDateCountdownDetailFragment
 */
public class MNDateCountdownDetailFragment extends MNPanelDetailFragment {

    private static final String TAG = "MNDateCountdownDetailFragment";

    @InjectView(R.id.date_countdown_detail_edittext) EditText titleEditText;
    @InjectView(R.id.date_countdown_detail_date_picker) MNDateCountdownDatePicker datePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.panel_date_countdown_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            /*
            // 메모 가져오기: 메모가 없다면 힌트를 보여주기
            if (getPanelDataObject().has(MEMO_DATA_CONTENT)) {
                // 기존 메모가 있다면 그것으로 설정
                try {
                    memoEditText.setText(getPanelDataObject().getString(MEMO_DATA_CONTENT));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // 기존 메모가 없다면 아카이빙된 메모가 있는지 확인하고 가져오기
                SharedPreferences prefs = getActivity().getSharedPreferences(MEMO_PREFS, Context.MODE_PRIVATE);
                String archivedString = prefs.getString(MEMO_PREFS_CONTENT, null);
                if (archivedString != null) {
                    memoEditText.setText(archivedString);
                }
            }
            */
        }
        return rootView;
    }

    @Override
    protected void archivePanelData() throws JSONException {

    }
}
