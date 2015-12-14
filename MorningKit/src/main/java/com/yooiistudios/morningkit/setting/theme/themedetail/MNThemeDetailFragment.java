package com.yooiistudios.morningkit.setting.theme.themedetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.MNSettingDetailActivity;

public class MNThemeDetailFragment extends Fragment {
    public static final int REQ_THEME_DETAIL_PHOTO = 9385;
    ListView listView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MNSoundEffectFragment.
     */
    public static MNThemeDetailFragment newInstance() {
        return new MNThemeDetailFragment();
    }
    public MNThemeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.setting_theme_detail_fragment, container, false);
        if (rootView != null) {
            listView = (ListView) rootView.findViewById(R.id.setting_theme_detail_listview);
            listView.setAdapter(new MNThemeDetailListAdapter((MNThemeDetailActivity) getActivity(), this));
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_THEME_DETAIL_PHOTO && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }
}
