package com.yooiistudios.morningkit.setting.info.credit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.tutorial.MNTutorialManager;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 8.
 *
 * MNCreditFragment
 *
 */
public class MNCreditFragment extends Fragment implements AdapterView.OnItemClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_info_credit_fragment, container, false);
        if (rootView != null) {
            ListView listView = (ListView) rootView.findViewById(R.id.setting_info_credit_listview);
            listView.setOnItemClickListener(this);
            listView.setAdapter(new MNCreditListAdapter(getActivity()));
        }
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1) {
            MNTutorialManager.resetTutorial(getActivity().getApplicationContext());
            Toast.makeText(getActivity().getApplicationContext(), "Tutorial Rest", Toast.LENGTH_SHORT).show();
        }
    }
}
