package com.yooiistudios.morningkit.panel.exchangerates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailFragment;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 3.
 *
 * MNExchangeRatesDetailFragment
 */
public class MNExchangeRatesDetailFragment extends MNPanelDetailFragment {

    private static final String TAG = "MNDateCountdownDetailFragment";

    @InjectView(R.id.panel_exchange_rates_info_button_view_base)
    MNExchangeInfoButtonView baseExchangeInfoButtonView;

    @InjectView(R.id.panel_exchange_rates_info_button_view_target)
    MNExchangeInfoButtonView targetExchangeInfoButtonView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_exchange_rates_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 국가 코드 가져오기
            String baseCountryCode = "USD";
            String targetCountryCode = "KRW";

            // 환율 정보 가져오기
            double baseCurrenyMoney = 1.0f;
            double exchangeRate = 1;

            baseExchangeInfoButtonView.loadExchangeCountry(baseCountryCode);
            targetExchangeInfoButtonView.loadExchangeCountry(targetCountryCode);
        }
        return rootView;
    }

    @Override
    protected void archivePanelData() throws JSONException {

    }

    public void onExchangeInfoButtonClicked(View v) {

    }

    public void onSwapButtonClicked(View v) {

    }
}
