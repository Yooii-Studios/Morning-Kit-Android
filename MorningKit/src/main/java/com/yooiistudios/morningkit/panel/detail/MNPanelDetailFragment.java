package com.yooiistudios.morningkit.panel.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.R;

import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 21.
 *
 * MNPanelDetailFragment
 *  패널 디테일 프래그먼트의 핵심을 담고 있는 부모 클래스
 */
public class MNPanelDetailFragment extends Fragment {

    @Getter @Setter JSONObject panelDataObject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_detail_fragment, container, false);
        return rootView;
    }
}
