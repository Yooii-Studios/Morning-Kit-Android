package com.yooiistudios.morningkit.panel.photoalbum;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumDisplayHelper;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumImageView;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumListFetcher;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumTransitionType;

import org.json.JSONException;

import java.io.File;
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
    public static final String KEY_DATA_USE_GRAYSCALE = "use grayscale";

    public static final String KEY_FILE_LIST = "file list info";
    public static final String KEY_FILELIST_PARENT = "parent dir";
    public static final String KEY_FILELIST_FILES = "file list";

    private ViewSwitcher viewSwitcher;
    private MNPhotoAlbumTransitionType transitionType;
    private long intervalInMillisec;
    private String rootDir;
    private ArrayList<String> parentList;
    private ArrayList<String> fileList;
    private ArrayList<File> allAbsoluteImageFileList;
    private boolean useGrayscale;

    private MNPhotoAlbumDisplayHelper displayHelper;

    public MNPhotoAlbumPanelLayout(Context context) {
        super(context);
    }
    public MNPhotoAlbumPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        Context context = getContext().getApplicationContext();

        TextView tempTextView = new TextView(context);
        tempTextView.setText("PhotoAlbum Test");
        addView(tempTextView);

        viewSwitcher = new ViewSwitcher(context);
        viewSwitcher.addView(new MNPhotoAlbumImageView(context),
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );
        viewSwitcher.addView(new MNPhotoAlbumImageView(context),
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );

        addView(viewSwitcher,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );
    }

    @Override
    public void onActivityResume() {
        super.onActivityResume();
        MNLog.i(TAG, "onActivityResume");

        MNViewSizeMeasure.setViewSizeObserver(MNPhotoAlbumPanelLayout.this,
                new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad() {

                MNPhotoAlbumListFetcher listFetcher = new MNPhotoAlbumListFetcher(
                        parentList, rootDir, fileList,
                        new MNPhotoAlbumListFetcher.OnListFetchListener() {
                            @Override
                            public void onPhotoListFetch(ArrayList<File> photoList) {
                                if (photoList != null) {
                                    for (File file : photoList) {
                                        MNLog.i(TAG, file.getAbsolutePath());
                                    }
                                    allAbsoluteImageFileList = photoList;
                                }

                                if (allAbsoluteImageFileList != null) {
                                    MNLog.i(TAG, "create display helper " +
                                            "instance.");
                                    displayHelper =
                                        new MNPhotoAlbumDisplayHelper(
                                                (Activity) getContext(),
                                                viewSwitcher,
                                                MNPhotoAlbumPanelLayout.this,
                                                allAbsoluteImageFileList,
                                                transitionType,
                                                intervalInMillisec,
                                                MNPhotoAlbumPanelLayout.this
                                                        .getWidth(),
                                                MNPhotoAlbumPanelLayout.this
                                                        .getHeight(),
                                                useGrayscale);
//                                    displayHelper
//                                        .notifyContainingActivityWillBeShown();
                                    startTimer();
                                }
                            }
                        }
                );
                listFetcher.execute();
            }
        });
    }

    @Override
    public void onActivityPause() {
        super.onActivityPause();
        MNLog.i(TAG, "onActivityPause");

        if (displayHelper != null) {
            displayHelper.notifyContainingActivityWillBeGone();
        }
        stopTimer();
    }

    private void startTimer() {
        MNLog.i("Timer", "start called");
        if (displayHelper != null && !displayHelper.isRunning()) {
            displayHelper.start();
            MNLog.i("Timer", "started. isRunning : " + displayHelper.isRunning());
        }
    }
    private void stopTimer() {
        MNLog.i("Timer", "stop called");
        if (displayHelper != null) {
            displayHelper.stop();
            displayHelper = null;
            MNLog.i("Timer", "stopped");
            // isRunning : " + displayHelper.isRunning()
        }
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        if (getPanelDataObject().has(KEY_DATA_FILE_ROOT)) {
            rootDir = getPanelDataObject().getString(KEY_DATA_FILE_ROOT);
            MNLog.i(TAG, "root : " + rootDir);
        }
        else {
            rootDir = null;
        }
        if (getPanelDataObject().has(KEY_DATA_FILE_PARENT_LIST)) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            parentList = new Gson().fromJson(
                    getPanelDataObject().getString(KEY_DATA_FILE_PARENT_LIST),
                    type);
            for (String name : parentList) {
                MNLog.i(TAG, "parent : " + name);
            }
        }
        else {
            parentList = new ArrayList<String>();
            parentList.add(MNPhotoAlbumDetailFragment.DEFAULT_PARENT_DIR
                    .getAbsolutePath());

        }
        if (getPanelDataObject().has(KEY_DATA_FILE_FILELIST)) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            fileList = new Gson().fromJson(
                    getPanelDataObject().getString(KEY_DATA_FILE_FILELIST),
                    type);
            for (String name : fileList) {
                MNLog.i(TAG, "file : " + name);
            }
        }
        else {
            fileList = null;
        }

        if (getPanelDataObject().has(KEY_DATA_TRANS_TYPE)) {
            String key = getPanelDataObject().getString(KEY_DATA_TRANS_TYPE);
            transitionType = MNPhotoAlbumTransitionType.getTypeByKey(key);
        }
        else {
            transitionType = MNPhotoAlbumTransitionType.NONE;
        }
        if (getPanelDataObject().has(KEY_DATA_INTERVAL_SECOND) &&
                getPanelDataObject().has(KEY_DATA_INTERVAL_MINUTE)) {
            int intervalSecond = getPanelDataObject()
                    .getInt(KEY_DATA_INTERVAL_SECOND);
            int intervalMinute = getPanelDataObject()
                    .getInt(KEY_DATA_INTERVAL_MINUTE);

            intervalInMillisec = (intervalMinute*60 + intervalSecond)*1000;
        }
        else {
            intervalInMillisec = MNPhotoAlbumDetailFragment.INVALID_INTERVAL;
        }

        if (getPanelDataObject().has(KEY_DATA_USE_GRAYSCALE)) {
            useGrayscale =
                    getPanelDataObject().getBoolean(KEY_DATA_USE_GRAYSCALE);
        }
        else {
            useGrayscale = false;
        }



//        boolean test = true;
//        if (test) {
//            return;
//        }

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
