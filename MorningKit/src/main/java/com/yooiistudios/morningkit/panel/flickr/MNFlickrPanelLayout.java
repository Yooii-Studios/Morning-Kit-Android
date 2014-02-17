package com.yooiistudios.morningkit.panel.flickr;

import android.content.Context;
import android.util.AttributeSet;

import com.yooiistudios.morningkit.panel.MNPanelLayout;
import com.yooiistudios.morningkit.panel.MNPanelType;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 17.
 *
 * MNFlickrPanelLayout
 */
public class MNFlickrPanelLayout extends MNPanelLayout {
    public MNFlickrPanelLayout(Context context) {
        super(context);
    }

    public MNFlickrPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        initNetworkPanel();
        setPanelType(MNPanelType.FLICKR);
    }

    @Override
    protected void processLoading() {
        super.processLoading();

        // 플리커 키워드를 받아온다
        String keyword = "Morning";

        // 플리커 키워드를 가지고 사진 url을 추출

        // 사진 url을 통해 이미지 출력

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void updateUI() {
        stopLoadingAnimation();
        super.updateUI();
    }
}
