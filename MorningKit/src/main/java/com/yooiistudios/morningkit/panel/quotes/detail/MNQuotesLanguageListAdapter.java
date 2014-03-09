package com.yooiistudios.morningkit.panel.quotes.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;

public class MNQuotesLanguageListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;

	public MNQuotesLanguageListAdapter(Context _c) {
		context = _c;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return MNQuotesLanguage.values().length;
	}

	@Override
	public MNQuotesLanguage getItem(int i) {
        return MNQuotesLanguage.values()[i];
    }

	@Override
	public long getItemId(int i) {
        return i;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if( convertView == null ) {
			convertView = inflater.inflate(R.layout.panel_quotes_detail_language_item, parent, false );
		}

		// 나중에 스타일을 바꿔줄 필요성은 있을 듯
		CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.panel_quotes_detail_language_checkbox);
		TextView languageNameText = (TextView) convertView.findViewById(R.id.panel_quotes_detail_language_textview);

		int stringId = R.string.setting_language_english;
		switch( getItem(position) ) {
		case ENGLISH:
            stringId = R.string.setting_language_english;
            break;

		case KOREAN:
            stringId = R.string.setting_language_korean;
            break;

		case JAPANESE:
            stringId = R.string.setting_language_japanese;
            break;

		case SIMPLIFIED_CHINESE:
            stringId = R.string.setting_language_simplified_chinese;
            break;

		case TRADITIONAL_CHINESE:
            stringId = R.string.setting_language_traditional_chinese;
            break;
		}
		
//		checkbox.setText( stringId );
		languageNameText.setText(stringId);
//		languageNameText.setTypeface(CommonTypeface.get());
//		languageNameText.setTextColor(GeneralSetting.getModalSubFontColor());

		/*
		// if checkbox is english checkbox
		if( position == 0 )
		{
			checkbox.setChecked(true);
			checkbox.setEnabled(false);
		}
		*/

		return convertView;
	}


}
