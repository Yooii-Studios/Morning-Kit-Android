package com.yooiistudios.morningkit.setting.store.iab;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.yooiistudios.morningkit.common.encryption.MNMd5Utils;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.setting.store.util.IabHelper;
import com.yooiistudios.morningkit.setting.store.util.IabResult;
import com.yooiistudios.morningkit.setting.store.util.Inventory;

import java.util.List;

import lombok.Getter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 9.
 *
 * SKIabManager
 */
public class SKIabManager {
    public static final int IAB_REQUEST_CODE = 10002;
//    public static final String DEVELOPER_PAYLOAD= "SKIabManager_Payload";
    private static final String TAG = "SKIabManager";
    private SKIabManagerListener iapManagerListener;
    @Getter private IabHelper helper;
    private ActionBarActivity activity;
    private String base64EncodedPublicKey;

    private SKIabManager() {}
    public SKIabManager(ActionBarActivity activity, SKIabManagerListener iapManagerListener) {
        this.activity = activity;
        this.iapManagerListener = iapManagerListener;
        this.base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh2yCTQXMk/33q3PzCmCwlpmZ+0gh3Ya09dQdw01T6izPnTpbx/Ab66Wgt+ilCuqMik2LWd7go9pCGnYtB8h8pBxE/7r++cF7krgATB52u4jsqXd91WntfPjvEe8wxCGRoOf76h2CV+hCPfnj+qJ1ugMRJTn9IwmAvrV9i2qni2vCHSDYjVBOc35u0vl8/vUMip4QUmppmSmJL53qMKx9j/1pBM2pumI7sqC2+85smYTXbgbCjW3BZLH2RQhTl0WkXEP6hIHt+8AHNZyb7e0UgcITqs8d3lgoiZjMXo0HgHnEn9PeoTn1aMQYq3dFjgvDiwyq/cSgXfVel4nQAWV/swIDAQAB";
    }

    public void loadWithAllItems() {
        load(false);
    }

    public void loadWithOnlyOwnedItems() {
        load(true);
    }

    private void load(final boolean isOwnItemsOnly) {
        // compute your public key and store it in base64EncodedPublicKey
        helper = new IabHelper(activity, base64EncodedPublicKey);
//        helper.enableDebugLogging(true); // You shoud off this when you publish

        helper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    MNLog.e(TAG, "Problem setting up In-app Billing: " + result);
                    iapManagerListener.onIabSetupFailed(result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (helper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                iapManagerListener.onIabSetupFinished(result);

                if (isOwnItemsOnly) {
                    queryOwnItemsInformation();
                } else {
                    queryAllItemsInformation();
                }
            }
        });
    }

    private void queryAllItemsInformation() {
        List<String> iabProductsSkuList = SKIabProducts.makeProductKeyList();
        helper.queryInventoryAsync(true, iabProductsSkuList, new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                // Have we been disposed of in the meantime? If so, quit.
                if (helper == null) return;

                // Is it a failure?
                if (result.isFailure()) {
                    iapManagerListener.onQueryFailed(result);
                } else {
                    SKIabProducts.saveIabProducts(inv, activity); // 구매한 상품은 저장
                    iapManagerListener.onQueryFinished(inv);
                }
            }
        });
    }

    private void queryOwnItemsInformation() {
        helper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                // Have we been disposed of in the meantime? If so, quit.
                if (helper == null) return;

                // Is it a failure?
                if (result.isFailure()) {
                    iapManagerListener.onQueryFailed(result);
                } else {
                    iapManagerListener.onQueryFinished(inv);
                }
            }
        });
    }

    public void dispose() {
        if (helper != null) {
            helper.dispose();
            helper = null;
        }
    }

    public void processPurchase(String sku, IabHelper.OnIabPurchaseFinishedListener onIabPurchaseFinishedListener) {
        try {
            // 페이로드를 특정 스트링으로 했었는데, 창하님의 조언으로는 sku의 md5 값과 맞추는 것이 그나마 해킹 확률이 줄어 들 것이라고 말하심
            helper.launchPurchaseFlow(activity, sku, IabHelper.ITEM_TYPE_INAPP, IAB_REQUEST_CODE, onIabPurchaseFinishedListener, MNMd5Utils.getMd5String(sku));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
