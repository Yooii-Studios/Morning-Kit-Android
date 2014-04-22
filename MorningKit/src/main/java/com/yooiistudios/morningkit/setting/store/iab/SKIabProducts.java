package com.yooiistudios.morningkit.setting.store.iab;

import android.content.Context;
import android.content.SharedPreferences;

import com.yooiistudios.morningkit.common.unlock.MNUnlockActivity;
import com.yooiistudios.morningkit.setting.store.MNStoreDebugChecker;
import com.yooiistudios.morningkit.setting.store.util.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 9.
 *
 * SKIabProducts
 */
public class SKIabProducts {
    public static final String SKU_FULL_VERSION = "full_version_test"; // full_version
    public static final String SKU_MORE_ALARM_SLOTS = "more_alarm_slots_test"; // "functions.more_alarm_slots";
    public static final String SKU_NO_ADS = "no_ads_test"; // functions.no_ads
    public static final String SKU_PANEL_MATRIX_2X3 = "panel_matrix_2_3_test"; // panel_matrix_2_3
    public static final String SKU_DATE_COUNTDOWN = "date_countdown_test"; // "panels.date_countdown";
    public static final String SKU_MEMO = "memo_test"; // "panels.memo";
    public static final String SKU_MODERNITY = "modernity_test"; // "themes.modernity";
    public static final String SKU_CELESTIAL = "celestial_test"; // "themes.celestial";

    private static final String SHARED_PREFERENCES_IAB = "SHARED_PREFERENCES_IAB";
    private static final String SHARED_PREFERENCES_IAB_DEBUG = "SHARED_PREFERENCES_IAB_DEBUG";

    public static List<String> makeProductKeyList() {
        List<String> iabKeyList = new ArrayList<String>();
        iabKeyList.add(SKU_FULL_VERSION);
        iabKeyList.add(SKU_MORE_ALARM_SLOTS);
        iabKeyList.add(SKU_NO_ADS);
        iabKeyList.add(SKU_PANEL_MATRIX_2X3);
        iabKeyList.add(SKU_DATE_COUNTDOWN);
        iabKeyList.add(SKU_MEMO);
        iabKeyList.add(SKU_MODERNITY);
        iabKeyList.add(SKU_CELESTIAL);
        return iabKeyList;
    }

    // 구매완료시 적용
    public static void saveIabProduct(String sku, Context context) {
        SharedPreferences prefs;
        if (MNStoreDebugChecker.isUsingStore(context)) {
            prefs = context.getSharedPreferences(SHARED_PREFERENCES_IAB, Context.MODE_PRIVATE);
        } else {
            prefs = context.getSharedPreferences(SHARED_PREFERENCES_IAB_DEBUG, Context.MODE_PRIVATE);
        }
        prefs.edit().putBoolean(sku, true).commit();
    }

    // 인앱 정보를 읽어오며 자동으로 적용
    public static void saveIabProducts(Inventory inventory, Context context) {
        List<String> ownedSkus = inventory.getAllOwnedSkus();

        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFERENCES_IAB, Context.MODE_PRIVATE).edit();
        edit.clear(); // 모두 삭제 후 다시 추가
        for (String sku : ownedSkus) {
            edit.putBoolean(sku, true);
        }
        edit.commit();
    }

    // 구매된 아이템들을 로드
    public static List<String> loadOwnedIabProducts(Context context) {
        List<String> ownedSkus = new ArrayList<String>();

        SharedPreferences prefs;
        if (MNStoreDebugChecker.isUsingStore(context)) {
            prefs = context.getSharedPreferences(SHARED_PREFERENCES_IAB, Context.MODE_PRIVATE);
        } else {
            prefs = context.getSharedPreferences(SHARED_PREFERENCES_IAB_DEBUG, Context.MODE_PRIVATE);
        }
        if (prefs.getBoolean(SKU_FULL_VERSION, false)) {
            ownedSkus.add(SKU_FULL_VERSION);
            ownedSkus.add(SKU_MORE_ALARM_SLOTS);
            ownedSkus.add(SKU_NO_ADS);
            ownedSkus.add(SKU_PANEL_MATRIX_2X3);
            ownedSkus.add(SKU_DATE_COUNTDOWN);
            ownedSkus.add(SKU_MEMO);
            ownedSkus.add(SKU_MODERNITY);
            ownedSkus.add(SKU_CELESTIAL);
        } else {
            if (prefs.getBoolean(SKU_MORE_ALARM_SLOTS, false)) {
                ownedSkus.add(SKU_MORE_ALARM_SLOTS);
            }
            if (prefs.getBoolean(SKU_NO_ADS, false)) {
                ownedSkus.add(SKU_NO_ADS);
            }
            if (prefs.getBoolean(SKU_PANEL_MATRIX_2X3, false)) {
                ownedSkus.add(SKU_PANEL_MATRIX_2X3);
            }
            if (prefs.getBoolean(SKU_DATE_COUNTDOWN, false)) {
                ownedSkus.add(SKU_DATE_COUNTDOWN);
            }
            if (prefs.getBoolean(SKU_MEMO, false)) {
                ownedSkus.add(SKU_MEMO);
            }
            if (prefs.getBoolean(SKU_MODERNITY, false)) {
                ownedSkus.add(SKU_MODERNITY);
            }
            if (prefs.getBoolean(SKU_CELESTIAL, false)) {
                ownedSkus.add(SKU_CELESTIAL);
            }

            // 추가: 언락화면에서 리뷰로 사용한 아이템도 체크
            SharedPreferences unlockPrefs = context.getSharedPreferences(MNUnlockActivity.SHARED_PREFS, Context.MODE_PRIVATE);
            String reviewUsedProductSku = unlockPrefs.getString(MNUnlockActivity.REVIEW_USED_PRODUCT_SKU, null);
            if (reviewUsedProductSku != null && ownedSkus.indexOf(reviewUsedProductSku) == -1) {
                ownedSkus.add(reviewUsedProductSku);
            }
        }
        return ownedSkus;
    }

    /**
     * For Debug mode
     */
    // 구매완료시 적용
    public static void saveIabProductDebug(String sku, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_IAB_DEBUG, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(sku, true).commit();
    }

    public static List<String> loadOwnedIabProductsDebug(Context context) {
        // 구매된 아이템들을 로드
        List<String> ownedSkus = new ArrayList<String>();

        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_IAB_DEBUG,
                Context.MODE_PRIVATE);

        if (prefs.getBoolean(SKU_FULL_VERSION, false)) {
            ownedSkus.add(SKU_FULL_VERSION);
            ownedSkus.add(SKU_MORE_ALARM_SLOTS);
            ownedSkus.add(SKU_NO_ADS);
            ownedSkus.add(SKU_PANEL_MATRIX_2X3);
            ownedSkus.add(SKU_DATE_COUNTDOWN);
            ownedSkus.add(SKU_MEMO);
            ownedSkus.add(SKU_MODERNITY);
            ownedSkus.add(SKU_CELESTIAL);
        } else {
            if (prefs.getBoolean(SKU_MORE_ALARM_SLOTS, false)) {
                ownedSkus.add(SKU_MORE_ALARM_SLOTS);
            }
            if (prefs.getBoolean(SKU_NO_ADS, false)) {
                ownedSkus.add(SKU_NO_ADS);
            }
            if (prefs.getBoolean(SKU_PANEL_MATRIX_2X3, false)) {
                ownedSkus.add(SKU_PANEL_MATRIX_2X3);
            }
            if (prefs.getBoolean(SKU_DATE_COUNTDOWN, false)) {
                ownedSkus.add(SKU_DATE_COUNTDOWN);
            }
            if (prefs.getBoolean(SKU_MEMO, false)) {
                ownedSkus.add(SKU_MEMO);
            }
            if (prefs.getBoolean(SKU_MODERNITY, false)) {
                ownedSkus.add(SKU_MODERNITY);
            }
            if (prefs.getBoolean(SKU_CELESTIAL, false)) {
                ownedSkus.add(SKU_CELESTIAL);
            }
            // 추가: 언락화면에서 리뷰로 사용한 아이템도 체크
            SharedPreferences unlockPrefs = context.getSharedPreferences(MNUnlockActivity.SHARED_PREFS, Context.MODE_PRIVATE);
            String reviewUsedProductSku = unlockPrefs.getString(MNUnlockActivity.REVIEW_USED_PRODUCT_SKU, null);
            if (reviewUsedProductSku != null && ownedSkus.indexOf(reviewUsedProductSku) == -1) {
                ownedSkus.add(reviewUsedProductSku);
            }
        }
        return ownedSkus;
    }

    public static void resetIabProductsDebug(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFERENCES_IAB_DEBUG,
                Context.MODE_PRIVATE).edit();
        edit.clear().commit();
    }
}
