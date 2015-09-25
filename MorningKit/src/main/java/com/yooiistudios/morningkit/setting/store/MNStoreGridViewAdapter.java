package com.yooiistudios.morningkit.setting.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.MNIabInfo;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.iab.NaverIabInventoryItem;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.store.util.IabHelper;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 12.
 *
 * MNStoreGridViewAdapter
 */
public class MNStoreGridViewAdapter extends BaseAdapter {
    private static final String TAG = "MNStoreGridViewAdapter";
    private Context context;
    private MNStoreTabType type;
    private IabHelper.OnIabPurchaseFinishedListener onIabPurchaseFinishedListener;
    @Setter Map<String, String> googleSkuToPriceMap;
    @Setter List<String> ownedSkus;

    // For Naver Iab
    @Setter List<NaverIabInventoryItem> naverIabInventoryItemList;

    private MNStoreGridViewOnClickListener storeGridViewOnClickListener;

    public interface MNStoreGridViewOnClickListener {
        public void onItemClickedDebug(String sku);
        public void onItemClickedForNaver(String sku);
    }

    private MNStoreGridViewAdapter(){}
    public MNStoreGridViewAdapter(Context context, MNStoreTabType type, Map<String, String> googleSkuToPriceMap,
                                  MNStoreGridViewOnClickListener storeGridViewOnClickListener) {
        this.context = context;
        this.type = type;
        this.googleSkuToPriceMap = googleSkuToPriceMap;
        ownedSkus = SKIabProducts.loadOwnedIabProducts(context);

        // debug
        this.storeGridViewOnClickListener = storeGridViewOnClickListener;
    }

    @Override
    public int getCount() {
        switch (type) {
            case FUNCTIONS:
                return 2;
            case PANELS:
                return 3;
            case THEMES:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.setting_store_grid_item, parent, false);
        if (convertView != null) {
            MNStoreGridViewItemViewHolder viewHolder = new MNStoreGridViewItemViewHolder(convertView);

            initInnerLayout(viewHolder);

            switch (type) {
                case FUNCTIONS:
                    switch (position) {
                        case 0:
                            viewHolder.getItemNameTextView().setText(R.string.store_item_more_alarm_slots);
                            viewHolder.getIconImageView().setImageResource(R.drawable.shop_more_alarms_icon_ipad);
                            viewHolder.getPriceTextView().setTag(SKIabProducts.SKU_MORE_ALARM_SLOTS);
                            break;
                        case 1:
                            viewHolder.getItemNameTextView().setText(R.string.store_item_no_ads);
                            viewHolder.getIconImageView().setImageResource(R.drawable.shop_no_ad_icon_ipad);
                            viewHolder.getPriceTextView().setTag(SKIabProducts.SKU_NO_ADS);
                            break;
//                        case 2:
//                            viewHolder.getItemNameTextView().setText(R.string.store_item_matrix);
//                            viewHolder.getIconImageView().setImageResource(R.drawable.shop_more_matrix_icon_ipad);
//                            viewHolder.getPriceTextView().setTag(SKIabProducts.SKU_PANEL_MATRIX_2X3);
//                            break;
                    }
                    break;

                case PANELS:
                    switch (position) {
                        case 0:
                            viewHolder.getItemNameTextView().setText(R.string.store_item_widget_date_countdown);
                            viewHolder.getIconImageView().setImageResource(R.drawable.shop_widget_date_countdown_icon_ipad);
                            viewHolder.getPriceTextView().setTag(SKIabProducts.SKU_DATE_COUNTDOWN);
                            break;
                        case 1:
                            viewHolder.getItemNameTextView().setText(R.string.store_item_widget_memo);
                            viewHolder.getIconImageView().setImageResource(R.drawable.shop_widget_memo_icon_ipad);
                            viewHolder.getPriceTextView().setTag(SKIabProducts.SKU_MEMO);
                            break;
                        case 2:
                            viewHolder.getItemNameTextView().setText(R.string.store_item_widget_photo_album);
                            viewHolder.getIconImageView().setImageResource(R.drawable.shop_photo_ipad);
                            viewHolder.getPriceTextView().setTag(SKIabProducts.SKU_PHOTO_FRAME);
                            break;
                    }
                    break;

                case THEMES:
                    switch (position) {
                        case 0:
                            viewHolder.getItemNameTextView().setText(R.string.store_item_classic_white);
                            viewHolder.getIconImageView().setImageResource(R.drawable.shop_theme_white_icon_ipad);
                            viewHolder.getPriceTextView().setTag(SKIabProducts.SKU_MODERNITY);
                            break;
//                        case 1:
//                            viewHolder.getItemNameTextView().setText(R.string.store_item_skyblue);
//                            viewHolder.getIconImageView().setImageResource(R.drawable.shop_theme_skyblue_icon_ipad);
//                            viewHolder.getPriceTextView().setTag(SKIabProducts.SKU_CELESTIAL);
//                            break;
                    }
                    break;
            }
            initPriceTextView(viewHolder);
        }
        return convertView;
    }

    private void initInnerLayout(final MNStoreGridViewItemViewHolder viewHolder) {
        viewHolder.getInnerLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MNSound.isSoundOn(context)) {
                    MNSoundEffectsPlayer.play(R.raw.effect_view_open, context);
                }
                if (MNStoreDebugChecker.isUsingStore(context)) {
                    storeGridViewOnClickListener.onItemClickedForNaver(
                            (String) viewHolder.getPriceTextView().getTag());
                } else {
                    storeGridViewOnClickListener.onItemClickedDebug((String) viewHolder.getPriceTextView().getTag());
                }
            }
        });
    }

    private void initPriceTextView(final MNStoreGridViewItemViewHolder viewHolder) {
        viewHolder.getPriceTextView().setSelected(true);

        // price - check from inventory
        String sku = (String) viewHolder.getPriceTextView().getTag();

        if (MNIabInfo.STORE_TYPE.equals(MNStoreType.NAVER)) {
            // Naver
            boolean hasDetails = false;
            if (naverIabInventoryItemList != null) {
                for (NaverIabInventoryItem naverIabInventoryItem : naverIabInventoryItemList) {
                    // TODO: 네이버 인앱
//                    if (naverIabInventoryItem.getKey().equals(NIAPUtils.naverSkuMap.get(sku))) {
//                        hasDetails = true;
//
//                        if (naverIabInventoryItem.isAvailable()) {
//                            viewHolder.getPriceTextView().setText(R.string.store_purchased);
//                        } else {
//                            viewHolder.getPriceTextView().setText(
//                                    "₩" + MNDecimalFormatUtils.makeStringComma(naverIabInventoryItem.getPrice()));
//                        }
//                    }
                }
                if (!hasDetails) {
                    viewHolder.getPriceTextView().setText(R.string.loading);
                }
            } else {
                viewHolder.getPriceTextView().setText(R.string.loading);
            }
        } else {
            // Google
            if (googleSkuToPriceMap != null) {
                if (googleSkuToPriceMap.containsKey(sku)) {
                    if (SKIabProducts.containsSku(sku, context)) {
                        viewHolder.getPriceTextView().setText(R.string.store_purchased);
                    } else {
                        viewHolder.getPriceTextView().setText(googleSkuToPriceMap.get(sku));
                    }
                } else {
                    viewHolder.getPriceTextView().setText(R.string.loading);
                }
            } else {
                viewHolder.getPriceTextView().setText(R.string.loading);
            }
        }

        // price - purchase check from ownedSkus - 풀버전, 언락은 ownedSkus에서 체크
        if (ownedSkus != null && ownedSkus.contains(sku)) {
            viewHolder.getPriceTextView().setText(R.string.store_purchased);
        } else {
            if (!MNStoreDebugChecker.isUsingStore(context)) {
                viewHolder.getPriceTextView().setText("$0.99");
            }
        }

        // onClick
        viewHolder.getPriceTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MNSound.isSoundOn(context)) {
                    MNSoundEffectsPlayer.play(R.raw.effect_view_open, context);
                }
                if (MNStoreDebugChecker.isUsingStore(context)) {
                    storeGridViewOnClickListener.onItemClickedForNaver((String) v.getTag());
                } else {
                    storeGridViewOnClickListener.onItemClickedDebug((String) v.getTag());
                }
            }
        });

        // set clickable
        if (viewHolder.getPriceTextView().getText().toString().equals(context.getResources().getText(R.string.store_purchased))) {
            viewHolder.getPriceTextView().setClickable(false);
            viewHolder.getInnerLayout().setFocusable(false);
            viewHolder.getInnerLayout().setClickable(false);
            viewHolder.getInnerLayout().setBackgroundResource(
                    R.drawable.shape_rounded_view_classic_gray_normal);
        } else {
            viewHolder.getPriceTextView().setClickable(true);
            viewHolder.getInnerLayout().setFocusable(true);
            viewHolder.getInnerLayout().setClickable(true);
            viewHolder.getInnerLayout().setBackgroundResource(
                    R.drawable.shape_rounded_view_classic_gray);
        }
    }

    protected String makeStringComma(String priceString) {
        if (priceString == null || priceString.length() == 0)
            return "";
        long value = Long.parseLong(priceString);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }

    /**
     * ViewHolder
     */
    static class MNStoreGridViewItemViewHolder {
        @Getter @InjectView(R.id.setting_store_grid_item_inner_layout)      RelativeLayout innerLayout;
        @Getter @InjectView(R.id.setting_store_grid_item_name_textview)     TextView itemNameTextView;
        @Getter @InjectView(R.id.setting_store_grid_item_price_textview)    TextView priceTextView;
        @Getter @InjectView(R.id.setting_store_grid_item_imageview)         ImageView iconImageView;

        public MNStoreGridViewItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
