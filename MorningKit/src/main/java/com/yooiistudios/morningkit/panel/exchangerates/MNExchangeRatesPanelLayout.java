package com.yooiistudios.morningkit.panel.exchangerates;

import android.content.Context;
import android.util.AttributeSet;

import com.yooiistudios.morningkit.panel.MNPanelLayout;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 3.
 *
 * MNExchangeRatesPanelLayout
 */
public class MNExchangeRatesPanelLayout extends MNPanelLayout{

    public static final String EXCHANGE_RATES_PREFS = "EXCHANGE_RATES_PREFS";
    public static final String EXCHANGE_RATES_DATA_EXCHANGE_INFO = "EXCHANGE_RATES_DATA_EXCHANGE_INFO";

    public MNExchangeRatesPanelLayout(Context context) {
        super(context);
    }

    public MNExchangeRatesPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
