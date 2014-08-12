package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.yooiistudios.morningkit.common.log.MNLog;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 9.
 *
 * MNPhotoAlbumListFetcher
 */
public class MNPhotoAlbumListFetcher extends AsyncTask<Void, Void,
        ArrayList<String>> {
    private static final String TAG = "PhotoListFetcher";

    private String mParentForFileList;
    private OnListFetchListener onListFetchListener;
    private boolean mIsRunning;
    private static final boolean DEBUG = true;

    public MNPhotoAlbumListFetcher(String parentForFileList,
                                   OnListFetchListener onListFetchListener) {
        mParentForFileList = parentForFileList;
        this.onListFetchListener = onListFetchListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mIsRunning = true;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        MNLog.i(TAG, "start loading");

//        try {
//            Thread.sleep(2000);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }

        return getFileList(new File(mParentForFileList), null, "image");
    }

    @Override
    protected void onPostExecute(ArrayList<String> resultList) {
        super.onPostExecute(resultList);
        if (isCancelled()) {
            resultList = null;
        }
        if (resultList == null) {
            if (onListFetchListener != null) {
                onListFetchListener.onError();
            }
            return;
        }
        if (onListFetchListener != null) {
            onListFetchListener.onPhotoListFetch(resultList);
        }
        mIsRunning = false;
    }
    public boolean isRunning() {
        return mIsRunning;
    }

    public static ArrayList<String> getFileList(File rootDir, File curDir,
                                                String mimeType) {
        // 둘 다 null 이라면 제대로 얻을 수 없어서 체크 필요 - by 우성
        if (curDir == null && rootDir == null) {
            return null;
        }
        ArrayList<String> inFiles = new ArrayList<String>();
        File[] files =
                curDir != null ? curDir.listFiles() : rootDir.listFiles();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            if (DEBUG) {
                if (file.isHidden()) {
                    continue;
                }
            }
            if (file.isDirectory()) {
                inFiles.addAll(getFileList(rootDir, file, mimeType));
            } else {
                if (checkMimetype(file, mimeType)) {
                    String relativePath = file.getAbsolutePath().replace(
                            rootDir.getAbsolutePath(), "");
                    inFiles.add(relativePath);
                }
            }
        }
        return inFiles;
    }

    public static boolean checkMimetype(File fileToCheck,
                                        String mimeTypeToCompare) {

        if (mimeTypeToCompare == null) {
            return true;
        }

        URL urlOfFile;
        try {
            urlOfFile = fileToCheck.toURI().toURL();
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        String path;
        try {
            path = URLEncoder.encode(urlOfFile.toString(), "UTF-8");
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(path).toLowerCase();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String mimeType = mime.getMimeTypeFromExtension(fileExtension);

            if (mimeType == null) {
                return false;
            } else {
                return mimeType.contains(mimeTypeToCompare);
            }
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface OnListFetchListener {
        public void onPhotoListFetch(ArrayList<String> photoList);
        public void onError();
    }
}
