package com.yooiistudios.morningkit.iab;

import android.app.Activity;
import android.content.Intent;

import com.naver.android.appstore.iap.InvalidProduct;
import com.naver.android.appstore.iap.NIAPHelper;
import com.naver.android.appstore.iap.NIAPHelperErrorType;
import com.naver.android.appstore.iap.Product;
import com.naver.android.appstore.iap.Purchase;
import com.yooiistudios.morningkit.common.encryption.MNMd5Utils;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 9.
 *
 * SKIabManager
 *  In-App-Billing(Naver) 과 관련된 로직을 래핑한 클래스
 */
@Accessors(prefix = "m")
public class NaverIabManager extends IabManager {
    public static final int IAB_REQUEST_CODE = 10003;
    private static final String TAG = NaverIabManager.class.getSimpleName();
    private IabListener mIapManagerListener;
    @Getter
    private NIAPHelper mHelper = null;
    private Activity mActivity;

    private Map<String, String> mPrices;

    private NIAPHelperErrorType mInitializeErrorType = null;

    private boolean mProductDetailsLoaded = false;
    private boolean mPurchaseInfoLoadFinished = false;
    private boolean mFailedDuringQuery = false;

    private NaverIabManager() {}
    public NaverIabManager(Activity activity, IabListener iapManagerListener) {
        mActivity = activity;
        mIapManagerListener = iapManagerListener;
    }

    @Override
    public void setup() {
        // compute your public key and store it in mBase64EncodedPublicKey
        mHelper = new NIAPHelper(mActivity.getApplicationContext(), NIAPUtils.NIAP_PUBLIC_KEY);
        mHelper.initialize(new NIAPHelper.OnInitializeFinishedListener() {
            @Override
            public void onSuccess() {
                // 리스너 호출 시점에 액티비티가 종료되었을 경우 리스너도 종료
                if (isHelperDisposed()) {
                    return;
                }
                if (mIapManagerListener != null) {
                    mIapManagerListener.onIabSetupFinished();
                }
                queryProductsInfo();
                queryPurchases();
            }

            @Override
            public void onFail(NIAPHelperErrorType errorType) {
                if (isHelperDisposed()) {
                    return;
                }
                if (mIapManagerListener != null) {
                    mIapManagerListener.onIabSetupFailed(errorType.getErrorDetails());
                }

                mInitializeErrorType = errorType;
                if (isAppstoreUnavailable()) {
                    mHelper.updateOrInstallAppstore(mActivity);
                }
            }
        });
    }

    private boolean isAppstoreUnavailable() {
        return mInitializeErrorType == NIAPHelperErrorType.NEED_INSTALL_OR_UPDATE_APPSTORE;
    }

    private void queryProductsInfo() {
        // 전체 아이템의 정보를 불러옴
        mHelper.getProductDetailsAsync(SKIabProducts.makeProductKeyList(), new NIAPHelper.GetProductDetailsListener() {
            @Override
            public void onSuccess(List<Product> products, List<InvalidProduct> invalidProducts) {
                if (isHelperDisposed()) {
                    return;
                }

                try {
                    mPrices = getPrices(products);

                    // 구매 목록 콜백이 이미 진행이 되었으면 완료 후 바로 업데이트
                    mProductDetailsLoaded = true;
                    checkAllQuerySucceed();
                } catch (IabDetailNotFoundException e) {
                    configOnQueryFailed(e.getMessage());
                }
            }

            @Override
            public void onFail(NIAPHelperErrorType niapHelperErrorType) {
                if (isHelperDisposed()) {
                    return;
                }
                configOnQueryFailed(niapHelperErrorType.getErrorDetails());
            }
        });
    }

    private void queryPurchases() {
        // 구매 목록 요청
        mHelper.getPurchasesAsync(new NIAPHelper.GetPurchasesListener() {
            @Override
            public void onSuccess(List<Purchase> purchases) {
                if (isHelperDisposed()) {
                    return;
                }
                // 구매 목록 저장
                SKIabProducts.saveIabProducts(mActivity.getApplicationContext(), purchases);

                // 전체 목록 콜백이 이미 진행이 되었으면 완료 후 바로 업데이트
                mPurchaseInfoLoadFinished = true;
                checkAllQuerySucceed();
            }

            @Override
            public void onFail(NIAPHelperErrorType niapHelperErrorType) {
                if (isHelperDisposed()) {
                    return;
                }
                if (niapHelperErrorType.equals(NIAPHelperErrorType.USER_NOT_LOGGED_IN)) {
                    mPurchaseInfoLoadFinished = true;
                    checkAllQuerySucceed();
                } else {
                    configOnQueryFailed(niapHelperErrorType.getErrorDetails());
                }
            }
        });
    }

    private void checkAllQuerySucceed() {
        if (isQueryDone() && mIapManagerListener != null) {
            mIapManagerListener.onQueryFinished(mPrices);
        }
    }

    private Map<String, String> getPrices(List<Product> products) throws IabDetailNotFoundException {
        List<String> productCodes = SKIabProducts.makeProductKeyList();
        Map<String, String> prices = new HashMap<>();

        // build map first.
        for (Product product : products) {
            String googleSku = NIAPUtils.convertToGoogleSku(product.getProductCode());
            prices.put(googleSku, String.valueOf(product.getProductPrice()));
        }

        // then check the map built contains all product codes.
        for (String productCode : productCodes) {
            if (!prices.containsKey(NIAPUtils.convertToGoogleSku(productCode))) {
                throw new IabDetailNotFoundException();
            }
        }

        return prices;
    }

    private void configOnQueryFailed(String message) {
        if (!mFailedDuringQuery) {
            mFailedDuringQuery = true;
            if (mIapManagerListener != null) {
                mIapManagerListener.onQueryFailed(message);
            }
        }
    }

    private boolean isQueryDone() {
        return mProductDetailsLoaded && mPurchaseInfoLoadFinished;
    }

    public void dispose() {
        if (mHelper != null) {
            mHelper.terminate();
            mHelper = null;
        }
    }

    @Override
    public void purchase(String googleSku) {
        if (isAppstoreUnavailable()) {
            if (mIapManagerListener != null) {
                mIapManagerListener.onIabPurchaseFailed(mInitializeErrorType.getErrorDetails());
            }
            mHelper.updateOrInstallAppstore(mActivity);
            return;
        }

        String productCode = NIAPUtils.convertToNaverSku(googleSku);
        // 페이로드를 특정 스트링으로 했었는데, 창하님의 조언으로는 sku 의 md5 값과 맞추는 것이 그나마 해킹 확률이 줄어 들 것이라고 말하심
        mHelper.requestPayment(mActivity, productCode, MNMd5Utils.getMd5String(productCode), IAB_REQUEST_CODE,
                new NIAPHelper.RequestPaymentListener() {
                    @Override
                    public void onSuccess(Purchase purchase) {
                        String productCode = purchase.getProductCode();

                        boolean hasValidMd5 = purchase.getDeveloperPayload().equals(
                                MNMd5Utils.getMd5String(productCode));
                        if (hasValidMd5) {
                            String googleSku = NIAPUtils.convertToGoogleSku(productCode);

                            SKIabProducts.saveIabProduct(googleSku, mActivity.getApplicationContext());
                            if (mIapManagerListener != null) {
                                mIapManagerListener.onIabPurchaseFinished(googleSku);
                            }
                        } else {
                            if (mIapManagerListener != null) {
                                mIapManagerListener.onIabPurchaseFailed("Payload problem");
                            }
                        }
                    }

                    @Override
                    public void onFail(NIAPHelperErrorType niapHelperErrorType) {
                        if (mIapManagerListener != null) {
                            mIapManagerListener.onIabPurchaseFailed(niapHelperErrorType.getErrorDetails());
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (mIapManagerListener != null) {
                            mIapManagerListener.onIabPurchaseFailed("User cancelled.");
                        }
                    }
                });
    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return mHelper.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean isHelperDisposed() {
        return mHelper == null;
    }
}
