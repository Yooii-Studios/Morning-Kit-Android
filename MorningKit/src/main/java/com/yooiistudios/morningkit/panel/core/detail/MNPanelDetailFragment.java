package com.yooiistudios.morningkit.panel.core.detail;

import android.support.v4.app.Fragment;

import com.yooiistudios.morningkit.panel.calendar.MNCalendarDetailFragment;
import com.yooiistudios.morningkit.panel.core.MNPanel;
import com.yooiistudios.morningkit.panel.core.MNPanelType;
import com.yooiistudios.morningkit.panel.date.MNDateDetailFragment;
import com.yooiistudios.morningkit.panel.datecountdown.MNDateCountdownDetailFragment;
import com.yooiistudios.morningkit.panel.exchangerates.detail.MNExchangeRatesDetailFragment;
import com.yooiistudios.morningkit.panel.flickr.detail.MNFlickrDetailFragment;
import com.yooiistudios.morningkit.panel.memo.MNMemoDetailFragment;
import com.yooiistudios.morningkit.panel.quotes.detail.MNQuotesDetailFragment;
import com.yooiistudios.morningkit.panel.weather.MNWeatherDetailFragment;
import com.yooiistudios.morningkit.panel.worldclock.MNWorldClockDetailFragment;

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
    @Setter MNPanelDetailFragmentListener fragmentListener;

    interface MNPanelDetailFragmentListener {
        public void onActionBarDoneButtonClicked();
    }

    public static MNPanelDetailFragment newInstance(MNPanelType panelType, int panelIndex,
                                                    MNPanelDetailFragmentListener fragmentListener) {
        MNPanelDetailFragment newPanelDetailFragment;
        switch (panelType) {
            case WEATHER:
                newPanelDetailFragment = new MNWeatherDetailFragment();
                break;

            case DATE:
                newPanelDetailFragment = new MNDateDetailFragment();
                break;

            case CALENDAR:
                newPanelDetailFragment = new MNCalendarDetailFragment();
                break;

            case WORLD_CLOCK:
                newPanelDetailFragment = new MNWorldClockDetailFragment();
                break;

            case QUOTES:
                newPanelDetailFragment = new MNQuotesDetailFragment();
                break;

            case FLICKR:
                newPanelDetailFragment = new MNFlickrDetailFragment();
                break;

            case EXCHANGE_RATES:
                newPanelDetailFragment = new MNExchangeRatesDetailFragment();
                break;

            case MEMO:
                newPanelDetailFragment = new MNMemoDetailFragment();
                break;

            case DATE_COUNTDOWN:
                newPanelDetailFragment = new MNDateCountdownDetailFragment();
                break;

            default:
                newPanelDetailFragment = new MNMemoDetailFragment();
                break;
        }

        // 기본적인 panelDataObject 세팅
        JSONObject newJSONObject = new JSONObject();
        try {
            newJSONObject.put(MNPanel.PANEL_UNIQUE_ID, panelType.getUniqueId());
            newJSONObject.put(MNPanel.PANEL_WINDOW_INDEX, panelIndex);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        newPanelDetailFragment.setPanelDataObject(newJSONObject);

        // 리스너 설정
        newPanelDetailFragment.setFragmentListener(fragmentListener);
        return newPanelDetailFragment;
    }

    protected abstract void archivePanelData() throws JSONException;

    // 프래그먼트 단에서 액티비티에 처리 요청
    protected void onActionBarDoneClicked() {
        if (fragmentListener != null) {
            fragmentListener.onActionBarDoneButtonClicked();
        }
    }
}
