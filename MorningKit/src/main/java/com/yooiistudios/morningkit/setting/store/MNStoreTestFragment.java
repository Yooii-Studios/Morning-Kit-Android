package com.yooiistudios.morningkit.setting.store;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.MNSettingActivity;
import com.yooiistudios.morningkit.setting.store.iab.SKIabManager;
import com.yooiistudios.morningkit.setting.store.iab.SKIabManagerListener;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.store.util.IabHelper;
import com.yooiistudios.morningkit.setting.store.util.IabResult;
import com.yooiistudios.morningkit.setting.store.util.Inventory;
import com.yooiistudios.morningkit.setting.store.util.Purchase;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import lombok.Getter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 6.
 *
 * MNStoreFragment
 *  세팅 - 스토어 프래그먼트
 */
public class MNStoreTestFragment extends Fragment implements SKIabManagerListener, IabHelper.OnIabPurchaseFinishedListener {
    private static final String TAG = "MNStoreFragment";
    @Getter private SKIabManager iabManager;
    MNSettingActivity activity;

    @InjectView(R.id.price_full_version) TextView fullVersionTextView;
    @InjectView(R.id.button_full_version) Button fullVersionButton;

    @InjectView(R.id.price_more_alarm_slots) TextView moreAlarmSlotsTextView;
    @InjectView(R.id.button_more_alarm_slots) Button moreAlarmSlotsButton;

    @InjectView(R.id.price_no_ads) TextView noAdsTextView;
    @InjectView(R.id.button_no_ads) Button noAdsButton;

    @InjectView(R.id.price_date_countdown) TextView dateCountdownTextView;
    @InjectView(R.id.button_date_countdown) Button dateCountdownButton;

    @InjectView(R.id.price_memo) TextView memoTextView;
    @InjectView(R.id.button_memo) Button memoButton;

    @InjectView(R.id.price_modernity) TextView modernityTextView;
    @InjectView(R.id.button_modernity) Button modernityButton;

    @InjectView(R.id.price_celestial) TextView celestialTextView;
    @InjectView(R.id.button_celestial) Button celestialButton;

    @InjectView(R.id.setting_store_progressBar_test) ProgressBar progressBar;

    public MNStoreTestFragment(MNSettingActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_store_fragment_test, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);
        }

        // You have uncomment this when you want to test this fragment
        // iap init
//        iabManager = new SKIabManager(activity, this);
//        iabManager.loadWithAllItems();
//        activity.setIabHelper(iabManager.getHelper());

        progressBar.setVisibility(ProgressBar.VISIBLE);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (iabManager != null) {
            iabManager.dispose();
        }
    }

    /**
     * IAP Listener
     */
    @Override
    public void onIabSetupFinished(IabResult result) {
//        progressBar.setVisibility(ProgressBar.INVISIBLE);
//        Log.i(TAG, "onIabSetupFinished");
    }

    @Override
    public void onIabSetupFailed(IabResult result) {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        Toast.makeText(getActivity(), "onIabSetupFailed", Toast.LENGTH_SHORT).show();
//        Log.i(TAG, "onIabSetupFailed");
    }

    @Override
    public void onQueryFinished(Inventory inventory) {
        Toast.makeText(getActivity(), "onQueryFinished", Toast.LENGTH_SHORT).show();
//        Log.i(TAG, "onQueryFinished");

        if (inventory.hasDetails(SKIabProducts.SKU_FULL_VERSION)) {
            setPriceText(fullVersionTextView, inventory.getSkuDetails(SKIabProducts.SKU_FULL_VERSION).getPrice());
        }
        if (inventory.hasDetails(SKIabProducts.SKU_NO_ADS)) {
            setPriceText(noAdsTextView, inventory.getSkuDetails(SKIabProducts.SKU_NO_ADS).getPrice());
        }
        if (inventory.hasDetails(SKIabProducts.SKU_MORE_ALARM_SLOTS)) {
            setPriceText(moreAlarmSlotsTextView, inventory.getSkuDetails(SKIabProducts.SKU_MORE_ALARM_SLOTS).getPrice());
        }
        if (inventory.hasDetails(SKIabProducts.SKU_DATE_COUNTDOWN)) {
            setPriceText(dateCountdownTextView, inventory.getSkuDetails(SKIabProducts.SKU_DATE_COUNTDOWN).getPrice());
        }
        if (inventory.hasDetails(SKIabProducts.SKU_MEMO)) {
            setPriceText(memoTextView, inventory.getSkuDetails(SKIabProducts.SKU_MEMO).getPrice());
        }
        if (inventory.hasDetails(SKIabProducts.SKU_MODERNITY)) {
            setPriceText(modernityTextView, inventory.getSkuDetails(SKIabProducts.SKU_MODERNITY).getPrice());
        }
        if (inventory.hasDetails(SKIabProducts.SKU_CELESTIAL)) {
            setPriceText(celestialTextView, inventory.getSkuDetails(SKIabProducts.SKU_CELESTIAL).getPrice());
        }
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        updateUI();
    }

    @Override
    public void onQueryFailed(IabResult result) {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        Toast.makeText(getActivity(), "onQueryFailed", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onQueryFailed");
        Log.i(TAG, result.toString());
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        // 구매된 리스트를 확인해 SharedPreferences에 적용하기
        if (result.isSuccess()) {
            Log.i(TAG, result.toString());
            Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();

            if (info != null && info.getDeveloperPayload().equals(SKIabManager.DEVELOPER_PAYLOAD)) {
                Log.i(TAG, info.toString());
                Log.i(TAG, info.getSku());
                Log.i(TAG, info.getDeveloperPayload());

                // 프레퍼런스에 저장
                SKIabProducts.saveIabProduct(info.getSku(), activity);
                updateUI();
            } else if (info != null) {
                AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
                bld.setMessage("No purchase info: " + result.getMessage());
                bld.setNeutralButton(getString(R.string.ok), null);
                bld.create().show();
            } else {
                AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
                bld.setMessage("Payload problem: " + result.getMessage());
                bld.setNeutralButton(getString(R.string.ok), null);
                bld.create().show();
            }
        } else {
            AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
            bld.setMessage("Purchase Failed: " + result.getMessage());
            bld.setNeutralButton(getString(R.string.ok), null);
            bld.create().show();
        }
    }

    private void setPriceText(TextView textView, String string) {
        if (textView != null && textView.getText() != null) {
            String previousString = textView.getText().toString();
            textView.setText(previousString + string);
        }
    }

    // SharedPreferences에서 구매된 목록 확인
    void updateUI() {
        List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(activity);

        if (ownedSkus.contains(SKIabProducts.SKU_FULL_VERSION)) {
            fullVersionButton.setText("Purchased");
        }
        if (ownedSkus.contains(SKIabProducts.SKU_MORE_ALARM_SLOTS)) {
            moreAlarmSlotsButton.setText("Purchased");
        }
        if (ownedSkus.contains(SKIabProducts.SKU_NO_ADS)) {
            noAdsButton.setText("Purchased");
        }
        if (ownedSkus.contains(SKIabProducts.SKU_DATE_COUNTDOWN)) {
            dateCountdownButton.setText("Purchased");
        }
        if (ownedSkus.contains(SKIabProducts.SKU_MEMO)) {
            memoButton.setText("Purchased");
        }
        if (ownedSkus.contains(SKIabProducts.SKU_MODERNITY)) {
            modernityButton.setText("Purchased");
        }
        if (ownedSkus.contains(SKIabProducts.SKU_CELESTIAL)) {
            celestialButton.setText("Purchased");
        }
    }

    /**
     * Buttons
     */
    @OnClick(R.id.button_full_version)
    public void fullVersionClicked(Button button) {
        iabManager.processPurchase(SKIabProducts.SKU_FULL_VERSION, this);
    }

    @OnClick(R.id.button_more_alarm_slots)
    public void moreAlarmSlotsClicked(Button button) {
        iabManager.processPurchase(SKIabProducts.SKU_MORE_ALARM_SLOTS, this);
    }

    @OnClick(R.id.button_no_ads)
    public void noAdsClicked(Button button) {
        iabManager.processPurchase(SKIabProducts.SKU_NO_ADS, this);
    }

    @OnClick(R.id.button_date_countdown)
    public void dateCountdownClicked(Button button) {
        iabManager.processPurchase(SKIabProducts.SKU_DATE_COUNTDOWN, this);
    }

    @OnClick(R.id.button_memo)
    public void memoClicked(Button button) {
        iabManager.processPurchase(SKIabProducts.SKU_MEMO, this);
    }

    @OnClick(R.id.button_modernity)
    public void modernityClicked(Button button) {
        iabManager.processPurchase(SKIabProducts.SKU_MODERNITY, this);
    }

    @OnClick(R.id.button_celestial)
    public void celestialClicked(Button button) {
        iabManager.processPurchase(SKIabProducts.SKU_CELESTIAL, this);
    }
}
