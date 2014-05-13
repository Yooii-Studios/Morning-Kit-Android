package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.yooiistudios.morningkit.common.log.MNLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 9.
 */
public class MNPhotoAlbumListFetcher extends AsyncTask<Void, Void,
        ArrayList<File>> {
    private static final String TAG = "PhotoListFetcher";

    public static final String KEY_FILELIST_PARENT = "parent dir";
    public static final String KEY_FILELIST_FILES = "file list";

    private ArrayList<String> mParemntDirList;
    private String mParentForFileList;
    private ArrayList<String> mFileList;
    private OnListFetchListener onListFetchListener;

    public MNPhotoAlbumListFetcher(ArrayList<String> parentDirList,
                                   String parentForFileList,
                                   ArrayList<String> fileList,
                                   OnListFetchListener onListFetchListener) {
        mParemntDirList = parentDirList;
        mParentForFileList = parentForFileList;
        mFileList = fileList;
        this.onListFetchListener = onListFetchListener;
    }

    @Override
    protected ArrayList<File> doInBackground(Void... voids) {
        MNLog.i(TAG, "start loading");
        JSONArray fileArray = null;
        try {
            fileArray = getFileListJSONArray(mParemntDirList,
                    mParentForFileList, mFileList, "image");
        } catch(JSONException e) {
            e.printStackTrace();
        }
        MNLog.i(TAG, "end loading");
        ArrayList<File> fileList;
        try {
            fileList = getFileList(fileArray);
        } catch(JSONException e) {
            e.printStackTrace();
            fileList = null;
        }
        return fileList;
    }

    @Override
    protected void onPostExecute(ArrayList<File> resultList) {
        super.onPostExecute(resultList);
        if (onListFetchListener != null) {
            onListFetchListener.onPhotoListFetch(resultList);
        }
    }

    public static JSONArray getFileListJSONArray(
            ArrayList<String> parentDirList,
            String parentForFileList, ArrayList<String> fileList,
            String mimeType) throws JSONException {
        JSONArray array = new JSONArray();

        for (String parentDir : parentDirList) {
            JSONObject set = new JSONObject();
            set.put(KEY_FILELIST_PARENT, parentDir);
            ArrayList<String> inFileList = getFileList(
                    parentDir, new File(parentDir), mimeType);
            set.put(KEY_FILELIST_FILES, new JSONArray(inFileList));
            array.put(set);
        }
        if (parentForFileList != null && fileList != null) {
            JSONObject set = new JSONObject();
            set.put(KEY_FILELIST_PARENT, parentForFileList);
            set.put(KEY_FILELIST_FILES, new JSONArray(fileList));
            array.put(set);
        }

        return array;
    }

    public static ArrayList<String> getFileList(String rootDir, File parentDir,
                                                String mimetype) {
        ArrayList<String> inFiles = new ArrayList<String>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getFileList(rootDir, file, mimetype));
            }
            else {
                if(checkMimetype(file, mimetype)) {
//                    inFiles.add(file.getName());
                    String relativePath = file.getAbsolutePath().replace(
                            rootDir, "");
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

    private ArrayList<File> getFileList(JSONArray fileListArr) throws
            JSONException {
        ArrayList<File> fileList = new ArrayList<File>();
        for (int i = 0; i < fileListArr.length(); i++) {
            JSONObject set = fileListArr.getJSONObject(i);
            String parentDir = set.getString(KEY_FILELIST_PARENT);
            JSONArray fileArr = set.getJSONArray(KEY_FILELIST_FILES);
            for (int j = 0; j < fileArr.length(); j++) {
                String fileName = fileArr.getString(j);
                fileList.add(new File(parentDir, fileName));
            }
        }

        return fileList;
    }

    public interface OnListFetchListener {
        public void onPhotoListFetch(ArrayList<File> photoList);
    }
}
