package com.yooiistudios.morningkit.common.file;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

// 동현이 소스
public class ExternalStorageManager {
    public static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().toString();
    public static final String APP_DIRECTORY_HIDDEN = "/.MorningKit";
    public static final String APP_DIRECTORY = "/MorningKit";

	/**
	 * 
	 * @param fileName
	 * @return null if file not exists.
	 */
	public static File getFileFromExternalDirectory(Context context, String fileName, String directory) {
		if (isExternalStorageReadable(context) && isExternalStorageWritable(context)) {
			File f = new File(createExternalDirectory(context, directory), fileName);

			if (f.exists()){
				return f;
			}
		}
		
		return null;
	}
	public static boolean deleteFileFromExternalDirectory(Context context, String fileName, String directory) {
		if (isExternalStorageReadable(context) && isExternalStorageWritable(context)) {
			File f = new File(createExternalDirectory(context, directory), fileName);

			if (f.exists()){
				return f.delete();
			}
		}
		
		return false;
	}
	
	/**
	 * created directory as file. Make new directory if there's no directory exists.
	 * @return null if problem occurred.
	 */
	public static File createExternalDirectory(Context context, String directory) {
		if (isExternalStorageReadable(context) && isExternalStorageWritable(context)) {
            // app root 디렉토리 체크
            File appDir = new File(Environment.getExternalStorageDirectory(), APP_DIRECTORY);
            File appHiddenDir = new File(Environment.getExternalStorageDirectory(), APP_DIRECTORY_HIDDEN);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            if (!appHiddenDir.exists()) {
                appHiddenDir.mkdir();
            }

            // 디렉토리 생성
			File dir = new File(Environment.getExternalStorageDirectory(), directory);
			if (!dir.exists()){
                dir.mkdir();
            }
			return dir;
		}
		return null;
	}
	
	/**
	 * create file in external directory. file deleted if exists.
	 * @param fileName
	 * @return
	 */
	public static File createFileInExternalDirectory(Context context, String fileName, String directory) {
		if (isExternalStorageReadable(context) && isExternalStorageWritable(context)) {
			try{
				File f = new File(createExternalDirectory(context, directory), fileName);

				if (f.exists()) {
                    f.delete();
                }

				boolean created = f.createNewFile();
				if (created){
					return f;
				}
			} catch(IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	/* Checks if external storage is available for read and write */
	private static boolean isExternalStorageWritable(Context context) {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    } else {
            return false;
	    }
	}

	/* Checks if external storage is available to at least read */
	private static boolean isExternalStorageReadable(Context context) {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    } else {
	    	return false;
	    }
	}
}
