package com.yooiistudios.morningkit.panel.exchangerates;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.stevenkim.waterlily.bitmapfun.util.RecyclingBitmapDrawable;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 4.
 *
 * MNExchangeInfoLayout
 *  환율 디테일 프래그먼트에 사용될 국가 표시
 */
public class MNExchangeInfoLayout extends LinearLayout{

    private static final int FLAG_IMAGE_VIEW_ID = 213551;

    private RecyclingImageView flagImageView;
    private TextView currencyCodeTextView;

    public MNExchangeInfoLayout(Context context) {
        super(context);
        if (!isInEditMode()) {
            init();
        }
    }

    public MNExchangeInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init();
        }
    }

    public MNExchangeInfoLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        setGravity(Gravity.CENTER);

        // relativeLayout
        RelativeLayout contentRelativeLayout = new RelativeLayout(getContext());
        LinearLayout.LayoutParams contentLayoutLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentLayoutLayoutParams.gravity = Gravity.CENTER;
        contentRelativeLayout.setLayoutParams(contentLayoutLayoutParams);
        addView(contentRelativeLayout);

        // imageView
        flagImageView = new RecyclingImageView(getContext());
        flagImageView.setId(FLAG_IMAGE_VIEW_ID);
        int flagWidth = getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_detail_flag_width);
        int flagHeight = getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_detail_flag_height);
        RelativeLayout.LayoutParams flagImageViewLayoutParams
                = new RelativeLayout.LayoutParams(flagWidth, flagHeight);
        flagImageViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        flagImageView.setLayoutParams(flagImageViewLayoutParams);
        contentRelativeLayout.addView(flagImageView);

        // textView
        currencyCodeTextView = new TextView(getContext());
        RelativeLayout.LayoutParams currencyCodeTextViewLayoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        currencyCodeTextViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, flagImageView.getId());
        currencyCodeTextViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        currencyCodeTextViewLayoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_outer);
        currencyCodeTextView.setLayoutParams(currencyCodeTextViewLayoutParams);
        float currencyCodeTextSize = getResources().getDimension(R.dimen.panel_exchange_rates_detail_currency_code_text_size);
        currencyCodeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currencyCodeTextSize);
        currencyCodeTextView.setGravity(Gravity.CENTER_VERTICAL);
        contentRelativeLayout.addView(currencyCodeTextView);

        applyTheme();
    }

    private void applyTheme() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getContext());
        setBackgroundColor(MNSettingColors.getExchangeRatesForwardColor(currentThemeType));
    }

    public void loadExchangeCountry(String currencyCode) {
        MNCurrencyInfo currency = MNCurrencyInfo.getCurrencyInfo(currencyCode);
        Bitmap countryFlagBitmap = FlagBitmapFactory.getGrayscaledFlagBitmap(getContext(),
                currency.usingCountryCode);
        int flagWidth = getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_detail_flag_width);
        int flagHeight = getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_detail_flag_height);
        countryFlagBitmap = Bitmap.createScaledBitmap(countryFlagBitmap, flagWidth, flagHeight, true);
        flagImageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(), countryFlagBitmap));

        currencyCodeTextView.setText(currencyCode);
    }
}
