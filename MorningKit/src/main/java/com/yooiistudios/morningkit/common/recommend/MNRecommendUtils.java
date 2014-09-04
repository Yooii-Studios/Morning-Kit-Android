package com.yooiistudios.morningkit.common.recommend;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.store.MNStoreFragment;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 9. 4.
 *
 * MNRecommendUtils
 *  친구에게 추천하기 관련 유틸리티 클래스
 */
public class MNRecommendUtils {
    private MNRecommendUtils() { throw new AssertionError("Must not create this class!"); }

    public static void showRecommendDialog(Activity activity) {
        Context context = activity.getApplicationContext();
        String appName = context.getString(R.string.recommend_app_full_name);
        String title = context.getString(R.string.recommend_title) + " [" + appName + "]";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);

        String link = getAppLink();
        String message = title + "\n\n" + context.getString(R.string.recommend_description) + "\n" + link;
        intent.putExtra(Intent.EXTRA_TEXT, message);

        // createChooser Intent
        Intent createChooser = Intent.createChooser(intent, context.getString(R.string.recommend_to_friends));

        // PendingIntent 가 완벽한 해법
        // (가로 모드에서 설정으로 와서 친구 추천하기를 누를 때 계속 반복 호출되는 상황을 막기 위함)
        PendingIntent pendingIntent =
                PendingIntent.getActivity(activity, 0, createChooser, 0);

        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    private static String getAppLink() {
        if (MNStoreFragment.IS_STORE_FOR_NAVER) {
            // 1500436# 은 여행의신(productNo)
            // 출시전이면 originalProductId, 후면 productNo
            // 모닝은 37676
            return "http://nstore.naver.com/appstore/web/detail.nhn?originalProductId=37676";
        } else {
            return "https://play.google.com/store/apps/details?id=com.yooiistudios.morningkit";
        }
    }
}
