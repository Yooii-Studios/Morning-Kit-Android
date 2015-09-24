package com.yooiistudios.morningkit.iab;

import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 6. 3.
 *
 * NaverIabProducts
 *  구글 Sku ID를 받아서 변환하는 클래스
 */
public class NIAPUtils {
    private NIAPUtils() { throw new AssertionError("You MUST not create this class!"); }

    public static final String NIAP_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC273wBt+dcVclW1WKmorA511mMgAjcYwzPWZyhSE8VOg7K9ixm/gLH/GdWxbmU2y+kqO7Z/Onqu4+opHJmZ3Si3dn8NWdrJXQvXfZMUaFV0vo27t5SF7lPVglpWi4QsQDCK+dHIFNFJIIMTecXFk4kQFdCdKdh5q2PcZHPOw1c7QIDAQAB";

    private static final String NAVER_IAB_FULL_VERSION = "1000007647";
    private static final String NAVER_IAB_MORE_ALARM_SLOTS = "1000007649";
    private static final String NAVER_IAB_NO_ADS = "1000007648";
    private static final String NAVER_IAB_PANEL_MATRIX_2X3 = "1000007653";
    private static final String NAVER_IAB_DATE_COUNTDOWN = "1000007651";
    private static final String NAVER_IAB_MEMO = "1000007652";
    private static final String NAVER_IAB_PHOTO_FRAME = "1000007725";
    private static final String NAVER_IAB_MODERNITY = "1000007650";
    private static final String NAVER_IAB_CELESTIAL = "1000007655";

    private static final Map<String, String> naverSkuMap;
    private static final Map<String, String> googleSkuMap;

    static {
        naverSkuMap = new HashMap<>();
        naverSkuMap.put(SKIabProducts.SKU_FULL_VERSION, NAVER_IAB_FULL_VERSION);
        naverSkuMap.put(SKIabProducts.SKU_MORE_ALARM_SLOTS, NAVER_IAB_MORE_ALARM_SLOTS);
        naverSkuMap.put(SKIabProducts.SKU_NO_ADS, NAVER_IAB_NO_ADS);
        naverSkuMap.put(SKIabProducts.SKU_PANEL_MATRIX_2X3, NAVER_IAB_PANEL_MATRIX_2X3);
        naverSkuMap.put(SKIabProducts.SKU_DATE_COUNTDOWN, NAVER_IAB_DATE_COUNTDOWN);
        naverSkuMap.put(SKIabProducts.SKU_MEMO, NAVER_IAB_MEMO);
        naverSkuMap.put(SKIabProducts.SKU_PHOTO_FRAME, NAVER_IAB_PHOTO_FRAME);
        naverSkuMap.put(SKIabProducts.SKU_MODERNITY, NAVER_IAB_MODERNITY);
        naverSkuMap.put(SKIabProducts.SKU_CELESTIAL, NAVER_IAB_CELESTIAL);

        googleSkuMap = new HashMap<>();
        googleSkuMap.put(NAVER_IAB_FULL_VERSION, SKIabProducts.SKU_FULL_VERSION);
        googleSkuMap.put(NAVER_IAB_MORE_ALARM_SLOTS, SKIabProducts.SKU_MORE_ALARM_SLOTS);
        googleSkuMap.put(NAVER_IAB_NO_ADS, SKIabProducts.SKU_NO_ADS);
        googleSkuMap.put(NAVER_IAB_PANEL_MATRIX_2X3, SKIabProducts.SKU_PANEL_MATRIX_2X3);
        googleSkuMap.put(NAVER_IAB_DATE_COUNTDOWN, SKIabProducts.SKU_DATE_COUNTDOWN);
        googleSkuMap.put(NAVER_IAB_MEMO, SKIabProducts.SKU_MEMO);
        googleSkuMap.put(NAVER_IAB_PHOTO_FRAME, SKIabProducts.SKU_PHOTO_FRAME);
        googleSkuMap.put(NAVER_IAB_MODERNITY, SKIabProducts.SKU_MODERNITY);
        googleSkuMap.put(NAVER_IAB_CELESTIAL, SKIabProducts.SKU_CELESTIAL);
    }

    public static String convertToGoogleSku(String naverSku) {
        return googleSkuMap.get(naverSku);
    }

    public static String convertToNaverSku(String googleSku) {
        return naverSkuMap.get(googleSku);
    }

    public static ArrayList<String> getAllProducts() {
        // 구글맵의 네이버 프로덕트 id 들
        return new ArrayList<>(googleSkuMap.keySet());
    }
}
