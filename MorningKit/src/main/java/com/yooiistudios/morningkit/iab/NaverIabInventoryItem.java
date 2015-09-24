package com.yooiistudios.morningkit.iab;

import android.os.Parcel;
import android.os.Parcelable;

public class NaverIabInventoryItem implements Parcelable {
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		@Override
		public NaverIabInventoryItem createFromParcel(Parcel in) {
			return new NaverIabInventoryItem(in);
		}

		@Override
		public NaverIabInventoryItem[] newArray(int size) {
			return new NaverIabInventoryItem[size];
		}
	};
	
	private String mKey;
	private String mPrice;
	private boolean mIsAvaiable;
	
	private NaverIabInventoryItem(Parcel in) {
		readFromParcel(in);
	}

	private void readFromParcel(Parcel in){
		mKey = in.readString();
		mPrice = in.readString();
		mIsAvaiable = in.readInt() == 1 ? true : false;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mKey);
		dest.writeString(mPrice);
		dest.writeInt(mIsAvaiable ? 1 : 0);
	}
	
	public NaverIabInventoryItem(String key, String price) {
		mKey = key;
		mPrice = price;
		mIsAvaiable = false;
	}
	
	public String getKey() {
		return mKey;
	}
	
	public String getPrice() {
		return mPrice;
	}

	public boolean isAvailable() {
		return mIsAvaiable;
	}
	public void setIsAvailable(boolean available) {
		mIsAvaiable = available;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
