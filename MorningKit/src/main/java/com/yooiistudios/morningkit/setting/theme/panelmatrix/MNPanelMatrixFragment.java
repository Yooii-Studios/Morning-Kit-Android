package com.yooiistudios.morningkit.setting.theme.panelmatrix;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yooiistudios.morningkit.R;

public class MNPanelMatrixFragment extends Fragment {

    ListView listView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MNSoundEffectFragment.
     */
    public static MNPanelMatrixFragment newInstance() {
        return new MNPanelMatrixFragment();
    }
    public MNPanelMatrixFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.setting_theme_sound_effect_fragment, container, false);
        if (rootView != null) {
            listView = (ListView) rootView.findViewById(R.id.setting_theme_soundeffect_listview);
            listView.setAdapter(new MNPanelMatrixListAdapter(getActivity()));
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
