package com.yooiistudios.morningkit.panel.quotes.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.quotes.model.MNQuote;
import com.yooiistudios.morningkit.panel.quotes.model.MNQuotesLanguage;
import com.yooiistudios.morningkit.panel.quotes.model.MNQuotesLoader;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.quotes.MNQuotesPanelLayout.QUOTES_LANGUAGES;
import static com.yooiistudios.morningkit.panel.quotes.MNQuotesPanelLayout.QUOTES_STRING;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 9.
 *
 * MNQuotesDetailFragment
 */
public class MNQuotesDetailFragment extends MNPanelDetailFragment implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MNQuotesDetailFragment";

    @InjectView(R.id.panel_quotes_detail_quote_textview) TextView quoteTextView;

    @InjectView(R.id.panel_quotes_detail_language_english_layout) RelativeLayout languageEnglishLayout;
    @InjectView(R.id.panel_quotes_detail_language_korean_layout) RelativeLayout languageKoreanLayout;
    @InjectView(R.id.panel_quotes_detail_language_japanese_layout) RelativeLayout languageJapaneseLayout;
    @InjectView(R.id.panel_quotes_detail_language_simplified_chinese_layout) RelativeLayout languageSimplifiedChineseLayout;
    @InjectView(R.id.panel_quotes_detail_language_traditional_chinese_layout) RelativeLayout languageTraditionalChineseLayout;
    List<CheckBox> languageCheckBoxes;

    List<Boolean> selectedLanguages;
    MNQuote quote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_quotes_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            //// Logic part ////
            if (getPanelDataObject().has(QUOTES_LANGUAGES)) {
                // 설정된 언어 불러오기
                try {
                    String selectedLanguagesJsonString = getPanelDataObject().getString(QUOTES_LANGUAGES);
                    Type type = new TypeToken<List<Boolean>>(){}.getType();
                    selectedLanguages = new Gson().fromJson(selectedLanguagesJsonString, type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // quote 정보 불러오기
                if (getPanelDataObject().has(QUOTES_STRING)) {
                    try {
                        String quoteJsonString = getPanelDataObject().getString(QUOTES_STRING);
                        Type type = new TypeToken<MNQuote>(){}.getType();
                        quote = new Gson().fromJson(quoteJsonString, type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // 명언 첫 로딩, 현재 언어 체크해서 명언 초기화해주기
                initFirstLoading();
            }

            //// UI part ////
            initLanguageLayouts();
            initQuoteTextView();
        }
        return rootView;
    }

    private void initFirstLoading() {
        // 현재 언어에 따라 첫 명언 언어 설정해주기
        MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(getActivity());

        // 러시아 명언은 없어서 영어로 대체
        if (currentLanguageType == MNLanguageType.RUSSIAN) {
            currentLanguageType = MNLanguageType.ENGLISH;
        }

        selectedLanguages = new ArrayList<Boolean>();

        for (int i = 0; i < 5; i++) {
            if (currentLanguageType.getIndex() == i) {
                selectedLanguages.add(true);
            } else {
                selectedLanguages.add(false);
            }
        }

        MNLog.i(TAG, "selectedLanguages: " + selectedLanguages.toString());

        // 해당 언어에 따라 명언 골라주기
        // while이 이상적이지만 혹시나 모를 무한루프 방지를 위해 100번만 돌림
        MersenneTwisterRNG randomGenerator = new MersenneTwisterRNG();
        int randomLanguageIndex = 0;
        for (int i = 0; i < 100; i++) {
            randomLanguageIndex = randomGenerator.nextInt(selectedLanguages.size());
            if (selectedLanguages.get(randomLanguageIndex)) {
                break;
            }
        }

        // 랜덤 명언 얻기
        MNQuotesLanguage quotesLanguage = MNQuotesLanguage.valueOf(randomLanguageIndex);
        quote = MNQuotesLoader.getRandomQuote(getActivity(), quotesLanguage);
    }

    private void initQuoteTextView() {
        if (quote != null) {
            quoteTextView.setVisibility(View.VISIBLE);
            quoteTextView.setText(quote.getQuote() + "\n" + quote.getAuthor());
        } else {
            quoteTextView.setVisibility(View.GONE);
        }
    }

    private void initLanguageLayouts() {
        languageCheckBoxes = new ArrayList<CheckBox>();
        languageCheckBoxes.add(getCheckBoxFromLayout(languageEnglishLayout));
        languageCheckBoxes.add(getCheckBoxFromLayout(languageKoreanLayout));
        languageCheckBoxes.add(getCheckBoxFromLayout(languageJapaneseLayout));
        languageCheckBoxes.add(getCheckBoxFromLayout(languageSimplifiedChineseLayout));
        languageCheckBoxes.add(getCheckBoxFromLayout(languageTraditionalChineseLayout));

        int i = 0;
        for (Boolean isLanguageSelected : selectedLanguages) {
            CheckBox checkBox = languageCheckBoxes.get(i);
            checkBox.setChecked(isLanguageSelected);
            checkBox.setOnCheckedChangeListener(this);
            i++;
        }

        checkCheckBoxStates();
    }

    private CheckBox getCheckBoxFromLayout(RelativeLayout layout) {
        return (CheckBox) layout.findViewById(R.id.panel_quotes_detail_language_checkbox);
    }

    private void checkCheckBoxStates() {
        // 1개만 선택이 되어 있다면 해당 체크박스는 disable해서 무조건 하나는 선택되어 있게 만든다
        // 그렇지 않다면 모두 선택할 수 있게 해주기
        int counter = 0;
        CheckBox lastCheckBoxWhichIsOn = null;
        for (CheckBox checkBox : languageCheckBoxes) {
            if (checkBox.isChecked()) {
                counter += 1;
                lastCheckBoxWhichIsOn = checkBox;
            }
        }
        if (counter == 1) {
            if (lastCheckBoxWhichIsOn != null) {
                lastCheckBoxWhichIsOn.setEnabled(false);
            }
        } else {
            for (CheckBox checkBox : languageCheckBoxes) {
                checkBox.setEnabled(true);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        applyTheme();
    }

    private void applyTheme() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
        if (getView() != null) {
            getView().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
            quoteTextView.setBackgroundColor(MNSettingColors.getExchangeRatesForwardColor(currentThemeType));
        } else {
            MNLog.e(TAG, "getView() is null!");
        }
    }
    @Override
    protected void archivePanelData() throws JSONException {
        // 스위치를 가지고 List를 만듬
        selectedLanguages.clear();
        for (CheckBox checkBox: languageCheckBoxes) {
            selectedLanguages.add(checkBox.isChecked());
        }
        // 직렬화 해서 panelDataObject에 저장
        String selectedLanguagesJsonString = new Gson().toJson(selectedLanguages);
        getPanelDataObject().put(QUOTES_LANGUAGES, selectedLanguagesJsonString);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        checkCheckBoxStates();
    }
}
