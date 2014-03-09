package com.yooiistudios.morningkit.panel.quotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;

import butterknife.ButterKnife;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 9.
 *
 * MNQuotesDetailFragment
 */
public class MNQuotesDetailFragment extends MNPanelDetailFragment {
    private static final String TAG = "MNQuotesDetailFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_quotes_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            //// Logic part ////

            //// UI part ////
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        applyTheme();
    }

    private void applyTheme() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
        if (getView() != null) {
            getView().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
        } else {
            MNLog.e(TAG, "getView() is null!");
        }
    }
    @Override
    protected void archivePanelData() throws JSONException {

    }
}
