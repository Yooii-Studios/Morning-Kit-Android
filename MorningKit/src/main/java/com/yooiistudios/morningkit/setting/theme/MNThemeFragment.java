package com.yooiistudios.morningkit.setting.theme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.memory.ViewUnbindHelper;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 6.
 *
 * MNThemeFragment
 *  세팅 - 테마 프래그먼트
 */
public class MNThemeFragment extends Fragment {
    private static final String TAG = "MNThemeFragment";

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
    public void onDestroy() {
        super.onDestroy();
        MNLog.i(TAG, "onDestroy");
        ViewUnbindHelper.unbindReferences(listView);
    }
}
