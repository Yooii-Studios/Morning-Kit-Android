package com.yooiistudios.morningkit.panel.newsfeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;

import butterknife.ButterKnife;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 2.
 */
public class MNNewsFeedDetailFragment extends MNPanelDetailFragment {

    private static final String TAG = "MNNewsFeedDetailFragment";

//    @InjectView(R.id.refreshTime) TextView refreshTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.panel_news_feed_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            try {
                initPanelDataObject();
            } catch (JSONException e) {
                e.printStackTrace();

                //TODO init with default setting
            }

            // UI
            initUI();
        }

        return rootView;
    }

    private void initPanelDataObject() throws JSONException {
//        SharedPreferences prefs = getActivity().getSharedPreferences(
//                PREF_PHOTO_ALBUM, Context.MODE_PRIVATE);
//        if (getPanelDataObject().has(KEY_DATA_INTERVAL_MINUTE)) {
//            intervalMinute = getPanelDataObject()
//                    .getInt(KEY_DATA_INTERVAL_MINUTE);
//            recentIntervalMinute = intervalMinute;
//        }
//        else {
//            intervalMinute = prefs.getInt(KEY_DATA_INTERVAL_MINUTE,
//                    DEFAULT_INTERVAL_MIN);
//            recentIntervalMinute = intervalMinute;
//        }
    }

    private void initUI() {
        // init preview wrapper

        // theme
        initTheme();
    }

    private void initTheme() {
        MNThemeType currentThemeType =
                MNTheme.getCurrentThemeType(getActivity());
    }

    @Override
    protected void archivePanelData() throws JSONException {
//        getPanelDataObject().put(KEY_DATA_INTERVAL_MINUTE, intervalMinute);
//
//        SharedPreferences prefs = getActivity().getSharedPreferences(
//                PREF_PHOTO_ALBUM, Context.MODE_PRIVATE);
//        prefs.edit().putInt(KEY_DATA_INTERVAL_MINUTE, intervalMinute)
//                .putInt(KEY_DATA_INTERVAL_SECOND, intervalSecond)
//                .putBoolean(KEY_DATA_USE_REFRESH, useRefresh)
//                .putString(KEY_DATA_TRANS_TYPE, transitionType.getKey())
//                .putString(KEY_DATA_FILE_SELECTED, selectedFileName)
//                .putString(KEY_DATA_FILE_ROOT, rootDirForFiles)
//                .putBoolean(KEY_DATA_USE_GRAYSCALE, useGrayscale)
//                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        //TODO start handler
    }

    @Override
    public void onPause() {
        super.onPause();

        //TODO stop handler
    }

}
