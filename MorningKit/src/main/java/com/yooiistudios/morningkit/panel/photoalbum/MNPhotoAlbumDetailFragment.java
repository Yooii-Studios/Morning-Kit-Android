package com.yooiistudios.morningkit.panel.photoalbum;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_FILE_ROOT;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_FILE_SELECTED;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_INTERVAL_MINUTE;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_INTERVAL_SECOND;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_TRANS_TYPE;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_USE_GRAYSCALE;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_USE_REFRESH;
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
    public static final int DEFAULT_INTERVAL_SEC = 5;

    // request code
    public static final int RC_LOAD_PHOTO = 1;

    @InjectView(R.id.preview_switcher) ViewSwitcher previewSwitcher;
    @InjectView(R.id.preview_unavailable_imageView)
    ImageView previewUnavailableImageView;
    @InjectView(R.id.preview_name) TextView previewName;
    @InjectView(R.id.toggleRefresh)
    ImageView refreshTimeToggleButton;
    @InjectView(R.id.edittext_min) EditText minuteEditText;
    @InjectView(R.id.edittext_sec) EditText secondEditText;
    @InjectView(R.id.transition_type_spinner) Spinner transitionTypeSpinner;
    @InjectView(R.id.label_min) TextView minLabel;
    @InjectView(R.id.label_sec) TextView secLabel;
    @InjectView(R.id.time_wrapper) ViewGroup timeWrapper;
    @InjectView(R.id.grayscale_toggleSwitch)
    MNPhotoAlbumCheckboxView grayscaleToggleButton;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.panel_photo_album_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            try {
                initPanelDataObject();
            } catch (JSONException e) {
                e.printStackTrace();

                intervalMinute = DEFAULT_INTERVAL_MIN;
                intervalSecond = DEFAULT_INTERVAL_SEC;
                useRefresh = false;
                transitionType = MNPhotoAlbumTransitionType.NONE;

                selectedFileName = null;
                rootDirForFiles = DEFAULT_PARENT_DIR.getAbsolutePath();
                useGrayscale = false;
            }

            rootView.findViewById(R.id.load).setOnClickListener(new View
                    .OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    }
                    startActivityForResult(intent, RC_LOAD_PHOTO);
                }
            });

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
                                    previewName.setText(messageResId);
                                }
                            }
                    );


            if (listFetcher != null) {
                listFetcher.cancel(true);
            }
            previewName.setText(R.string.loading);
            listFetcher = new MNPhotoAlbumListFetcher(
                    rootDirForFiles,
                    new MNPhotoAlbumListFetcher.OnListFetchListener() {
                        @Override
                        public void onPhotoListFetch(ArrayList<String> photoList) {
                            previewName.setText(new File(rootDirForFiles)
                                    .getName());
                            if (photoList != null) {
                                fileList = photoList;
                                if (selectedFileName == null &&
                                        photoList.size() > 0) {
                                    selectedFileName = photoList.get(0);
                                }
                                ViewGroup.LayoutParams lp =
                                        previewSwitcher.getLayoutParams();
                                displayHelper.setPhotoWidth(lp.width -
                                        (previewSwitcher.getPaddingLeft() +
                                        previewSwitcher.getPaddingLeft()));
                                displayHelper.setPhotoHeight(lp.height -
                                        (previewSwitcher.getPaddingTop() +
                                        previewSwitcher.getPaddingBottom()));

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
            listFetcher.execute();
        }
        return rootView;
    }
    private void togglePreviewWrapper(boolean available) {
        if (available) {
            previewSwitcher.setVisibility(View.VISIBLE);
            previewUnavailableImageView.setVisibility(View.GONE);
        }
        else {
            previewSwitcher.setVisibility(View.GONE);
            previewUnavailableImageView.setVisibility(View.VISIBLE);
        }
    }

    private void initPanelDataObject() throws JSONException {
        if (getPanelDataObject().has(KEY_DATA_INTERVAL_MINUTE)) {
            intervalMinute = getPanelDataObject()
                    .getInt(KEY_DATA_INTERVAL_MINUTE);
        }
        else {
            intervalMinute = INVALID_INTERVAL;
        }
        if (getPanelDataObject().has(KEY_DATA_INTERVAL_SECOND)) {
            intervalSecond = getPanelDataObject()
                    .getInt(KEY_DATA_INTERVAL_SECOND);
        }
        else {
            intervalSecond = INVALID_INTERVAL;
        }
        if (getPanelDataObject().has(KEY_DATA_USE_REFRESH)) {
            useRefresh = getPanelDataObject().getBoolean(KEY_DATA_USE_REFRESH);
        }
        else {
            useRefresh = false;
        }
        if (getPanelDataObject().has(KEY_DATA_TRANS_TYPE)) {
            String transitionTypeStr = getPanelDataObject().getString
                    (KEY_DATA_TRANS_TYPE);
            transitionType = MNPhotoAlbumTransitionType.getTypeByKey(
                    transitionTypeStr);
        }
        else {
            transitionType = MNPhotoAlbumTransitionType.NONE;
        }
        if (getPanelDataObject().has(KEY_DATA_FILE_SELECTED)) {
            selectedFileName = getPanelDataObject().getString(
                    KEY_DATA_FILE_SELECTED);
        }
        else {
            selectedFileName = null;
        }
        if (getPanelDataObject().has(KEY_DATA_FILE_ROOT)) {
            rootDirForFiles = getPanelDataObject().getString(
                    KEY_DATA_FILE_ROOT);
        }
        else {
            rootDirForFiles = DEFAULT_PARENT_DIR.getAbsolutePath();
        }
        if (getPanelDataObject().has(KEY_DATA_USE_GRAYSCALE)) {
            useGrayscale = getPanelDataObject().getBoolean(KEY_DATA_USE_GRAYSCALE);
        }
        else {
            useGrayscale = false;
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

        refreshTimeToggleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
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
        });
        grayscaleToggleButton.setOnCheckListener(
                new MNPhotoAlbumCheckboxView.OnCheckListener() {
                    @Override
                    public void onCheck(ImageView btn, boolean checked) {
                        useGrayscale = checked;
                        updatePreviewUI(true);

                    }
                });
        minuteEditText.setEnabled(false);
        secondEditText.setEnabled(false);
        grayscaleToggleButton.setChecked(useGrayscale);
        updateRefreshTimeUI();

        // theme
        initTheme();
    }

    private void initTheme() {
        MNThemeType currentThemeType =
                MNTheme.getCurrentThemeType(getActivity());
    }

    private void updateRefreshTimeUI() {
        if (intervalMinute != INVALID_INTERVAL &&
                intervalSecond != INVALID_INTERVAL) {
            timeWrapper.setVisibility(View.VISIBLE);

            minuteEditText.setText(
                    String.valueOf(intervalMinute));
            secondEditText.setText(
                    String.valueOf(intervalSecond));
        }
        else {
            timeWrapper.setVisibility(View.INVISIBLE);

            minuteEditText.setText(
                    String.valueOf(INVALID_INTERVAL));
            secondEditText.setText(
                    String.valueOf(INVALID_INTERVAL));
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
            if (intervalMinute == INVALID_INTERVAL ||
                    intervalSecond == INVALID_INTERVAL) {
                displayHelper.setInterval(INVALID_INTERVAL);
                ArrayList<String> tmpFileList = new ArrayList<String>();
                tmpFileList.add(selectedFileName);
                displayHelper.setFileList(tmpFileList);
                if (restart || displayHelper.isRunning()) {
                    displayHelper.restart();
                }
            }
            else {
                displayHelper.setFileList(fileList);
                displayHelper.restart();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_LOAD_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    MNLog.i(TAG, data.getData().getPath());

                    String[] filePathColumn = {MediaStore.Images.Media.DATA,
                            MediaStore.Images.Media.DESCRIPTION,
                            MediaStore.Images.Media.PICASA_ID,
                            MediaStore.Images.Media.TITLE};

                    Cursor cursor = getActivity().getContentResolver().query(
                            data.getData(), filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);

                        cursor.close();

                        System.out.println(picturePath);

                        File selectedFile = new File(picturePath);
                        if (!selectedFile.isFile()) {
                            Toast.makeText(getActivity(),
                                    "blah...Can't use photo in that folder.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        this.selectedFileName = selectedFile.getName();
                        this.rootDirForFiles = selectedFile.getParent();

                        // load image items for preview
                        previewName.setText("Loading...");

                        if (listFetcher != null) {
                            listFetcher.cancel(true);
                            listFetcher = new MNPhotoAlbumListFetcher(
                                    rootDirForFiles,
                                    new MNPhotoAlbumListFetcher.OnListFetchListener() {
                                        @Override
                                        public void onPhotoListFetch(ArrayList<String> photoList) {
                                            previewName.setText(
                                                    new File(rootDirForFiles)
                                                    .getName());
                                            if (photoList != null) {
                                                fileList = photoList;
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
                            listFetcher.execute();
                        }
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
    public void onPause() {
        super.onPause();
        if (listFetcher != null) {
            listFetcher.cancel(true);
        }
        if (displayHelper != null && displayHelper.isRunning()) {
            displayHelper.stop();
        }
    }

    @Override
    public void onConfirm(int minute, int second) {
        intervalMinute = minute;
        intervalSecond = second;
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
