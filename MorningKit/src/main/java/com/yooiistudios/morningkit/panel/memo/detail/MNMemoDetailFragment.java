package com.yooiistudios.morningkit.panel.memo.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailFragment;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.memo.MNMemoPanelLayout.MEMO_DATA_CONTENT;
import static com.yooiistudios.morningkit.panel.memo.MNMemoPanelLayout.MEMO_PREFS;
import static com.yooiistudios.morningkit.panel.memo.MNMemoPanelLayout.MEMO_PREFS_CONTENT;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 26.
 *
 * MNMemoDetailFragment
 */
public class MNMemoDetailFragment extends MNPanelDetailFragment {

    private static final String TAG = "MNMemoDetailFragment";

    @InjectView(R.id.memo_detail_edittext) EditText memoEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.panel_memo_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

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
        }
        return rootView;
    }

    @Override
    protected void archivePanelData() throws JSONException {
        String memoString = memoEditText.getText().toString();

        // SharedPreferences에 아카이빙
        SharedPreferences prefs = getActivity().getSharedPreferences(MEMO_PREFS, Context.MODE_PRIVATE);

        // length가 0이 아니어야 저장함
        if (memoString.length() != 0) {
            getPanelDataObject().put(MEMO_DATA_CONTENT, memoString);
            prefs.edit().putString(MEMO_PREFS_CONTENT, memoString).commit();
        } else {
            getPanelDataObject().remove(MEMO_DATA_CONTENT);
            prefs.edit().remove(MEMO_PREFS_CONTENT).commit();
        }
    }
}
