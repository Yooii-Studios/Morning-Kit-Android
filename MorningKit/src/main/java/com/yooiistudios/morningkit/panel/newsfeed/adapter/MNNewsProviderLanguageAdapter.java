package com.yooiistudios.morningkit.panel.newsfeed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrl;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderLanguage;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNNewsFeedUtil;

import java.util.List;

/**
 * Created by Dongheyon Jeong in morning-kit from Yooii Studios Co., LTD. on 15. 5. 26.
 *
 * MNNewsProviderLanguageAdapter
 *  뉴스 선택화면의 언어 리스트 표시에 사용
 */
public class MNNewsProviderLanguageAdapter extends BaseAdapter {
    private List<MNNewsProviderLanguage> mNewsProviderLanguages;
    private int mCurrentLanguageIndex = -1;

    public MNNewsProviderLanguageAdapter(List<MNNewsProviderLanguage> newsProviderLanguages,
                                         MNNewsFeedUrl feedUrl) {
        mNewsProviderLanguages = newsProviderLanguages;

        String currentLanguageRegionCode = MNNewsFeedUtil.makeLanguageRegionCode(
                feedUrl.languageCode,
                feedUrl.regionCode
        );
        for (int i = 0; i < newsProviderLanguages.size(); i++) {
            MNNewsProviderLanguage lang = newsProviderLanguages.get(i);
            String languageRegionCode =
                    MNNewsFeedUtil.makeLanguageRegionCode(lang.languageCode, lang.regionCode);
            if (languageRegionCode.equals(currentLanguageRegionCode)) {
                mCurrentLanguageIndex = i;
                break;
            }
        }
    }

    public MNNewsProviderLanguage getNewsProviderLanguageAt(int idx) {
        return mNewsProviderLanguages.get(idx);
    }

    @Override
    public int getCount() {
        return mNewsProviderLanguages.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsProviderLanguages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.panel_news_select_row, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(convertView);

        setLanguageName(position, viewHolder);
        setFontColor(context, viewHolder, position);

        return convertView;
    }

    private void setLanguageName(int position, ViewHolder viewHolder) {
        MNNewsProviderLanguage newsProviderLanguage = mNewsProviderLanguages.get(position);
        viewHolder.textView.setText(newsProviderLanguage.regionalLanguageName);
    }

    private void setFontColor(Context context, ViewHolder viewHolder, int position) {
        int fontColorResId;
        if (position == mCurrentLanguageIndex) {
            fontColorResId = R.color.pastel_green_main_font_color;
        } else {
            fontColorResId = R.color.pastel_green_sub_font_color;
        }
        int fontColor = context.getResources().getColor(fontColorResId);
        viewHolder.textView.setTextColor(fontColor);
    }

    private static class ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            textView = (TextView)view.findViewById(R.id.news_select_row_title);
        }
    }
}
