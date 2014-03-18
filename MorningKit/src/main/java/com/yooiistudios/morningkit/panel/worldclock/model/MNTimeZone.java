package com.yooiistudios.morningkit.panel.worldclock.model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MNTimeZone implements Serializable {
	String m_Name;
	String m_SearchedLocalizedName;
	String m_TimeZoneName;
	
	ArrayList<String> m_localizedNames = null;
	int m_Offset_Hour;
	int m_Offset_Min;
	
	public static final String TAG = "TimeZoneCity";
	
	// priorty 추가
	int m_priority;
	
	@SuppressWarnings("unchecked")
	public MNTimeZone(String name, String timeZoneName, int hour, int min, int priority,
                      ArrayList<String> localizedNames) {
		m_Name = name;
		m_TimeZoneName = timeZoneName;
		m_Offset_Hour = hour;
		m_Offset_Min = min;
		m_priority = priority;
		if (localizedNames != null) {
			m_localizedNames = new ArrayList<String>(0);

			for (String string : localizedNames) {
				m_localizedNames.add(string);
			}
		}
		m_SearchedLocalizedName = null;
	}
	
	public MNTimeZone() {
		m_Name = "";
		m_TimeZoneName = "";
		m_Offset_Hour = 0;
		m_Offset_Min = 0;
		m_priority = 100;
		m_localizedNames = null;
		m_SearchedLocalizedName = null;
	}
	
	public String getName() {	return m_Name;	}
	public void setName(String name) {	m_Name = name; }
	
	public String getTimeZoneName() {	return m_TimeZoneName;	}
	public void setTimeZoneName(String timeZoneName) {	m_TimeZoneName = timeZoneName; } 
	
	public int getOffsetHour() {	return m_Offset_Hour;	}
	public int getOffsetMin()  {	return m_Offset_Min;	}
	
	public int getPriority()   {	return m_priority;		}
	public void setPriority(int p)	{	m_priority = p;	}
	
	public ArrayList<String> getLocalizedNames() {	return m_localizedNames; }
	public void setLocalizedNames(ArrayList<String> m_localizedNames) {	this.m_localizedNames = m_localizedNames; }
	
	public String getSearchedLocalizedName() { return m_SearchedLocalizedName; }
	public void setSearchedLocalizedName(String searchedLocalizedName) { this.m_SearchedLocalizedName = searchedLocalizedName; }
}
