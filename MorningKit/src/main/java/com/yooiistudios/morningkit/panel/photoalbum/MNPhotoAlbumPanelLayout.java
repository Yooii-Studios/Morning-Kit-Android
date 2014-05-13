package com.yooiistudios.morningkit.panel.photoalbum;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yooiistudios.morningkit.panel.core.MNPanelLayout;

import org.json.JSONException;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 5. 13.
 *
 * MNPhotoAlbumPanelLayout
 *  사진 패널 레이아웃 by 동현
 */
public class MNPhotoAlbumPanelLayout extends MNPanelLayout {
    public MNPhotoAlbumPanelLayout(Context context) {
        super(context);
    }

    public MNPhotoAlbumPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        TextView tempTextView = new TextView(getContext().getApplicationContext());
        tempTextView.setText("PhotoAlbum Test");
        addView(tempTextView);
//        initAnalogClockUI();
//        initDigitalClockUI();
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();
    }

    @Override
    protected void updateUI() {
        super.updateUI();
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
