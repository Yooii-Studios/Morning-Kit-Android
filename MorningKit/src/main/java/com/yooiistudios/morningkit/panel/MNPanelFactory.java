package com.yooiistudios.morningkit.panel;

import android.content.Context;

import com.yooiistudios.morningkit.panel.flickr.MNFlickrPanelLayout;

import org.json.JSONException;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 16.
 *
 * MNPanelFactory
 *  패널 레이아웃을 생성해주는 클래스
 */
public class MNPanelFactory {
    private MNPanelFactory() { throw new AssertionError("You MUST not create this class!"); }

    public static MNPanelLayout newPanelLayoutInstance(MNPanelType newPanalType, int index,
                                                       Context context) {
        MNPanelLayout newPanalLayout = new MNPanelLayout(context);
        newPanalLayout.setIndex(index);

        switch (newPanalType) {
            case WEATHER:
                newPanalLayout.setPanelType(MNPanelType.WEATHER);
                newPanalLayout.initNetworkPanel();
                break;

            case DATE:
                newPanalLayout.setPanelType(MNPanelType.DATE);
                break;

            case CALENDAR:
                newPanalLayout.setPanelType(MNPanelType.CALENDAR);
                break;

            case WORLD_CLOCK:
                newPanalLayout.setPanelType(MNPanelType.WORLD_CLOCK);
                break;

            case QUOTES:
                newPanalLayout.setPanelType(MNPanelType.QUOTES);
                break;

            case FLICKR:
                newPanalLayout = new MNFlickrPanelLayout(context);
                break;

            case EXCHANGE_RATES:
                newPanalLayout.setPanelType(MNPanelType.EXCHANGE_RATES);
                newPanalLayout.initNetworkPanel();
                break;

            case MEMO:
                newPanalLayout.setPanelType(MNPanelType.MEMO);
                break;

            case DATE_COUNTDOWN:
                newPanalLayout.setPanelType(MNPanelType.DATE_COUNTDOWN);
                break;

            default:
                break;
        }
        try {
            // unique Id, 인덱스 입력
            newPanalLayout.getPanelDataObject().put(MNPanel.PANEL_UNIQUE_ID,
                    newPanalLayout.getPanelType().getUniqueId());
            newPanalLayout.getPanelDataObject().put(MNPanel.PANEL_INDEX,
                    index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newPanalLayout;
    }
}
