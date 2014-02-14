package com.yooiistudios.morningkit.common.review;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 28.
 *
 * MNReviewApp
 */
public class MNReviewApp {
    public static int REQ_REVIEW_APP = 4444;

    public static void showReviewActivity(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            ((Activity)context).startActivityForResult(goToMarket, REQ_REVIEW_APP);
//            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Couldn't launch the market", Toast.LENGTH_SHORT).show();
        }
    }
}
