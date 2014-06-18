package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 9.
 */
public class MNPhotoAlbumFileManager {

    private static final String TAG = "FileManager";
    public static final File DEFAULT_PARENT_DIR =
            Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM);

    public static ArrayList<String> getValidImageFileList(String rootDir,
            ArrayList<String> fileList){
        ArrayList<String> validFileList = new ArrayList<String>();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        for (String fileName : fileList) {
            if (fileName != null) {
                String path = new File(rootDir, fileName).getAbsolutePath();
                BitmapFactory.decodeFile(path, options);
                if (options.outWidth > 0 && options.outHeight > 0) {
                    validFileList.add(fileName);
                }
            }
        }
        return validFileList;
    }
}
