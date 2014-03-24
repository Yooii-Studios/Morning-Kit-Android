package com.yooiistudios.morningkit.panel.quotes;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.quotes.model.MNQuote;
import com.yooiistudios.morningkit.panel.quotes.model.MNQuotesLanguage;
import com.yooiistudios.morningkit.panel.quotes.model.MNQuotesLoader;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import org.json.JSONException;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 9.
 *
 * MNQuotesPanelLayout
 */
public class MNQuotesPanelLayout extends MNPanelLayout {
    private static final String TAG = "MNQuotesPanelLayout";

    public static final String QUOTES_STRING = "QUOTES_STRING";
    public static final String QUOTES_LANGUAGES = "QUOTES_LANGUAGES";

    TextView quoteTextView;

    List<Boolean> selectedLanguages;
    MNQuote quote;

    public MNQuotesPanelLayout(Context context) {
        super(context);
    }

    public MNQuotesPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        quoteTextView = new TextView(getContext());
        LayoutParams quoteTextViewLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.margin_outer);
        quoteTextViewLayoutParams.setMargins(margin, margin, margin, margin);
        quoteTextView.setLayoutParams(quoteTextViewLayoutParams);
        quoteTextView.setGravity(Gravity.CENTER);
        getContentLayout().addView(quoteTextView);

        quoteTextView.setBackgroundColor(Color.parseColor("#cfffcf"));
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        //// Logic part ////
        if (getPanelDataObject().has(QUOTES_LANGUAGES)) {
            // 설정된 언어 불러오기
            MNLog.i(TAG, "not first loading");
            try {
                String selectedLanguagesJsonString = getPanelDataObject().getString(QUOTES_LANGUAGES);
                Type type = new TypeToken<List<Boolean>>(){}.getType();
                selectedLanguages = new Gson().fromJson(selectedLanguagesJsonString, type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // 명언 첫 로딩, 현재 언어 체크해서 명언 초기화해주기
            MNLog.i(TAG, "first loading");
            initFirstLanguages();
        }

        getRandomQuote();
    }

    private void initFirstLanguages() throws JSONException {
        // 현재 언어에 따라 첫 명언 언어 설정해주기
        MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(getContext());

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

        // 초기화 이후 panelDataObject에 저장
        getPanelDataObject().put(QUOTES_LANGUAGES, new Gson().toJson(selectedLanguages));
    }

    private void getRandomQuote() {
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
        quote = MNQuotesLoader.getRandomQuote(getContext(), quotesLanguage);
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        if (quote != null) {
            quoteTextView.setVisibility(View.VISIBLE);
            quoteTextView.setText(quote.getQuote() + "\n" + quote.getAuthor());
        } else {
            quoteTextView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void archivePanelData() throws JSONException {
        super.archivePanelData();

        // 리프레시 된 명언을 추가해주기
        String quoteJsonString = new Gson().toJson(quote);
        getPanelDataObject().put(QUOTES_STRING, quoteJsonString);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
