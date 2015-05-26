package com.yooiistudios.morningkit.panel.newsfeed.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderLanguage;

import java.util.List;

/**
 * Created by Dongheyon Jeong in morning-kit from Yooii Studios Co., LTD. on 15. 5. 26.
 *
 * MNNewsProviderLanguageAdapter
 *  뉴스 선택화면의 언어 리스트 표시에 사용
 */
public class MNNewsProviderLanguageAdapter extends BaseAdapter {
    private List<MNNewsProviderLanguage> mNewsProviderLanguages;

    public MNNewsProviderLanguageAdapter(List<MNNewsProviderLanguage> newsProviderLanguages) {
        mNewsProviderLanguages = newsProviderLanguages;
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
        if (convertView == null) {
            convertView = new TextView(parent.getContext());
        }
        MNNewsProviderLanguage newsProviderLanguage = mNewsProviderLanguages.get(position);

        TextView messageTextView = (TextView)convertView;
        messageTextView.setText(newsProviderLanguage.regionalLanguageName);

        return convertView;
    }
}
