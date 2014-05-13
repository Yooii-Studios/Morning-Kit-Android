package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.graphics.BitmapFactory;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 9.
 */
public class MNPhotoAlbumFileManager {

    private static final String TAG = "FileManager";

    public static ArrayList<File> getValidImageFileList(
            ArrayList<File> fileList){
        ArrayList<File> validFileList = new ArrayList<File>();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        for (File file : fileList) {
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            if (options.outWidth > 0 && options.outHeight > 0) {
                validFileList.add(file);
            }
        }
        return validFileList;
    }
}
