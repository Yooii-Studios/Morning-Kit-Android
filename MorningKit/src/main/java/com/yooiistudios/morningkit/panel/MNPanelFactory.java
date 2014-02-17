package com.yooiistudios.morningkit.panel;

import android.content.Context;

import com.yooiistudios.morningkit.panel.flickr.MNFlickrPanelLayout;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 16.
 *
 * MNPanelFactory
 *  패널 레이아웃을 생성해주는 클래스
 */
public class MNPanelFactory {
    private MNPanelFactory() { throw new AssertionError("You MUST not create this class!"); }

    public static MNPanelLayout newPanelLayoutInstance(MNPanelType newPanalType, Context context) {
        MNPanelLayout newPanalLayout = new MNPanelLayout(context);

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
        return newPanalLayout;
    }
}
