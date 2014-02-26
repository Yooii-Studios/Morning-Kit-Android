package com.yooiistudios.morningkit.panel.flickr.detail;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapLoadSaver;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapProcessor;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrImageSizeGetter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 18.
 *
 * MNFlickrBitmapAsyncTask
 *  원본 플리커 비트맵을 다양하게 처리
 */
public class MNFlickrBitmapSaveAsyncTask extends AsyncTask<Void, Integer, Boolean> {

    private static final String TAG = "MNFlickrBitmapAsyncTask";

    String flickrUrlString;
    boolean isGrayScale;
    private MNFlickrBitmapSaveAsyncTaskListener flickrBitmapSaveAsyncTaskListener;
    private Context context;

    private SuperActivityToast superActivityToast;

    public interface MNFlickrBitmapSaveAsyncTaskListener {
        public void onBitmapSaveFinished();
        public void onBitmapSaveFailed();
    }

    public MNFlickrBitmapSaveAsyncTask(String flickrUrlString, boolean isGrayScale,
                                       MNFlickrBitmapSaveAsyncTaskListener flickrBitmapSaveAsyncTaskListener,
                                       Context context) {
        this.flickrUrlString = flickrUrlString;
        this.isGrayScale = isGrayScale;
        this.flickrBitmapSaveAsyncTaskListener = flickrBitmapSaveAsyncTaskListener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // 진행 시작 메시지 띄우기 - 버전별 구분
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            superActivityToast = new SuperActivityToast((Activity)context, SuperToast.Type.PROGRESS_HORIZONTAL);
            superActivityToast.setText(context.getString(R.string.saving));
            superActivityToast.setIndeterminate(true);
            superActivityToast.setMaxProgress(100);
            superActivityToast.setProgress(0);
            superActivityToast.show();
        } else {
            Toast.makeText(context, R.string.saving, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onProgressUpdate(final Integer... values) {
        super.onProgressUpdate(values);
        if (superActivityToast != null) {
            superActivityToast.setProgress(values[0]);
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        // photo id를 가지고 가장 큰 사진의 url를 얻기
        String biggiestFlickrImageUrlString = null;
        try {
            biggiestFlickrImageUrlString = MNFlickrImageSizeGetter.getBiggiestFlickrImageURL(flickrUrlString);
            onProgressUpdate(25);
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // url에서 사진을 bitmap으로 가져오기
        if (biggiestFlickrImageUrlString != null) {
            Bitmap bitmap = null;
//                bitmap = MNBitmapLoadSaver.loadBitmapImageFromURL(biggiestFlickrImageUrlString);
            bitmap = MNBitmapLoadSaver.bitmapFromUrl(biggiestFlickrImageUrlString);
            onProgressUpdate(50);

            if (bitmap != null) {
                if (isGrayScale) {
                    bitmap = MNBitmapProcessor.getGrayScaledBitmap(bitmap);
                }
                onProgressUpdate(75);

                // bitmap을 local에 저장하기
                String filePath = null;
                try {
                    filePath = MNBitmapLoadSaver.saveBitmapToLibraryInSDCard(bitmap);
                    bitmap.recycle();
                    onProgressUpdate(100);

                    // 저장되었다면 Media 업데이트하기
                    MediaScannerConnection.scanFile(context, new String[]{filePath}, null, null);

                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isBitmapSaved) {
        super.onPostExecute(isBitmapSaved);
        if (superActivityToast != null) {
            superActivityToast.dismiss();
        }
        if (isBitmapSaved) {
            Toast.makeText(context, R.string.flickr_photo_saved, Toast.LENGTH_SHORT).show();
            flickrBitmapSaveAsyncTaskListener.onBitmapSaveFinished();
        } else {
            flickrBitmapSaveAsyncTaskListener.onBitmapSaveFailed();
        }
    }
}
