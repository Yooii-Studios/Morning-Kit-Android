package com.yooiistudios.morningkit.iab;

import android.app.Activity;
import android.content.Intent;

import com.yooiistudios.morningkit.common.encryption.MNMd5Utils;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.store.util.IabHelper;
import com.yooiistudios.morningkit.setting.store.util.IabResult;
import com.yooiistudios.morningkit.setting.store.util.Inventory;
import com.yooiistudios.morningkit.setting.store.util.Purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by Wooseong Kim in Swish from Yooii Studios Co., LTD. on 2014. 1. 9.
 *
 * GoogleIabManager
 *  In-App-Billing(Google) 과 관련된 로직을 래핑한 클래스
 */
@Accessors(prefix = "m")
public class GoogleIabManager extends IabManager {
    public static final int IAB_REQUEST_CODE = 10002;
    private IabListener mIapManagerListener;
    @Getter
    private IabHelper mHelper;
    private Activity mActivity;

    // new
    private static final String piece1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCMT4wCvf12zdFgKCAQEAncxiSbSoj5IbR5GE1f0StVju0/nqRwPZAXdgeGR2y4AzJR73/32grzhG";
    private static final String piece2 = "/qU+Gq7S5rsdDAzljeoZnmBbQn+bn5CrE7TxBIZnKztdffmfj1z8dxH+oNKUzCXSo8mfmYaukNx8vitEohAXIRfQkR/jB5NjCyowGpqqu0C4uk2mPCb0E6n0W";
    private static final String piece3 = "+xSKXOTfMiBxa+AJ7qxEn+Fkt5EwNqED2EpW5+OzQ7NUZTv2/LMvgb7A19fjaLezwo3NZRsxmV8EESV9SZfStkq+3YRtoow1UBcy+";
    private static final String piece4 = "IHWG3fNsDfZmukmXtajwtzt6+oNjiiv44k1jFZH6eXzYacEjghmSWEfzcuFSIagbLFYhpCZsEMlTTWg4bSF1WmagSCMCKppQIDAQAB";
    private static final String key;

    static {
        key = piece1.replaceAll("MT4wCvf12zdF", "")
                + piece2.replaceAll("Kztdffmfj1z8d", "")
                + piece3.replaceAll("A19fjaLezwo3", "")
                + piece4.replaceAll("44k1jFZ", "");
    }

    private GoogleIabManager() {}
    public GoogleIabManager(Activity activity, IabListener iapManagerListener) {
        mActivity = activity;
        mIapManagerListener = iapManagerListener;
    }

    @Override
    public void setup() {
        // compute your public key and store it in mBase64EncodedPublicKey
        mHelper = new IabHelper(mActivity, key);
        mHelper.enableDebugLogging(true); // You should off this when you publish

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    mIapManagerListener.onIabSetupFailed(result.getMessage());
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (isHelperDisposed()) {
                    return;
                }

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                mIapManagerListener.onIabSetupFinished();

                queryAllItemsInformation();
            }
        });
    }

    private void queryAllItemsInformation() {
        mHelper.queryInventoryAsync(true, SKIabProducts.makeProductKeyList(),
                new IabHelper.QueryInventoryFinishedListener() {
                    @Override
                    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                        // Have we been disposed of in the meantime? If so, quit.
                        if (isHelperDisposed()) {
                            return;
                        }

                        if (result.isSuccess()) {
                            try {
                                SKIabProducts.saveIabProducts(inv, mActivity); // 구매한 상품은 저장

                                Map<String, String> prices = getPrices(inv);
                                mIapManagerListener.onQueryFinished(prices);
                            } catch (IabDetailNotFoundException e) {
                                mIapManagerListener.onQueryFailed(e.getMessage());
                            }
                        } else {
                            mIapManagerListener.onQueryFailed(result.getMessage());
                        }
                    }
                });
    }

    private Map<String, String> getPrices(Inventory inv) throws IabDetailNotFoundException {
        List<String> iabProductsSkuList = SKIabProducts.makeProductKeyList();
        Map<String, String> prices = new HashMap<>();

        for (String sku : iabProductsSkuList) {
            if (!inv.hasDetails(sku)) {
                throw new IabDetailNotFoundException();
            }
            prices.put(sku, inv.getSkuDetails(sku).getPrice());
        }

        return prices;
    }

//    private void queryOwnItemsInformation() {
//        mHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
//            @Override
//            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
//                // Have we been disposed of in the meantime? If so, quit.
//                if (mHelper == null) return;
//
//                // Is it a failure?
//                if (result.isFailure()) {
//                    mIapManagerListener.onQueryFailed(result);
//                } else {
//                    mIapManagerListener.onQueryFinished(inv);
//                }
//            }
//        });
//    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return mHelper.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void dispose() {
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }

    @Override
    public void purchase(final String sku) {
        try {
            // 페이로드를 특정 스트링으로 했었는데, 창하님의 조언으로는 sku 의 md5 값과 맞추는 것이 그나마 해킹 확률이 줄어 들 것이라고 말하심
            mHelper.launchPurchaseFlow(mActivity, sku, IabHelper.ITEM_TYPE_INAPP, IAB_REQUEST_CODE,
                    new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase info) {
                            boolean hasPurchaseInfo = info != null;
                            boolean hasValidMd5 = hasPurchaseInfo &&
                                    info.getDeveloperPayload().equals(MNMd5Utils.getMd5String(sku));
                            boolean isSuccess = result.isSuccess() && hasPurchaseInfo && hasValidMd5;
                            if (isSuccess) {
                                SKIabProducts.saveIabProduct(info.getSku(), mActivity.getApplicationContext());
                                mIapManagerListener.onIabPurchaseFinished(info.getSku());
                            } else {
                                String message;
                                if (!hasPurchaseInfo) {
                                    message = "No purchase info";
                                } else {
                                    message = "Payload problem";
                                }
                                mIapManagerListener.onIabPurchaseFailed(message);
                            }
                        }
                    }
                    , MNMd5Utils.getMd5String(sku));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isHelperDisposed() {
        return mHelper == null;
    }
}
