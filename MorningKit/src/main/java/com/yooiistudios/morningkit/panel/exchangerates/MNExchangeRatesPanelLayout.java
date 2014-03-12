package com.yooiistudios.morningkit.panel.exchangerates;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.stevenkim.waterlily.bitmapfun.util.RecyclingBitmapDrawable;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.textview.AutoResizeTextView;
import com.yooiistudios.morningkit.panel.MNPanelLayout;
import com.yooiistudios.morningkit.panel.exchangerates.model.FlagBitmapFactory;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNDefaultExchangeRatesInfo;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNExchangeRatesAsyncTask;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNExchangeRatesInfo;

import org.json.JSONException;

import java.lang.reflect.Type;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.LayoutParams.MATCH_PARENT;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 3.
 *
 * MNExchangeRatesPanelLayout
 */
public class MNExchangeRatesPanelLayout extends MNPanelLayout implements MNExchangeRatesAsyncTask.OnExchangeRatesAsyncTaskListener {

    private static final String TAG = "MNExchangeRatesPanelLayout";

    public static final String EXCHANGE_RATES_PREFS = "EXCHANGE_RATES_PREFS";
    public static final String EXCHANGE_RATES_DATA_EXCHANGE_INFO = "EXCHANGE_RATES_DATA_EXCHANGE_INFO";

    private RelativeLayout innerContentLayout;
    private LinearLayout imageViewLayout;
    private RecyclingImageView baseCurrencyImageView;
    private RecyclingImageView targetCurrencyImageView;
    private AutoResizeTextView baseToTargetCurrecyTextView;
    private AutoResizeTextView targetToBaseCurrecyTextView;

    MNExchangeRatesInfo exchangeRatesInfo;
    MNExchangeRatesAsyncTask exchangeRatesAsyncTask;

    public MNExchangeRatesPanelLayout(Context context) {
        super(context);
    }

    public MNExchangeRatesPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        // inner content layout - 회전 대응
        innerContentLayout = new RelativeLayout(getContext());
        LayoutParams innerContentLayoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        innerContentLayoutParams.addRule(CENTER_IN_PARENT);
        innerContentLayout.setLayoutParams(innerContentLayoutParams);
        getContentLayout().addView(innerContentLayout);

        // image layout
        imageViewLayout = new LinearLayout(getContext());
        imageViewLayout.setId(12312515);
        LayoutParams imageLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        imageLayoutParams.addRule(CENTER_HORIZONTAL);
        imageViewLayout.setLayoutParams(imageLayoutParams);
        innerContentLayout.addView(imageViewLayout);

        // base image
        int imageWidth = getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_flag_width);
        int imageHeight = getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_flag_height);

        baseCurrencyImageView = new RecyclingImageView(getContext());
        baseCurrencyImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams baseImageViewLayoutParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        baseCurrencyImageView.setLayoutParams(baseImageViewLayoutParams);
        imageViewLayout.addView(baseCurrencyImageView);

        // target image
        int marginOuter = getResources().getDimensionPixelSize(R.dimen.margin_outer);

        targetCurrencyImageView = new RecyclingImageView(getContext());
        targetCurrencyImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams targetImageViewLayoutParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        targetImageViewLayoutParams.leftMargin = marginOuter;
        targetCurrencyImageView.setLayoutParams(targetImageViewLayoutParams);
        imageViewLayout.addView(targetCurrencyImageView);

        // base-target currency
        float minTextSize = getResources().getDimension(R.dimen.panel_exchange_rates_minimum_font_size);

        baseToTargetCurrecyTextView = new AutoResizeTextView(getContext());
//        baseToTargetCurrecyTextView.setMinTextSize(minTextSize); // 나중에 구현하자
        baseToTargetCurrecyTextView.setId(8123747);
        baseToTargetCurrecyTextView.setGravity(Gravity.CENTER);
        baseToTargetCurrecyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_exchange_rates_main_font_size));
        baseToTargetCurrecyTextView.setSingleLine();
        LayoutParams baseToTargetLayoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        baseToTargetLayoutParams.topMargin = marginOuter;
        baseToTargetLayoutParams.leftMargin = marginOuter;
        baseToTargetLayoutParams.rightMargin = marginOuter;
        baseToTargetLayoutParams.addRule(BELOW, imageViewLayout.getId());
        baseToTargetLayoutParams.addRule(CENTER_HORIZONTAL);
        baseToTargetCurrecyTextView.setLayoutParams(baseToTargetLayoutParams);
        innerContentLayout.addView(baseToTargetCurrecyTextView);

        // target-base currency
        targetToBaseCurrecyTextView = new AutoResizeTextView(getContext());
//        targetToBaseCurrecyTextView.setMinTextSize(minTextSize);
//        targetToBaseCurrecyTextView.setMaxTextSize(getResources().getDimension(R.dimen.panel_exchange_rates_sub_font_size));
        targetToBaseCurrecyTextView.setGravity(Gravity.CENTER);
        targetToBaseCurrecyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_exchange_rates_sub_font_size));
        targetToBaseCurrecyTextView.setSingleLine();
        LayoutParams targetToBaseLayoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        targetToBaseLayoutParams.topMargin = marginOuter;
        targetToBaseLayoutParams.leftMargin = marginOuter;
        targetToBaseLayoutParams.rightMargin = marginOuter;
        targetToBaseLayoutParams.addRule(BELOW, baseToTargetCurrecyTextView.getId());
        targetToBaseLayoutParams.addRule(CENTER_HORIZONTAL);
        targetToBaseCurrecyTextView.setLayoutParams(targetToBaseLayoutParams);
        innerContentLayout.addView(targetToBaseCurrecyTextView);

        // test - color
        imageViewLayout.setBackgroundColor(Color.MAGENTA);
        innerContentLayout.setBackgroundColor(Color.CYAN);
        getContentLayout().setBackgroundColor(Color.YELLOW);
        baseToTargetCurrecyTextView.setBackgroundColor(Color.GREEN);
        targetToBaseCurrecyTextView.setBackgroundColor(Color.BLUE);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        // get info from panelDataObject
        initExchangeRatesInfo();

        // get exchange rates from server
        if (exchangeRatesAsyncTask != null) {
            exchangeRatesAsyncTask.cancel(true);
            exchangeRatesAsyncTask = null;
        }
        exchangeRatesAsyncTask = new MNExchangeRatesAsyncTask(exchangeRatesInfo.getBaseCurrencyCode(),
                exchangeRatesInfo.getTargetCurrencyCode(), this);
        exchangeRatesAsyncTask.execute();
    }

    private void initExchangeRatesInfo() {
        // date - JSONString에서 클래스로 캐스팅
        Type type = new TypeToken<MNExchangeRatesInfo>() {}.getType();
        if (getPanelDataObject().has(EXCHANGE_RATES_DATA_EXCHANGE_INFO)) {
            try {
                String exchangeInfoJsonString = getPanelDataObject().getString(EXCHANGE_RATES_DATA_EXCHANGE_INFO);
                exchangeRatesInfo = new Gson().fromJson(exchangeInfoJsonString, type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // 없다면 최근 설정을 검색, 그래도 없으면 언어에 따른 디폴트 환율 설정
            SharedPreferences prefs = getContext().getSharedPreferences(EXCHANGE_RATES_PREFS,
                    Context.MODE_PRIVATE);

            String exchangeInfoJsonString = prefs.getString(EXCHANGE_RATES_DATA_EXCHANGE_INFO, null);
            if (exchangeInfoJsonString != null) {
                exchangeRatesInfo = new Gson().fromJson(exchangeInfoJsonString, type);
            } else {
                // 현재 언어에 따라 기본 환율조합을 생성
                exchangeRatesInfo = MNDefaultExchangeRatesInfo.newInstance(getContext());
            }
        }
    }

    @Override
    public void onExchangeRatesLoad(double rates) {
        exchangeRatesInfo.setExchangeRate(rates);
        updateUI();
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        // image
        baseCurrencyImageView.setImageDrawable(null);
        Bitmap baseCurrencyBitmap = FlagBitmapFactory.getGrayscaledFlagBitmap(getContext(),
                exchangeRatesInfo.getBaseCountryCode());
        baseCurrencyImageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(),
                baseCurrencyBitmap));

        targetCurrencyImageView.setImageDrawable(null);
        Bitmap targetCurrencyBitmap = FlagBitmapFactory.getGrayscaledFlagBitmap(getContext(),
                exchangeRatesInfo.getTargetCountryCode());
        targetCurrencyImageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(),
                targetCurrencyBitmap));

        // string
        updateTextViews();
    }

    private void updateTextViews() {
        String baseCurrencyString = exchangeRatesInfo.getBaseCurrencySymbol() +
                MNExchangeRatesInfo.getMoneyString(exchangeRatesInfo.getBaseCurrencyMoney());
        String targetCurrencyString = exchangeRatesInfo.getTargetCurrencySymbol() +
                MNExchangeRatesInfo.getMoneyString(exchangeRatesInfo.getTargetCurrencyMoney());

        baseToTargetCurrecyTextView.setText(baseCurrencyString + " = " + targetCurrencyString);
        targetToBaseCurrecyTextView.setText(targetCurrencyString + " = " + baseCurrencyString);
    }

    /**
     * Rotate
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 로딩이 끝나기 전에는 진행하지 않음
        if (exchangeRatesInfo != null) {
            updateTextViews();
        }
    }
}
