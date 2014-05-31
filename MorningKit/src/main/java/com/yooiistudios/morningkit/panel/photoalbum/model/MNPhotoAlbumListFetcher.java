package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.yooiistudios.morningkit.common.log.MNLog;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 9.
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
                                                String mimetype) {
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
                inFiles.addAll(getFileList(rootDir, file, mimetype));
            } else {
                if (checkMimetype(file, mimetype)) {
                    String relativePath = file.getAbsolutePath().replace(
                            rootDir.getAbsolutePath(), "");
                    inFiles.add(relativePath);
                }
            }
        }
        return inFiles;
    }

    public static boolean checkMimetype(File fileToCheck,
                                        String mimetypeToCompare) {

        if (mimetypeToCompare == null) {
            return true;
        }

        URL urlOfFile;
        try {
            urlOfFile = fileToCheck.toURI().toURL();
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(urlOfFile
                .toString());
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String mimetype = mime.getMimeTypeFromExtension(fileExtension);

        if (mimetype == null) {
            return false;
        }
        else {
            if (mimetype.contains(mimetypeToCompare)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public interface OnListFetchListener {
        public void onPhotoListFetch(ArrayList<String> photoList);
        public void onError();
    }
}
