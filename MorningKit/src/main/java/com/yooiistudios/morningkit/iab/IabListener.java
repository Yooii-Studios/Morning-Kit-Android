package com.yooiistudios.morningkit.iab;

import java.util.Map;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 9.
 *
 * IabListener
 *  In-App-Billing 이벤트 콜백
 */
public interface IabListener {
    void onIabSetupFinished();
    void onIabSetupFailed(String message);

    void onQueryFinished(Map<String, String> googleSkuToPriceMap);
    void onQueryFailed(String message);

    void onIabPurchaseFinished(String googleSku);
    void onIabPurchaseFailed(String message);
//    public void handleOnActivityResult(int requestCode, int resultCode, Intent data); 현재로선 필요없을듯
}
