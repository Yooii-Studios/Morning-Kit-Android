package com.yooiistudios.morningkit.panel.exchangerates.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.stevenkim.waterlily.bitmapfun.util.RecyclingBitmapDrawable;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.dp.DipToPixel;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import java.util.ArrayList;

public class CurrencyInfoListAdapter extends BaseAdapter
{
	private ArrayList<MNCurrencyInfo> info;
	private Context context;
	private LayoutInflater inflater;

	public CurrencyInfoListAdapter(Context _context, ArrayList<MNCurrencyInfo> _info) 
	{
		context = _context;
		inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		info = _info;
	}

	@Override
	public int getCount() {	return info.size();	}

	@Override
	public Object getItem(int arg0) {	return info.get(arg0);	}

	@Override
	public long getItemId(int arg0) {	return 0;	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if( convertView == null )
		{
			convertView = inflater.inflate(R.layout.panel_exchange_rates_item_currency, parent, false);
		}

		RecyclingImageView imageView = (RecyclingImageView) convertView.findViewById(R.id.exchangerate_item_currency_image_countryflag);
		TextView textView_country = (TextView) convertView.findViewById(R.id.exchangerate_item_currency_text_country);
		TextView textView_currencyName = (TextView) convertView.findViewById(R.id.exchangerate_item_currency_text_currencyname);

        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
        textView_country.setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
		textView_currencyName.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
		
		MNCurrencyInfo currencyInfo = info.get(position);
		Bitmap flagBitmap = FlagBitmapFactory.getGrayscaledFlagBitmap(context, currencyInfo.usingCountryCode);
		
		flagBitmap = Bitmap.createScaledBitmap(flagBitmap,
				DipToPixel.getPixel(context, 80),
				DipToPixel.getPixel(context, 48),
				true);
		
		//Bitmap grayScaledBitmap = BitmapConverter.getGrayScaledBitmap(flagBitmap);
		
		imageView.setImageDrawable(new RecyclingBitmapDrawable(context.getResources(), flagBitmap));
		
		textView_country.setText( currencyInfo.currencyCode );
		textView_currencyName.setText( currencyInfo.currencyName );

		return convertView;
	}

}
