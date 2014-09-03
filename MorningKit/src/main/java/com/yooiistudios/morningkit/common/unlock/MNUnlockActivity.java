package com.yooiistudios.morningkit.common.unlock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.naver.iap.NaverIabActivity;
import com.naver.iap.NaverIabProductUtils;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.encryption.MNMd5Utils;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.review.MNReviewApp;
import com.yooiistudios.morningkit.setting.store.MNStoreFragment;
import com.yooiistudios.morningkit.setting.store.iab.SKIabManager;
import com.yooiistudios.morningkit.setting.store.iab.SKIabManagerListener;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.store.util.IabHelper;
import com.yooiistudios.morningkit.setting.store.util.IabResult;
import com.yooiistudios.morningkit.setting.store.util.Inventory;
import com.yooiistudios.morningkit.setting.store.util.Purchase;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

public class MNUnlockActivity extends ActionBarActivity implements MNUnlockOnClickListener,
        SKIabManagerListener, IabHelper.OnIabPurchaseFinishedListener {

    public static final String SHARED_PREFS = "UNLOCK_SHARED_PREFS";
    public static final String PRODUCT_SKU_KEY = "PRODUCT_SKU_KEY";
    public static final String REVIEW_USED = "REVIEW_USED";
    public static final String REVIEW_USED_PRODUCT_SKU = "REVIEW_USED_PRODUCT_SKU";

    private String productSku;

    @Getter private SKIabManager iabManager;


    private boolean isReviewScreenCalled = false;
    private int resumeCount = 0;

    @InjectView(R.id.unlock_listview_layout)        RelativeLayout          listViewLayout;
    @InjectView(R.id.unlock_description_textview)   TextView                descriptionTextView;
    @InjectView(R.id.unlock_listview)               ListView                listView;
    @InjectView(R.id.unlock_reset_button)           Button                  resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        ButterKnife.inject(this);

        initIab();
        initProductSku();
        initActionBar();
        initDescriptionTextView();
        initListViewLayout();

        // listView
        listView.setAdapter(new MNUnlockListAdapter(this, this, productSku));

        if (MNLog.isDebug) {
            resetButton.setVisibility(View.VISIBLE);
        } else {
            resetButton.setVisibility(View.INVISIBLE);
        }
    }

    private void initListViewLayout() {
        // shadow
        listViewLayout.setClickable(false);
        listViewLayout.setFocusable(false);
    }

    private void initDescriptionTextView() {
        // description - 0x00ccff 로 포인트 변경함
        descriptionTextView.setGravity(Gravity.NO_GRAVITY);
        descriptionTextView.setText(R.string.unlock_description);
        descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.unlock_description_textsize));

        if (descriptionTextView.getText() != null) {
            String descriptionString = descriptionTextView.getText().toString();
            SpannableString spannableDescriptionString = new SpannableString(descriptionString);

            String pointedString = getString(R.string.unlock_description_highlight);
            int pointedStringIndex = descriptionString.indexOf(pointedString);
            if (pointedStringIndex != -1) { // if pointedString found
                spannableDescriptionString.setSpan(new ForegroundColorSpan(Color.parseColor("#00ccff")),
                        pointedStringIndex, pointedStringIndex + pointedString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                descriptionTextView.setText(spannableDescriptionString);
            }
        }
    }

    private void initActionBar() {
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.unlock_notice);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setIcon(R.drawable.icon_actionbar_morning);
    }

    private void initProductSku() {
        // productSku
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productSku = extras.getString(PRODUCT_SKU_KEY);
        } else {
            throw new AssertionError("no product key in MNUnlockActivity");
        }
    }

    private void initIab() {
        boolean isStoreForNaver = MNStoreFragment.IS_STORE_FOR_NAVER;
        if (!isStoreForNaver) {
            iabManager = new SKIabManager(this, this);
            iabManager.loadWithAllItems();
        }
    }

    public void onResetButtonClicked(View view) {
        initDescriptionTextView();

        // 사용 이력을 전부 초기화해주자, 거의 리뷰에만 쓰일듯
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit().remove(REVIEW_USED).apply();
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit().remove(REVIEW_USED_PRODUCT_SKU).apply();
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, R.anim.activity_modal_down);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReviewScreenCalled) {
            resumeCount ++;
            if (resumeCount == 2) {
                onAfterReviewItemClicked();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (iabManager != null) {
            if (iabManager.getHelper() == null) return;

            // Pass on the activity result to the helper for handling
            if (!iabManager.getHelper().handleActivityResult(requestCode, resultCode, data)) {
                // not handled, so handle it ourselves (here's where you'd
                // perform any handling of activity results not related to in-app
                // billing...
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == MNReviewApp.REQ_REVIEW_APP) {
                    saveUnlockedItem();
                    onAfterReviewItemClicked();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            // 네이버 구매
            switch (requestCode) {
                case MNStoreFragment.RC_NAVER_IAB:
                    if (resultCode == Activity.RESULT_OK) {
                        String action = data.getStringExtra(NaverIabActivity.KEY_ACTION);
                        if (action.equals(NaverIabActivity.ACTION_PURCHASE)) {

                            String purchasedIabItemKey = data.getStringExtra(NaverIabActivity.KEY_PRODUCT_KEY);

                            if (purchasedIabItemKey != null) {
                                // SKIabProducts에 적용
                                String ownedSku = NaverIabProductUtils.googleSkuMap.get(purchasedIabItemKey);
                                SKIabProducts.saveIabProduct(ownedSku, this);

                                // 풀버전 구매를 대비해 전체 로딩을 한번 진행함 - 읽을 때 전부 추가
                                SKIabProducts.loadOwnedIabProducts(getApplicationContext());

                                // 풀버전 구매시는 풀버전을 구매했다고 표시
                                if (ownedSku.equals(SKIabProducts.SKU_FULL_VERSION)) {
                                    productSku = SKIabProducts.SKU_FULL_VERSION;
                                }

                                // 구매 후 UI 재로딩
                                refreshUI();
                            }
                        }
                    }
                    break;
            }
            // 리뷰 달기
            if (requestCode == MNReviewApp.REQ_REVIEW_APP) {
                saveUnlockedItem();
                isReviewScreenCalled = true;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iabManager != null) {
            iabManager.dispose();
        }
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                if (MNStoreFragment.IS_STORE_FOR_NAVER) {
                    Intent intent = new Intent(this, NaverIabActivity.class);
                    intent.putExtra(NaverIabActivity.KEY_ACTION, NaverIabActivity.ACTION_PURCHASE);
                    intent.putExtra(NaverIabActivity.KEY_PRODUCT_KEY,
                            NaverIabProductUtils.naverSkuMap.get(SKIabProducts.SKU_FULL_VERSION));
                    startActivityForResult(intent, MNStoreFragment.RC_NAVER_IAB);
                } else {
                    iabManager.processPurchase(SKIabProducts.SKU_FULL_VERSION, this);
                }
                break;

            case 1:
                if (MNStoreFragment.IS_STORE_FOR_NAVER) {
                    Intent intent = new Intent(this, NaverIabActivity.class);
                    intent.putExtra(NaverIabActivity.KEY_ACTION, NaverIabActivity.ACTION_PURCHASE);
                    intent.putExtra(NaverIabActivity.KEY_PRODUCT_KEY,
                            NaverIabProductUtils.naverSkuMap.get(productSku));
                    startActivityForResult(intent, MNStoreFragment.RC_NAVER_IAB);
                } else {
                    iabManager.processPurchase(productSku, this);
                }
                break;

            case 2:
//                makeReviewGuideDialog().show();
                MNReviewApp.showReviewActivity(MNUnlockActivity.this);
                break;
        }
    }

    // 유저들이 리뷰후 해제가 안된다는 불평이 많아 안내 메시지를 사용하려고 했으나,
    // 아이템을 먼저 잠금해제 해 주고 UI만 나중에 바꾸는 식으로 변경해서 해결함
    /*
    private AlertDialog makeReviewGuideDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MNReviewApp.showReviewActivity(MNUnlockActivity.this);
            }
        });
        AlertDialog reviewGuideDialog = builder.create();
        reviewGuideDialog.setCancelable(false);
        reviewGuideDialog.setCanceledOnTouchOutside(false);
        reviewGuideDialog.setTitle(R.string.app_name);
        reviewGuideDialog.setMessage(getString(R.string.unlock_should_come_back_to_morningkit));
        return reviewGuideDialog;
    }
    */

    private String getProductString() {
        if (productSku.equals(SKIabProducts.SKU_FULL_VERSION)) {
            return this.getString(R.string.store_item_full_version);
        } else if (productSku.equals(SKIabProducts.SKU_MORE_ALARM_SLOTS)) {
            return this.getString(R.string.store_item_more_alarm_slots);
        } else if (productSku.equals(SKIabProducts.SKU_NO_ADS)) {
            return this.getString(R.string.store_item_no_ads);
        } else if (productSku.equals(SKIabProducts.SKU_PANEL_MATRIX_2X3)) {
            return this.getString(R.string.store_item_matrix);
        } else if (productSku.equals(SKIabProducts.SKU_DATE_COUNTDOWN)) {
            String productString = this.getString(R.string.store_item_widget_date_countdown);
            productString = productString.replace("\n", " ");
            return productString;
        } else if (productSku.equals(SKIabProducts.SKU_MEMO)) {
            return this.getString(R.string.memo);
        } else if (productSku.equals(SKIabProducts.SKU_PHOTO_FRAME)) {
            return this.getString(R.string.photo_album);
        } else if (productSku.equals(SKIabProducts.SKU_MODERNITY)) {
            return this.getString(R.string.setting_theme_color_classic_white);
        } else if (productSku.equals(SKIabProducts.SKU_CELESTIAL)) {
            return this.getString(R.string.setting_theme_color_skyblue);
        } else if (productSku.equals(SKIabProducts.SKU_CAT)) {
            return this.getString(R.string.cat);
        }
        return null;
    }

    private void refreshUnlockedDescriptionTextView() {
        // text
        String productString = getProductString();
        String unlockedString = productString + " : " + getString(R.string.unlock_unlocked);
        SpannableString spannableString = new SpannableString(unlockedString);

        // point productString
        int pointedStringIndex = unlockedString.indexOf(productString);
        if (pointedStringIndex != -1) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#00ccff")),
                    pointedStringIndex, pointedStringIndex + productString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.unlock_description_textsize_scale_up));
            descriptionTextView.setGravity(Gravity.CENTER);
            descriptionTextView.setText(spannableString);
        }

        // animation
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.view_scale_up_and_down);
        if (scaleAnimation != null) {
            descriptionTextView.startAnimation(scaleAnimation);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    // animate activity
                    finish();
                    overridePendingTransition(R.anim.activity_hold, R.anim.activity_modal_down);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }
    }

    private void refreshUI() {
        // listView
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

        // textView
        refreshUnlockedDescriptionTextView();
    }

    private void saveUnlockedItem() {
        SharedPreferences.Editor edit = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
        edit.putBoolean(REVIEW_USED, true);
        edit.putString(REVIEW_USED_PRODUCT_SKU, productSku);
        edit.apply();
    }

    private void onAfterReviewItemClicked() {
        refreshUI();

        // 플러리 - 세팅 패널 탭에서 패널 변경
        String unlockedProduct = null;
        if (productSku.equals(SKIabProducts.SKU_FULL_VERSION)) {
            unlockedProduct = "Full Version";
        } else if (productSku.equals(SKIabProducts.SKU_MORE_ALARM_SLOTS)) {
            unlockedProduct = "More Alarm Slots";
        } else if (productSku.equals(SKIabProducts.SKU_NO_ADS)) {
            unlockedProduct = "No Ads";
        } else if (productSku.equals(SKIabProducts.SKU_PANEL_MATRIX_2X3)) {
            unlockedProduct = "Matrix 2 X 3";
        } else if (productSku.equals(SKIabProducts.SKU_DATE_COUNTDOWN)) {
            unlockedProduct = "Date Countdown";
        } else if (productSku.equals(SKIabProducts.SKU_MEMO)) {
            unlockedProduct = "Memo";
        } else if (productSku.equals(SKIabProducts.SKU_PHOTO_FRAME)) {
            unlockedProduct = "Photo Frame";
        } else if (productSku.equals(SKIabProducts.SKU_MODERNITY)) {
            unlockedProduct = "Classic White";
        } else if (productSku.equals(SKIabProducts.SKU_CELESTIAL)) {
            unlockedProduct = "Sky Blue";
        } else if (productSku.equals(SKIabProducts.SKU_CAT)) {
            unlockedProduct = "Cat";
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(MNFlurry.UNLOCKED_PRODUCT, unlockedProduct);
        FlurryAgent.logEvent(MNFlurry.UNLOCK, params);
    }

    /**
     * Google Iab
     */
    @Override
    public void onIabSetupFinished(IabResult result) {}

    @Override
    public void onIabSetupFailed(IabResult result) {
//        Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
        showComplain("Setup Failed: " + result.getMessage());
    }

    @Override
    public void onQueryFinished(Inventory inventory) {}

    @Override
    public void onQueryFailed(IabResult result) {
//        Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
        showComplain("Query Failed: " + result.getMessage());
    }

    /**
     * OnIabPurchaseFinishedListener
     */
    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        // 구매된 리스트를 확인해 SharedPreferences에 적용하기
        if (result.isSuccess()) {
//            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
            
            // 창하님 조언으로 수정: payload는 sku의 md5해시값으로 비교해 해킹을 방지
            // 또한 orderId는 무조건 37자리여야 한다고 함. 프리덤같은 가짜 결제는 자릿수가 짧게 온다고 하심
            if (info != null && info.getDeveloperPayload().equals(MNMd5Utils.getMd5String(info.getSku())) &&
                    info.getOrderId().length() == 37) {
                SKIabProducts.saveIabProduct(info.getSku(), this);
                refreshUI();
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
            showComplain("Purchase Failed: " + result.getMessage());
        }
    }

    private void showComplain(String string) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(string);
        bld.setNeutralButton(getString(R.string.ok), null);
        bld.create().show();
    }
}
