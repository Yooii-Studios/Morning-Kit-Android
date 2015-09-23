package com.yooiistudios.morningkit.panel.quotes;

import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
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
//    private static final String TAG = "MNQuotesDetailFragment";

    @InjectView(R.id.panel_quotes_detail_quote_textview) TextView quoteTextView;

    @InjectView(R.id.panel_quotes_detail_content_layout) LinearLayout contentLayout;
    @InjectView(R.id.panel_quotes_detail_language_english_layout) RelativeLayout englishLayout;
    @InjectView(R.id.panel_quotes_detail_language_korean_layout) RelativeLayout koreanLayout;
    @InjectView(R.id.panel_quotes_detail_language_japanese_layout) RelativeLayout japaneseLayout;
    @InjectView(R.id.panel_quotes_detail_language_simplified_chinese_layout) RelativeLayout sChineseLayout;
    @InjectView(R.id.panel_quotes_detail_language_traditional_chinese_layout) RelativeLayout tChineseLayout;
    @InjectView(R.id.panel_quotes_detail_language_spanish_layout) RelativeLayout spanishLayout;
    @InjectView(R.id.panel_quotes_detail_language_french_layout) RelativeLayout frenchLayout;
    List<ImageButton> languageImageButtons;
    List<TextView> languageTextViews;

    List<Boolean> selectedLanguages;
    MNQuote quote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_quotes_detail_fragment, container, false);
        if (rootView != null && savedInstanceState == null) {
            ButterKnife.inject(this, rootView);

            //// Logic part ////
            if (getPanelDataObject().has(QUOTES_LANGUAGES)) {
                // 설정된 언어 불러오기
                try {
                    String selectedLanguagesJsonString = getPanelDataObject().getString(QUOTES_LANGUAGES);
                    Type type = new TypeToken<List<Boolean>>(){}.getType();
                    selectedLanguages = new Gson().fromJson(selectedLanguagesJsonString, type);

                    // 새로 명언이 추가된 경우 추가된 언어들은 false 선택으로 추가해주기
                    int quoteLanguageSize = MNQuotesLanguage.values().length;
                    if (selectedLanguages.size() != quoteLanguageSize
                            && quoteLanguageSize > selectedLanguages.size()) {
                        int differences = quoteLanguageSize - selectedLanguages.size();
                        for (int i = 0; i < differences; i++) {
                            selectedLanguages.add(false);
                        }
                    }
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
        selectedLanguages = MNQuotesLanguage.initFirstQuoteLanguage(getActivity());

        int languageIndex = 0;
        for (int i = 0; i < MNQuotesLanguage.values().length; i++) {
            if (selectedLanguages.get(i)) {
                languageIndex = i;
            }
        }

        // 현재 언어에 따른 랜덤 명언 얻기
        MNQuotesLanguage quotesLanguage = MNQuotesLanguage.values()[languageIndex];
        quote = MNQuotesLoader.getRandomQuote(getActivity(), quotesLanguage);
    }

    private void initQuoteTextView() {
        if (Build.VERSION.SDK_INT > 10) {
            quoteTextView.setTextIsSelectable(true);
        }
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
        sortLanguageLayouts();

        languageImageButtons = new ArrayList<>();
        languageImageButtons.add(getImageButtonFromLayout(englishLayout));
        languageImageButtons.add(getImageButtonFromLayout(koreanLayout));
        languageImageButtons.add(getImageButtonFromLayout(japaneseLayout));
        languageImageButtons.add(getImageButtonFromLayout(sChineseLayout));
        languageImageButtons.add(getImageButtonFromLayout(tChineseLayout));
        languageImageButtons.add(getImageButtonFromLayout(spanishLayout));
        languageImageButtons.add(getImageButtonFromLayout(frenchLayout));

        languageTextViews = new ArrayList<>();
        languageTextViews.add(getTextViewFromLayout(englishLayout));
        languageTextViews.add(getTextViewFromLayout(koreanLayout));
        languageTextViews.add(getTextViewFromLayout(japaneseLayout));
        languageTextViews.add(getTextViewFromLayout(sChineseLayout));
        languageTextViews.add(getTextViewFromLayout(tChineseLayout));
        languageTextViews.add(getTextViewFromLayout(spanishLayout));
        languageTextViews.add(getTextViewFromLayout(frenchLayout));

        // TODO: 기존 index 에서 값을 결정하는 것이 아닌, uniqueId 에서 index 를 추출하도록 구현하자
        int i = 0;
        for (Boolean isLanguageSelected : selectedLanguages) {
            ImageButton imageButton = languageImageButtons.get(i);
            TextView textView = languageTextViews.get(i);

            imageButton.setTag(i); // tag 를 index 로 사용할 예정
            textView.setTag(i);

            if (isLanguageSelected) {
                imageButton.setImageResource(R.drawable.icon_panel_detail_checkbox_on);
            } else {
                imageButton.setImageResource(R.drawable.icon_panel_detail_checkbox);
            }
            imageButton.setOnClickListener(this);
            textView.setOnClickListener(this);
            i++;
        }
        checkCheckBoxStates();
    }

    private void sortLanguageLayouts() {
        MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(getActivity());
        contentLayout.removeView(englishLayout);
        contentLayout.removeView(japaneseLayout);
        contentLayout.removeView(koreanLayout);
        contentLayout.removeView(sChineseLayout);
        contentLayout.removeView(tChineseLayout);
        contentLayout.removeView(spanishLayout);
        contentLayout.removeView(frenchLayout);

        if (currentLanguageType == MNLanguageType.KOREAN) {
            adjustUpperLanguageLayoutMargin(koreanLayout);
            contentLayout.addView(koreanLayout);
            contentLayout.addView(englishLayout);
            contentLayout.addView(japaneseLayout);
            contentLayout.addView(sChineseLayout);
            contentLayout.addView(tChineseLayout);
            contentLayout.addView(spanishLayout);
            contentLayout.addView(frenchLayout);
        } else if (currentLanguageType == MNLanguageType.JAPANESE) {
            adjustUpperLanguageLayoutMargin(japaneseLayout);
            contentLayout.addView(japaneseLayout);
            contentLayout.addView(englishLayout);
            contentLayout.addView(koreanLayout);
            contentLayout.addView(sChineseLayout);
            contentLayout.addView(tChineseLayout);
            contentLayout.addView(spanishLayout);
            contentLayout.addView(frenchLayout);
        } else if (currentLanguageType == MNLanguageType.SIMPLIFIED_CHINESE) {
            adjustUpperLanguageLayoutMargin(sChineseLayout);
            contentLayout.addView(sChineseLayout);
            contentLayout.addView(tChineseLayout);
            contentLayout.addView(englishLayout);
            contentLayout.addView(japaneseLayout);
            contentLayout.addView(koreanLayout);
            contentLayout.addView(spanishLayout);
            contentLayout.addView(frenchLayout);
        } else if (currentLanguageType == MNLanguageType.TRADITIONAL_CHINESE) {
            adjustUpperLanguageLayoutMargin(tChineseLayout);
            contentLayout.addView(tChineseLayout);
            contentLayout.addView(sChineseLayout);
            contentLayout.addView(englishLayout);
            contentLayout.addView(japaneseLayout);
            contentLayout.addView(koreanLayout);
            contentLayout.addView(spanishLayout);
            contentLayout.addView(frenchLayout);
        } else if (currentLanguageType == MNLanguageType.SPANISH) {
            adjustUpperLanguageLayoutMargin(spanishLayout);
            contentLayout.addView(spanishLayout);
            contentLayout.addView(englishLayout);
            contentLayout.addView(frenchLayout);
            contentLayout.addView(tChineseLayout);
            contentLayout.addView(sChineseLayout);
            contentLayout.addView(japaneseLayout);
            contentLayout.addView(koreanLayout);
        } else if (currentLanguageType == MNLanguageType.FRENCH) {
            adjustUpperLanguageLayoutMargin(frenchLayout);
            contentLayout.addView(frenchLayout);
            contentLayout.addView(englishLayout);
            contentLayout.addView(spanishLayout);
            contentLayout.addView(tChineseLayout);
            contentLayout.addView(sChineseLayout);
            contentLayout.addView(japaneseLayout);
            contentLayout.addView(koreanLayout);
        } else {
            // 영어 및 기본
            adjustUpperLanguageLayoutMargin(englishLayout);
            contentLayout.addView(englishLayout);
            contentLayout.addView(frenchLayout);
            contentLayout.addView(spanishLayout);
            contentLayout.addView(sChineseLayout);
            contentLayout.addView(tChineseLayout);
            contentLayout.addView(japaneseLayout);
            contentLayout.addView(koreanLayout);
        }
    }

    private void adjustUpperLanguageLayoutMargin(RelativeLayout languageLayout) {
        ((LinearLayout.LayoutParams) languageLayout.getLayoutParams()).topMargin =
                getResources().getDimensionPixelSize(R.dimen.panel_detail_bigger_padding);
    }

    private ImageButton getImageButtonFromLayout(RelativeLayout layout) {
        return (ImageButton) layout.findViewById(R.id.panel_quotes_detail_language_image_button);
    }

    private TextView getTextViewFromLayout(RelativeLayout layout) {
        return (TextView) layout.findViewById(R.id.panel_quotes_detail_language_textview);
    }

    private void checkCheckBoxStates() {
        // 1개만 선택이 되어 있다면 해당 체크박스는 disable 해서 무조건 하나는 선택되어 있게 만든다
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
    protected void archivePanelData() throws JSONException {
        // 직렬화 해서 panelDataObject 에 저장
        String selectedLanguagesJsonString = new Gson().toJson(selectedLanguages);
        getPanelDataObject().put(QUOTES_LANGUAGES, selectedLanguagesJsonString);
    }

    @Override
    public void onClick(View view) {
        int index = (Integer) view.getTag();

        ImageButton imageButton = languageImageButtons.get(index);
        if (imageButton.isEnabled()) {
            // TODO: 기존 index 로 값을 세팅해주는 것이 아닌, index 에서 uniqueId 를 추출해서 세팅하자
            // 해당 스위치 토글
            selectedLanguages.set(index, !selectedLanguages.get(index));
            if (selectedLanguages.get(index)) {
                imageButton.setImageResource(R.drawable.icon_panel_detail_checkbox_on);
            } else {
                imageButton.setImageResource(R.drawable.icon_panel_detail_checkbox);
            }
            checkCheckBoxStates();
        }
    }
}
