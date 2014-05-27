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

    public MNPhotoAlbumListFetcher(String parentForFileList,
                                   OnListFetchListener onListFetchListener) {
        mParentForFileList = parentForFileList;
        this.onListFetchListener = onListFetchListener;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        MNLog.i(TAG, "start loading");

        return getFileList(new File(mParentForFileList), null, "image");
    }

    @Override
    protected void onPostExecute(ArrayList<String> resultList) {
        super.onPostExecute(resultList);
        if (isCancelled()) {
            resultList = null;
        }
        if (onListFetchListener != null) {
            onListFetchListener.onPhotoListFetch(resultList);
        }
    }

    public static ArrayList<String> getFileList(File rootDir, File curDir,
                                                String mimetype) {
        ArrayList<String> inFiles = new ArrayList<String>();
        File[] files =
                curDir != null ? curDir.listFiles() : rootDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getFileList(rootDir, file, mimetype));
            }
            else {
                if(checkMimetype(file, mimetype)) {
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
    }
}
