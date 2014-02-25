package com.yooiistudios.morningkit.panel.flickr.detail;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.yooiistudios.morningkit.R;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 18.
 *
 * MNFlickrBitmapAsyncTask
 *  원본 플리커 비트맵을 다양하게 처리
 */
public class MNFlickrBitmapSaveAsyncTask extends AsyncTask<Void, Integer, Void> {

    private static final String TAG = "MNFlickrBitmapAsyncTask";
    String flickrUrlString;
    boolean isGrayScale;
    private MNFlickrBitmapSaveAsyncTaskListener flickrBitmapSaveAsyncTaskListener;
    private Context context;

    private SuperActivityToast superActivityToast;

    public interface MNFlickrBitmapSaveAsyncTaskListener {
        public void onBitmapSaveFinished();
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

        // 진행 시작 메시지 띄우기
        superActivityToast = new SuperActivityToast((Activity)context, SuperToast.Type.PROGRESS_HORIZONTAL);
        superActivityToast.setText(context.getString(R.string.saving));
        superActivityToast.setIndeterminate(true);
        superActivityToast.setMaxProgress(100);
        superActivityToast.setProgress(0);
        superActivityToast.show();
    }

    @Override
    protected void onProgressUpdate(final Integer... values) {
        super.onProgressUpdate(values);
        superActivityToast.setProgress(values[0]);
    }

    @Override
    protected Void doInBackground(Void... params) {

        // photo id를 가지고 가장 큰 사진의 url를 얻기
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onProgressUpdate(10);

        // url에서 사진을 bitmap으로 가져오기
//        try {
//            Thread.sleep(150);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        onProgressUpdate(20);

        // grayScale 적용
//        try {
//            Thread.sleep(150);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        onProgressUpdate(40);

        // bitmap을 local에 저장하기
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onProgressUpdate(60);

        // 완료 메시지 띄우기
//        try {
//            Thread.sleep(150);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        onProgressUpdate(80);

        // 저장되었다면 Media 업데이트하기
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onProgressUpdate(100);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        superActivityToast.dismiss();
        flickrBitmapSaveAsyncTaskListener.onBitmapSaveFinished();
    }
}
