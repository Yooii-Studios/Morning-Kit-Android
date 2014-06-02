package com.yooiistudios.morningkit.common.unlock;

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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.review.MNReviewApp;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.setting.store.iab.SKIabManager;
import com.yooiistudios.morningkit.setting.store.iab.SKIabManagerListener;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.store.util.IabHelper;
import com.yooiistudios.morningkit.setting.store.util.IabResult;
import com.yooiistudios.morningkit.setting.store.util.Inventory;
import com.yooiistudios.morningkit.setting.store.util.Purchase;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import lombok.Getter;

public class MNUnlockActivity extends ActionBarActivity implements MNUnlockOnClickListener,
        SKIabManagerListener, IabHelper.OnIabPurchaseFinishedListener {

    public static final String SHARED_PREFS = "UNLOCK_SHARED_PREFS";
    public static final String PRODUCT_SKU_KEY = "PRODUCT_SKU_KEY";
    protected static final String REVIEW_USED = "REVIEW_USED";
    public static final String REVIEW_USED_PRODUCT_SKU = "REVIEW_USED_PRODUCT_SKU";

    private String productSku;

    @InjectView(R.id.unlock_container)              RelativeLayout          containerLayout;
    @InjectView(R.id.unlock_listview_layout)        RelativeLayout          listViewLayout;
    @InjectView(R.id.unlock_description_textview)   TextView                descriptionTextView;
    @InjectView(R.id.unlock_listview)               ListView                listView;

    @Getter private SKIabManager iabManager;

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
        descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.unlock_description_textsize));

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
        iabManager = new SKIabManager(this, this);
        iabManager.loadWithAllItems();
    }

    @OnClick(R.id.unlock_reset_button)
    void onResetButtonClicked(Button button) {

        initDescriptionTextView();

        // 사용 이력을 전부 초기화해주자, 거의 리뷰에만 쓰일듯
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit().remove(REVIEW_USED).commit();
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit().remove(REVIEW_USED_PRODUCT_SKU).commit();
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, R.anim.activity_modal_down);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (iabManager.getHelper() == null) return;

        // Pass on the activity result to the helper for handling
        if (!iabManager.getHelper().handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            if (resultCode != MNReviewApp.REQ_REVIEW_APP) {
                onAfterReviewItemClicked();
            }
            super.onActivityResult(requestCode, resultCode, data);
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
                iabManager.processPurchase(SKIabProducts.SKU_FULL_VERSION, this);
                break;

            case 1:
                iabManager.processPurchase(productSku, this);
                break;

            case 2:
                MNReviewApp.showReviewActivity(this);
                break;
        }
    }

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
        } else if (productSku.equals(SKIabProducts.SKU_MODERNITY)) {
            return this.getString(R.string.setting_theme_color_classic_white);
        } else if (productSku.equals(SKIabProducts.SKU_CELESTIAL)) {
            return this.getString(R.string.setting_theme_color_skyblue);
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

            descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.unlock_description_textsize_scale_up));
            descriptionTextView.setGravity(Gravity.CENTER);
            descriptionTextView.setText(spannableString);
        }

        // animation
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.view_scale_up_and_down);
        if (scaleAnimation != null) {
            descriptionTextView.startAnimation(scaleAnimation);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // animate activity
                    finish();
                    overridePendingTransition(R.anim.activity_hold, R.anim.activity_modal_down);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }
    private void refreshUI() {
        // listView
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

        // textView
        refreshUnlockedDescriptionTextView();
    }

    private void onAfterReviewItemClicked() {
        SharedPreferences.Editor edit = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
        edit.putBoolean(REVIEW_USED, true);
        edit.putString(REVIEW_USED_PRODUCT_SKU, productSku);
        edit.commit();

        refreshUI();
    }

    /**
     * Iab
     */
    @Override
    public void onIabSetupFinished(IabResult result) {

    }

    @Override
    public void onIabSetupFailed(IabResult result) {
        Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQueryFinished(Inventory inventory) {

    }

    @Override
    public void onQueryFailed(IabResult result) {
        Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * OnIabPurchaseFinishedListener
     */
    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        // 구매된 리스트를 확인해 SharedPreferences에 적용하기
        if (result.isSuccess()) {
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();

            if (info != null && info.getDeveloperPayload().equals(SKIabManager.DEVELOPER_PAYLOAD)) {
                SKIabProducts.saveIabProduct(info.getSku(), this);
                refreshUI();
            } else if (info != null) {
                showComplain("No purchase info: " + result.getMessage());
            } else {
                showComplain("Payload problem: " + result.getMessage());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (MNSound.isSoundOn(this)) {
                MNSoundEffectsPlayer.play(R.raw.effect_view_close, this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Catch the back button and make fragment animate
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (MNSound.isSoundOn(this)) {
                MNSoundEffectsPlayer.play(R.raw.effect_view_close, this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
