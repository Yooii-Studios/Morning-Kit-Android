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
    private static final String FLICKR_API_KEY = "ccc5c75e5380273b78d246a71353fab9";
    private static final Integer FLICKR_FIRST_LOADING_PER_PAGE = 20;

    private MNFlickrFetcher() { throw new AssertionError("You MUST not create this class!"); }

    // 첫번째 리퀘스트, 사진의 첫 페이지를 로딩하며 총 사진 갯수를 측정, 다음 로딩시 더 빠르게 하기 위함
    public static void requestFirst(final String keyword, final MNFlickrFetcherListner flickrFetcherListner, Context context) {
        // 플리커 키워드를 가지고 사진 url을 추출
        String escapedKeyword = MNUtf.getConverted_UTF_8_String(keyword);
        String queryUrlString = "http://api.flickr.com/services/rest/?sort=random"
                + "&method=flickr.photos.search"
                + "&tags=" + escapedKeyword + "&tag_mode=any"
                + "&per_page=" + FLICKR_FIRST_LOADING_PER_PAGE + "&page=1"
                + "&api_key=" + FLICKR_API_KEY + "&format=json&nojsoncallback=1";
        MNLog.i(TAG, queryUrlString);

        // 쿼리
        final RequestQueue mRequsetQueue = Volley.newRequestQueue(context);
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

                                // 난수 생성
                                MersenneTwisterRNG randomGenerator = new MersenneTwisterRNG();
                                int randomIndex = randomGenerator.nextInt(totalPhotosInThisPage);

                                // photoItem에서 url 조립
                                JSONObject photoItem = photos.getJSONArray("photo").getJSONObject(randomIndex);

                                String idString, secretString, serverString, farmString;
                                idString = photoItem.getString("id");
                                secretString = photoItem.getString("secret");
                                serverString = photoItem.getString("server");
                                farmString = photoItem.getString("farm");

                                if (idString != null && secretString != null
                                        && serverString != null && farmString != null) {
                                    flickrPhotoInfo.setPhotoUrlString(
                                            String.format("http://farm%s.staticflickr.com/%s/%s_%s_z.jpg",
                                                    farmString, serverString, idString, secretString));

                                    MNLog.i(TAG, flickrPhotoInfo.getPhotoUrlString());
                                    flickrFetcherListner.onFlickrPhotoInfoLoaded(flickrPhotoInfo);
                                } else {
                                    TestFlight.log("flickrPhotoUrlString is null");
                                    flickrFetcherListner.onErrorResponse();
                                }
                            } else {
                                TestFlight.log("photos is null");
                                flickrFetcherListner.onErrorResponse();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            TestFlight.log(e.toString());
                            flickrFetcherListner.onErrorResponse();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        TestFlight.log("onErrorResponse: " + volleyError.toString());
                        MNLog.e(TAG, "onErrorResponse: " + volleyError.toString());
                        flickrFetcherListner.onErrorResponse();
                    }
                })
        );
    }
}
