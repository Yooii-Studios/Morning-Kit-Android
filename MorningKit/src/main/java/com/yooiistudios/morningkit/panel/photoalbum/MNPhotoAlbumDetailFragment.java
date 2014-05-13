package com.yooiistudios.morningkit.panel.photoalbum;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.photoalbum.model.MNPhotoAlbumTransitionType;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ru.bartwell.exfilepicker.ExFilePickerActivity;
import ru.bartwell.exfilepicker.ExFilePickerParcelObject;

import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_FILE_FILELIST;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_FILE_PARENT_LIST;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_FILE_ROOT;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_INTERVAL_MINUTE;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_INTERVAL_SECOND;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_TRANS_TYPE;
import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumPanelLayout.KEY_DATA_USE_REFRESH;

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
    public static final File DEFAULT_PARENT_DIR =
            Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM);

    //request code
    public static final int RC_LOAD_PHOTO = 1;

    @InjectView(R.id.toggleSwitch) CompoundButton refreshTimeToggleButton;
    @InjectView(R.id.edittext_min) EditText minuteEditText;
    @InjectView(R.id.edittext_sec) EditText secondEditText;
    @InjectView(R.id.transition_group) RadioGroup transitionEffectRadioGroup;
    @InjectView(R.id.label_min) TextView minLabel;
    @InjectView(R.id.label_sec) TextView secLabel;

    private int intervalMinute;
    private int intervalSecond;
//    private PhotoType type;
    private boolean useRefresh;
    private MNPhotoAlbumTransitionType transitionType;
    private ArrayList<String> parentDirList;
    private String rootDirForFiles;
    private ArrayList<String> fileList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_photo_album_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            try {
                initPanelDataObject();
            } catch (JSONException e) {
                e.printStackTrace();

//                type = PhotoType.DEFAULT;
                intervalMinute = DEFAULT_INTERVAL_MIN;
                intervalSecond = DEFAULT_INTERVAL_SEC;
                useRefresh = false;
                transitionType = MNPhotoAlbumTransitionType.NONE;

                parentDirList = new ArrayList<String>();
                parentDirList.add(DEFAULT_PARENT_DIR.getAbsolutePath());
                rootDirForFiles = null;
                fileList = new ArrayList<String>();
            }

            rootView.findViewById(R.id.load).setOnClickListener(new View
                    .OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ExFilePickerActivity.class);
                    startActivityForResult(intent, RC_LOAD_PHOTO);
                }
            });

            // UI
            initUI();
        }
        return rootView;
    }

    private void initPanelDataObject() throws JSONException {
//        if (getPanelDataObject().has(KEY_TYPE)) {
//            String typeStr = getPanelDataObject().getString(KEY_TYPE);
//            type = PhotoType.getTypeByKey(typeStr);
//        }
//        else {
//            type = PhotoType.DEFAULT;
//        }
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
        if (getPanelDataObject().has(KEY_DATA_FILE_PARENT_LIST)) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            parentDirList = new Gson().fromJson(
                    getPanelDataObject().getString(KEY_DATA_FILE_PARENT_LIST),
                    type);
        }
        else {
            parentDirList = new ArrayList<String>();
            parentDirList.add(DEFAULT_PARENT_DIR.getAbsolutePath());
        }
        if (getPanelDataObject().has(KEY_DATA_FILE_ROOT)) {
            rootDirForFiles = getPanelDataObject().getString(
                    KEY_DATA_FILE_ROOT);
        }
        else {
            rootDirForFiles = null;
        }
        if (getPanelDataObject().has(KEY_DATA_FILE_FILELIST)) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            fileList = new Gson().fromJson(
                    getPanelDataObject().getString(KEY_DATA_FILE_FILELIST),
                    type);
        }
        else {
            fileList = new ArrayList<String>();
        }
    }

    private void initUI() {
        refreshTimeToggleButton.setOnCheckedChangeListener(
                onRefreshTimeCheckChangedListener);
        transitionEffectRadioGroup.setOnCheckedChangeListener
                (onTransitionChangedListener);
        minuteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence,
                                          int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence,
                                      int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    intervalMinute = Integer.parseInt(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
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

        refreshTimeToggleButton.setChecked(useRefresh);
        setTransitionType(transitionType, true);
        updateTimeUI();

        // theme
        initTheme();
    }

    private void initTheme() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
    }

    private void setTransitionType(MNPhotoAlbumTransitionType type, boolean updateUI) {
        transitionType = type;
        if (updateUI) {
            transitionEffectRadioGroup.check(type.getRadioId());
        }
    }

    private void updateTimeUI() {//int intervalMin, int intervalSec
        if (intervalMinute != INVALID_INTERVAL &&
                intervalSecond != INVALID_INTERVAL) {
            minuteEditText.setVisibility(View.VISIBLE);
            secondEditText.setVisibility(View.VISIBLE);
            minLabel.setVisibility(View.VISIBLE);
            secLabel.setVisibility(View.VISIBLE);

            minuteEditText.setText(
                    String.valueOf(intervalMinute));
            secondEditText.setText(
                    String.valueOf(intervalSecond));
        }
        else {
            minuteEditText.setVisibility(View.INVISIBLE);
            secondEditText.setVisibility(View.INVISIBLE);
            minLabel.setVisibility(View.INVISIBLE);
            secLabel.setVisibility(View.INVISIBLE);

            minuteEditText.setText(
                    String.valueOf(INVALID_INTERVAL));
            secondEditText.setText(
                    String.valueOf(INVALID_INTERVAL));
        }
    }

    @Override
    protected void archivePanelData() throws JSONException {
//        getPanelDataObject().put(KEY_TYPE, type.getKey());
        getPanelDataObject().put(KEY_DATA_INTERVAL_MINUTE, intervalMinute);
        getPanelDataObject().put(KEY_DATA_INTERVAL_SECOND, intervalSecond);
        getPanelDataObject().put(KEY_DATA_USE_REFRESH, useRefresh);
        getPanelDataObject().put(KEY_DATA_TRANS_TYPE, transitionType.getKey());

        getPanelDataObject().put(KEY_DATA_FILE_PARENT_LIST,
                new Gson().toJson(parentDirList));
        if (rootDirForFiles != null) {
            getPanelDataObject().put(KEY_DATA_FILE_ROOT, rootDirForFiles);
        }
        getPanelDataObject().put(KEY_DATA_FILE_FILELIST,
                new Gson().toJson(fileList));
    }


    private CompoundButton.OnCheckedChangeListener
            onRefreshTimeCheckChangedListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton btn, boolean checked) {
            useRefresh = checked;
            switch (btn.getId()) {
                case R.id.toggleSwitch:
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
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_LOAD_PHOTO:
                if (data != null) {
                    ExFilePickerParcelObject object = (ExFilePickerParcelObject)
                            data.getParcelableExtra(
                                    ExFilePickerParcelObject.class.
                                            getCanonicalName());
                    if (object.count > 0) {
                        ArrayList<String> parentDirList =
                                new ArrayList<String>();
                        ArrayList<String> fileList =
                                new ArrayList<String>();
                        for (String name : object.names) {
                            File file = new File(object.path, name);
                            if (file.isDirectory()) {
                                parentDirList.add(file.getAbsolutePath());
                            }
                            else {
                                fileList.add(name);
                            }

                            Log.i(TAG, file.getAbsolutePath());
                        }
//                        loadData(parentDirList, object.path, fileList,
//                                _getSetting());
                    }
                }
                break;
        }
    }
}
