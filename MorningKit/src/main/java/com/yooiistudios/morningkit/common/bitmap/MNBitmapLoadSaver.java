package com.yooiistudios.morningkit.common.bitmap;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.testflightapp.lib.TestFlight;
import com.yooiistudios.morningkit.common.log.MNLog;

import org.json.JSONException;

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
                TestFlight.log("onErrorResponse: " + error.toString());
                MNLog.e(TAG, "onErrorResponse: " + error.toString());
            }
        });
    }

    // 기존 코드. volley 사용하는 방향으로 갈 예정
    /*
    public static Bitmap loadBitmapImageFromURL(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        Bitmap bitmap = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            int nSize = conn.getContentLength();

            BufferedInputStream bis = new BufferedInputStream(
                    conn.getInputStream(), nSize);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    */
}
