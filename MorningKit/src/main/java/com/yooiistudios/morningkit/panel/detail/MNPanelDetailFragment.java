package com.yooiistudios.morningkit.panel.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.MNPanel;
import com.yooiistudios.morningkit.panel.MNPanelType;
import com.yooiistudios.morningkit.panel.flickr.detail.MNFlickrDetailFragment;
import com.yooiistudios.morningkit.panel.memo.detail.MNMemoDetailFragment;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 21.
 *
 * MNPanelDetailFragment
 *  패널 디테일 프래그먼트의 핵심을 담고 있는 부모 클래스
 */
public abstract class MNPanelDetailFragment extends Fragment {

    @Getter @Setter JSONObject panelDataObject;

    public static MNPanelDetailFragment newInstance(MNPanelType panelType, int panelIndex) {
        MNPanelDetailFragment newPanelDetailFragment;
        switch (panelType) {
            case FLICKR:
                newPanelDetailFragment = new MNFlickrDetailFragment();
                break;
            default:
                newPanelDetailFragment = new MNMemoDetailFragment();
                break;
        }

        // 기본적인 panelDataObject 세팅
        JSONObject newJSONObject = new JSONObject();
        try {
            newJSONObject.put(MNPanel.PANEL_UNIQUE_ID, panelType.getUniqueId());
            newJSONObject.put(MNPanel.PANEL_INDEX, panelIndex);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        newPanelDetailFragment.setPanelDataObject(newJSONObject);

        return newPanelDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_detail_fragment, container, false);
        return rootView;
    }

    protected abstract void archivePanelData() throws JSONException;
}
