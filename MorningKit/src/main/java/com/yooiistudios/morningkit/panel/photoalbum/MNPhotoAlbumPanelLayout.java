package com.yooiistudios.morningkit.panel.photoalbum;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 5. 13.
 *
 * MNPhotoAlbumPanelLayout
 *  사진 패널 레이아웃 by 동현
 */
public class MNPhotoAlbumPanelLayout extends MNPanelLayout {
    private static final String TAG = "MNPhotoAlbumPanelLayout";

//    public static final String KEY_TYPE = "type";
    public static final String KEY_DATA_INTERVAL_MINUTE = "minute";
    public static final String KEY_DATA_INTERVAL_SECOND = "second";
    public static final String KEY_DATA_USE_REFRESH = "use refresh";
    public static final String KEY_DATA_TRANS_TYPE = "transition type";
    public static final String KEY_DATA_FILE_PARENT_LIST = "selected files";
    public static final String KEY_DATA_FILE_ROOT = "selected file's root dir";
    public static final String KEY_DATA_FILE_FILELIST = "selected file list";

    public static final String KEY_FILE_LIST = "file list info";
    public static final String KEY_FILELIST_PARENT = "parent dir";
    public static final String KEY_FILELIST_FILES = "file list";

    public MNPhotoAlbumPanelLayout(Context context) {
        super(context);
    }
    public MNPhotoAlbumPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private ImageView imageView;

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
        if (getPanelDataObject().has(KEY_DATA_FILE_ROOT)) {
            MNLog.i(TAG, "root : " +
                    getPanelDataObject().getString(KEY_DATA_FILE_ROOT));
        }
        if (getPanelDataObject().has(KEY_DATA_FILE_PARENT_LIST)) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> tempArr = new Gson().fromJson(
                    getPanelDataObject().getString(KEY_DATA_FILE_PARENT_LIST),
                    type);
            for (String name : tempArr) {
                MNLog.i(TAG, "parent : " + name);
            }
        }
        if (getPanelDataObject().has(KEY_DATA_FILE_FILELIST)) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> tempArr = new Gson().fromJson(
                    getPanelDataObject().getString(KEY_DATA_FILE_FILELIST),
                    type);
            for (String name : tempArr) {
                MNLog.i(TAG, "file : " + name);
            }
        }
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

//        MNViewSizeMeasure.setViewSizeObserver(imageView, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
//            @Override
//            public void onLayoutLoad() {
//                try {
//                    getPolishedFlickrBitmap();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
