package com.yooiistudios.morningkit.panel.weather.model.locationinfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.stevenkim.waterlily.bitmapfun.util.RecyclingBitmapDrawable;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.exchangerates.model.FlagBitmapFactory;
import com.yooiistudios.morningkit.panel.weather.model.countrycode.CountryCodeInverter;
import com.yooiistudios.morningkit.panel.weather.model.countrycode.USStateCodeInverter;

import java.util.ArrayList;
import java.util.List;

public class MNWeatherLocationInfoAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;

	private List<MNWeatherLocationInfo> locationInfoList;

	public MNWeatherLocationInfoAdapter(Context context) {
		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.locationInfoList = new ArrayList<MNWeatherLocationInfo>();
	}

	public void clear()	{ locationInfoList.clear(); }
    public void setLocationInfoList(List<MNWeatherLocationInfo> locationInfoList) {
        this.locationInfoList = locationInfoList;
    }

	@Override
	public int getCount() {	return locationInfoList.size();	}

	@Override
	public MNWeatherLocationInfo getItem(int position) {
		try {
			return locationInfoList.get(position);
		} catch( IndexOutOfBoundsException e ) {
			return null;
		}
	}

	@Override
	public long getItemId(int position) { return position;	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.panel_weather_detail_list_item, parent, false);
            convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		
		if( position >= locationInfoList.size()) {
            convertView.setVisibility(View.INVISIBLE);
			return convertView;
		} else {
            convertView.setVisibility(View.VISIBLE);
		}

        LinearLayout containerLayout = (LinearLayout) convertView.findViewById(R.id.panel_weather_detail_list_item_container);
        RecyclingImageView flagImageView = (RecyclingImageView) convertView.findViewById(R.id.panel_weather_detail_list_item_image_view);
        TextView cityNameTextView = (TextView) convertView.findViewById(R.id.panel_weather_detail_list_item_city_text_view);
        TextView countryNameTextView = (TextView) convertView.findViewById(R.id.panel_weather_detail_list_item_country_text_view);

        // flag image view
        MNWeatherLocationInfo weatherLocationInfo = getItem(position);
        Bitmap countryFlagBitmap = FlagBitmapFactory.getGrayscaledFlagBitmap(context, weatherLocationInfo.countryCode);
        flagImageView.setImageDrawable(new RecyclingBitmapDrawable(context.getResources(), countryFlagBitmap));
        flagImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String city = weatherLocationInfo.name;
        city = city.substring(0, 1).toUpperCase()+city.substring(1);
        cityNameTextView.setText(city);

        String countryNameString;
        if (weatherLocationInfo.countryCode.compareTo("US") == 0) {
            // ex)New Jersey/United States
            countryNameString = USStateCodeInverter.getStateNameOfCode(context, weatherLocationInfo.regionCode)
                    + "/" + CountryCodeInverter.getCountryNameOfCode(context, weatherLocationInfo.countryCode);
        } else {
            // ex)Korea
            countryNameString = CountryCodeInverter.getCountryNameOfCode(context, weatherLocationInfo.countryCode);
        }
        countryNameTextView.setText(countryNameString);

		return convertView;
	}

}