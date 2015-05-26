package com.yooiistudios.morningkit.panel.newsfeed.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderCountry;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderLanguage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dongheyon Jeong in morning-kit from Yooii Studios Co., LTD. on 15. 5. 26.
 *
 * MNNewsProviderCountryAdapter
 *  뉴스 선택화면의 언어 리스트 표시에 사용
 */
public class MNNewsProviderCountryAdapter extends BaseAdapter {
    private MNNewsProviderLanguage mNewsProviderLanguage;
    private List<MNNewsProviderCountry> mNewsProviderCountries;

    public MNNewsProviderCountryAdapter(MNNewsProviderLanguage newsProviderLanguage) {
        mNewsProviderLanguage = newsProviderLanguage;
        mNewsProviderCountries = new ArrayList<MNNewsProviderCountry>(
                newsProviderLanguage.newsProviderCountries.values()
        );
    }

    public MNNewsProviderCountry getNewsProviderCountryAt(int idx) {
        return mNewsProviderCountries.get(idx);
    }

    @Override
    public int getCount() {
        return mNewsProviderCountries.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsProviderCountries.get(position);
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
        MNNewsProviderCountry newsProviderCountry = mNewsProviderCountries.get(position);

        TextView messageTextView = (TextView)convertView;
        messageTextView.setText(newsProviderCountry.countryLocalName);

        return convertView;
    }
}
