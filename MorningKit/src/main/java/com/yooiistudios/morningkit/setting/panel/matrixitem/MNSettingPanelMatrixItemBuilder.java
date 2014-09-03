package com.yooiistudios.morningkit.setting.panel.matrixitem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.ImageView;

import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;
import com.yooiistudios.morningkit.panel.core.MNPanelType;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingResources;
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
//            MNLog.i("MNSettingPanelMatrixItemBuilder", "recycle panelImageView");
            MNBitmapUtils.recycleImageView(panelImageView);

            // new resources
            int panelImageResourceId = 0;

            switch (panelType) {
                case WEATHER:
                    panelImageResourceId = MNSettingResources.getWeatherResourceId();
                    break;
                case DATE:
                    panelImageResourceId = MNSettingResources.getDateResourceId();
                    break;
                case CALENDAR:
                    panelImageResourceId = MNSettingResources.getCalendarResourceId();
                    break;
                case WORLD_CLOCK:
                    panelImageResourceId = MNSettingResources.getWorldClockResourceId();
                    break;
                case QUOTES:
                    panelImageResourceId = MNSettingResources.getQuotesResourceId();
                    break;
                case FLICKR:
                    panelImageResourceId = MNSettingResources.getFlickrResourceId();
                    break;
                case EXCHANGE_RATES:
                    panelImageResourceId = MNSettingResources.getExchangeRatesResourceId();
                    break;
                case MEMO:
                    panelImageResourceId = MNSettingResources.getMemoResourceId();
                    break;
                case DATE_COUNTDOWN:
                    panelImageResourceId = MNSettingResources.getDateCountdownResourceId();
                    break;
                case PHOTO_FRAME:
                    panelImageResourceId = MNSettingResources.getPhotoFrameResourceId();
                    break;
                case NEWS_FEED:
                    panelImageResourceId = MNSettingResources.getNewsResourceId();
                    break;
                case CAT:
                    panelImageResourceId = MNSettingResources.getCatResourceId();
                    break;
            }

            // pastel green 컬러 필터, 예외적인 아트는 은실이 따로 제작
            switch (panelType) {
                case EXCHANGE_RATES:
                case WORLD_CLOCK:
                case FLICKR:
                    panelMatrixItem.getPanelImageView().setColorFilter(null);
                    break;

                default:
                    int highlightColor = MNSettingColors.getSubFontColor(MNThemeType.PASTEL_GREEN);
                    PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor,
                            PorterDuff.Mode.SRC_ATOP);
                    panelMatrixItem.getPanelImageView().setColorFilter(colorFilter);
                    break;
            }

            Bitmap panelImageBitmap = BitmapFactory.decodeResource(
                    context.getApplicationContext().getResources(),
                    panelImageResourceId, MNBitmapUtils.getDefaultOptions());
            panelImageView.setImageBitmap(panelImageBitmap);

            // text
            panelMatrixItem.getPanelNameTextView().setText(MNPanelType.toString(panelType.getIndex(), context));
            panelMatrixItem.getPanelNameTextView().setTextColor(MNSettingColors.getSubFontColor(MNThemeType.PASTEL_GREEN));

            // onclick
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
