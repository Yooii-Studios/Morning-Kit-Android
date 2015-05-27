package com.yooiistudios.morningkit.common.review;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.yooiistudios.morningkit.MNIabInfo;
import com.yooiistudios.morningkit.setting.store.MNStoreType;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 28.
 *
 * MNReviewApp
 */
public class MNReviewApp {
    public static int REQ_REVIEW_APP = 4444;

    public static void showReviewActivity(Context context) {
        Uri uri = Uri.parse(getLink(context));
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            ((Activity)context).startActivityForResult(goToMarket, REQ_REVIEW_APP);
//            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Couldn't launch the market", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getLink(Context context) {
        if (MNIabInfo.STORE_TYPE.equals(MNStoreType.NAVER)) {
            // 1500436# 은 여행의신(productNo)
            // 출시전이면 originalProductId, 후면 productNo
            // 모닝은 37676
            return "http://nstore.naver.com/appstore/web/detail.nhn?originalProductId=37676";
        } else {
            return "market://details?id=" + context.getPackageName();
        }
    }
}
