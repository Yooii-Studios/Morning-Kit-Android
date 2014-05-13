package com.yooiistudios.morningkit.panel.photoalbum;

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
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 5. 13.
 *
 * MNPhotoAlbumDetailFragment
 *  포토 앨범 패널 디테일 프래그먼트 by 동현
 */
public class MNPhotoAlbumDetailFragment extends MNPanelDetailFragment {

//    @InjectView(R.id.panel_detail_weather_linear_layout)                    LinearLayout containerLayout;
//    @InjectView(R.id.panel_detail_weather_use_current_location_textview)    TextView useCurrentLocationTextView;
//    @InjectView(R.id.panel_detail_weather_use_current_location_checkbox)    CheckBox useCurrentLocationCheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_photo_album_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            try {
                initPanelDataObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // UI
            initUI();
        }
        return rootView;
    }

    private void initPanelDataObject() throws JSONException {
        // 메인에서 panelDataObject를 얻어서 여기서 처리
        // 참고용 코드
//        if (getPanelDataObject().has(WEATHER_DATA_IS_USING_CURRENT_LOCATION)) {
//            // 기본은 현재위치 사용
//            isUsingCurrentLocation = getPanelDataObject().getBoolean(WEATHER_DATA_IS_USING_CURRENT_LOCATION);
//        }
    }

    private void initUI() {
        // theme
        initTheme();
    }

    private void initTheme() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
    }

    @Override
    protected void archivePanelData() throws JSONException {
        // 참고용 코드
//        getPanelDataObject().put(WEATHER_DATA_IS_USING_CURRENT_LOCATION, isUsingCurrentLocation);
//        getPanelDataObject().put(WEATHER_DATA_IS_DISPLAYING_LOCAL_TIME, isDisplayingLocaltime);
//        getPanelDataObject().put(WEATHER_DATA_TEMP_CELSIUS, isUsingCelsius);
//        // 선택 위치 정보가 없다면 명시적으로 삭제
//        if (selectedLocationInfo != null) {
//            getPanelDataObject().put(WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO,
//                    new Gson().toJson(selectedLocationInfo));
//        } else {
//            getPanelDataObject().put(WEATHER_DATA_IS_USING_CURRENT_LOCATION, true);
//            getPanelDataObject().remove(WEATHER_DATA_SELECTED_WEATHER_LOCATION_INFO);
//        }
    }
}
