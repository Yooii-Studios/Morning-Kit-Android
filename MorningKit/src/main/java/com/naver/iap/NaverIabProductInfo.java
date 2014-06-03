package com.naver.iap;

public class NaverIabProductInfo {
	private String mKey;
	private String mGoogleSKU;
	
	public NaverIabProductInfo(String key, String googleSKU) {
		mKey = key;
		mGoogleSKU = googleSKU;
	}
	
	public String getKey() {
		return mKey;
	}
	
	public String getGoogleSKU() {
		return mGoogleSKU;
	}
}
