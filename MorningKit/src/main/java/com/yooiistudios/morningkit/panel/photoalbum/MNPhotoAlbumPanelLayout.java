package com.yooiistudios.morningkit.panel.photoalbum;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumDisplayHelper;
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

    public static final String KEY_FILE_LIST = "file list info";
    public static final String KEY_FILELIST_PARENT = "parent dir";
    public static final String KEY_FILELIST_FILES = "file list";

    private ViewSwitcher mViewSwitcher;
    private MNPhotoAlbumTransitionType mTransitionType;
    private long mIntervalInMillisec;
    private String mRootDir;
    private ArrayList<String> mParentList;
    private ArrayList<String> mFileList;
    private ArrayList<File> mAllAbsoluteImageFileList;

    private MNPhotoAlbumDisplayHelper mDisplayHelper;

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

        mViewSwitcher = new ViewSwitcher(context);
        mViewSwitcher.addView(new ImageView(context),
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        mViewSwitcher.addView(new ImageView(context),
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        addView(mViewSwitcher,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
//        initAnalogClockUI();
//        initDigitalClockUI();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        MNLog.i(TAG, "onAttachedToWindow");

        startTimer();
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        MNLog.i(TAG, "onDetachedFromWindow");

        stopTimer();
    }
    private void startTimer() {
        MNLog.i("Timer", "start called");
        if (mDisplayHelper != null && !mDisplayHelper.isRunning()) {
            mDisplayHelper.start();
            MNLog.i("Timer", "started. isRunning : " + mDisplayHelper.isRunning());
        }
    }
    private void stopTimer() {
        MNLog.i("Timer", "stop called");
        if (mDisplayHelper != null) {
            mDisplayHelper.stop();
            mDisplayHelper = null;
            MNLog.i("Timer", "stopped");
            // isRunning : " + mDisplayHelper.isRunning()
        }
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        if (getPanelDataObject().has(KEY_DATA_FILE_ROOT)) {
            mRootDir = getPanelDataObject().getString(KEY_DATA_FILE_ROOT);
            MNLog.i(TAG, "root : " + mRootDir);
        }
        else {
            mRootDir = null;
        }
        if (getPanelDataObject().has(KEY_DATA_FILE_PARENT_LIST)) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            mParentList = new Gson().fromJson(
                    getPanelDataObject().getString(KEY_DATA_FILE_PARENT_LIST),
                    type);
            for (String name : mParentList) {
                MNLog.i(TAG, "parent : " + name);
            }
        }
        else {
            mParentList = new ArrayList<String>();
            mParentList.add(MNPhotoAlbumDetailFragment.DEFAULT_PARENT_DIR
                    .getAbsolutePath());

        }
        if (getPanelDataObject().has(KEY_DATA_FILE_FILELIST)) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            mFileList = new Gson().fromJson(
                    getPanelDataObject().getString(KEY_DATA_FILE_FILELIST),
                    type);
            for (String name : mFileList) {
                MNLog.i(TAG, "file : " + name);
            }
        }
        else {
            mFileList = null;
        }

        if (getPanelDataObject().has(KEY_DATA_TRANS_TYPE)) {
            String key = getPanelDataObject().getString(KEY_DATA_TRANS_TYPE);
            mTransitionType = MNPhotoAlbumTransitionType.getTypeByKey(key);
        }
        else {
            mTransitionType = MNPhotoAlbumTransitionType.NONE;
        }
        if (getPanelDataObject().has(KEY_DATA_INTERVAL_SECOND) &&
                getPanelDataObject().has(KEY_DATA_INTERVAL_MINUTE)) {
            int intervalSecond = getPanelDataObject()
                    .getInt(KEY_DATA_INTERVAL_SECOND);
            int intervalMinute = getPanelDataObject()
                    .getInt(KEY_DATA_INTERVAL_MINUTE);

            mIntervalInMillisec = (intervalMinute*60 + intervalSecond)*1000;
        }
        else {
            mIntervalInMillisec = MNPhotoAlbumDetailFragment.INVALID_INTERVAL;
        }


//        boolean test = true;
//        if (test) {
//            return;
//        }

        MNViewSizeMeasure.setViewSizeObserver(mViewSwitcher, new MNViewSizeMeasure
                .OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad() {

                MNPhotoAlbumListFetcher listFetcher = new MNPhotoAlbumListFetcher(
                        mParentList, mRootDir, mFileList,
                        new MNPhotoAlbumListFetcher.OnListFetchListener() {
                            @Override
                            public void onPhotoListFetch(ArrayList<File> photoList) {
                                if (photoList != null) {
                                    for (File file : photoList) {
                                        MNLog.i(TAG, file.getAbsolutePath());
                                    }
                                    mAllAbsoluteImageFileList = photoList;
                                }

                                if (mAllAbsoluteImageFileList != null) {
                                    MNLog.i(TAG, "create display helper " +
                                            "instance.");
                                    mDisplayHelper =
                                            new MNPhotoAlbumDisplayHelper(
                                                    (Activity) getContext(),
                                                    mViewSwitcher,
                                                    mAllAbsoluteImageFileList,
                                                    mTransitionType,
                                                    mIntervalInMillisec,
                                                    mViewSwitcher.getWidth(),
                                                    mViewSwitcher.getHeight());
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
