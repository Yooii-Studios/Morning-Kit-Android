package com.yooiistudios.morningkit.panel.photoalbum;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
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
import java.util.Random;

import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumDetailFragment.DEFAULT_INTERVAL_MIN;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumDetailFragment.DEFAULT_INTERVAL_SEC;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumDetailFragment.DEFAULT_TRANSITION_TYPE;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumDetailFragment.INVALID_INTERVAL;
import static com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumFileManager.DEFAULT_PARENT_DIR;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 5. 13.
 *
 * MNPhotoAlbumPanelLayout
 *  사진 패널 레이아웃 by 동현
 */
public class MNPhotoAlbumPanelLayout extends MNPanelLayout {
    private static final String TAG = "MNPhotoAlbumPanelLayout";

    public static final String PREF_PHOTO_ALBUM = "photo album setting";

    public static final String KEY_DATA_INTERVAL_MINUTE = "minute";
    public static final String KEY_DATA_INTERVAL_SECOND = "second";
    public static final String KEY_DATA_USE_REFRESH = "use refresh";
    public static final String KEY_DATA_TRANS_TYPE = "transition type";
    public static final String KEY_DATA_FILE_SELECTED = "selected file";
    public static final String KEY_DATA_FILE_ROOT = "selected file's root dir";
    public static final String KEY_DATA_USE_GRAYSCALE = "use grayscale";

    public static final String KEY_DATA_IS_FIRST_LOADING = "is first loading";

    private ViewSwitcher viewSwitcher;
    private MNPhotoAlbumTransitionType transitionType;
    private long intervalInMillisec;
    private String rootDir;
    private String selectedFile;
    private String fileToDisplay;
    private String previousSelectedFile;
    private ArrayList<String> allAbsoluteImageFileList;
    private boolean useGrayscale;

    private boolean isFirstLoading;

    private MNPhotoAlbumDisplayHelper displayHelper;
    private MNPhotoAlbumListFetcher listFetcher;

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

    private void refreshDisplayingFile() {
        if (fileToDisplay == null || selectedFile != previousSelectedFile) {
            fileToDisplay = selectedFile;
        }
        else {
            if (allAbsoluteImageFileList != null) {
                Random random = new Random(System.currentTimeMillis());
                fileToDisplay = allAbsoluteImageFileList.get(
                        random.nextInt(allAbsoluteImageFileList.size()));
            }
        }
    }

    @Override
    public void onActivityResume() {
        super.onActivityResume();
        MNLog.i(TAG, "onActivityResume");
        if (displayHelper != null) {
            displayHelper.notifyContainingActivityWillBeShown();
        }
        startLoadingAnimation();
        loadFileList();
//        updateUI();
    }

    @Override
    public void onActivityPause() {
        super.onActivityPause();
        MNLog.i(TAG, "onActivityPause");

        if (displayHelper != null) {
            displayHelper.notifyContainingActivityWillBeGone();
        }
        stopTimer();
        if (listFetcher != null) {
            listFetcher.cancel(true);
        }
    }

    private void startTimer(int photoWidth, int photoHeight) {

        boolean isFirstLoading;

        try {
            if (getPanelDataObject().has(KEY_DATA_IS_FIRST_LOADING)) {
                isFirstLoading = getPanelDataObject().getBoolean(
                        KEY_DATA_IS_FIRST_LOADING);
            }
            else {
                isFirstLoading = true;
            }
            getPanelDataObject().put(KEY_DATA_IS_FIRST_LOADING, false);
        } catch (JSONException e) {
            e.printStackTrace();
            isFirstLoading = true;
        }
        this.isFirstLoading = isFirstLoading;

        MNLog.i("Timer", "start called");
        if (displayHelper != null && !displayHelper.isRunning()) {
            ArrayList<String> list;
            if (fileToDisplay != null) {
                if (intervalInMillisec == INVALID_INTERVAL) {
                    list = new ArrayList<String>();
                    list.add(fileToDisplay);
                }
                else {
                    list = allAbsoluteImageFileList;
                }
            }
            else {
                list = allAbsoluteImageFileList;
            }
            displayHelper.start(rootDir, list, selectedFile,
                    transitionType, intervalInMillisec, useGrayscale,
                    photoWidth, photoHeight, isFirstLoading);
            MNLog.i("Timer", "started. isRunning : " + displayHelper.isRunning());
        }
    }
    private void stopTimer() {
        MNLog.i("Timer", "stop called");
        if (displayHelper != null) {
            displayHelper.stop();
            MNLog.i("Timer", "stopped");
        }
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();
        startLoadingAnimation();

        SharedPreferences prefs = getContext().getSharedPreferences(
                PREF_PHOTO_ALBUM, Context.MODE_PRIVATE);
        if (getPanelDataObject().has(KEY_DATA_FILE_ROOT)) {
            rootDir = getPanelDataObject().getString(KEY_DATA_FILE_ROOT);
            MNLog.i(TAG, "root : " + rootDir);
        }
        else {
            rootDir = prefs.getString(KEY_DATA_FILE_ROOT,
                    DEFAULT_PARENT_DIR.getAbsolutePath());
            getPanelDataObject().put(KEY_DATA_FILE_ROOT, rootDir);
        }

        if (getPanelDataObject().has(KEY_DATA_FILE_SELECTED)) {
            selectedFile = getPanelDataObject().getString(KEY_DATA_FILE_SELECTED);
//            refreshDisplayingFile();
//            previousSelectedFile = selectedFile;
        }
        else {
            selectedFile = prefs.getString(KEY_DATA_FILE_SELECTED, null);
            getPanelDataObject().put(KEY_DATA_FILE_SELECTED, selectedFile);
        }
        refreshDisplayingFile();
        previousSelectedFile = selectedFile;

        if (getPanelDataObject().has(KEY_DATA_TRANS_TYPE)) {
            String key = getPanelDataObject().getString(KEY_DATA_TRANS_TYPE);
            transitionType = MNPhotoAlbumTransitionType.getTypeByKey(key);
        }
        else {
            String key = prefs.getString(KEY_DATA_TRANS_TYPE,
                    DEFAULT_TRANSITION_TYPE.getKey());
            transitionType = MNPhotoAlbumTransitionType.getTypeByKey(key);
            getPanelDataObject().put(KEY_DATA_TRANS_TYPE, key);
        }
        if (getPanelDataObject().has(KEY_DATA_INTERVAL_SECOND) &&
                getPanelDataObject().has(KEY_DATA_INTERVAL_MINUTE)) {
            int intervalMinute = getPanelDataObject()
                    .getInt(KEY_DATA_INTERVAL_MINUTE);
            int intervalSecond = getPanelDataObject()
                    .getInt(KEY_DATA_INTERVAL_SECOND);


            intervalInMillisec = MNPhotoAlbumCommonUtil
                    .getTransitionInterval(
                            intervalMinute,
                            intervalSecond);
        }
        else {
            int intervalMin = prefs.getInt(KEY_DATA_INTERVAL_MINUTE,
                    DEFAULT_INTERVAL_MIN);
            int intervalSec = prefs.getInt(KEY_DATA_INTERVAL_SECOND,
                    DEFAULT_INTERVAL_SEC);
            intervalInMillisec =
                MNPhotoAlbumCommonUtil.getTransitionInterval(intervalMin,
                        intervalSec);

            getPanelDataObject().put(KEY_DATA_INTERVAL_MINUTE,
                    intervalMin);
            getPanelDataObject().put(KEY_DATA_INTERVAL_SECOND,
                    intervalSec);
        }

        if (getPanelDataObject().has(KEY_DATA_USE_GRAYSCALE)) {
            useGrayscale =
                    getPanelDataObject().getBoolean(KEY_DATA_USE_GRAYSCALE);
        }
        else {
            useGrayscale = prefs.getBoolean(KEY_DATA_USE_GRAYSCALE, false);
            getPanelDataObject().put(KEY_DATA_USE_GRAYSCALE, useGrayscale);
        }

        allAbsoluteImageFileList = null;

        MNViewSizeMeasure.setViewSizeObserver(MNPhotoAlbumPanelLayout.this,
            new MNViewSizeMeasure.OnGlobalLayoutObserver() {
                @Override
                public void onLayoutLoad() {
                    loadFileList();
                }
            });
    }
    private void loadFileList() {
        if (listFetcher != null && listFetcher.isRunning()) {
            listFetcher.cancel(true);
        }
        if (rootDir != null) {
            listFetcher = new MNPhotoAlbumListFetcher(
                    rootDir,
                    new MNPhotoAlbumListFetcher.OnListFetchListener() {
                        @Override
                        public void onPhotoListFetch(ArrayList<String> photoList) {
                            if (photoList != null) {
                                allAbsoluteImageFileList = photoList;
                                updateUI();
                            }
                        }

                        @Override
                        public void onError() {
                            stopLoadingAnimation();
                            showCoverLayout(R.string.photo_album_prompt_select_image);
                        }
                    }
            );
            // 앞 큐에 있는 AsyncTask 가 막힐 경우 뒷 쓰레드가 되게 하기 위한 코드
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                listFetcher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                listFetcher.execute();
            }
        }
    }

    @Override
    protected void updateUI() {
        super.updateUI();
        hideCoverLayout();
        startLoadingAnimation();

        if (allAbsoluteImageFileList != null) {
            if (displayHelper != null) {
                displayHelper.stop();
            }
            else {
                displayHelper =
                        new MNPhotoAlbumDisplayHelper(
                                (Activity) getContext(),viewSwitcher,
                                new MNPhotoAlbumDisplayHelper.OnStartListener() {
                                    @Override
                                    public void onStartLoadingBitmap() {
                                        startLoadingAnimation();
                                    }

                                    @Override
                                    public void onFirstBitmapLoad() {
                                        stopLoadingAnimation();
                                    }

                                    @Override
                                    public void onError(int messageResId) {
                                        stopLoadingAnimation();
                                        showCoverLayout(messageResId);
                                    }
                                });
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
