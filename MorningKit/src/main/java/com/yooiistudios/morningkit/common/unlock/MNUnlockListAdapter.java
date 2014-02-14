package com.yooiistudios.morningkit.common.unlock;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.dp.DipToPixel;
import com.yooiistudios.morningkit.common.shadow.SlateThemeShadowLayout;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 27.
 *
 * MNUnlockListAdapter
 */
public class MNUnlockListAdapter extends BaseAdapter {

    private Context context;
    private MNUnlockOnClickListener onClickListener;
    private String productSku;

    private MNUnlockListAdapter(){}
    protected MNUnlockListAdapter(Context context, MNUnlockOnClickListener onClickListener, String productSku) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.productSku = productSku;
    }

    @Override
    public int getCount() {
        return 3;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.unlock_listview_item, parent, false);
        if (convertView != null) {
            MNUnlockListViewItemViewHolder viewHolder = new MNUnlockListViewItemViewHolder(convertView);
            boolean isCellUsed = false;

            List<String> owndSkus = SKIabProducts.loadOwnedIabProducts(context);

            // 사용 되었다면 폰트색은 a8a8a8, 아니라면 white
            switch (position) {
                case 0:
                    isCellUsed = owndSkus.contains(SKIabProducts.SKU_FULL_VERSION);
                    if (isCellUsed) {
                        viewHolder.getDescriptionTextView().setText(R.string.unlock_everything);
                        viewHolder.getIconImageView().setImageResource(R.drawable.unlock_fullversion_icon_off);
                    } else {
                        setPointColoredTextView(viewHolder.getDescriptionTextView(),
                                context.getString(R.string.unlock_everything),
                                context.getString(R.string.unlock_everything_highlight));
                        viewHolder.getIconImageView().setImageResource(R.drawable.unlock_fullversion_icon_on);
                    }
                    break;

                case 1:
                    isCellUsed = owndSkus.contains(productSku);
                    viewHolder.getDescriptionTextView().setText(R.string.unlock_only_this);
                    if (isCellUsed) {
                        viewHolder.getIconImageView().setImageResource(R.drawable.unlock_buyit_icon_off);
                    } else {
                        viewHolder.getIconImageView().setImageResource(R.drawable.unlock_buyit_icon_on);
                    }
                    break;

                case 2:
                    // 구매를 했다면 사용할 필요가 없고, 구매를 하지 않았다면 리뷰 아이템을 클릭했는지를 체크
                    if (owndSkus.contains(productSku) || owndSkus.contains(SKIabProducts.SKU_FULL_VERSION)) {
                        isCellUsed = true;
                    } else {
                        isCellUsed = context.getSharedPreferences(MNUnlockActivity.SHARED_PREFS, Context.MODE_PRIVATE)
                                .getBoolean(MNUnlockActivity.REVIEW_USED, false);
                    }

                    if (isCellUsed) {
                        viewHolder.getDescriptionTextView().setText(R.string.unlock_review);
                        viewHolder.getIconImageView().setImageResource(R.drawable.unlock_rating_icon_off);
                    } else {
                        setPointColoredTextView(viewHolder.getDescriptionTextView(),
                                context.getString(R.string.unlock_review),
                                context.getString(R.string.unlock_review_highlight));
                        viewHolder.getIconImageView().setImageResource(R.drawable.unlock_rating_icon_on);
                    }
                    break;
            }
            viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getForwardBackgroundColor(MNThemeType.SLATE_GRAY));
            viewHolder.getShadowLayout().setRoundRectRadius(DipToPixel.dpToPixel(context, 5));

            if (isCellUsed) {
                viewHolder.getDescriptionTextView().setTextColor(Color.parseColor("#a8a8a8"));
                viewHolder.getShadowLayout().setSolidAreaColor(Color.parseColor("#454545"));
                viewHolder.getShadowLayout().setTouchEnabled(false);
                viewHolder.getShadowLayout().setOnClickListener(null);
            } else {
                viewHolder.getShadowLayout().setSolidAreaColor(Color.parseColor("#5b5b5b"));
                viewHolder.getShadowLayout().setTouchEnabled(true);
                viewHolder.getShadowLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onItemClick(position);
                    }
                });
            }
        }
        return convertView;
    }

    private void setPointColoredTextView(TextView textView, String descriptionString, String pointedString) {
        if (textView != null) {
            SpannableString spannableString = new SpannableString(descriptionString);

            int pointedStringIndex = descriptionString.indexOf(pointedString);
            if (pointedStringIndex != -1) {
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#00ccff")),
                        pointedStringIndex, pointedStringIndex + pointedString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                textView.setText(spannableString);
            }
        } else {
            throw new AssertionError("TextView shouldn't be null");
        }
    }

    /**
     * ViewHolder
     */
    static class MNUnlockListViewItemViewHolder {
        @Getter @InjectView(R.id.unlock_listview_item_outer_layout)         RelativeLayout outerLayout;
        @Getter @InjectView(R.id.unlock_listview_item_inner_layout)         RelativeLayout innerLayout;
        @Getter @InjectView(R.id.unlock_listview_item_shadow_layout)
        SlateThemeShadowLayout shadowLayout;
        @Getter @InjectView(R.id.unlock_listview_item_imageview)            ImageView iconImageView;
        @Getter @InjectView(R.id.unlock_listview_item_description_textview) TextView descriptionTextView;

        public MNUnlockListViewItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
