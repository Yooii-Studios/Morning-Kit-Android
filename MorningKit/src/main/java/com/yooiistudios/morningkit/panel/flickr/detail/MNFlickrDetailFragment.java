package com.yooiistudios.morningkit.panel.flickr.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.stevenkim.waterlily.bitmapfun.util.RecyclingBitmapDrawable;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapProcessor;
import com.yooiistudios.morningkit.common.file.ExternalStorageManager;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.MNPanel;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailFragment;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

import static com.yooiistudios.morningkit.panel.flickr.MNFlickrPanelLayout.FLICKR_DATA_GRAYSCALE;
import static com.yooiistudios.morningkit.panel.flickr.MNFlickrPanelLayout.FLICKR_DATA_KEYWORD;
import static com.yooiistudios.morningkit.panel.flickr.MNFlickrPanelLayout.FLICKR_DATA_PHOTO_ID;
import static com.yooiistudios.morningkit.panel.flickr.MNFlickrPanelLayout.FLICKR_PREFS;
import static com.yooiistudios.morningkit.panel.flickr.MNFlickrPanelLayout.FLICKR_PREFS_KEYWORD;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 21.
 *
 * MNFlickrDetailFragment
 */
public class MNFlickrDetailFragment extends MNPanelDetailFragment implements MNFlickrBitmapSaveAsyncTask.MNFlickrBitmapSaveAsyncTaskListener {

    private static final String TAG = "MNFlickrDetailFragment";

    @InjectView(R.id.flickr_detail_imageview) RecyclingImageView imageView;
    @InjectView(R.id.flickr_detail_edittext) EditText keywordEditText;
    @InjectView(R.id.flickr_detail_grayscale_textview) TextView grayScaleTextView;
    @Optional @InjectView(R.id.flickr_detail_grayscale_checkbox) CheckBox grayscaleCheckbox; // < V14
    Switch grayscaleSwitch; // >= V14

    boolean isGrayScale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_flickr_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 버전 체크해 스위치 얻어내기
            isGrayScale = false;
            if (getPanelDataObject().has(FLICKR_DATA_GRAYSCALE)) {
                try {
                    isGrayScale = getPanelDataObject().getBoolean(FLICKR_DATA_GRAYSCALE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                grayscaleSwitch = (Switch) rootView.findViewById(R.id.flickr_detail_grayscale_switch);
                grayscaleSwitch.setChecked(isGrayScale);
            } else {
                grayscaleCheckbox.setChecked(isGrayScale);
            }

            // 비트맵 로컬에서 읽어오기
            try {
                Bitmap bitmap = MNBitmapProcessor.loadBitmapFromDirectory(getActivity(),
                        "flickr_" + getPanelDataObject().getInt(MNPanel.PANEL_INDEX),
                        ExternalStorageManager.APP_DIRECTORY_HIDDEN + "/flickr");
                if (bitmap != null) {
                    imageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(), bitmap));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    setImgViewOnClickListener();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 키워드 텍스트
            try {
                String keywordString = getPanelDataObject().getString(FLICKR_DATA_KEYWORD);
                keywordEditText.setText(keywordString);
                keywordEditText.setSelection(keywordString.length());
                keywordEditText.setPrivateImeOptions("defaultInputmode=english;");
                keywordEditText.requestFocus();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 그레이스케일 텍스트뷰
            grayScaleTextView.setText(R.string.flickr_use_gray_scale);

        }
        return rootView;
    }

    @Override
    protected void archivePanelData() throws JSONException {
        MNLog.now("archivePanelData");

        // grayscale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getPanelDataObject().put(FLICKR_DATA_GRAYSCALE, grayscaleSwitch.isChecked());
        } else {
            getPanelDataObject().put(FLICKR_DATA_GRAYSCALE, grayscaleCheckbox.isChecked());
        }

        // 키워드 길이가 0 이상일 경우에만 적용
        if (keywordEditText.getText().length() > 0) {
            getPanelDataObject().put(FLICKR_DATA_KEYWORD, keywordEditText.getText().toString());

            // SharedPreferences에 키워드 아카이빙
            SharedPreferences prefs = getActivity().getSharedPreferences(FLICKR_PREFS, Context.MODE_PRIVATE);
            prefs.edit().putString(FLICKR_PREFS_KEYWORD, keywordEditText.getText().toString()).commit();
        }
    }

    private void setImgViewOnClickListener() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPanelDataObject().has(FLICKR_DATA_PHOTO_ID)) {
                    // AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getResources().getString(R.string.flickr_save_to_library_guide))
                            .setPositiveButton(getResources().getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                saveImageToLibrary(getPanelDataObject().getString(FLICKR_DATA_PHOTO_ID));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                            .setNegativeButton(getResources().getString(R.string.cancel), null)
                            .create().show();
                }
            }
        });
    }

    private void saveImageToLibrary(String flickrUrlString) {
        new MNFlickrBitmapSaveAsyncTask(flickrUrlString, isGrayScale, this, getActivity()).execute();
    }

    /**
     * 사진 저장 후 콜백. 사실 의미없는 메서드
     */
    @Override
    public void onBitmapSaveFinished() {
        MNLog.i(TAG, "onBitmapSaveFinished");
    }

    @Override
    public void onBitmapSaveFailed() {
        MNLog.e(TAG, "onBitmapSaveFailed");
    }
}
