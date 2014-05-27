package com.yooiistudios.morningkit.panel.photoalbum;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumCheckboxView;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumCommonUtil;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumDisplayHelper;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumListFetcher;
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
public class MNPhotoAlbumDetailFragment extends MNPanelDetailFragment {
    private static final String TAG = "MNPhotoAlbumDetailFragment";

    public static final int INVALID_INTERVAL = -1;

    //default constants
    public static final int DEFAULT_INTERVAL_MIN = 0;
    public static final int DEFAULT_INTERVAL_SEC = 5;

    //request code
    public static final int RC_LOAD_PHOTO = 1;

    @InjectView(R.id.preview_switcher) ViewSwitcher previewSwitcher;
    @InjectView(R.id.preview_name) TextView previewName;
    @InjectView(R.id.toggleRefresh)
    MNPhotoAlbumCheckboxView refreshTimeToggleButton;
    @InjectView(R.id.edittext_min) EditText minuteEditText;
    @InjectView(R.id.edittext_sec) EditText secondEditText;
    @InjectView(R.id.transition_group) RadioGroup transitionEffectRadioGroup;
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
                                    previewName.setText(R.string.loading);
                                }

                                @Override
                                public void onFirstBitmapLoad() {
                                    previewName.setText(new File(rootDirForFiles)
                                            .getName());
                                }

                                @Override
                                public void onError(int messageResId) {
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
                                displayHelper.setPhotoWidth(lp.width);
                                displayHelper.setPhotoHeight(lp.height);

                                long interval = MNPhotoAlbumCommonUtil
                                        .getTransitionInterval(
                                                intervalMinute,
                                                intervalSecond);
                                displayHelper.setInterval(interval);

                                updatePreviewUI(true);
                            }
                        }
                    }
            );
            listFetcher.execute();
        }
        return rootView;
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
        // init transition type ui
//        for (MNPhotoAlbumTransitionType type :
//                MNPhotoAlbumTransitionType.values()) {
////            transitionEffectRadioGroup
//        }

        refreshTimeToggleButton.setOnCheckListener(
                onSwitchCheckChangedListener);
        grayscaleToggleButton.setOnCheckListener(
                onSwitchCheckChangedListener);
        transitionEffectRadioGroup.setOnCheckedChangeListener
                (onTransitionChangedListener);
        minuteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence,
                                          int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence,
                                      int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    intervalMinute = Integer.parseInt(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        minuteEditText.setOnEditorActionListener(onEditorActionListener);
        secondEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence,
                                          int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence,
                                      int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    intervalSecond = Integer.parseInt(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        secondEditText.setOnEditorActionListener(onEditorActionListener);

        refreshTimeToggleButton.setChecked(useRefresh);
        grayscaleToggleButton.setChecked(useGrayscale);
        setTransitionType(transitionType, true);
        updateTimeUI();

        // theme
        initTheme();
    }
    TextView.OnEditorActionListener onEditorActionListener =
            new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                            keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                if (displayHelper != null) {
                    long interval = MNPhotoAlbumCommonUtil
                            .getTransitionInterval(
                                    intervalMinute,
                                    intervalSecond);
                    displayHelper.setInterval(interval);
                }
                updatePreviewUI();
            }
            return false;
        }
    };

    private void initTheme() {
        MNThemeType currentThemeType =
                MNTheme.getCurrentThemeType(getActivity());
    }

    private void setTransitionType(MNPhotoAlbumTransitionType type,
                                   boolean updateUI) {
        transitionType = type;
        if (updateUI) {
            transitionEffectRadioGroup.check(type.getRadioId());
        }
    }

    private void updateTimeUI() {
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


    private MNPhotoAlbumCheckboxView.OnCheckListener
            onSwitchCheckChangedListener
            = new MNPhotoAlbumCheckboxView.OnCheckListener() {
        @Override
        public void onCheck(ImageButton btn, boolean checked) {
            switch (btn.getId()) {
                case R.id.toggleRefresh:
                    useRefresh = checked;
                    if (checked) {
                        if (intervalMinute == INVALID_INTERVAL ||
                                intervalSecond == INVALID_INTERVAL) {
                            intervalMinute = DEFAULT_INTERVAL_MIN;
                            intervalSecond = DEFAULT_INTERVAL_SEC;
                        }
                    } else {
                        intervalMinute = INVALID_INTERVAL;
                        intervalSecond = INVALID_INTERVAL;
                    }

                    updateTimeUI();

                    if (displayHelper != null) {
                        long interval = MNPhotoAlbumCommonUtil
                                .getTransitionInterval(
                                        intervalMinute,
                                        intervalSecond);
                        displayHelper.setInterval(interval);
                    }
                    updatePreviewUI();
                    break;
                case R.id.grayscale_toggleSwitch:
                    useGrayscale = checked;
                    updatePreviewUI(true);
                    break;
            }

        }
    };


    private RadioGroup.OnCheckedChangeListener onTransitionChangedListener
            = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            switch(id) {
                case R.id.radio_none:
                    transitionType = MNPhotoAlbumTransitionType.NONE;
                    break;
                case R.id.radio_alpha:
                    transitionType = MNPhotoAlbumTransitionType.ALPHA;
                    break;
            }

            updatePreviewUI();
        }
    };
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


}
