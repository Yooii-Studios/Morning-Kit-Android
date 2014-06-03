package com.naver.iap;

import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 6. 3.
 *
 * NaverIabProducts
 *  구글 Sku ID를 받아서 변환하는 클래스
 */
public class NaverIabProductUtils {
    private NaverIabProductUtils() { throw new AssertionError("You MUST not create this class!"); }

    private static final String NAVER_IAB_FULL_VERSION = "1000007647";
    private static final String NAVER_IAB_MORE_ALARM_SLOTS = "1000007649";
    private static final String NAVER_IAB_NO_ADS = "1000007648";
    private static final String NAVER_IAB_PANEL_MATRIX_2X3 = "1000007653";
    private static final String NAVER_IAB_DATE_COUNTDOWN = "1000007651";
    private static final String NAVER_IAB_MEMO = "1000007652";
    private static final String NAVER_IAB_MODERNITY = "1000007650";
    private static final String NAVER_IAB_CELESTIAL = "1000007655";

    public static final Map<String, String> naverSkuMap;

    static {
        naverSkuMap = new HashMap<String, String>();
        naverSkuMap.put(SKIabProducts.SKU_FULL_VERSION, NAVER_IAB_FULL_VERSION);
        naverSkuMap.put(SKIabProducts.SKU_MORE_ALARM_SLOTS, NAVER_IAB_MORE_ALARM_SLOTS);
        naverSkuMap.put(SKIabProducts.SKU_NO_ADS, NAVER_IAB_NO_ADS);
        naverSkuMap.put(SKIabProducts.SKU_PANEL_MATRIX_2X3, NAVER_IAB_PANEL_MATRIX_2X3);
        naverSkuMap.put(SKIabProducts.SKU_DATE_COUNTDOWN, NAVER_IAB_DATE_COUNTDOWN);
        naverSkuMap.put(SKIabProducts.SKU_MEMO, NAVER_IAB_MEMO);
        naverSkuMap.put(SKIabProducts.SKU_MODERNITY, NAVER_IAB_MODERNITY);
        naverSkuMap.put(SKIabProducts.SKU_CELESTIAL, NAVER_IAB_CELESTIAL);
    }
}
