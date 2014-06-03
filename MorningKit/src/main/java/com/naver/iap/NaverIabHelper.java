package com.naver.iap;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class NaverIabHelper {
//	private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC273wBt+dcVclW1WKmorA511mMgAjcYwzPWZyhSE8VOg7K9ixm/gLH/GdWxbmU2y+kqO7Z/Onqu4+opHJmZ3Si3dn8NWdrJXQvXfZMUaFV0vo27t5SF7lPVglpWi4QsQDCK+dHIFNFJIIMTecXFk4kQFdCdKdh5q2PcZHPOw1c7QIDAQAB";
	
	private static NaverIabHelper instance;
	
	private ArrayList<NaverIabProductInfo> mProductList;
	private String mAppCode;
	private String KEY3 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC273w";
	private String KEY7 = "Bt+dcVclW1WKmorA511mMgAjcYwzPWZyhSE8VOg7K9i";
	private String KEY0 = "xm/gLH/GdWxbmU2y+kqO7Z/Onqu4+opHJm";
	private String KEY2 = "Z3Si3dn8NWdrJXQvXfZMUa";
	private String KEY5 = "FV0vo27t5SF7lPVglpWi4QsQDCK+dHIFNFJIIMTecXFk4k";
	private String KEY31 = "QFdCdKdh5q2PcZHPOw1c7QIDAQAB";
	private String KEY10 = "";
	
	public static NaverIabHelper getInstance(Context context) {
		if (instance == null) {
			instance = new NaverIabHelper(context);
		}
		
		return instance;
	}
	
	public NaverIabHelper(Context context) {
		init(context);
	}
	
	public ArrayList<NaverIabProductInfo> getProductList() {
		return mProductList;
	}
	public String getAppCode() {
		return mAppCode;
	}
	public NaverIabProductInfo getProduct(String key) {
		for (NaverIabProductInfo product : mProductList) {
			if (product.getKey().equals(key)) {
				return product;
			}
		}
		
		return null;
	}
	public NaverIabProductInfo getProductByGoogleSKU(String sku) {
		for (NaverIabProductInfo product : mProductList) {
			if (product.getGoogleSKU().equalsIgnoreCase(sku)) {
				return product;
			}
		}
		
		return null;
	}
	
	private boolean init(Context context){
		InputStream in = null;
		try {
			in = context.getAssets().open("iabinfo_naver.mp3");
			DocumentBuilder builder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in, null);

			in.close();
			in = null;

			mAppCode = doc.getElementsByTagName("app_code").item(0).getChildNodes().item(0).getNodeValue();

			NodeList enabledWords = doc.getElementsByTagName("product");
			int totalEnabledCnt = enabledWords.getLength();
			mProductList = new ArrayList<NaverIabProductInfo>();
			for (int i=0;i<totalEnabledCnt;i++) {
				Element element = (Element)enabledWords.item(i);
				String key = element.getElementsByTagName("key").item(0).getChildNodes().item(0).getNodeValue();
				String googleSKU = element.getElementsByTagName("googleSKU").item(0).getChildNodes().item(0).getNodeValue();
				
				mProductList.add(new NaverIabProductInfo(key, googleSKU));
			}
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

    public java.lang.String _getKey(){ return KEY3 + KEY7 + KEY0 + KEY2 + KEY5 + KEY31 + KEY10; }
}
