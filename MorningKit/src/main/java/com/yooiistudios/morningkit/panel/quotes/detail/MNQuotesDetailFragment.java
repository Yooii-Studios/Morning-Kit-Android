package com.yooiistudios.morningkit.panel.quotes.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.quotes.MNQuotesPanelLayout.QUOTES_STRING;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 9.
 *
 * MNQuotesDetailFragment
 */
public class MNQuotesDetailFragment extends MNPanelDetailFragment {
    private static final String TAG = "MNQuotesDetailFragment";

    @InjectView(R.id.panel_quotes_detail_quote_textview) TextView quoteTextView;

//    @InjectView(R.id.panel_quotes_detail_language_listview) MNQuotesLanguageListView languageListView;

    @InjectView(R.id.panel_quotes_detail_language_english_layout) RelativeLayout languageEnglishLayout;
    @InjectView(R.id.panel_quotes_detail_language_english_layout) RelativeLayout languageKoreanLayout;
    @InjectView(R.id.panel_quotes_detail_language_english_layout) RelativeLayout languageJapaneseLayout;
    @InjectView(R.id.panel_quotes_detail_language_english_layout) RelativeLayout languageSimplefiedChineseLayout;
    @InjectView(R.id.panel_quotes_detail_language_english_layout) RelativeLayout languageTraditionalChineseLayout;

    List<Boolean> selectedLanguages;
    MNQuote quote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_quotes_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            //// Logic part ////
            if (getPanelDataObject().has(QUOTES_STRING)) {
                // 설정된 언어 불러오기
            } else {
                // 명언 첫 로딩, 현재 언어 체크해서 명언 초기화해주기
                initFirstLoading();
            }

            //// UI part ////
            initLanguageLayouts();
            initQuoteTextView();

            // listView의 높이를 맞추어줌

//            languageListView.post(new Runnable() {
//                @Override
//                public void run() {
//                    setListViewHeightBasedOnChildren(languageListView);
//                }
//            });
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
            quoteTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void initLanguageLayouts() {
        int counter = 0;
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

    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    /*
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            MNLog.now("view: " + view.toString());
            MNLog.now("view.getMeasureHeight: " + view.getMeasuredHeight());

            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        MNLog.now("totalHeight: " + totalHeight);

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = DipToPixel.dpToPixel(listView.getContext(), 300);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    */
}
