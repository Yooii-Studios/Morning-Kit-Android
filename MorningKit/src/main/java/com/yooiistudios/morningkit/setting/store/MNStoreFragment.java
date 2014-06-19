package com.yooiistudios.morningkit.setting.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.flurry.android.FlurryAgent;
import com.naver.iap.NaverIabActivity;
import com.naver.iap.NaverIabInventoryItem;
import com.naver.iap.NaverIabProductUtils;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.encryption.MNMd5Utils;
import com.yooiistudios.morningkit.common.log.MNFlurry;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @InjectView(R.id.setting_store_reset_button) Button resetButton;
    @InjectView(R.id.setting_store_debug_button) Button debugButton;

    // For Naver
    public static final boolean IS_STORE_FOR_NAVER = true;
    public static final int RC_NAVER_IAB = 8374;
    public boolean isNaverStoreStartLoading = false;
//    public boolean isNaverStoreFinishLoading = false;
    @Setter boolean isFragmentForActivity = false;
    @Getter List<NaverIabInventoryItem> productList;

    // setActivity 반드시 해줘야함
    public MNStoreFragment(){}

    // 이전에 생성된 프래그먼트를 유지
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true); // 2탭 멀리 있을 때 새로 생성해 주어야 제대로 초기화가 진행
        setRetainInstance(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_store_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showLoadingViews();

        onTabClicked(functionTabLayout);

        initIab();
        initUI();
        checkDebug();

        // 플러리
        Map<String, String> params = new HashMap<String, String>();
        params.put(MNFlurry.CALLED_FROM, "Setting - Store");
        FlurryAgent.logEvent(MNFlurry.STORE, params);
    }

    private void initIab() {
        // 이 부분 때문에 크래시가 나서 일단 null 체크를 해줌
        if (IS_STORE_FOR_NAVER) {
            // 네이버는 로딩을 탭 클릭 시로 미룸, 단 상점 액티비티는 처음에 로딩
            if (isFragmentForActivity) {
                onFirstStoreLoading();
            }
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
                    fullVersionButtonTextView.setText(inventory.getSkuDetails(SKIabProducts.SKU_FULL_VERSION).getPrice());
                    fullVersionImageView.setClickable(true);
                    fullVersionButtonImageView.setClickable(true);
                }
            }
            // Other
            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).setInventory(inventory);
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).setInventory(inventory);
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).setInventory(inventory);

            List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(getActivity());
            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).setOwnedSkus(ownedSkus);
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).setOwnedSkus(ownedSkus);
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).setOwnedSkus(ownedSkus);

            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).notifyDataSetChanged();
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).notifyDataSetChanged();
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).notifyDataSetChanged();
        }
    }

    private void checkDebug() {
        if (MNLog.isDebug) {
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

    public void onFirstStoreLoading() {
        showLoadingViews();

        Intent intent = new Intent(getActivity(), NaverIabActivity.class);
        intent.putExtra(NaverIabActivity.KEY_ACTION, NaverIabActivity.ACTION_QUERY_PURCHASE);
        startActivityForResult(intent, RC_NAVER_IAB);
        isNaverStoreStartLoading = true;
    }

    public void onRefreshPurchases() {
        // 구매된 아이템들 UI 다시 확인
        List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(getActivity());
        if (ownedSkus.contains(SKIabProducts.SKU_FULL_VERSION)) {
            // Full version
            fullVersionButtonTextView.setText(R.string.store_purchased);
            fullVersionImageView.setClickable(false);
            fullVersionButtonImageView.setClickable(false);
        }
        ((MNStoreGridViewAdapter) functionGridView.getAdapter()).setOwnedSkus(ownedSkus);
        ((MNStoreGridViewAdapter) panelGridView.getAdapter()).setOwnedSkus(ownedSkus);
        ((MNStoreGridViewAdapter) themeGridView.getAdapter()).setOwnedSkus(ownedSkus);

        if (productList != null) {
            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).setNaverIabInventoryItemList(productList);
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).setNaverIabInventoryItemList(productList);
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).setNaverIabInventoryItemList(productList);
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
        if (progressBar != null) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingViews() {
        if (progressBar != null) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
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

            // 창하님 조언으로 수정: payload는 sku의 md5해시값으로 비교해 해킹을 방지
            // 또한 orderId는 무조건 37자리여야 한다고 함. 프리덤같은 가짜 결제는 자릿수가 짧게 온다고 하심
            if (info != null && info.getDeveloperPayload().equals(MNMd5Utils.getMd5String(info.getSku())) &&
                    info.getOrderId().length() == 37) {
                // 프레퍼런스에 저장
                SKIabProducts.saveIabProduct(info.getSku(), getActivity());
                updateUIAfterPurchase(info);
            } else if (info != null) {
                showComplain("No purchase info");
            } else {
                showComplain("Payload problem");
                if (!info.getDeveloperPayload().equals(MNMd5Utils.getMd5String(info.getSku()))) {
                    Log.e("MNStoreFragment", "payload not equals to md5 hash of sku");
                } else if (info.getOrderId().length() != 37) {
                    Log.e("MNStoreFragment", "length of orderId is not 37");
                }
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
    public void onItemClickedForNaver(String sku) {
        showLoadingViews();

        Intent intent = new Intent(getActivity(), NaverIabActivity.class);
        intent.putExtra(NaverIabActivity.KEY_ACTION, NaverIabActivity.ACTION_PURCHASE);
        intent.putExtra(NaverIabActivity.KEY_PRODUCT_KEY,
                NaverIabProductUtils.naverSkuMap.get(sku));
        startActivityForResult(intent, MNStoreFragment.RC_NAVER_IAB);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_NAVER_IAB:
                if (resultCode == Activity.RESULT_OK) {
                    String action = data.getStringExtra(NaverIabActivity.KEY_ACTION);
                    if (action.equals(NaverIabActivity.ACTION_PURCHASE)) {
                        String purchasedIabItemKey = data.getStringExtra(NaverIabActivity.KEY_PRODUCT_KEY);

                        if (purchasedIabItemKey != null) {
                            // SKIabProducts에 적용
                            String ownedSku = NaverIabProductUtils.googleSkuMap.get(purchasedIabItemKey);
                            SKIabProducts.saveIabProduct(ownedSku, getActivity());

                            // 구매 후 UI 재로딩
                            updateUIAfterPurchase(ownedSku);
                        }

                    } else if (action.equals(NaverIabActivity.ACTION_QUERY_PURCHASE)) {
                        ArrayList<NaverIabInventoryItem> productList =
                                data.getParcelableArrayListExtra(NaverIabActivity.KEY_PRODUCT_LIST);

                        // 구매 목록 SKIabProducts에 적용
                        SKIabProducts.saveIabProducts(productList, getActivity());
                        this.productList = productList;

                        // 이후 UI 로딩
                        initUIAfterLoading(productList);
                    }
                }
                // 네이버 인앱 페이지에서 돌아오면 로딩뷰 감추기
                hideLoadingViews();
                break;
        }
    }

    public void initUIAfterLoading(List<NaverIabInventoryItem> productList) {
        List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(getActivity());

        NaverIabInventoryItem fullversionNaverIabItem = null;
        for (NaverIabInventoryItem naverIabInventoryItem : productList) {
            if (naverIabInventoryItem.getKey().equals(
                    NaverIabProductUtils.naverSkuMap.get(SKIabProducts.SKU_FULL_VERSION))) {
                fullversionNaverIabItem = naverIabInventoryItem;
            }
        }

        if (fullversionNaverIabItem != null) {
            // 네이버에서 구매했거나, 다른 곳에서 구매해서 ownedSkus 에 있는지 둘 다 확인 필요
            if (fullversionNaverIabItem.isAvailable() || ownedSkus.contains(SKIabProducts.SKU_FULL_VERSION)) {
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

            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).setOwnedSkus(ownedSkus);
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).setOwnedSkus(ownedSkus);
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).setOwnedSkus(ownedSkus);

            ((MNStoreGridViewAdapter) functionGridView.getAdapter()).notifyDataSetChanged();
            ((MNStoreGridViewAdapter) panelGridView.getAdapter()).notifyDataSetChanged();
            ((MNStoreGridViewAdapter) themeGridView.getAdapter()).notifyDataSetChanged();
        }
        hideLoadingViews();
//        isNaverStoreFinishLoading = true;
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

        // 만약 로그인이나 로딩이 잘못되어 구매했을 때 까지 정보가 뜨지 않았다면 다시 가격 정보 로딩을 해 줄것
//        if (!isNaverStoreFinishLoading) {
//            onFirstStoreLoading();
//        }
    }
}
