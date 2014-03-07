package com.yooiistudios.morningkit.panel.exchangerates;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.MNPanelLayout;

import org.json.JSONException;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.LayoutParams.MATCH_PARENT;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 3.
 *
 * MNExchangeRatesPanelLayout
 */
public class MNExchangeRatesPanelLayout extends MNPanelLayout{

    public static final String EXCHANGE_RATES_PREFS = "EXCHANGE_RATES_PREFS";
    public static final String EXCHANGE_RATES_DATA_EXCHANGE_INFO = "EXCHANGE_RATES_DATA_EXCHANGE_INFO";

    private RecyclingImageView baseCurrencyImageView;
    private RecyclingImageView targetCurrencyImageView;
    private TextView baseToTargetCurrecyTextView;
    private TextView targetToBaseCurrecyTextView;

    public MNExchangeRatesPanelLayout(Context context) {
        super(context);
    }

    public MNExchangeRatesPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        // content layout
        LayoutParams innerContentLayoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        getContentLayout().setLayoutParams(innerContentLayoutParams);

        // base image
        int imageWidth = getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_flag_width);
        int imageHeight = getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_flag_height);

        baseCurrencyImageView = new RecyclingImageView(getContext());
        baseCurrencyImageView.setId(12312515);
        LayoutParams baseImageViewLayoutParams = new LayoutParams(imageWidth, imageHeight);
        baseImageViewLayoutParams.addRule(CENTER_HORIZONTAL);
        baseCurrencyImageView.setLayoutParams(baseImageViewLayoutParams);
        getContentLayout().addView(baseCurrencyImageView);

        // target image
        int marginOuter = getResources().getDimensionPixelSize(R.dimen.margin_outer);
        targetCurrencyImageView = new RecyclingImageView(getContext());
        LayoutParams targetImageViewLayoutParams = new LayoutParams(imageWidth, imageHeight);
        targetImageViewLayoutParams.leftMargin = marginOuter;
        targetImageViewLayoutParams.addRule(RIGHT_OF, baseCurrencyImageView.getId());
        targetCurrencyImageView.setLayoutParams(baseImageViewLayoutParams);
        getContentLayout().addView(targetCurrencyImageView);

        // base-target curreny
        int padding = getResources().getDimensionPixelSize(R.dimen.panel_layout_padding);

        baseToTargetCurrecyTextView = new TextView(getContext());
        baseToTargetCurrecyTextView.setId(8123747);
        baseToTargetCurrecyTextView.setPadding(padding, 0, padding, 0);
        LayoutParams baseToTargetLayoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        baseToTargetLayoutParams.topMargin = marginOuter;
        baseToTargetLayoutParams.addRule(BELOW, baseCurrencyImageView.getId());
        baseToTargetCurrecyTextView.setLayoutParams(baseToTargetLayoutParams);
        getContentLayout().addView(baseToTargetCurrecyTextView);

        // target-base curreny
        targetToBaseCurrecyTextView = new TextView(getContext());
        targetToBaseCurrecyTextView.setPadding(padding, 0, padding, 0);
        LayoutParams targetToBaseLayoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        targetToBaseLayoutParams.topMargin = marginOuter;
        targetToBaseLayoutParams.addRule(BELOW, targetToBaseCurrecyTextView.getId());
        targetToBaseCurrecyTextView.setLayoutParams(targetToBaseLayoutParams);
        getContentLayout().addView(targetToBaseCurrecyTextView);

        // test
        getContentLayout().setBackgroundColor(Color.YELLOW);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

    }

    @Override
    protected void updateUI() {
        super.updateUI();

        // 읽어온 정보를 사용해 UI 갱신
    }

    /**
     * Rotate
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
