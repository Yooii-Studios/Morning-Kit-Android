package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 9.
 */
public class MNPhotoAlbumListFetcher extends AsyncTaskLoader<JSONArray> {
    private static final String TAG = "PhotoListFetcher";

    private ArrayList<String> mParemntDirList;
    private String mParentForFileList;
    private ArrayList<String> mFileList;

    public MNPhotoAlbumListFetcher(Context context, ArrayList<String> parentDirList,
                                   String parentForFileList,
                                   ArrayList<String> fileList) {
        super(context);

        mParemntDirList = parentDirList;
        mParentForFileList = parentForFileList;
        mFileList = fileList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public JSONArray loadInBackground() {
//        Log.i(TAG, "start loading");
//        JSONArray fileArray = null;
//        try {
//            fileArray = PhotoSetting.getFileListJSONArray(mParemntDirList,
//                    mParentForFileList, mFileList, "image");
//        } catch(JSONException e) {
//            e.printStackTrace();
//        }
//        Log.i(TAG, "end loading");
//        return fileArray;
        return null;
    }

}
