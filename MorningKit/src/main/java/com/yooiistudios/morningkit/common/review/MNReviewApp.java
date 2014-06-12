package com.yooiistudios.morningkit.common.review;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.yooiistudios.morningkit.setting.store.MNStoreFragment;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 28.
 *
 * MNReviewApp
 */
public class MNReviewApp {
    public static int REQ_REVIEW_APP = 4444;

    public static void showReviewActivity(Context context) {
        Uri uri;
        if (MNStoreFragment.IS_STORE_FOR_NAVER) {
            // 1500436# 은 여행의신(productNo)
            // 출시전이면 originalProductId, 후면 productNo
            // 모닝은 37676
            uri = (Uri.parse("http://nstore.naver.com/appstore/web/detail.nhn?originalProductId=37676"));
        } else {
            uri = Uri.parse("market://details?id=" + context.getPackageName());
        }
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//        goToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        try {
            ((Activity)context).startActivityForResult(goToMarket, REQ_REVIEW_APP);
//            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Couldn't launch the market", Toast.LENGTH_SHORT).show();
        }
    }
}
