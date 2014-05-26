package com.yooiistudios.morningkit.panel.photoalbum;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumCommonUtil;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumDisplayHelper;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumImageView;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumListFetcher;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumTransitionType;

import org.json.JSONException;

import java.util.ArrayList;

import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumDetailFragment.INVALID_INTERVAL;

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
//    public static final String KEY_DATA_FILE_PARENT_LIST = "selected files";
    public static final String KEY_DATA_FILE_SELECTED = "selected file";
    public static final String KEY_DATA_FILE_ROOT = "selected file's root dir";
//    public static final String KEY_DATA_FILE_FILELIST = "selected file list";
    public static final String KEY_DATA_USE_GRAYSCALE = "use grayscale";

    public static final String KEY_FILE_LIST = "file list info";
    public static final String KEY_FILELIST_PARENT = "parent dir";
    public static final String KEY_FILELIST_FILES = "file list";

    private ViewSwitcher viewSwitcher;
    private MNPhotoAlbumTransitionType transitionType;
    private long intervalInMillisec;
    private String rootDir;
    private String selectedFile;
//    private ArrayList<String> parentList;
//    private ArrayList<String> fileList;
    private ArrayList<String> allAbsoluteImageFileList;
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

        int strokeMargin = getResources().getDimensionPixelSize(R.dimen.theme_shape_width_stroke);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(strokeMargin, strokeMargin, strokeMargin, strokeMargin);

        getContentLayout().addView(viewSwitcher, lp);
    }

    @Override
    public void onActivityResume() {
        super.onActivityResume();
        MNLog.i(TAG, "onActivityResume");
        if (displayHelper != null) {
            displayHelper.notifyContainingActivityWillBeShown();
        }
        updateUI();
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

    private void startTimer(int photoWidth, int photoHeight) {
        MNLog.i("Timer", "start called");
        if (displayHelper != null && !displayHelper.isRunning()) {
            ArrayList<String> list;
            if (selectedFile != null) {
                if (intervalInMillisec == INVALID_INTERVAL) {
                    list = new ArrayList<String>();
                    list.add(selectedFile);
                }
                else {
                    list = allAbsoluteImageFileList;
                }
            }
            else {
                list = allAbsoluteImageFileList;
            }
            displayHelper.start(rootDir, list,
                    transitionType, intervalInMillisec, useGrayscale,
                    photoWidth, photoHeight);
            MNLog.i("Timer", "started. isRunning : " + displayHelper.isRunning());
        }
    }
    private void stopTimer() {
        MNLog.i("Timer", "stop called");
        if (displayHelper != null) {
            displayHelper.stop();
//            displayHelper = null;
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

        if (getPanelDataObject().has(KEY_DATA_FILE_SELECTED)) {
            selectedFile = getPanelDataObject().getString(KEY_DATA_FILE_SELECTED);
        }
        else {
            selectedFile = null;
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


            intervalInMillisec = MNPhotoAlbumCommonUtil
                    .getTransitionInterval(
                            intervalMinute,
                            intervalSecond);
        }
        else {
            intervalInMillisec = INVALID_INTERVAL;
        }

        if (getPanelDataObject().has(KEY_DATA_USE_GRAYSCALE)) {
            useGrayscale =
                    getPanelDataObject().getBoolean(KEY_DATA_USE_GRAYSCALE);
        }
        else {
            useGrayscale = false;
        }

        allAbsoluteImageFileList = null;

        MNViewSizeMeasure.setViewSizeObserver(MNPhotoAlbumPanelLayout.this,
            new MNViewSizeMeasure.OnGlobalLayoutObserver() {
                @Override
                public void onLayoutLoad() {

                    MNPhotoAlbumListFetcher listFetcher = new MNPhotoAlbumListFetcher(
                            rootDir,
                            new MNPhotoAlbumListFetcher.OnListFetchListener() {
                                @Override
                                public void onPhotoListFetch(ArrayList<String> photoList) {
                                    if (photoList != null) {
                                        allAbsoluteImageFileList = photoList;
                                        updateUI();
                                    }
                                }
                            }
                    );
                    listFetcher.execute();
                }
            });

//        boolean test = true;
//        if (test) {
//            return;
//        }

    }

    @Override
    protected void updateUI() {
        super.updateUI();

        if (allAbsoluteImageFileList != null) {
            if (displayHelper == null) {
                displayHelper =
                        new MNPhotoAlbumDisplayHelper((Activity) getContext(),
                                viewSwitcher
                        );
            }
            startTimer(viewSwitcher.getWidth(), viewSwitcher.getHeight());
        }
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        MNLog.i("view size changed", "onSizeChanged\t" +
                String.format("width: %d, " +"height : %d",
                        w, h));

        MNViewSizeMeasure.setViewSizeObserver(viewSwitcher, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad() {
                updateUI();
            }
        });
    }
}
