package com.yooiistudios.morningkit.panel.exchangerates;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNExchangeRatesAsyncTask;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNExchangeRatesInfo;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 3.
 * <p/>
 * MNExchangeRatesDetailFragment
 */
public class MNExchangeRatesDetailFragment extends MNPanelDetailFragment implements MNExchangeRatesAsyncTask.OnExchangeRatesAsyncTaskListener {

    private static final String TAG = "MNExchangeRatesDetailFragment";

    @InjectView(R.id.panel_exchange_rates_info_layout_base)     MNExchangeRatesInfoLayout baseInfoLayout;
    @InjectView(R.id.panel_exchange_rates_info_layout_target)   MNExchangeRatesInfoLayout targetInfoLayout;
    MNExchangeRatesInfo exchangeRatesInfo;

    @InjectView(R.id.panel_exchange_rates_edit_text_container)  LinearLayout editTextsLayout;
    @InjectView(R.id.panel_exchange_rates_edit_base_symbol)     EditText baseSymbolEditText;
    @InjectView(R.id.panel_exchange_rates_edit_base)            EditText baseEditText;
    @InjectView(R.id.panel_exchange_rates_edit_target)          EditText targetEditText;

    @InjectView(R.id.panel_exchange_rates_swap_textview)        TextView swapTextView;

    MNExchangeRatesAsyncTask exchangeRatesAsyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_exchange_rates_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            //// Logic part ////
            // 국가 코드 가져오기 - 임시
            String baseCurrencyCode = "KRW";
            String targetCurrencyCode = "CNY";

            // 환율 정보 가져오기 - 임시
            double baseCurrenyMoney = 1.0f;
            double exchangeRate = 1;

            // exchangeInfo model
            exchangeRatesInfo = new MNExchangeRatesInfo(baseCurrencyCode, targetCurrencyCode);
            exchangeRatesInfo.setBaseCurrencyMoney(baseCurrenyMoney);
            exchangeRatesInfo.setExchangeRate(exchangeRate);

            getExchangeRatesFromServer();

            //// UI part ////
            // exchange rates info layout
            baseInfoLayout.loadExchangeCountry(baseCurrencyCode);
            targetInfoLayout.loadExchangeCountry(targetCurrencyCode);

            initEditTexts();

        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        applyTheme();
    }

    private void getExchangeRatesFromServer() {
        if (exchangeRatesAsyncTask != null) {
            exchangeRatesAsyncTask.cancel(true);
            exchangeRatesAsyncTask = null;
        }
        exchangeRatesAsyncTask = new MNExchangeRatesAsyncTask(exchangeRatesInfo.getBaseCurrencyCode(),
                exchangeRatesInfo.getTargetCurrencyCode(), this);
        exchangeRatesAsyncTask.execute();
    }

    private void initEditTexts() {
        // base
        baseEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if( hasFocus ) {
                    String input = baseEditText.getText().toString();

                    input = input.replace(',', ' ');
                    input = input.replaceAll(" ", "");

                    baseEditText.setText(input);
                    baseEditText.setSelection(input.length());
                } else {
                    String input = baseEditText.getText().toString();

                    double base = 0;
                    if (input.length() != 0) {
                        base = MNExchangeRatesInfo.getDoubleMoney(input);
                        input = MNExchangeRatesInfo.getMoneyString(base);
                        baseEditText.setText(input);
                    } else {
                        baseEditText.setText("0");
                    }
                    exchangeRatesInfo.setBaseCurrencyMoney(base);
                }
            }
        });
        // 숫자가 입력될 때마다
        baseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                calculate(false);
            }
        });

        // .을 한번만 찍을 수 있게 구현
        baseEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if( keyCode == KeyEvent.KEYCODE_NUMPAD_DOT || keyCode == KeyEvent.KEYCODE_PERIOD ) {
                    return baseEditText.getText().toString().contains(".");
                }
                return false;
            }
        });

        baseEditText.setFilters(new InputFilter[] { newBaseEditTextFilterInstance() });
        baseEditText.setText(MNExchangeRatesInfo.getMoneyString(exchangeRatesInfo.getBaseCurrencyMoney()));
        baseEditText.setSelection(baseEditText.length());

        // target - 누르면 base 에 포커스를 주기
        targetEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                baseEditText.requestFocus();
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(baseEditText, 0);   //mPwd는 EditText의 변수 - 내리기
                return false;
            }
        });
    }

    private InputFilter newBaseEditTextFilterInstance() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if( dest.toString().contains(".") ) {
                    String sourceString = source.toString();
                    boolean dotRestrict = false;
                    if (sourceString.contains(".") || sourceString.contains(".")) {
                        dotRestrict = true;
                    }
                    if( dotRestrict ) {
                        return "";
                    }
                }
                return null;
            }
        };
    }

    // 환율을 계산. base 숫자가 아주 작을 경우 따로 처리를 해 줘야만 한다.
    private void calculate(boolean shouldRevising) {
        String baseString = baseEditText.getText().toString();

        if( baseString.length() > 0 ) {

            double base = MNExchangeRatesInfo.getDoubleMoney(baseString);
            double target = base * exchangeRatesInfo.getExchangeRate();

            // base currency 단위가 1이라면, target currency 값이 0.1이상이 될 때까지 base와 target의 단위를 10씩 곱해준다.
            // 그래서 너무 작은 값이 나오지 않을 수 있도록 도와줄 수 있게 한다.
            if (shouldRevising) {
                if (base == 1) {
                    while (target < 0.1) {
                        base *= 10;
                        target *= 10;
                    }
                    baseEditText.setText(MNExchangeRatesInfo.getMoneyString(base));
                }
            }
            exchangeRatesInfo.setBaseCurrencyMoney(base);
            targetEditText.setText(exchangeRatesInfo.getTargetCurrencySymbol() + " "
                    + MNExchangeRatesInfo.getMoneyString(exchangeRatesInfo.getTargetCurrencyMoney()));
        } else {
            exchangeRatesInfo.setBaseCurrencyMoney(0);
            targetEditText.setText(exchangeRatesInfo.getTargetCurrencySymbol() + " 0");
        }
        if (exchangeRatesInfo.getTargetCurrencyMoney() == -1) {
            targetEditText.setText(getString(R.string.no_network_connection));
        }
        baseSymbolEditText.setText(exchangeRatesInfo.getBaseCurrencySymbol());
    }

    private void applyTheme() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
        if (getView() != null) {
            getView().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
        } else {
            MNLog.e(TAG, "getView() is null!");
        }
        editTextsLayout.setBackgroundColor(MNSettingColors.getExchangeRatesForwardColor(currentThemeType));
        swapTextView.setBackgroundColor(MNSettingColors.getExchangeRatesForwardColor(currentThemeType));
    }

    @Override
    protected void archivePanelData() throws JSONException {

    }

    @OnClick({R.id.panel_exchange_rates_info_layout_base, R.id.panel_exchange_rates_info_layout_target})
    public void onExchangeInfoButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.panel_exchange_rates_info_layout_base:
                MNLog.i(TAG, "baseExchangeInfoButtonClicked");
                break;
            case R.id.panel_exchange_rates_info_layout_target:
                MNLog.i(TAG, "targetExchangeInfoButtonClicked");
                break;
        }
    }

    @OnClick(R.id.panel_exchange_rates_swap_textview)
    public void onSwapTextViewClicked(View v) {
        // reverse
        exchangeRatesInfo = exchangeRatesInfo.getReverseExchangeInfo();

        // update ui
        baseInfoLayout.loadExchangeCountry(exchangeRatesInfo.getBaseCurrencyCode());
        targetInfoLayout.loadExchangeCountry(exchangeRatesInfo.getTargetCurrencyCode());
        calculate(true);
    }

    @Override
    public void onExchangeRatesLoad(double rates) {
        exchangeRatesInfo.setExchangeRate(rates);
        calculate(false);
    }
}
