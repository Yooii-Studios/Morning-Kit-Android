package com.yooiistudios.morningkit.panel.exchangerates.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.exchangerates.currencydialog.MNExchangeRatesSelectDialog;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNCurrencyInfo;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNDefaultExchangeRatesInfo;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNExchangeRatesAsyncTask;
import com.yooiistudios.morningkit.panel.exchangerates.model.MNExchangeRatesInfo;

import org.json.JSONException;

import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.yooiistudios.morningkit.panel.exchangerates.MNExchangeRatesPanelLayout.EXCHANGE_RATES_DATA_EXCHANGE_INFO;
import static com.yooiistudios.morningkit.panel.exchangerates.MNExchangeRatesPanelLayout.EXCHANGE_RATES_PREFS;
import static com.yooiistudios.morningkit.panel.exchangerates.currencydialog.MNExchangeInfoType.BASE;
import static com.yooiistudios.morningkit.panel.exchangerates.currencydialog.MNExchangeInfoType.TARGET;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 3.
 * <p/>
 * MNExchangeRatesDetailFragment
 */
public class MNExchangeRatesDetailFragment extends MNPanelDetailFragment implements MNExchangeRatesAsyncTask.OnExchangeRatesAsyncTaskListener, MNExchangeRatesSelectDialog.OnExchangeRatesSelectDialogListener {

    private static final String TAG = "MNExchangeRatesDetailFragment";

    @InjectView(R.id.panel_exchange_rates_info_layout_base)     MNExchangeRatesInfoLayout baseInfoLayout;
    @InjectView(R.id.panel_exchange_rates_info_layout_target)   MNExchangeRatesInfoLayout targetInfoLayout;
    MNExchangeRatesInfo exchangeRatesInfo;

    @InjectView(R.id.panel_exchange_rates_edit_text_container)  LinearLayout editTextsLayout;
    @InjectView(R.id.panel_exchange_rates_edit_base_symbol)     EditText baseSymbolEditText;
    @InjectView(R.id.panel_exchange_rates_edit_base)            EditText baseEditText;
    @InjectView(R.id.panel_exchange_rates_edit_target)          EditText targetEditText;

    @InjectView(R.id.panel_exchange_rates_swap_imageview)       ImageView swapImageView;
    @InjectView(R.id.panel_exchange_rates_swap_textview)        TextView swapTextView;

    MNExchangeRatesAsyncTask exchangeRatesAsyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_exchange_rates_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            //// Logic part ////
            MNCurrencyInfo.loadAllCurrency(getActivity()); // static이라 가장 먼저 읽어주기 - 나중에 리팩토링 하자
            initExchangeRatesInfo();
            getExchangeRatesFromServer();

            //// UI part ////
            // exchange rates info layout
            baseInfoLayout.loadExchangeCountry(exchangeRatesInfo.getBaseCurrencyCode());
            targetInfoLayout.loadExchangeCountry(exchangeRatesInfo.getTargetCurrencyCode());

            initEditTexts();
            initSwapLayout();
        }
        return rootView;
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
            SharedPreferences prefs = getActivity().getSharedPreferences(EXCHANGE_RATES_PREFS,
                    Context.MODE_PRIVATE);

            String exchangeInfoJsonString = prefs.getString(EXCHANGE_RATES_DATA_EXCHANGE_INFO, null);
            if (exchangeInfoJsonString != null) {
                exchangeRatesInfo = new Gson().fromJson(exchangeInfoJsonString, type);
            } else {
                // 현재 언어에 따라 기본 환율조합을 생성
                exchangeRatesInfo = MNDefaultExchangeRatesInfo.newInstance(getActivity());
            }
        }
    }

    public void initSwapLayout() {
        int highlightColor = getResources().getColor(R.color.pastel_green_sub_font_color);
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor,
                PorterDuff.Mode.SRC_ATOP);
        swapImageView.setColorFilter(colorFilter);
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
        targetEditText.setText(null);
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
    }

    @Override
    protected void archivePanelData() throws JSONException {
        SharedPreferences prefs = getActivity().getSharedPreferences(EXCHANGE_RATES_PREFS,
                Context.MODE_PRIVATE);

        // 돈이 0이라면 최근 설정값을 확인, 있다면 그것으로 바꾸어주고, 없다면 그냥 0으로 표시
        if (exchangeRatesInfo.getBaseCurrencyMoney() == 0) {
            String exchangeInfoJsonString = prefs.getString(EXCHANGE_RATES_DATA_EXCHANGE_INFO, null);
            if (exchangeInfoJsonString != null) {
                Type type = new TypeToken<MNExchangeRatesInfo>() {}.getType();
                MNExchangeRatesInfo latestExchangeRatesInfo = new Gson().fromJson(exchangeInfoJsonString, type);
                exchangeRatesInfo.setBaseCurrencyMoney(latestExchangeRatesInfo.getBaseCurrencyMoney());
            }
        }

        // 직렬화
        String exchangeInfoJsonString = new Gson().toJson(exchangeRatesInfo);

        // panelDataObject에 저장
        getPanelDataObject().put(EXCHANGE_RATES_DATA_EXCHANGE_INFO, exchangeInfoJsonString);

        // SharedPreferences에도 아카이빙 저장
        prefs.edit().putString(EXCHANGE_RATES_DATA_EXCHANGE_INFO, exchangeInfoJsonString).commit();
    }

    @OnClick({R.id.panel_exchange_rates_info_layout_base, R.id.panel_exchange_rates_info_layout_target})
    public void onExchangeInfoButtonClicked(View v) {

        // Holo Dark 테마를 사용하기 위해서 ContextThemeWrapper를 사용
        Context dialogContext;
        if (Build.VERSION.SDK_INT >= 11) {
            dialogContext = new ContextThemeWrapper(getActivity(),
                    android.R.style.Theme_Holo_Dialog_NoActionBar);
        } else {
            dialogContext = new ContextThemeWrapper(getActivity(),
                    android.R.style.Theme_NoTitleBar);
        }

        switch (v.getId()) {
            case R.id.panel_exchange_rates_info_layout_base:
                new MNExchangeRatesSelectDialog(dialogContext).showOnClick(BASE, exchangeRatesInfo, this);
                break;
            case R.id.panel_exchange_rates_info_layout_target:
                new MNExchangeRatesSelectDialog(dialogContext).showOnClick(TARGET, exchangeRatesInfo, this);
                break;
        }
    }

    @OnClick(R.id.panel_exchange_rates_swap_layout)
    public void onSwapTextViewClicked(View v) {
        // reverse
        exchangeRatesInfo = exchangeRatesInfo.getReverseExchangeInfo();

        // update ui
        baseInfoLayout.loadExchangeCountry(exchangeRatesInfo.getBaseCurrencyCode());
        targetInfoLayout.loadExchangeCountry(exchangeRatesInfo.getTargetCurrencyCode());
        calculate(true);
    }

    // 비동기 파서에서 데이터를 읽은 후의 콜백 메서드
    @Override
    public void onExchangeRatesLoad(double rates) {
        exchangeRatesInfo.setExchangeRate(rates);
        calculate(false);
    }

    // 나라 선택 다이얼로그의 콜백 메서드
    @Override
    public void onSelectCurrency(MNExchangeRatesInfo exchangeRatesInfo) {
        this.exchangeRatesInfo = exchangeRatesInfo;

        baseInfoLayout.loadExchangeCountry(exchangeRatesInfo.getBaseCurrencyCode());
        targetInfoLayout.loadExchangeCountry(exchangeRatesInfo.getTargetCurrencyCode());

        getExchangeRatesFromServer();
    }
}
