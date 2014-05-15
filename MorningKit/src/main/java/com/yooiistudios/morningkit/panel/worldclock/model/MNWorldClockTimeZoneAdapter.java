package com.yooiistudios.morningkit.panel.worldclock.model;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;

import java.util.ArrayList;

public class MNWorldClockTimeZoneAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;

	private ArrayList<MNTimeZone> timeZones;

	public MNWorldClockTimeZoneAdapter(Context _context) {
		context = _context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		timeZones = new ArrayList<MNTimeZone>();
	}

	public void clear()	{ timeZones.clear(); }

    public void setTimeZones(ArrayList<MNTimeZone> timeZones) {
        this.timeZones = timeZones;
    }

    public void addTimeZone(MNTimeZone t) {
		timeZones.add(t);

		/*
		Collections.sort(m_List, new Comparator<TimeZoneCity>() {
			@Override
			public int compare(TimeZoneCity lhs,
					TimeZoneCity rhs) {
				return lhs.getPriority()-rhs.getPriority();
			}
		});
		 */
	}

	@Override
	public int getCount() {	return timeZones.size(); }

	@Override
	public MNTimeZone getItem(int position) {
		try {
			return timeZones.get(position);
		} catch( IndexOutOfBoundsException e ) {
			return null;
		}
	}

	@Override
	public long getItemId(int position) { return position;	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.panel_world_clock_listitem, parent, false);
            convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		
		if( position >= timeZones.size()) {
//			convertView.setAlpha(0);
            convertView.setVisibility(View.INVISIBLE);
			return convertView;
		} else {
//			convertView.setAlpha(1);
            convertView.setVisibility(View.VISIBLE);
		}

		TextView cityNameTextView = (TextView) convertView.findViewById(R.id.panel_detail_world_clock_list_item_city_text);

		String cityName;
		if (timeZones.get(position).getSearchedLocalizedName() != null) {
			cityName = timeZones.get(position).getSearchedLocalizedName();
		} else {
			cityName = timeZones.get(position).getName();
		}

		cityName = cityName.substring(0, 1).toUpperCase() + cityName.substring(1);
		cityNameTextView.setText(cityName);

		TextView timeZoneNameTextView = (TextView) convertView.findViewById(R.id.panel_detail_world_clock_list_item_timezone_text);
		timeZoneNameTextView.setText(timeZones.get(position).getTimeZoneName());
//		timezoneView.setTextColor(GeneralSetting.getModalSubFontColor());

        // Theme
//        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
//        cityNameTextView.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
//        timeZoneNameTextView.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));

		return convertView;
	}

}