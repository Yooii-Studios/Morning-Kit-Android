package com.yooiistudios.morningkit.panel.quotes.detail;

import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.quotes.model.MNQuote;
import com.yooiistudios.morningkit.panel.quotes.model.MNQuotesLanguage;
import com.yooiistudios.morningkit.panel.quotes.model.MNQuotesLoader;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.quotes.MNQuotesPanelLayout.QUOTES_LANGUAGES;
import static com.yooiistudios.morningkit.panel.quotes.MNQuotesPanelLayout.QUOTES_STRING;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 9.
 *
 * MNQuotesDetailFragment
 */
public class MNQuotesDetailFragment extends MNPanelDetailFragment implements View.OnClickListener {
    private static final String TAG = "MNQuotesDetailFragment";

    @InjectView(R.id.panel_quotes_detail_quote_textview) TextView quoteTextView;

    @InjectView(R.id.panel_quotes_detail_language_english_layout) RelativeLayout languageEnglishLayout;
    @InjectView(R.id.panel_quotes_detail_language_korean_layout) RelativeLayout languageKoreanLayout;
    @InjectView(R.id.panel_quotes_detail_language_japanese_layout) RelativeLayout languageJapaneseLayout;
    @InjectView(R.id.panel_quotes_detail_language_simplified_chinese_layout) RelativeLayout languageSimplifiedChineseLayout;
    @InjectView(R.id.panel_quotes_detail_language_traditional_chinese_layout) RelativeLayout languageTraditionalChineseLayout;
    List<ImageButton> languageImageButtons;

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
//        MersenneTwisterRNG randomGenerator = new MersenneTwisterRNG();
        Random randomGenerator = new Random();
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
        quoteTextView.setTextColor(MNSettingColors.getSubFontColor(MNTheme.getCurrentThemeType(
                getActivity().getApplicationContext())));

        if (quote != null) {
            quoteTextView.setVisibility(View.VISIBLE);
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(
                    getActivity().getApplicationContext());

            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

            // 명언 텍스트 조립
            SpannableString contentString = new SpannableString(quote.getQuote());
            // 폰트 색
            contentString.setSpan(
                    new ForegroundColorSpan(MNSettingColors.getSubFontColor(currentThemeType)),
                    0, contentString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.append(contentString);

            // 내용과 저자 텍스트 사이 간격 주기(텍스트 사이즈를 줄여서 한 줄의 높이를 적당히 조절)
            SpannableString emptyString = new SpannableString("\n\n");
            emptyString.setSpan(new RelativeSizeSpan(0.4f), 0, emptyString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.append(emptyString);

            // 저자 텍스트 조립
            SpannableString authorString = new SpannableString("- " + quote.getAuthor());
            // 폰트 색
            authorString.setSpan(
                    new ForegroundColorSpan(MNSettingColors.getMainFontColor(currentThemeType)),
                    0, authorString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 크기 좀 더 작게 표시
            authorString.setSpan(new RelativeSizeSpan(0.85f), 0, authorString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 오른정렬
            authorString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
                    0, authorString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            stringBuilder.append(authorString);

            // 기본 사이즈로 초기화 후 리사이징
            quoteTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimensionPixelSize(R.dimen.panel_quotes_detail_quote_text_size));
            quoteTextView.setText(stringBuilder, TextView.BufferType.SPANNABLE);
        } else {
            quoteTextView.setVisibility(View.GONE);
        }
    }

    private void initLanguageLayouts() {
        languageImageButtons = new ArrayList<ImageButton>();
        languageImageButtons.add(getImageButtonFromLayout(languageEnglishLayout));
        languageImageButtons.add(getImageButtonFromLayout(languageKoreanLayout));
        languageImageButtons.add(getImageButtonFromLayout(languageJapaneseLayout));
        languageImageButtons.add(getImageButtonFromLayout(languageSimplifiedChineseLayout));
        languageImageButtons.add(getImageButtonFromLayout(languageTraditionalChineseLayout));

        int i = 0;
        for (Boolean isLanguageSelected : selectedLanguages) {
            ImageButton imageButton = languageImageButtons.get(i);
            imageButton.setTag(i); // tag 를 index로 사용할 예정
            if (isLanguageSelected) {
                imageButton.setImageResource(R.drawable.icon_panel_detail_checkbox_on);
            } else {
                imageButton.setImageResource(R.drawable.icon_panel_detail_checkbox);
            }
            imageButton.setOnClickListener(this);
            i++;
        }
        checkCheckBoxStates();
    }

    private ImageButton getImageButtonFromLayout(RelativeLayout layout) {
        return (ImageButton) layout.findViewById(R.id.panel_quotes_detail_language_image_button);
    }

    private void checkCheckBoxStates() {
        // 1개만 선택이 되어 있다면 해당 체크박스는 disable해서 무조건 하나는 선택되어 있게 만든다
        // 그렇지 않다면 모두 선택할 수 있게 해주기
        int counter = 0;
        ImageButton lastImageButtonWhichIsOn = null;
        int index = 0;
        for (Boolean selected : selectedLanguages) {
            if (selected) {
                lastImageButtonWhichIsOn = languageImageButtons.get(index);
                counter += 1;
            }
            index ++;
        }
        if (counter == 1) {
            if (lastImageButtonWhichIsOn != null) {
                lastImageButtonWhichIsOn.setEnabled(false);
                lastImageButtonWhichIsOn.setImageResource(R.drawable.icon_panel_detail_checkbox_on_disabled);
            }
        } else {
            for (int i = 0; i < selectedLanguages.size(); i++) {
                ImageButton imageButton = languageImageButtons.get(i);
                imageButton.setEnabled(true);
                if (selectedLanguages.get(i)) {
                    imageButton.setImageResource(R.drawable.icon_panel_detail_checkbox_on);
                } else {
                    imageButton.setImageResource(R.drawable.icon_panel_detail_checkbox);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        applyTheme();
    }

    private void applyTheme() {
//        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
//        if (getView() != null) {
//            getView().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
//        } else {
//            MNLog.e(TAG, "getView() is null!");
//        }
    }
    @Override
    protected void archivePanelData() throws JSONException {
        // 직렬화 해서 panelDataObject에 저장
        String selectedLanguagesJsonString = new Gson().toJson(selectedLanguages);
        getPanelDataObject().put(QUOTES_LANGUAGES, selectedLanguagesJsonString);
    }

    @Override
    public void onClick(View imageButton) {
        int index = (Integer) imageButton.getTag();
        // 해당 스위치 토글
        selectedLanguages.set(index, !selectedLanguages.get(index));
        if (selectedLanguages.get(index)) {
            ((ImageButton) imageButton).setImageResource(R.drawable.icon_panel_detail_checkbox_on);
        } else {
            ((ImageButton) imageButton).setImageResource(R.drawable.icon_panel_detail_checkbox);
        }
        checkCheckBoxStates();
    }
}
