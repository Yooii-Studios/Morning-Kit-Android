package com.yooiistudios.morningkit.setting.theme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.memory.ViewUnbindHelper;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageActivity;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrixActivity;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeDetailActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 6.
 *
 * MNThemeFragment
 *  세팅 - 테마 프래그먼트
 */
public class MNThemeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "MNThemeFragment";
    public static final int REQ_THEME_DETAIL = 9385;

    @InjectView(R.id.setting_theme_listview) ListView listView;

    // 이전에 생성된 프래그먼트를 유지
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_theme_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);
            listView.setAdapter(new MNThemeListAdapter(getActivity()));
            listView.setOnItemClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
        listView.setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(MNTheme.getCurrentThemeType(getActivity())));
        getView().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(MNTheme.getCurrentThemeType(getActivity())));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MNThemeItemType themeItemType = MNThemeItemType.valueOf(i);
        switch (themeItemType) {
            case THEME:
                startActivityForResult(new Intent(getActivity(), MNThemeDetailActivity.class), REQ_THEME_DETAIL);
                break;

            case LANGUAGE:
                startActivity(new Intent(getActivity(), MNLanguageActivity.class));
                break;

            case PANEL_MATRIX:
                startActivity(new Intent(getActivity(), MNPanelMatrixActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_THEME_DETAIL && resultCode == Activity.RESULT_OK) {
            getActivity().finish();
        }
    }
}
