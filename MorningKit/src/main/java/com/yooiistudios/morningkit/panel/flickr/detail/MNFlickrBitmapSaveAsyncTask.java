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
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrImageSizeGetter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 18.
 *
 * MNFlickrBitmapSaveAsyncTask
 *  원본 플리커 이미지를 저장하는 태스크
 */
public class MNFlickrBitmapSaveAsyncTask extends AsyncTask<Void, Integer, Boolean> {

    private static final String TAG = "MNFlickrBitmapAsyncTask";

    private String imageId;
    private boolean isGrayScale;
    private Context context;
    private Context applicationContext;
    private SuperActivityToast superActivityToast;
    private Bitmap bitmap;

    private Boolean result;

    public MNFlickrBitmapSaveAsyncTask(String imageId, boolean isGrayScale, Context context,
                                       Context applicationContext) {
        this.imageId = imageId;
        this.isGrayScale = isGrayScale;
        this.context = context;
        this.applicationContext = applicationContext;
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
            biggiestFlickrImageUrlString = MNFlickrImageSizeGetter.getBiggiestFlickrImageURL(imageId);
            onProgressUpdate(35);
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // url에서 사진을 bitmap으로 가져오기
        if (biggiestFlickrImageUrlString != null) {
            bitmap = MNBitmapLoadSaver.bitmapFromUrl(biggiestFlickrImageUrlString);
            onProgressUpdate(70);

            if (bitmap != null) {
                if (isGrayScale) {
                    bitmap = MNBitmapProcessor.getGrayScaledBitmap(bitmap);
                }
                onProgressUpdate(80);

                // bitmap을 local에 저장하기
                String filePath = null;
                try {
                    filePath = MNBitmapLoadSaver.saveBitmapToLibraryInSDCard(bitmap);
                    bitmap.recycle();

                    onProgressUpdate(100);

                    // 저장되었다면 Media 업데이트하기
                    MediaScannerConnection.scanFile(applicationContext, new String[]{filePath}, null, null);
                    return true;
                } catch (IOException e) {
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
            MNLog.i(TAG, "Flickr bitmap save succeed");
            Toast.makeText(context, R.string.flickr_photo_saved, Toast.LENGTH_SHORT).show();
        } else {
            MNLog.e(TAG, "bitmap save failed");
        }
    }

    @Override
    protected void onCancelled(Boolean result) {
        handleCancelled(result);
    }

    @Override
    protected void onCancelled() {
        handleCancelled(this.result);
    }

    private void handleCancelled(Boolean result) {
        MNLog.e(TAG, "onCancelled");

        if (superActivityToast != null) {
            superActivityToast.dismiss();
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
