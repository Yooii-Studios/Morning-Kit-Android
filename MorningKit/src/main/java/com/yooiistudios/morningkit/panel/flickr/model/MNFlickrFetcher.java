package com.yooiistudios.morningkit.panel.flickr.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.testflightapp.lib.TestFlight;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.utf.MNUtf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.uncommons.maths.random.MersenneTwisterRNG;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 18.
 *
 * MNFlickrFetcher
 *  플리커 사진 정보를 읽는 로직을 담당
 */
public class MNFlickrFetcher {
    private static final String TAG = "MNFlickrFetcher";
    public static final String FLICKR_API_KEY = "ccc5c75e5380273b78d246a71353fab9";
    private static final Integer FLICKR_FIRST_LOADING_PER_PAGE = 20;

    private MNFlickrFetcher() { throw new AssertionError("You MUST not create this class!"); }

    public interface OnFetcherListner {
        public void onFlickrPhotoInfoLoaded(MNFlickrPhotoInfo flickrPhotoInfo);
        public void onErrorResponse();
    }

    // 첫번째 리퀘스트, 사진의 첫 페이지를 로딩하며 총 사진 갯수를 측정, 다음 로딩시 더 빠르게 하기 위함
    public static JsonObjectRequest requestFirstQuery(final String keyword, final OnFetcherListner onFetcherListner, Context context) {
        // 플리커 키워드를 가지고 사진 url을 추출
        String queryUrlString = makeQueryUrlString(keyword, FLICKR_FIRST_LOADING_PER_PAGE, 1);
        MNLog.i(TAG, queryUrlString);

        // 쿼리
        return addRequest(queryUrlString, context, onFetcherListner);
    }

    // 두 번째 리퀘스트, 총 사진 갯수를 가지고 랜덤 사진 쿼리
    public static JsonObjectRequest requestQuery(final String keyword, int totalPhotos, final OnFetcherListner onFetcherListner, Context context) {
        // 랜덤 페이지 생성
        int randomPage;
        if (totalPhotos >= 4000) {
            totalPhotos = 4000; // 숫자가 너무 클 경우 문제가 생기기에 적절하게 조정
        }
        MersenneTwisterRNG randomGenerator = new MersenneTwisterRNG();
        randomPage = randomGenerator.nextInt(totalPhotos) + 1;

        // 플리커 키워드를 가지고 사진 url을 추출
        String queryUrlString = makeQueryUrlString(keyword, 1, randomPage);
        MNLog.i(TAG, queryUrlString);

        return addRequest(queryUrlString, context, onFetcherListner);
    }

    private static String makeQueryUrlString(String keyword, int perPage, int pageNum) {
        String escapedKeyword = MNUtf.getConverted_UTF_8_String(keyword);

        return "http://api.flickr.com/services/rest/?sort=random"
                + "&method=flickr.photos.search"
                + "&tags=" + escapedKeyword + "&tag_mode=any"
                + "&per_page=" + perPage + "&page=" + pageNum
                + "&api_key=" + FLICKR_API_KEY + "&format=json&nojsoncallback=1";
    }

    private static JsonObjectRequest addRequest(String queryUrlString, Context context, final OnFetcherListner onFetcherListner) {
        final RequestQueue mRequsetQueue = Volley.newRequestQueue(context);
        JsonObjectRequest queryRequest = new JsonObjectRequest(Request.Method.GET, queryUrlString, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // 사진 url 추출
                        try {
                            JSONObject photos = jsonObject.getJSONObject("photos");
                            if (photos != null) {
                                MNFlickrPhotoInfo flickrPhotoInfo = new MNFlickrPhotoInfo();

                                flickrPhotoInfo.setTotalPhotos(photos.getInt("total"));

                                // 난수 생성
                                JSONArray photoItems = photos.getJSONArray("photo");
                                MersenneTwisterRNG randomGenerator = new MersenneTwisterRNG();
                                MNLog.now("photoItems.length(): " + photoItems.length());
                                if (photoItems.length() != 0) {
                                    int randomIndex = randomGenerator.nextInt(photoItems.length());

                                    // photoItem에서 url 조립
                                    JSONObject photoItem = photoItems.getJSONObject(randomIndex);

                                    String idString, secretString, serverString, farmString;
                                    idString = photoItem.getString("id");
                                    flickrPhotoInfo.setPhotoId(idString);
                                    secretString = photoItem.getString("secret");
                                    serverString = photoItem.getString("server");
                                    farmString = photoItem.getString("farm");

                                    if (idString != null && secretString != null
                                            && serverString != null && farmString != null) {
                                        flickrPhotoInfo.setPhotoUrlString(
                                                String.format("http://farm%s.staticflickr.com/%s/%s_%s_z.jpg",
                                                        farmString, serverString, idString, secretString));

                                        MNLog.i(TAG, flickrPhotoInfo.getPhotoUrlString());
                                        onFetcherListner.onFlickrPhotoInfoLoaded(flickrPhotoInfo);
                                    } else {
                                        TestFlight.log("flickrPhotoUrlString is null");
                                        onFetcherListner.onErrorResponse();
                                    }
                                } else {
                                    TestFlight.log("flickr photoItems.length() is 0");
                                    onFetcherListner.onErrorResponse();
                                }
                            } else {
                                TestFlight.log("photos is null");
                                onFetcherListner.onErrorResponse();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            TestFlight.log(e.toString());
                            onFetcherListner.onErrorResponse();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        TestFlight.log("onErrorResponse: " + volleyError.toString());
                        MNLog.e(TAG, "onErrorResponse: " + volleyError.toString());
                        onFetcherListner.onErrorResponse();
                    }
                });

        if (queryRequest != null) {
            mRequsetQueue.add(queryRequest);
        }
        return queryRequest;
    }
}
