package com.yooiistudios.morningkit.panel.flickr.model;

import com.stevenkim.waterlily.bitmapfun.util.AsyncTask;
import com.yooiistudios.morningkit.common.json.MNJsonUtils;
import com.yooiistudios.morningkit.common.utf.MNUtf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 16.
 *
 * MNFlickrFetchAsyncTask
 *  Volley에서 AsyncTask로 변경(Volley에 여러 문제가 있다고 판단)
 */
public class MNPhotoInfoFetcher extends AsyncTask<Void, Void, MNFlickrPhotoInfo> {

    private static final String TAG = "MNPhotoInfoFetcher";
    public static final String FLICKR_API_KEY = "ccc5c75e5380273b78d246a71353fab9";
    private static final Integer FLICKR_FIRST_LOADING_PER_PAGE = 20;

    String keyword;
    int totalPhotos = -1;
    OnPhotoInfoFetchListener listener;

    boolean isInfoNotFoundForKeyword = false;
    boolean isError = false;

    // Listener Interface
    public interface OnPhotoInfoFetchListener {
        public void onErrorOnLoad();
        public void onNotFoundPhotoInfoOnKeyword();
        public void onPhotoInfoLoaded(MNFlickrPhotoInfo flickrPhotoInfo);
    }

    // 첫번째 리퀘스트, 사진의 첫 페이지를 로딩하며 총 사진 갯수를 측정, 다음 로딩시 더 빠르게 하기 위함
    public static MNPhotoInfoFetcher newFirstQueryInstance(String keyword,
                                                           OnPhotoInfoFetchListener listener) {
        return new MNPhotoInfoFetcher(keyword, listener);
    }

    // 두 번째 리퀘스트, 총 사진 갯수를 가지고 랜덤 사진 쿼리
    public static MNPhotoInfoFetcher newQueryInstance(String keyword, int totalPhotos,
                                                      OnPhotoInfoFetchListener listener) {
        return new MNPhotoInfoFetcher(keyword, totalPhotos, listener);
    }

    private MNPhotoInfoFetcher(String keyword, OnPhotoInfoFetchListener listener) {
        this.keyword = keyword;
        this.totalPhotos = -1;
        this.listener = listener;
    }

    private MNPhotoInfoFetcher(String keyword, int totalPhotos,
                               OnPhotoInfoFetchListener listener) {
        this.keyword = keyword;
        this.totalPhotos = totalPhotos;
        this.listener = listener;
    }

    @Override
    protected MNFlickrPhotoInfo doInBackground(Void... params) {
        // 쿼리문 작성
        String queryUrlString;

        if (totalPhotos != -1) {
            // 두 번째 리퀘스트, 총 사진 갯수를 가지고 랜덤 페이지를 얻어 쿼리
            int randomPage;
            if (totalPhotos >= 4000) {
                totalPhotos = 4000; // 숫자가 너무 클 경우 문제가 생기기에 적절하게 조정
            }

            // 오픈소스 사용 X - 퍼포먼스 굉장히 좋지 않음
//        MersenneTwisterRNG randomGenerator = new MersenneTwisterRNG();
            Random randomGenerator = new Random();
            randomPage = randomGenerator.nextInt(totalPhotos) + 1;
            queryUrlString = makeQueryUrlString(keyword, 1, randomPage);
        } else {
            // 첫번째 리퀘스트, 사진의 첫 페이지를 로딩하며 총 사진 갯수를 측정, 다음 로딩시 더 빠르게 하기 위함
            queryUrlString = makeQueryUrlString(keyword, FLICKR_FIRST_LOADING_PER_PAGE, 1);
        }

        // 쿼리 및 결과 파싱
        JSONObject resultJsonObject = MNJsonUtils.getJsonObjectFromUrl(queryUrlString);

        if (resultJsonObject != null) {
            // 사진 url 추출
            try {
                JSONObject photos = resultJsonObject.getJSONObject("photos");
                if (photos != null) {
                    MNFlickrPhotoInfo flickrPhotoInfo = new MNFlickrPhotoInfo();

                    flickrPhotoInfo.setTotalPhotos(photos.getInt("total"));

                    // 난수 생성
                    JSONArray photoItems = photos.getJSONArray("photo");
                    // 오픈소스 사용 X - 퍼포먼스 굉장히 좋지 않음
//                                MersenneTwisterRNG randomGenerator = new MersenneTwisterRNG();
                    Random randomGenerator = new Random();
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
                            return flickrPhotoInfo;
                        } else {
                            isError = true;
                        }
                    } else {
                        isInfoNotFoundForKeyword = true;
                    }
                } else {
                    isError = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                isError = true;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(MNFlickrPhotoInfo flickrPhotoInfo) {
        super.onPostExecute(flickrPhotoInfo);

        if (listener != null) {
            if (!isInfoNotFoundForKeyword && !isError && flickrPhotoInfo != null) {
                listener.onPhotoInfoLoaded(flickrPhotoInfo);
            } else if (isInfoNotFoundForKeyword) {
                listener.onNotFoundPhotoInfoOnKeyword();
            } else {
                listener.onErrorOnLoad();
            }
        }
    }

    private static String makeQueryUrlString(String keyword, int perPage, int pageNum) {
        String escapedKeyword = MNUtf.getConverted_UTF_8_String(keyword);

        return "https://api.flickr.com/services/rest/?sort=random"
                + "&method=flickr.photos.search"
                + "&tags=" + escapedKeyword + "&tag_mode=any"
                + "&per_page=" + perPage + "&page=" + pageNum
                + "&api_key=" + FLICKR_API_KEY + "&format=json&nojsoncallback=1";
    }
}
