package com.yooiistudios.morningkit.panel.flickr.detail;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailFragment;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

import static com.yooiistudios.morningkit.panel.flickr.MNFlickrPanelLayout.FLICKR_DATA_GRAYSCALE;
import static com.yooiistudios.morningkit.panel.flickr.MNFlickrPanelLayout.FLICKR_DATA_KEYWORD;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 21.
 *
 * MNFlickrDetailFragment
 */
public class MNFlickrDetailFragment extends MNPanelDetailFragment {

    @InjectView(R.id.flickr_detail_imageview) RecyclingImageView imageView;
    @InjectView(R.id.flickr_detail_edittext) EditText keywordEditText;
    @InjectView(R.id.flickr_detail_grayscale_textview) TextView grayScaleTextView;
    @Optional @InjectView(R.id.flickr_detail_grayscale_checkbox) CheckBox grayscaleCheckbox; // < V14
    Switch grayscaleSwitch; // >= V14

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_flickr_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);
            // 버전 체크해 스위치 얻어내기
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                grayscaleSwitch = (Switch) rootView.findViewById(R.id.flickr_detail_grayscale_switch);
            }
            try {
//                Bitmap bitmap = MNBitmapProcessor.getBitmapFromString(getPanelDataObject().getString("imageData"));
//                if (bitmap != null) {
//                    imageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(), bitmap));
//                }
                keywordEditText.setText(getPanelDataObject().getString(FLICKR_DATA_KEYWORD));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }

    @Override
    protected void archivePanelData() {
        MNLog.now("archivePanelData");

        try {
            // grayscale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                getPanelDataObject().put(FLICKR_DATA_GRAYSCALE, grayscaleSwitch.isChecked());
            } else {
                getPanelDataObject().put(FLICKR_DATA_GRAYSCALE, grayscaleCheckbox.isChecked());
            }
            getPanelDataObject().put(FLICKR_DATA_KEYWORD, keywordEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
