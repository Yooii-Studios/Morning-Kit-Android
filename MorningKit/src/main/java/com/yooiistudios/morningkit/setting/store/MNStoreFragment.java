package com.yooiistudios.morningkit.setting.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.iap.NaverIabActivity;
import com.naver.iap.NaverIabInventoryItem;
import com.naver.iap.NaverIabProductUtils;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.number.MNDecimalFormatUtils;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.setting.MNSettingActivity;
import com.yooiistudios.morningkit.setting.store.iab.SKIabManager;
import com.yooiistudios.morningkit.setting.store.iab.SKIabManagerListener;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.store.util.IabHelper;
import com.yooiistudios.morningkit.setting.store.util.IabResult;
import com.yooiistudios.morningkit.setting.store.util.Inventory;
import com.yooiistudios.morningkit.setting.store.util.Purchase;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 12.
 * <p/>
 * MNStoreFragment
 */
public class MNStoreFragment extends Fragment implements SKIabManagerListener, IabHelper.OnIabPurchaseFinishedListener, MNStoreGridViewAdapter.MNStoreGridViewOnClickListener {
//    private static final String TAG = "MNStoreFragment";
    @Setter @Getter private SKIabManager iabManager;
    @Setter MNSettingActivity activity;

    @InjectView(R.id.setting_store_progressBar) ProgressBar progressBar;
    @InjectView(R.id.setting_store_loading_view) View loadingView;

    @InjectView(R.id.setting_store_full_version_image_view) ImageView fullVersionImageView;
    @InjectView(R.id.setting_store_full_version_button_imageview) ImageView fullVersionButtonImageView;
    @InjectView(R.id.setting_store_full_version_button_textview) TextView fullVersionButtonTextView;

    @InjectView(R.id.setting_store_tab_left_divider) View leftTabDivider;
    @InjectView(R.id.setting_store_tab_right_divider) View rightTabDivider;

    @InjectView(R.id.setting_store_tab_function_layout) RelativeLayout functionTabLayout;
    @InjectView(R.id.setting_store_tab_panel_layout) RelativeLayout panelTabLayout;
    @InjectView(R.id.setting_store_tab_theme_layout) RelativeLayout themeTabLayout;

    @InjectView(R.id.setting_store_tab_function_textview) TextView functionTextView;
    @InjectView(R.id.setting_store_tab_panel_textview) TextView  panelTextView;
    @InjectView(R.id.setting_store_tab_theme_textview) TextView  themeTextView;

    @InjectView(R.id.setting_store_function_gridview) GridView functionGridView;
    @InjectView(R.id.setting_store_panel_gridview) GridView panelGridView;
    @InjectView(R.id.setting_store_theme_gridview) GridView themeGridView;

    // For Test
    private static final boolean IS_DEBUG = true; // 출시 시 false로 변경할 것
    @InjectView(R.id.setting_store_reset_button) Button resetButton;
    @InjectView(R.id.setting_store_debug_button) Button debugButton;

    // For Naver
    protected static final boolean IS_STORE_FOR_NAVER = true;
    private static final int RC_NAVER_IAB = 8374;

    // setActivity 반드시 해줘야함
    public MNStoreFragment(){}

    // 이전에 생성된 프래그먼트를 유지
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_store_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            showLoadingViews();

            onTabClicked(functionTabLayout);

            initIab();
            initUI();
            checkDebug();
        }
        return rootView;
    }

    private void initIab() {
        // 이 부분 때문에 크래시가 나서 일단 null 체크를 해줌
        if (IS_STORE_FOR_NAVER) {
            showLoadingViews();

            Intent intent = new Intent(getActivity(), NaverIabActivity.class);
            intent.putExtra(NaverIabActivity.KEY_ACTION, NaverIabActivity.ACTION_QUERY_PURCHASE);
            startActivityForResult(intent, RC_NAVER_IAB);
        } else {
            if (activity != null) {
                iabManager = new SKIabManager(activity, this);
                iabManager.loadWithAllItems();
                activity.setIabHelper(iabManager.getHelper());
            }
        }
    }

    private void initUI() {
        functionGridView.setAdapter(new MNStoreGridViewAdapter(getActivity(), MNStoreTabType.FUNCTIONS,
                null, iabManager, this, this));
        panelGridView.setAdapter(new MNStoreGridViewAdapter(getActivity(), MNStoreTabType.PANELS,
                null, iabManager, this, this));
        themeGridView.setAdapter(new MNStoreGridViewAdapter(getActivity(), MNStoreTabType.THEMES,
                null, iabManager, this, this));
    }

    // For Google
    private void initUIAfterLoading(Inventory inventory) {
        if (inventory.hasDetails(SKIabProducts.SKU_FULL_VERSION)) {
            // Full version
            if (inventory.hasPurchase(SKIabProducts.SKU_FULL_VERSION)) {
                fullVersionButtonTextView.setText(R.string.store_purchased);
                fullVersionImageView.setClickable(false);
                fullVersionButtonImageView.setClickable(false);
            } else {
                if (!MNStoreDebugChecker.isUsingStore(getActivity())) {
                    initFullVersionUIDebug();
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getActivity(),
                            R.anim.store_view_scale_up_and_down);
                    if (animation != null) {
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation textAniamtion = AnimationUtils.loadAnimation(getActivity(),
                                        R.anim.store_view_scale_up_and_down);
                                if (textAniamtion != null) {
                                    fullVersionButtonTextView.startAnimation(textAniamtion);
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        fullVersionButtonImageView.startAnimation(animation);
                    }
                    fullVersionButtonTextView.setText(inventory.getSkuDetails(SKIabProducts.SKU_FULL_VERSION).getPrice());
                    fullVersionImageView.setClickable(true);
                    fullVersionButtonImageView.setClickable(true);
                }
            }
            // Other
            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).setInventory(inventory);
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).setInventory(inventory);
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).setInventory(inventory);

            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).notifyDataSetChanged();
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).notifyDataSetChanged();
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).notifyDataSetChanged();
        }
    }

    private void checkDebug() {
        if (IS_DEBUG) {
            resetButton.setVisibility(View.VISIBLE);
            debugButton.setVisibility(View.VISIBLE);
            if (MNStoreDebugChecker.isUsingStore(getActivity())) {
                if (IS_STORE_FOR_NAVER) {
                    debugButton.setText("Naver Store");
                } else {
                    debugButton.setText("Google Store");
                }
            } else {
                debugButton.setText("Debug");
            }
        } else {
            resetButton.setVisibility(View.GONE);
            debugButton.setVisibility(View.GONE);
            MNStoreDebugChecker.setUsingStore(true, getActivity());
        }
    }

    private void updateUIAfterPurchase(Purchase info) {
        if (info.getSku().equals(SKIabProducts.SKU_FULL_VERSION)) {
            // Full version
            fullVersionButtonTextView.setText(R.string.store_purchased);
            fullVersionImageView.setClickable(false);
            fullVersionButtonImageView.setClickable(false);
        } else {
            // Others
            List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(getActivity());
            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).setOwnedSkus(ownedSkus);
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).setOwnedSkus(ownedSkus);
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).setOwnedSkus(ownedSkus);
        }
        ((MNStoreGridViewAdapter) functionGridView.getAdapter()).notifyDataSetChanged();
        ((MNStoreGridViewAdapter) panelGridView.getAdapter()).notifyDataSetChanged();
        ((MNStoreGridViewAdapter) themeGridView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (iabManager != null) {
            iabManager.dispose();
        }
    }

    /**
     * Loading
     */
    private void showLoadingViews() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingViews() {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    /**
     * Tab onclick
     */
    @OnClick({ R.id.setting_store_tab_function_layout, R.id.setting_store_tab_panel_layout,
            R.id.setting_store_tab_theme_layout})
    public void onTabClicked(RelativeLayout tabLayout) {
        functionTabLayout.setBackgroundColor(getResources().getColor(R.color.setting_store_tab_unselected_color));
        panelTabLayout.setBackgroundColor(getResources().getColor(R.color.setting_store_tab_unselected_color));
        themeTabLayout.setBackgroundColor(getResources().getColor(R.color.setting_store_tab_unselected_color));

        functionTextView.setTextColor(getResources().getColor(R.color.setting_store_tab_unselected_text_color));
        panelTextView.setTextColor(getResources().getColor(R.color.setting_store_tab_unselected_text_color));
        themeTextView.setTextColor(getResources().getColor(R.color.setting_store_tab_unselected_text_color));

        functionGridView.setVisibility(View.GONE);
        panelGridView.setVisibility(View.GONE);
        themeGridView.setVisibility(View.GONE);

        leftTabDivider.setVisibility(View.GONE);
        rightTabDivider.setVisibility(View.GONE);

        if (tabLayout.equals(functionTabLayout)) {
            functionTabLayout.setBackgroundColor(getResources().getColor(R.color.setting_store_tab_selected_color));
            functionTextView.setTextColor(getResources().getColor(R.color.setting_store_tab_selected_text_color));
            functionGridView.setVisibility(View.VISIBLE);
            rightTabDivider.setVisibility(View.VISIBLE);
        } else if (tabLayout.equals(panelTabLayout)) {
            panelTabLayout.setBackgroundColor(getResources().getColor(R.color.setting_store_tab_selected_color));
            panelTextView.setTextColor(getResources().getColor(R.color.setting_store_tab_selected_text_color));
            panelGridView.setVisibility(View.VISIBLE);
        } else {
            themeTabLayout.setBackgroundColor(getResources().getColor(R.color.setting_store_tab_selected_color));
            themeTextView.setTextColor(getResources().getColor(R.color.setting_store_tab_selected_text_color));
            themeGridView.setVisibility(View.VISIBLE);
            leftTabDivider.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Full version button
     */
    @OnClick({ R.id.setting_store_full_version_image_view, R.id.setting_store_full_version_button_imageview})
    public void onFullVersionClicked(ImageView v) {
        if (MNSound.isSoundOn(getActivity())) {
            MNSoundEffectsPlayer.play(R.raw.effect_view_open, getActivity());
        }
        if (MNStoreDebugChecker.isUsingStore(getActivity())) {
            if (IS_STORE_FOR_NAVER) {
                showLoadingViews();

                Intent intent = new Intent(getActivity(), NaverIabActivity.class);
                intent.putExtra(NaverIabActivity.KEY_ACTION, NaverIabActivity.ACTION_PURCHASE);
                intent.putExtra(NaverIabActivity.KEY_PRODUCT_KEY,
                        NaverIabProductUtils.naverSkuMap.get(SKIabProducts.SKU_FULL_VERSION));
                startActivityForResult(intent, RC_NAVER_IAB);
            } else {
                iabManager.processPurchase(SKIabProducts.SKU_FULL_VERSION, this);
            }
        } else {
            SKIabProducts.saveIabProduct(SKIabProducts.SKU_FULL_VERSION, getActivity());
            initUI();
            initFullVersionUIDebug();
        }
    }

    /**
     * SKIabManagerListener
     */
    @Override
    public void onIabSetupFinished(IabResult result) {
    }

    @Override
    public void onIabSetupFailed(IabResult result) {
        Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
        hideLoadingViews();
    }

    @Override
    public void onQueryFinished(Inventory inventory) {
        // called after SKIabProducts.saveIabProducts(inv, activity)
        initUIAfterLoading(inventory);
        hideLoadingViews();
    }

    @Override
    public void onQueryFailed(IabResult result) {
        Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
        hideLoadingViews();
    }

    /**
     * IabHelper.OnIabPurchaseFinishedListener
     */
    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        // 구매된 리스트를 확인해 SharedPreferences에 적용하기
        if (result.isSuccess()) {
            Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();

            if (info != null && info.getDeveloperPayload().equals(SKIabManager.DEVELOPER_PAYLOAD)) {
                // 프레퍼런스에 저장
                SKIabProducts.saveIabProduct(info.getSku(), getActivity());
                updateUIAfterPurchase(info);
            } else if (info != null) {
                showComplain("No purchase info");
            } else {
                showComplain("Payload problem");
            }
        } else {
            showComplain("Purchase Failed");
        }
    }

    private void showComplain(String string) {
        /*
        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
        bld.setMessage(string);
        bld.setNeutralButton(getString(R.string.ok), null);
        bld.create().show();
        */

        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    /**
     * Debug Test
     */
    @OnClick(R.id.setting_store_debug_button)
    void debugButtonClicked() {
        if (MNStoreDebugChecker.isUsingStore(getActivity())) {
            debugButton.setText("Debug");
            MNStoreDebugChecker.setUsingStore(false, getActivity());
        } else {
            if (IS_STORE_FOR_NAVER) {
                debugButton.setText("Naver Store");
            } else {
                debugButton.setText("Google Store");
            }
            MNStoreDebugChecker.setUsingStore(true, getActivity());
        }
    }

    @OnClick(R.id.setting_store_reset_button)
    void resetButtonClicked() {
        // 디버그 상태에서 구매했던 아이템들을 리셋
        if (MNStoreDebugChecker.isUsingStore(getActivity())) {
            if (iabManager != null) {
                iabManager.loadWithAllItems();
            }
            initUI();
        } else {
            SKIabProducts.resetIabProductsDebug(getActivity());
            initUI();
            initFullVersionUIDebug();

        }
    }

    // 풀 버전을 제외한 아이템을 클릭하면 구매 처리하기

    /**
     * MNStoreGridViewOnClickListener
     */
    @Override
    public void onItemClickedDebug(String sku) {
        SKIabProducts.saveIabProduct(sku, getActivity());
        initUI();
    }

    @Override
    public void onItemClickedForNaver(String sku) {
        showLoadingViews();

        Intent intent = new Intent(getActivity(), NaverIabActivity.class);
        intent.putExtra(NaverIabActivity.KEY_ACTION, NaverIabActivity.ACTION_PURCHASE);
        intent.putExtra(NaverIabActivity.KEY_PRODUCT_KEY,
                NaverIabProductUtils.naverSkuMap.get(sku));
        startActivityForResult(intent, MNStoreFragment.RC_NAVER_IAB);
    }

    private void initFullVersionUIDebug() {
        List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(getActivity());
        if (ownedSkus.contains(SKIabProducts.SKU_FULL_VERSION)) {
            fullVersionButtonTextView.setText(R.string.store_purchased);
            fullVersionImageView.setClickable(false);
            fullVersionButtonImageView.setClickable(false);
        } else {
            Animation animation = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.store_view_scale_up_and_down);
            if (animation != null) {
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation textAniamtion = AnimationUtils.loadAnimation(getActivity(),
                                R.anim.store_view_scale_up_and_down);
                        if (textAniamtion != null) {
                            fullVersionButtonTextView.startAnimation(textAniamtion);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                fullVersionButtonImageView.startAnimation(animation);
            }
            fullVersionButtonTextView.setText("$1.99");
            fullVersionImageView.setClickable(true);
            fullVersionButtonImageView.setClickable(true);
        }
    }

    /**
     *  Naver App Store
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MNLog.now("onActivityResult/requestCode: " + requestCode + "/resultCode: " + resultCode);
        switch (requestCode) {
            case RC_NAVER_IAB:
                if (resultCode == Activity.RESULT_OK) {
                    String action = data.getStringExtra(NaverIabActivity.KEY_ACTION);
                    if (action.equals(NaverIabActivity.ACTION_PURCHASE)) {
                        MNLog.now("ACTION_PURCHASE");

                        String purchasedIabItemKey = data.getStringExtra(NaverIabActivity.KEY_PRODUCT_KEY);
                        MNLog.now("purchasedIabItemKey: " + purchasedIabItemKey);

                        if (purchasedIabItemKey != null) {
                            // SKIabProducts에 적용
                            String ownedSku = NaverIabProductUtils.googleSkuMap.get(purchasedIabItemKey);
                            SKIabProducts.saveIabProduct(ownedSku, getActivity());

                            // 구매 후 UI 재로딩
                            updateUIAfterPurchase(ownedSku);
                        }

                    } else if (action.equals(NaverIabActivity.ACTION_QUERY_PURCHASE)) {
                        MNLog.now("ACTION_QUERY_PURCHASE");
                        ArrayList<NaverIabInventoryItem> productList =
                                data.getParcelableArrayListExtra(NaverIabActivity.KEY_PRODUCT_LIST);
                        MNLog.now(productList.toString());

                        // 구매 목록 SKIabProducts에 적용
                        SKIabProducts.saveIabProducts(productList, getActivity());

                        // 이후 UI 로딩
                        initUIAfterLoading(productList);
                    }
                }
                // 네이버 인앱 페이지에서 돌아오면 로딩뷰 감추기
                hideLoadingViews();
                break;
        }
    }

    private void initUIAfterLoading(List<NaverIabInventoryItem> productList) {
        NaverIabInventoryItem fullversionNaverIabItem = null;
        for (NaverIabInventoryItem naverIabInventoryItem : productList) {
            if (naverIabInventoryItem.getKey().equals(
                    NaverIabProductUtils.naverSkuMap.get(SKIabProducts.SKU_FULL_VERSION))) {
                fullversionNaverIabItem = naverIabInventoryItem;
            }
        }

        if (fullversionNaverIabItem != null) {
            if (fullversionNaverIabItem.isAvailable()) {
                fullVersionButtonTextView.setText(R.string.store_purchased);
                fullVersionImageView.setClickable(false);
                fullVersionButtonImageView.setClickable(false);
            } else {
                if (!MNStoreDebugChecker.isUsingStore(getActivity())) {
                    initFullVersionUIDebug();
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getActivity(),
                            R.anim.store_view_scale_up_and_down);
                    if (animation != null) {
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {}
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation textAniamtion = AnimationUtils.loadAnimation(getActivity(),
                                        R.anim.store_view_scale_up_and_down);
                                if (textAniamtion != null) {
                                    fullVersionButtonTextView.startAnimation(textAniamtion);
                                }
                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) {}
                        });
                        fullVersionButtonImageView.startAnimation(animation);
                    }
                    fullVersionButtonTextView.setText(
                            "₩" + MNDecimalFormatUtils.makeStringComma(fullversionNaverIabItem.getPrice()));
                    fullVersionImageView.setClickable(true);
                    fullVersionButtonImageView.setClickable(true);
                }
            }
            // Other
            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).setNaverIabInventoryItemList(productList);
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).setNaverIabInventoryItemList(productList);
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).setNaverIabInventoryItemList(productList);

            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).notifyDataSetChanged();
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).notifyDataSetChanged();
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).notifyDataSetChanged();
        }
    }

    private void updateUIAfterPurchase(String ownedSku) {
        if (ownedSku.equals(SKIabProducts.SKU_FULL_VERSION)) {
            // Full version
            fullVersionButtonTextView.setText(R.string.store_purchased);
            fullVersionImageView.setClickable(false);
            fullVersionButtonImageView.setClickable(false);
        }
        // Others
        List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(getActivity());
        ((MNStoreGridViewAdapter) functionGridView.getAdapter()).setOwnedSkus(ownedSkus);
        ((MNStoreGridViewAdapter) panelGridView.getAdapter()).setOwnedSkus(ownedSkus);
        ((MNStoreGridViewAdapter) themeGridView.getAdapter()).setOwnedSkus(ownedSkus);

        ((MNStoreGridViewAdapter) functionGridView.getAdapter()).notifyDataSetChanged();
        ((MNStoreGridViewAdapter) panelGridView.getAdapter()).notifyDataSetChanged();
        ((MNStoreGridViewAdapter) themeGridView.getAdapter()).notifyDataSetChanged();
    }
}
