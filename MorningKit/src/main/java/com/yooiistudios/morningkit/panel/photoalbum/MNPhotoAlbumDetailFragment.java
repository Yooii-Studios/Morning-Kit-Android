package com.yooiistudios.morningkit.panel.photoalbum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.photoalbum.adapter.MNPhotoAlbumDropdownAdapter;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumCheckboxView;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumCommonUtil;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumDisplayHelper;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumListFetcher;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumRefreshTimeDialogFragment;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumTransitionType;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_FILE_ROOT;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_FILE_SELECTED;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_INTERVAL_MINUTE;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_INTERVAL_SECOND;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_IS_FIRST_LOADING;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_TRANS_TYPE;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_USE_GRAYSCALE;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_USE_REFRESH;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.PREF_PHOTO_ALBUM;
import static com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumFileManager.DEFAULT_PARENT_DIR;

//import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_FILE_FILELIST;
//import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_FILE_PARENT_LIST;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 5. 13.
 *
 * MNPhotoAlbumDetailFragment
 *  포토 앨범 패널 디테일 프래그먼트 by 동현
 */
public class MNPhotoAlbumDetailFragment extends MNPanelDetailFragment
        implements MNPhotoAlbumRefreshTimeDialogFragment.OnClickListener{
    private static final String TAG = "MNPhotoAlbumDetailFragment";

    // tags
    private static final Object TAG_TRANSITION_CHECKBOX = new Object();
    public static final String TAG_TRANSITION_DIALOG = "transition dialog";
    public static final String TAG_TRANSITION_TYPE_DIALOG = "transition " +
            "type dialog";

    public static final int INVALID_INTERVAL = -1;

    // default constants
    public static final int DEFAULT_INTERVAL_MIN = 0;
    public static final int DEFAULT_INTERVAL_SEC = 3;
    public static final MNPhotoAlbumTransitionType DEFAULT_TRANSITION_TYPE =
            MNPhotoAlbumTransitionType.ALPHA;

    // request code
    public static final int RC_LOAD_PHOTO = 1;

    @InjectView(R.id.preview_switcher) ViewSwitcher previewSwitcher;
    @InjectView(R.id.preview_unavailable)
    View previewUnavailableView;
    @InjectView(R.id.preview_name) TextView previewName;
    @InjectView(R.id.toggleRefresh)
    ImageView refreshTimeToggleButton;
//    @InjectView(R.id.edittext_min) EditText minuteEditText;
//    @InjectView(R.id.edittext_sec) EditText secondEditText;
    @InjectView(R.id.transition_type_spinner) Spinner transitionTypeSpinner;
//    @InjectView(R.id.label_min) TextView minLabel;
//    @InjectView(R.id.label_sec) TextView secLabel;
//    @InjectView(R.id.time_wrapper) ViewGroup timeWrapper;
    @InjectView(R.id.grayscale_toggleSwitch)
    MNPhotoAlbumCheckboxView grayscaleToggleButton;
    @InjectView(R.id.refreshTime) TextView refreshTime;

    private MNPhotoAlbumDisplayHelper displayHelper;
    private MNPhotoAlbumListFetcher listFetcher;

    private int intervalMinute;
    private int intervalSecond;
    private boolean useRefresh;
    private MNPhotoAlbumTransitionType transitionType;
    private String rootDirForFiles;
    private String selectedFileName;
    private ArrayList<String> fileList;
    private boolean useGrayscale;

    private int recentIntervalMinute = DEFAULT_INTERVAL_MIN;
    private int recentIntervalSecond = DEFAULT_INTERVAL_SEC;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.panel_photo_album_detail_fragment, container, false);
        if (rootView != null && savedInstanceState == null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            try {
                initPanelDataObject();
            } catch (JSONException e) {
                e.printStackTrace();

                intervalMinute = DEFAULT_INTERVAL_MIN;
                intervalSecond = DEFAULT_INTERVAL_SEC;
                recentIntervalMinute = DEFAULT_INTERVAL_MIN;
                recentIntervalSecond = DEFAULT_INTERVAL_SEC;
                useRefresh = true;
                transitionType = DEFAULT_TRANSITION_TYPE;

                selectedFileName = null;
                rootDirForFiles = DEFAULT_PARENT_DIR.getAbsolutePath();
                useGrayscale = false;
            }

            rootView.findViewById(R.id.load)
                    .setOnClickListener(onLoadBtnClickListener);
            rootView.findViewById(R.id.preview_wrapper)
                    .setOnClickListener(onLoadBtnClickListener);

            // UI
            initUI();

            displayHelper =
                    new MNPhotoAlbumDisplayHelper(
                            getActivity(), previewSwitcher,
                            new MNPhotoAlbumDisplayHelper.OnStartListener() {
                                @Override
                                public void onStartLoadingBitmap() {
                                    togglePreviewWrapper(true);
                                    previewName.setText(R.string.loading);
                                }

                                @Override
                                public void onFirstBitmapLoad() {
                                    previewName.setText(new File(rootDirForFiles)
                                            .getName());
                                }

                                @Override
                                public void onError(int messageResId) {
                                    togglePreviewWrapper(false);
//                                    previewName.setText(messageResId);
                                }
                            }
                    );
            ViewGroup.LayoutParams lp =
                    previewSwitcher.getLayoutParams();
            displayHelper.setPhotoWidth(lp.width -
                    (previewSwitcher.getPaddingLeft() +
                            previewSwitcher.getPaddingLeft()));
            displayHelper.setPhotoHeight(lp.height -
                    (previewSwitcher.getPaddingTop() +
                            previewSwitcher.getPaddingBottom()));
            loadFileList();
        }
        return rootView;
    }
    private View.OnClickListener onLoadBtnClickListener =
            new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            }
            startActivityForResult(intent, RC_LOAD_PHOTO);
        }
    };

    private void loadFileList() {
        if (listFetcher != null) {
            listFetcher.cancel(true);
        }

        togglePreviewWrapper(true);
        previewName.setText(R.string.loading);
        listFetcher = new MNPhotoAlbumListFetcher(
                rootDirForFiles,
                new MNPhotoAlbumListFetcher.OnListFetchListener() {
                    @Override
                    public void onPhotoListFetch(ArrayList<String> photoList) {
                        togglePreviewWrapper(true);
                        previewName.setText(new File(rootDirForFiles)
                                .getName());
                        if (photoList != null) {
                            fileList = photoList;
                            if (selectedFileName == null &&
                                    photoList.size() > 0) {
                                selectedFileName = photoList.get(0);
                            }

                            long interval = MNPhotoAlbumCommonUtil
                                    .getTransitionInterval(
                                            intervalMinute,
                                            intervalSecond);
                            displayHelper.setInterval(interval);

                            updatePreviewUI(true);
                        }
                    }

                    @Override
                    public void onError() {
                        togglePreviewWrapper(false);
                        previewName.setText(R.string.photo_album_no_image);
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
    private void togglePreviewWrapper(boolean available) {
        if (available) {
            previewName.setVisibility(View.VISIBLE);
            previewSwitcher.setVisibility(View.VISIBLE);
            previewUnavailableView.setVisibility(View.GONE);
        }
        else {
            previewName.setVisibility(View.GONE);
            previewSwitcher.setVisibility(View.GONE);
            previewUnavailableView.setVisibility(View.VISIBLE);
        }
    }

    private void initPanelDataObject() throws JSONException {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                PREF_PHOTO_ALBUM, Context.MODE_PRIVATE);
        if (getPanelDataObject().has(KEY_DATA_INTERVAL_MINUTE)) {
            intervalMinute = getPanelDataObject()
                    .getInt(KEY_DATA_INTERVAL_MINUTE);
            recentIntervalMinute = intervalMinute;
        }
        else {
            intervalMinute = prefs.getInt(KEY_DATA_INTERVAL_MINUTE,
                    DEFAULT_INTERVAL_MIN);
            recentIntervalMinute = intervalMinute;
        }
        if (getPanelDataObject().has(KEY_DATA_INTERVAL_SECOND)) {
            intervalSecond = getPanelDataObject()
                    .getInt(KEY_DATA_INTERVAL_SECOND);
            recentIntervalSecond = intervalSecond;
        }
        else {
            intervalSecond = prefs.getInt(KEY_DATA_INTERVAL_SECOND,
                    DEFAULT_INTERVAL_SEC);
            recentIntervalSecond = intervalSecond;
        }
        if (getPanelDataObject().has(KEY_DATA_USE_REFRESH)) {
            useRefresh = getPanelDataObject().getBoolean(KEY_DATA_USE_REFRESH);
        }
        else {
            useRefresh = prefs.getBoolean(KEY_DATA_USE_REFRESH, true);
        }
        if (getPanelDataObject().has(KEY_DATA_TRANS_TYPE)) {
            String transitionTypeStr = getPanelDataObject().getString
                    (KEY_DATA_TRANS_TYPE);
            transitionType = MNPhotoAlbumTransitionType.getTypeByKey(
                    transitionTypeStr);
        }
        else {
            String previousTransTypeKey = prefs.getString(KEY_DATA_TRANS_TYPE,
                    DEFAULT_TRANSITION_TYPE.getKey());
            transitionType = MNPhotoAlbumTransitionType.getTypeByKey(
                    previousTransTypeKey);
        }
        if (getPanelDataObject().has(KEY_DATA_FILE_SELECTED)) {
            selectedFileName = getPanelDataObject().getString(
                    KEY_DATA_FILE_SELECTED);
        }
        else {
            selectedFileName = prefs.getString(KEY_DATA_FILE_SELECTED, null);
        }
        if (getPanelDataObject().has(KEY_DATA_FILE_ROOT)) {
            rootDirForFiles = getPanelDataObject().getString(
                    KEY_DATA_FILE_ROOT);
        }
        else {
            rootDirForFiles = prefs.getString(KEY_DATA_FILE_ROOT,
                    DEFAULT_PARENT_DIR.getAbsolutePath());
        }
        if (getPanelDataObject().has(KEY_DATA_USE_GRAYSCALE)) {
            useGrayscale = getPanelDataObject().getBoolean(KEY_DATA_USE_GRAYSCALE);
        }
        else {
            useGrayscale = prefs.getBoolean(KEY_DATA_USE_GRAYSCALE, false);
        }
    }

    private void initUI() {
        // init preview wrapper
        togglePreviewWrapper(true);

        // init transition type ui
        ArrayList<String> transitionNameList = new ArrayList<String>();
        for (MNPhotoAlbumTransitionType type :
                MNPhotoAlbumTransitionType.values()) {
            transitionNameList.add(type.name());
        }
        MNPhotoAlbumDropdownAdapter spinnerArrayAdapter =
                new MNPhotoAlbumDropdownAdapter(
                        getActivity(), transitionNameList);
        transitionTypeSpinner.setAdapter(spinnerArrayAdapter);
        transitionTypeSpinner.setSelection(transitionType.ordinal());
        transitionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                transitionType = MNPhotoAlbumTransitionType.values()[i];

                transitionTypeSpinner.setSelection(transitionType.ordinal());
                updatePreviewUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        refreshTimeToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (useRefresh) {
                    onTurnOff();
                }
                else {
                    onConfirm(recentIntervalMinute, recentIntervalSecond);
//                    showTimeDialog();
                }
            }
        });
        refreshTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimeDialog();
            }
        });
        grayscaleToggleButton.setOnCheckListener(
                new MNPhotoAlbumCheckboxView.OnCheckListener() {
                    @Override
                    public void onCheck(ImageView btn, boolean checked) {
                        useGrayscale = checked;
                        updatePreviewUI(true);

                    }
                });
//        minuteEditText.setEnabled(false);
//        secondEditText.setEnabled(false);
        grayscaleToggleButton.setChecked(useGrayscale);
        updateRefreshTimeUI();

        // theme
        initTheme();
    }

    private void showTimeDialog() {
        int minute;
        int second;
        if (intervalMinute == INVALID_INTERVAL ||
                intervalSecond == INVALID_INTERVAL) {
            minute = DEFAULT_INTERVAL_MIN;
            second = DEFAULT_INTERVAL_SEC;
        }
        else {
            minute = intervalMinute;
            second = intervalSecond;
        }
        DialogFragment newFragment =
                MNPhotoAlbumRefreshTimeDialogFragment.
                        newInstance(minute, second);
        newFragment.setTargetFragment(
                MNPhotoAlbumDetailFragment.this, -1);
        newFragment.show(getFragmentManager(), TAG_TRANSITION_DIALOG);
    }

    private void initTheme() {
//        MNThemeType currentThemeType =
//                MNTheme.getCurrentThemeType(getActivity());
    }

    private void updateRefreshTimeUI() {
        if (intervalMinute != INVALID_INTERVAL &&
                intervalSecond != INVALID_INTERVAL) {
            refreshTime.setVisibility(View.VISIBLE);

            String timeStr = "";

            if (intervalMinute > 0) {
                timeStr += intervalMinute + " " +
                        getString(R.string.photo_album_minute);
            }
            if (intervalSecond > 0) {
                if (timeStr.length() > 0) {
                    timeStr += "  ";
                }
                timeStr += intervalSecond + " " +
                        getString(R.string.photo_album_second);
            }
            refreshTime.setText(timeStr);
        }
        else {
            refreshTime.setVisibility(View.VISIBLE);
            refreshTime.setText("");
        }

        if (useRefresh) {
            refreshTimeToggleButton.setImageResource(R.drawable
                    .icon_panel_detail_checkbox_on);
        }
        else {
            refreshTimeToggleButton.setImageResource(R.drawable
                    .icon_panel_detail_checkbox);
        }
    }

    @Override
    protected void archivePanelData() throws JSONException {
        getPanelDataObject().put(KEY_DATA_INTERVAL_MINUTE, intervalMinute);
        getPanelDataObject().put(KEY_DATA_INTERVAL_SECOND, intervalSecond);
        getPanelDataObject().put(KEY_DATA_USE_REFRESH, useRefresh);
        getPanelDataObject().put(KEY_DATA_TRANS_TYPE, transitionType.getKey());
        getPanelDataObject().put(KEY_DATA_FILE_SELECTED, selectedFileName);
        getPanelDataObject().put(KEY_DATA_FILE_ROOT, rootDirForFiles);
        getPanelDataObject().put(KEY_DATA_USE_GRAYSCALE, useGrayscale);
        getPanelDataObject().put(KEY_DATA_IS_FIRST_LOADING, true);

        SharedPreferences prefs = getActivity().getSharedPreferences(
                PREF_PHOTO_ALBUM, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_DATA_INTERVAL_MINUTE, intervalMinute)
                .putInt(KEY_DATA_INTERVAL_SECOND, intervalSecond)
                .putBoolean(KEY_DATA_USE_REFRESH, useRefresh)
                .putString(KEY_DATA_TRANS_TYPE, transitionType.getKey())
                .putString(KEY_DATA_FILE_SELECTED, selectedFileName)
                .putString(KEY_DATA_FILE_ROOT, rootDirForFiles)
                .putBoolean(KEY_DATA_USE_GRAYSCALE, useGrayscale)
        .apply();
    }

    private void onTimeUpdated() {
        updateRefreshTimeUI();

        if (displayHelper != null) {
            long interval = MNPhotoAlbumCommonUtil
                    .getTransitionInterval(
                            intervalMinute,
                            intervalSecond);
            displayHelper.setInterval(interval);
        }
        updatePreviewUI(true);
    }

    private void updatePreviewUI() {
        updatePreviewUI(false);
    }
    private void updatePreviewUI(boolean restart) {
        if (displayHelper != null && rootDirForFiles != null &&
                fileList != null) {
            displayHelper.setRootDir(rootDirForFiles);
            displayHelper.setTransitionType(transitionType);
            displayHelper.setUseGrayscale(useGrayscale);
            displayHelper.setSelectedFile(selectedFileName);
            displayHelper.setFileList(fileList);
            if (intervalMinute == INVALID_INTERVAL ||
                    intervalSecond == INVALID_INTERVAL) {
                displayHelper.setInterval(INVALID_INTERVAL);
                if (restart || displayHelper.isRunning()) {
                    displayHelper.restart(true);
                }
            }
            else {
                displayHelper.restart(true);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_LOAD_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
//                    MNLog.i(TAG, data.getData().getPath());

                    Uri uri = data.getData();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        final int takeFlags = data.getFlags()
//                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
//                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                        // Check for the freshest data.
//                        getActivity().getContentResolver()
//                                .takePersistableUriPermission(uri, takeFlags);
//                    }

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(
                            uri, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);

                        cursor.close();

                        File selectedFile = new File(picturePath);
                        if (!selectedFile.isFile()) {
                            Toast.makeText(getActivity(),
                                    "blah...Can't use photo in that folder.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        this.rootDirForFiles = selectedFile.getParent();
                        this.selectedFileName = selectedFile.getAbsolutePath
                                ().replace(rootDirForFiles, "");

//                        // load image items for preview
//                        previewName.setText("Loading...");
//
//                        if (listFetcher != null) {
//                            listFetcher.cancel(true);
//                            listFetcher = new MNPhotoAlbumListFetcher(
//                                    rootDirForFiles,
//                                    new MNPhotoAlbumListFetcher.OnListFetchListener() {
//                                        @Override
//                                        public void onPhotoListFetch(ArrayList<String> photoList) {
//                                            previewName.setText(
//                                                    new File(rootDirForFiles)
//                                                    .getName());
//                                            if (photoList != null) {
//                                                fileList = photoList;
//                                                updatePreviewUI(true);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            togglePreviewWrapper(false);
//                                            previewName.setText(R.string.photo_album_no_image);
//                                        }
//                                    }
//                            );
//                            listFetcher.execute();
//                        }
                    }
                    else {
                        Toast.makeText(getActivity(),
                                "blah...error while getting image.",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        loadFileList();
//        else {
//            if (displayHelper != null) {
//                displayHelper.restart();
//            }
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MNLog.i(TAG, "onStop");
        if (listFetcher != null) {
            listFetcher.cancel(true);
        }
        if (displayHelper != null && displayHelper.isRunning()) {
            displayHelper.stop();
        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if (listFetcher != null) {
//            listFetcher.cancel(true);
//        }
//        if (displayHelper != null && displayHelper.isRunning()) {
//            displayHelper.stop();
//        }
//    }

    @Override
    public void onConfirm(int minute, int second) {
        intervalMinute = minute;
        intervalSecond = second;

        if (intervalMinute < 0) {
            intervalMinute = DEFAULT_INTERVAL_MIN;
        }
        if (intervalSecond < 0) {
            intervalSecond = DEFAULT_INTERVAL_SEC;
        }

        recentIntervalMinute = intervalMinute;
        recentIntervalSecond = intervalSecond;
        useRefresh = true;

        onTimeUpdated();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onTurnOff() {
        intervalMinute = INVALID_INTERVAL;
        intervalSecond = INVALID_INTERVAL;
        useRefresh = false;

        onTimeUpdated();
    }
}
