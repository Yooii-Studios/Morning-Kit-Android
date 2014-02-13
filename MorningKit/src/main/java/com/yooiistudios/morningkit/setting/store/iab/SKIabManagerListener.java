package com.yooiistudios.morningkit.setting.store.iab;

import com.yooiistudios.morningkit.setting.store.util.IabResult;
import com.yooiistudios.morningkit.setting.store.util.Inventory;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 9.
 *
 * SKIabManagerListener
 */
public interface SKIabManagerListener {
    public void onIabSetupFinished(IabResult result);
    public void onIabSetupFailed(IabResult result);

    public void onQueryFinished(Inventory inventory);
    public void onQueryFailed(IabResult result);
//    public void handleOnActivityResult(int requestCode, int resultCode, Intent data); 현재로선 필요없을듯
}
