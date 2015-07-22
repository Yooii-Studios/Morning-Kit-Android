package com.yooiistudios.morningkit.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Wooseong Kim in News-Android-L from Yooii Studios Co., LTD. on 15. 1. 6.
 *
 * FacebookUtils
 *  페이스북의 유이스튜디오 페이지 링크를 열어주는 유틸
 */
public class FacebookUtils {
//    private static final String FB_YOOII_ID = "652380814790935";
    private static final String FACEBOOK_URL = "https://www.facebook.com/YooiiMooii";

    private FacebookUtils() { throw new AssertionError("You can't create this class!"); }

    public static void openYooiiPage(Context context) {
        try {
            Uri uri = Uri.parse("fb://facewebmodal/f?href=" + FACEBOOK_URL);
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/YooiiMooii")));
        }
    }
}
