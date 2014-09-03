package com.yooiistudios.morningkit.panel.exchangerates;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.common.textview.AutoResizeTextView;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.exchangerates.model.FlagBitmapFactory;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNDefaultExchangeRatesInfo;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNExchangeRatesAsyncTask;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNExchangeRatesInfo;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;

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

//    private static final String TAG = "MNExchangeRatesPanelLayout";

    public static final String EXCHANGE_RATES_PREFS = "EXCHANGE_RATES_PREFS";
    public static final String EXCHANGE_RATES_DATA_EXCHANGE_INFO = "EXCHANGE_RATES_DATA_EXCHANGE_INFO";

    private ImageView baseCurrencyImageView;
    private ImageView targetCurrencyImageView;
    private AutoResizeTextView baseToTargetCurrecyTextView;
    private AutoResizeTextView targetToBaseCurrecyTextView;

    MNExchangeRatesInfo exchangeRatesInfo;
    MNExchangeRatesAsyncTask exchangeRatesAsyncTask;

    public MNExchangeRatesPanelLayout(Context context) {
        super(context);
    }
//    public MNExchangeRatesPanelLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }

    @Override
    protected void init() {
        super.init();

        // inner content layout - 회전 대응
        RelativeLayout innerContentLayout = new RelativeLayout(getContext());
        LayoutParams innerContentLayoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        innerContentLayoutParams.addRule(CENTER_IN_PARENT);
        innerContentLayout.setLayoutParams(innerContentLayoutParams);
        getContentLayout().addView(innerContentLayout);

        // image layout
        LinearLayout imageViewLayout = new LinearLayout(getContext());
        imageViewLayout.setId(12312515);
        LayoutParams imageLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        imageLayoutParams.addRule(CENTER_HORIZONTAL);
        imageViewLayout.setLayoutParams(imageLayoutParams);
        innerContentLayout.addView(imageViewLayout);

        // base image
        int imageWidth = getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_flag_width);
        int imageHeight = getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_flag_height);

        baseCurrencyImageView = new ImageView(getContext());
        baseCurrencyImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams baseImageViewLayoutParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        baseCurrencyImageView.setLayoutParams(baseImageViewLayoutParams);
        imageViewLayout.addView(baseCurrencyImageView);

        // target image
        int marginOuter = getResources().getDimensionPixelSize(R.dimen.panel_layout_padding);

        targetCurrencyImageView = new ImageView(getContext());
        targetCurrencyImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams targetImageViewLayoutParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        targetImageViewLayoutParams.leftMargin = marginOuter;
        targetCurrencyImageView.setLayoutParams(targetImageViewLayoutParams);
        imageViewLayout.addView(targetCurrencyImageView);

        // base-target currency
//        float minTextSize = getResources().getDimension(R.dimen.panel_exchange_rates_minimum_font_size);

        int panelBiggerMargin = getResources().getDimensionPixelSize(R.dimen.panel_detail_bigger_padding);
        baseToTargetCurrecyTextView = new AutoResizeTextView(getContext());
//        baseToTargetCurrecyTextView.setMinTextSize(minTextSize); // 나중에 구현하자
        baseToTargetCurrecyTextView.setId(8123747);
        baseToTargetCurrecyTextView.setGravity(Gravity.CENTER);
        baseToTargetCurrecyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_exchange_rates_main_font_size));
        baseToTargetCurrecyTextView.setSingleLine();
        LayoutParams baseToTargetLayoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        baseToTargetLayoutParams.topMargin = marginOuter;
        baseToTargetLayoutParams.leftMargin = panelBiggerMargin;
        baseToTargetLayoutParams.rightMargin = panelBiggerMargin;
        baseToTargetLayoutParams.addRule(BELOW, imageViewLayout.getId());
        baseToTargetLayoutParams.addRule(CENTER_HORIZONTAL);
        baseToTargetCurrecyTextView.setLayoutParams(baseToTargetLayoutParams);
        innerContentLayout.addView(baseToTargetCurrecyTextView);

        // target-base currency
        targetToBaseCurrecyTextView = new AutoResizeTextView(getContext());
//        targetToBaseCurrecyTextView.setMinTextSize(minTextSize);
        targetToBaseCurrecyTextView.setMaxTextSize(getResources().getDimension(R.dimen.panel_exchange_rates_sub_font_size));
        targetToBaseCurrecyTextView.setGravity(Gravity.CENTER);
        targetToBaseCurrecyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_exchange_rates_sub_font_size));
        targetToBaseCurrecyTextView.setSingleLine();
        LayoutParams targetToBaseLayoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        targetToBaseLayoutParams.leftMargin = panelBiggerMargin;
        targetToBaseLayoutParams.rightMargin = panelBiggerMargin;
        targetToBaseLayoutParams.addRule(BELOW, baseToTargetCurrecyTextView.getId());
        targetToBaseLayoutParams.addRule(CENTER_HORIZONTAL);
        targetToBaseCurrecyTextView.setLayoutParams(targetToBaseLayoutParams);
        innerContentLayout.addView(targetToBaseCurrecyTextView);

        // test - color
        if (DEBUG_UI) {
            imageViewLayout.setBackgroundColor(Color.MAGENTA);
            innerContentLayout.setBackgroundColor(Color.CYAN);
            getContentLayout().setBackgroundColor(Color.YELLOW);
            baseToTargetCurrecyTextView.setBackgroundColor(Color.GREEN);
            targetToBaseCurrecyTextView.setBackgroundColor(Color.BLUE);
        }
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        // recycle image views
        MNBitmapUtils.recycleImageView(baseCurrencyImageView);
        MNBitmapUtils.recycleImageView(targetCurrencyImageView);

        // get info from panelDataObject
        initExchangeRatesInfo();

        // get exchange rates from server
        if (exchangeRatesAsyncTask != null) {
            exchangeRatesAsyncTask.cancel(true);
            exchangeRatesAsyncTask = null;
        }
        exchangeRatesAsyncTask = new MNExchangeRatesAsyncTask(exchangeRatesInfo.getBaseCurrencyCode(),
                exchangeRatesInfo.getTargetCurrencyCode(), this);
        // 앞 큐에 있는 AsyncTask 가 막힐 경우 뒷 쓰레드가 되게 하기 위한 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            exchangeRatesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            exchangeRatesAsyncTask.execute();
        }
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
        Bitmap baseCurrencyBitmap = FlagBitmapFactory.getGrayscaledFlagBitmap(getContext().getApplicationContext(),
                exchangeRatesInfo.getBaseCountryCode());
        baseCurrencyImageView.setImageDrawable(
                new BitmapDrawable(getContext().getApplicationContext().getResources(), baseCurrencyBitmap));

        Bitmap targetCurrencyBitmap = FlagBitmapFactory.getGrayscaledFlagBitmap(getContext().getApplicationContext(),
                exchangeRatesInfo.getTargetCountryCode());
        targetCurrencyImageView.setImageDrawable(
                new BitmapDrawable(getContext().getApplicationContext().getResources(), targetCurrencyBitmap));

        // string
        updateTextViews();
    }

    private void updateTextViews() {
        String baseCurrencyString = exchangeRatesInfo.getBaseCurrencySymbol() +
                MNExchangeRatesInfo.getMoneyString(exchangeRatesInfo.getBaseCurrencyMoney(), getContext());
        String targetCurrencyString = exchangeRatesInfo.getTargetCurrencySymbol() +
                MNExchangeRatesInfo.getMoneyString(exchangeRatesInfo.getTargetCurrencyMoney(), getContext());

        // Base To Target
        // 기본 폰트 크기로 설정하면 자동으로 리사이징 진행
        SpannableStringBuilder baseToTargetStringBuilder = new SpannableStringBuilder();
        baseToTargetStringBuilder.append(baseCurrencyString).append(" = ").append(targetCurrencyString);
        baseToTargetCurrecyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_main_font_size));
        baseToTargetCurrecyTextView.setText(baseToTargetStringBuilder, TextView.BufferType.SPANNABLE);

        // Target To Base
        MNExchangeRatesInfo reverseExchangeRatesInfo = exchangeRatesInfo.getReverseExchangeInfo();

        // reverseTargetMoney가 너무 작을 경우 일정 이상의 값으로 환산해줌 - 적어도 10번 이상은 하지 않게 방어하자
        int limit = 0;
        while (reverseExchangeRatesInfo.getBaseCurrencyMoney() != 0 &&
                reverseExchangeRatesInfo.getTargetCurrencyMoney() < 0.1 && limit < 10) {
            limit ++;
            reverseExchangeRatesInfo.setBaseCurrencyMoney(reverseExchangeRatesInfo.getBaseCurrencyMoney() * 10);
        }

        String reverseBaseCurrencyString = reverseExchangeRatesInfo.getBaseCurrencySymbol() +
                MNExchangeRatesInfo.getMoneyString(reverseExchangeRatesInfo.getBaseCurrencyMoney(), getContext());
        String reverseTargetCurrencyString = reverseExchangeRatesInfo.getTargetCurrencySymbol() +
                MNExchangeRatesInfo.getMoneyString(reverseExchangeRatesInfo.getTargetCurrencyMoney(), getContext());

        // 기본 폰트 크기로 설정하면 자동으로 리사이징 진행
        SpannableStringBuilder targetToBaseStringBuilder = new SpannableStringBuilder();
        targetToBaseStringBuilder.append(reverseBaseCurrencyString).append(" = ").append(reverseTargetCurrencyString);
        targetToBaseCurrecyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_sub_font_size));
        targetToBaseCurrecyTextView.setText(targetToBaseStringBuilder, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getContext());

        // weather condition image view
        baseToTargetCurrecyTextView.setTextColor(MNMainColors.getMainFontColor(currentThemeType,
                getContext().getApplicationContext()));
        targetToBaseCurrecyTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType,
                getContext().getApplicationContext()));
    }

    /**
     * Rotate
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 로딩이 끝나기 전에는 진행하지 않음
        if (exchangeRatesInfo != null) {
            MNViewSizeMeasure.setViewSizeObserver(this, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
                @Override
                public void onLayoutLoad() {
                    updateTextViews();
                }
            });

        }
    }
}
