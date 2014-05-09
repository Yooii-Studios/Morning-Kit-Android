package com.yooiistudios.morningkit.setting.panel.matrixitem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;
import com.yooiistudios.morningkit.common.log.MNLog;
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

            // recycle resources
            MNLog.i("MNSettingPanelMatrixItemBuilder", "recycle panelImageView");
            MNBitmapUtils.recycleImageView(panelImageView);

            // new resources
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
            int panelImageResourceId = 0;

            switch (panelType) {
                case WEATHER:
                    panelImageResourceId = MNSettingResources.getWeatherResourceId(currentThemeType);
                    break;
                case DATE:
                    panelImageResourceId = MNSettingResources.getDateResourceId(currentThemeType);
                    break;
                case CALENDAR:
                    panelImageResourceId = MNSettingResources.getCalendarResourceId(currentThemeType);
                    break;
                case WORLD_CLOCK:
                    panelImageResourceId = MNSettingResources.getWorldClockResourceId(currentThemeType);
                    break;
                case QUOTES:
                    panelImageResourceId = MNSettingResources.getQuotesResourceId(currentThemeType);
                    break;
                case FLICKR:
                    panelImageResourceId = MNSettingResources.getFlickrResourceId(currentThemeType);
                    break;
                case EXCHANGE_RATES:
                    panelImageResourceId = MNSettingResources.getExchangeRatesResourceId(currentThemeType);
                    break;
                case MEMO:
                    panelImageResourceId = MNSettingResources.getMemoResourceId(currentThemeType);
                    break;
                case DATE_COUNTDOWN:
                    panelImageResourceId = MNSettingResources.getDateCountdownResourceId(currentThemeType);
                    break;
            }

            Bitmap panelImageBitmap = BitmapFactory.decodeResource(
                    context.getApplicationContext().getResources(),
                    panelImageResourceId);
            panelImageView.setImageBitmap(panelImageBitmap);

            // text
            panelMatrixItem.getPanelNameTextView().setText(MNPanelType.toString(panelType.getIndex(), context));
            panelMatrixItem.getPanelNameTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));

            // shadowLayout onclick
            panelMatrixItem.getContainerLayout().setOnClickListener(new View.OnClickListener() {
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
