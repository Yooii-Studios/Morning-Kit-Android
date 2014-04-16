package com.yooiistudios.morningkit.common.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.testflightapp.lib.TestFlight;
import com.yooiistudios.morningkit.common.log.MNLog;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 18.
 *
 * MNBitmapLoadSaver
 *  비트맵 저장과 로드에 관련된 로직을 수행
 */
public class MNBitmapLoadSaver {
    private static final String TAG = "MNBitmapLoadSaver";

    // 비트맵 로딩 리스너
    public interface OnLoadListener {
        public void onBitmapLoad(Bitmap bitmap) throws JSONException;
    }

    public static void loadBitmapUsingVolley(String bitmapUrlString, Context context,
                                             final OnLoadListener bitmapLoadListener) {

        // BitmapLruCache를 null 처리 하고 싶으나 생성이 안되서 크기가 없는 것으로 대입
        ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(context), new BitmapLruCache(1));
        imageLoader.get(bitmapUrlString, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                // 가져온 비트맵 가공
                if (response.getBitmap() != null) {
                    try {
                        bitmapLoadListener.onBitmapLoad(response.getBitmap());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                MNLog.e(TAG, "onErrorResponse: " + error.toString());
            }
        });
    }

    // 기존 코드. volley 사용하는 방향으로 갈 예정, 플리커 로컬 저장 시 사용함.
    /*
    public static Bitmap loadBitmapImageFromURL(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        Bitmap bitmap = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            int nSize = conn.getContentLength();

            // CHECK IMAGE SIZE BEFORE LOAD
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);

            int scale = 1;
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(bis, null, opts);

            // IF IMAGE IS BIGGER THAN MAX, DOWN SAMPLING
            opts.inSampleSize = calculateInSampleSize(opts, opts.outWidth, opts.outHeight);
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = scale;

//            bis = new BufferedInputStream(conn.getInputStream(), nSize);

            MNLog.now(bis.toString());

            bitmap = BitmapFactory.decodeStream(bis, null, opts);

            MNLog.now(bitmap.toString());

            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    */

    // 외부 SD 카드에 플리커 사진 저장 - 유니크한 이름을 위해 Morning Kit_현재 시간 MilliSec.PNG로
    static public String saveBitmapToLibraryInSDCard(Bitmap bitmap) throws IOException {

        String external_storage_path =  Environment.getExternalStorageDirectory().toString();
        String appAbsolutePath = external_storage_path + "/MorningKit/";

        // 최초 저장시 MorningKit 폴더 생성해주기
        File appPath = new File(appAbsolutePath);
        if (!appPath.isDirectory()) {
            appPath.mkdirs();
        }

        String fileName = "MorningKit_Flickr_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        String filePath = appAbsolutePath + fileName;
        FileOutputStream fos = new FileOutputStream(filePath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();

        return filePath;
    }

    // bitmapfun에서 가져온 코드
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more 2w2 with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            // 2x just once, not while - by Wooseong Kim
            if (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
            }

            /*
            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
            */
        }
        return inSampleSize;
    }


    /**
     *  This Function takes some time.
     *  Don't Call it in UI Thread.
     */
    public static Bitmap bitmapFromUrl (String imageURL){
        try {
            byte[] datas = getImageDataFromUrl( new URL(imageURL) );

            // CHECK IMAGE SIZE BEFORE LOAD
            int scale = 1;
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);

            // IF IMAGE IS BIGGER THAN MAX, DOWN SAMPLING
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = calculateInSampleSize(opts, opts.outWidth, opts.outHeight);

            return BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] getImageDataFromUrl (URL url) {
        byte[] datas = {};

        try {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            datas = IOUtils.toByteArray(input);

            input.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return datas;
    }
}
