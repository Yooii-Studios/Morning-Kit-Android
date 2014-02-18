package com.yooiistudios.morningkit.panel.flickr;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.testflightapp.lib.TestFlight;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.utf.MNUtf;
import com.yooiistudios.morningkit.panel.MNPanelLayout;
import com.yooiistudios.morningkit.panel.MNPanelType;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrPhotoInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.uncommons.maths.random.MersenneTwisterRNG;

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
        MNLog.i(TAG, queryUrlString);

        // 쿼리
        RequestQueue mRequsetQueue = Volley.newRequestQueue(getContext());
        mRequsetQueue.add(new JsonObjectRequest(Request.Method.GET, queryUrlString, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    // 사진 url 추출
                    try {
                        JSONObject photos = jsonObject.getJSONObject("photos");
                        if (photos != null) {
                            MNFlickrPhotoInfo flickrPhotoInfo = new MNFlickrPhotoInfo();
                            flickrPhotoInfo.setTotalPhotos(photos.getInt("total"));

                            // 총 사진 갯수가 한 페이지를 넘어가면 index를 20 내에서 난수 생성 필요
                            int totalPhotosInThisPage = flickrPhotoInfo.getTotalPhotos();
                            if (totalPhotosInThisPage >= FLICKR_FIRST_LOADING_PER_PAGE) {
                                totalPhotosInThisPage = FLICKR_FIRST_LOADING_PER_PAGE;
                            }

                            // 난수
                            MersenneTwisterRNG randomGenerator = new MersenneTwisterRNG();
                            int randomIndex = randomGenerator.nextInt(totalPhotosInThisPage);

                            JSONObject photoItem = photos.getJSONArray("photo").getJSONObject(randomIndex);

                            String idString, secretString, serverString, farmString;
                            idString = photoItem.getString("id");
                            secretString = photoItem.getString("secret");
                            serverString = photoItem.getString("server");
                            farmString = photoItem.getString("farm");

                            if (idString != null && secretString != null && serverString != null && farmString != null) {
                                flickrPhotoInfo.setPhotoUrlString(
                                        String.format("http://farm%s.staticflickr.com/%s/%s_%s_z.jpg",
                                                farmString, serverString, idString, secretString));
                                MNLog.now(flickrPhotoInfo.getPhotoUrlString());
                            } else {
                                Toast.makeText(getContext(), getResources().getString(R.string.flickr_error_access_server), Toast.LENGTH_SHORT).show();
                                TestFlight.log("flickrPhotoUrlString is null");
                                showNetworkIsUnavailable();
                            }
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.flickr_error_access_server), Toast.LENGTH_SHORT).show();
                            TestFlight.log("photos is null");
                            showNetworkIsUnavailable();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), getResources().getString(R.string.flickr_error_access_server), Toast.LENGTH_SHORT).show();
                        TestFlight.log(e.toString());
                        showNetworkIsUnavailable();
                    }

                    // 추출한 url을 통해 비트맵 가져오기
                    // 가져온 비트맵 가공
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    TestFlight.log("onErrorResponse: " + volleyError.toString());
                    MNLog.e(TAG, "onErrorResponse: " + volleyError.toString());

                    Toast.makeText(getContext(), getResources().getString(R.string.flickr_error_access_server), Toast.LENGTH_SHORT).show();
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
