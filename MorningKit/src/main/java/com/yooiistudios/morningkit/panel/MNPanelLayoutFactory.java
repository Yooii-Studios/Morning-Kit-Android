package com.yooiistudios.morningkit.panel;

import android.content.Context;

import com.yooiistudios.morningkit.panel.datecountdown.MNDateCountdownLayout;
import com.yooiistudios.morningkit.panel.flickr.MNFlickrPanelLayout;
import com.yooiistudios.morningkit.panel.memo.MNMemoPanelLayout;

import org.json.JSONException;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 16.
 *
 * MNPanelFactory
 *  패널 레이아웃을 생성해주는 클래스
 */
public class MNPanelLayoutFactory {
    private MNPanelLayoutFactory() { throw new AssertionError("You MUST not create this class!"); }

    public static MNPanelLayout newPanelLayoutInstance(MNPanelType newPanalType, int index,
                                                       Context context) {
        MNPanelLayout newPanelLayout = new MNPanelLayout(context);

        switch (newPanalType) {
            case WEATHER:
                newPanelLayout.setPanelType(MNPanelType.WEATHER);
                newPanelLayout.initNetworkPanel();
                break;

            case DATE:
                newPanelLayout.setPanelType(MNPanelType.DATE);
                break;

            case CALENDAR:
                newPanelLayout.setPanelType(MNPanelType.CALENDAR);
                break;

            case WORLD_CLOCK:
                newPanelLayout.setPanelType(MNPanelType.WORLD_CLOCK);
                break;

            case QUOTES:
                newPanelLayout.setPanelType(MNPanelType.QUOTES);
                break;

            case FLICKR:
                newPanelLayout = new MNFlickrPanelLayout(context);
                newPanelLayout.setPanelType(MNPanelType.FLICKR);
                newPanelLayout.initNetworkPanel();
                break;

            case EXCHANGE_RATES:
                newPanelLayout.setPanelType(MNPanelType.EXCHANGE_RATES);
                newPanelLayout.initNetworkPanel();
                break;

            case MEMO:
                newPanelLayout = new MNMemoPanelLayout(context);
                newPanelLayout.setPanelType(MNPanelType.MEMO);
                break;

            case DATE_COUNTDOWN:
                newPanelLayout = new MNDateCountdownLayout(context);
                newPanelLayout.setPanelType(MNPanelType.DATE_COUNTDOWN);
                break;

            default:
                throw new AssertionError("PanelType is not defined!");
        }
        // 인덱스와 패널 데이터 대입
        newPanelLayout.setPanelIndex(index);
        newPanelLayout.setPanelDataObject(MNPanel.getPanelDataList(context).get(index));

        try {
            // 패널 데이터에 unique Id, 인덱스 입력
            newPanelLayout.getPanelDataObject().put(MNPanel.PANEL_UNIQUE_ID,
                    newPanelLayout.getPanelType().getUniqueId());
            newPanelLayout.getPanelDataObject().put(MNPanel.PANEL_WINDOW_INDEX,
                    index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newPanelLayout;
    }
}
