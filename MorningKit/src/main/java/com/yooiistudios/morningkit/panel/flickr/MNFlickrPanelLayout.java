package com.yooiistudios.morningkit.panel.flickr;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yooiistudios.morningkit.common.utf.MNUtf;
import com.yooiistudios.morningkit.panel.MNPanelLayout;
import com.yooiistudios.morningkit.panel.MNPanelType;

import org.json.JSONObject;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 17.
 *
 * MNFlickrPanelLayout
 */
public class MNFlickrPanelLayout extends MNPanelLayout {
    private static final String TAG = "MNFlickrPanelLayout";
    private static final String FLICKR_API_KEY = "ccc5c75e5380273b78d246a71353fab9";
    private static final Integer FLICKR_FIRST_LOADING_PER_PAGE = 20;

    public MNFlickrPanelLayout(Context context) {
        super(context);
    }

    public MNFlickrPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        initNetworkPanel();
        setPanelType(MNPanelType.FLICKR);
    }

    @Override
    protected void processLoading() {
        super.processLoading();

        // 플리커 키워드를 받아온다
        String keyword = "Morning";

        // 플리커 키워드를 가지고 사진 url을 추출
        String escapedKeyword = MNUtf.getConverted_UTF_8_String(keyword);
        String queryUrlString = "http://api.flickr.com/services/rest/?sort=random"
                + "&method=flickr.photos.search"
                + "&tags=" + escapedKeyword + "&tag_mode=any"
                + "&per_page=" + FLICKR_FIRST_LOADING_PER_PAGE + "&page=1"
                + "&api_key=" + FLICKR_API_KEY + "&format=json&nojsoncallback=1";

        // 쿼리
        RequestQueue mRequsetQueue = Volley.newRequestQueue(getContext());
        mRequsetQueue.add(new JsonObjectRequest(Request.Method.GET, queryUrlString, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.i(TAG, jsonObject.toString());

                    // 사진 url 추출
                    // 추출한 url을 통해 비트맵 가져오기
                    // 가져온 비트맵 가공
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                    showNetworkIsUnavailable();
                }
            })
        );

        // 사진 url을 통해 이미지 출력
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void updateUI() {
        stopLoadingAnimation();
        super.updateUI();

        // 마무리 가공된 Bitmap을 RecycleImageView에 대입
    }
}
