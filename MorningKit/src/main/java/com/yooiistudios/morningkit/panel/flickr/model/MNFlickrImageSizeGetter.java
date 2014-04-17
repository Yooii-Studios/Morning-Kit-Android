package com.yooiistudios.morningkit.panel.flickr.model;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

// photo id를 가지고 사용 가능한 사진의 크기를 얻어내는 클래스
// 제일 큰 것은 너무 크다. 뒤에서 세 번째 것으로 사용하자
public class MNFlickrImageSizeGetter {

    static final String TAG = "MNFlickrImageSizeGetter";

    // Json으로 바꾸려고 했으나 XML로도 속도는 충분하다고 판단
    public static String getBiggiestFlickrImageURL(String photoId) throws XmlPullParserException, IOException {

        // photoID를 가지고 flikr에 요청
        URL queryUrl = new URL(makeQueryUrlString(photoId));

        // xml을 파싱
        XmlPullParserFactory parserCreator = XmlPullParserFactory
                .newInstance();
        XmlPullParser parser = parserCreator.newPullParser();

        parser.setInput(queryUrl.openStream(), null);

        int parserEvent = parser.getEventType();
        String tag;

        // 제일 뒤쪽 아이템이 가장 큰 사이즈. 이것의 소스를 얻어내기
        ArrayList<String> urlStrings = new ArrayList<String>();
        while (parserEvent != XmlPullParser.END_DOCUMENT) {
            switch (parserEvent) {
                case XmlPullParser.START_TAG:
                    tag = parser.getName();
                    if (tag.compareTo("size") == 0) {
                        urlStrings.add(parser.getAttributeValue(null, "source"));
                    }
                    break;
            }
            parserEvent = parser.next();
        }

        if (urlStrings.size() > 3) {
            return urlStrings.get(urlStrings.size() - 3);
        } else {
            return urlStrings.get(0);
        }
//        return urlString;
    }

    private static String makeQueryUrlString(String photoId) {
        return "http://api.flickr.com/services/rest/?"
                + "method=flickr.photos.getSizes"
                + "&api_key=" + MNFlickrFetcher.FLICKR_API_KEY
                + "&photo_id=" + photoId
                + "&format=rest";
    }
}
