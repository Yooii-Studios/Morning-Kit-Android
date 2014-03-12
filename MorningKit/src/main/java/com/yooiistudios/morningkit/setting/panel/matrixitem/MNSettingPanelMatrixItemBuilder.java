package com.yooiistudios.morningkit.setting.panel.matrixitem;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.yooiistudios.morningkit.panel.core.MNPanelType;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingResources;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 2. 3.
 *
 * MNSettingPanelMatrixItemBuilder
 *  MNSettingPanelMatirxItem에 타입을 넣으면 해당하는 리소스와 텍스트를 넣어주는 클래스
 */
public class MNSettingPanelMatrixItemBuilder {
    private MNSettingPanelMatrixItemBuilder(){ throw new AssertionError("You MUST not create this class!"); }

    public static void buildItem(MNSettingPanelMatrixItem panelMatrixItem, MNPanelType panelType,
                                 Context context, final int position,
                                 final MNSettingPanelMatrixItemClickListener onClickListener) {
        if (panelMatrixItem != null && context != null) {

            // panelImageView
            ImageView panelImageView = panelMatrixItem.getPanelImageView();

            /*
            // recycle resources
            Drawable drawable = panelImageView.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    Log.i("MNSettingPanelMatrixItemBuilder", "bitmap recycled");
                } else {
                    Log.i("MNSettingPanelMatrixItemBuilder", "bitmap is null");
                }
                panelImageView.setImageDrawable(null);
            }
            */

            // new resources
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
            switch (panelType) {
                case WEATHER:
                    panelImageView.setImageResource(MNSettingResources.getWeatherResourceId(currentThemeType));
                    break;
                case DATE:
                    panelImageView.setImageResource(MNSettingResources.getDateResourceId(currentThemeType));
                    break;
                case CALENDAR:
                    panelImageView.setImageResource(MNSettingResources.getCalendarResourceId(currentThemeType));
                    break;
                case WORLD_CLOCK:
                    panelImageView.setImageResource(MNSettingResources.getWorldClockResourceId(currentThemeType));
                    break;
                case QUOTES:
                    panelImageView.setImageResource(MNSettingResources.getQuotesResourceId(currentThemeType));
                    break;
                case FLICKR:
                    panelImageView.setImageResource(MNSettingResources.getFlickrResourceId(currentThemeType));
                    break;
                case EXCHANGE_RATES:
                    panelImageView.setImageResource(MNSettingResources.getExchangeRatesResourceId(currentThemeType));
                    break;
                case MEMO:
                    panelImageView.setImageResource(MNSettingResources.getMemoResourceId(currentThemeType));
                    break;
                case DATE_COUNTDOWN:
                    panelImageView.setImageResource(MNSettingResources.getDateCountdownResourceId(currentThemeType));
                    break;
            }

            // text
            panelMatrixItem.getPanelNameTextView().setText(MNPanelType.toString(panelType.getIndex(), context));
            panelMatrixItem.getPanelNameTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));

            // shadowLayout onclick
            panelMatrixItem.getShadowLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onPanelItemClick(position);
                }
            });

            panelMatrixItem.setTag(position);
            panelMatrixItem.setPanelType(panelType);
        } else {
            throw new AssertionError("MNSettingPanelMatrixItem or Context is null!");
        }
    }
}
