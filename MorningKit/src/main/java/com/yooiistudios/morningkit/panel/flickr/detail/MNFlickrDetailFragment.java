package com.yooiistudios.morningkit.panel.flickr.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailFragment;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 21.
 *
 * MNFlickrDetailFragment
 */
public class MNFlickrDetailFragment extends MNPanelDetailFragment {

    @InjectView(R.id.flickr_detail_imageview) RecyclingImageView imageView;
    @InjectView(R.id.flickr_detail_edittext) EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_flickr_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            try {
//                Bitmap bitmap = MNBitmapProcessor.getBitmapFromString(getPanelDataObject().getString("imageData"));
//                if (bitmap != null) {
//                    imageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(), bitmap));
//                }
                editText.setText(getPanelDataObject().getString("keyword"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }
}
