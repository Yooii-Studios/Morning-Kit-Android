package com.yooiistudios.morningkit.common.review;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.unlock.MNUnlockActivity;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 6. 5.
 *
 * MNReviewUtil
 *  앱 실행 수를 체크해 리뷰를 요청
 */
public class MNReviewUtil {

    private static final String PREFS_KEY_REVIEW = "Review";
    private static final String KEY_LAUNCH_COUNT = "Launch Count";
    private static final String KEY_ASK_STATE_ORDINAL = "Ask Again";

    private static final int VALUE_DEFAULT_LAUNCH_COUNT = 0;

    private static final int ASK_THRESHOLD_FIRST = 10;
    private static final int ASK_THRESHOLD_AGAIN = 40;
    private enum ASK_STATE {
        FIRST,
        LAST,
        REVIEWED
    }

    public static void checkRate(final Activity activity) {
        if (activity.getSharedPreferences(MNUnlockActivity.SHARED_PREFS,
                Context.MODE_PRIVATE).getBoolean(MNUnlockActivity.REVIEW_USED, false)) {
            return;
        }
        final SharedPreferences prefs = activity.getSharedPreferences(PREFS_KEY_REVIEW, Context.MODE_PRIVATE);

        //check launch count.
        int launchCount = prefs.getInt(KEY_LAUNCH_COUNT, VALUE_DEFAULT_LAUNCH_COUNT);
        int stateOrdinal = prefs.getInt(KEY_ASK_STATE_ORDINAL, ASK_STATE.FIRST.ordinal());
        final ASK_STATE state = ASK_STATE.values()[stateOrdinal];
        int threshold;

        if (state.equals(ASK_STATE.FIRST)) {
            threshold = ASK_THRESHOLD_FIRST;
        } else if (state.equals(ASK_STATE.LAST)) {
            threshold = ASK_THRESHOLD_AGAIN;
        } else {
            return;
        }
        prefs.edit().putInt(KEY_LAUNCH_COUNT, ++launchCount).apply();

        if (launchCount >= threshold) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                builder = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK);
            } else {
                builder = new AlertDialog.Builder(activity);
            }
            builder.setTitle(activity.getString(R.string.rate_morning_kit));
            String appName = activity.getString(R.string.recommend_app_full_name);
            String message = activity.getString(R.string.rate_it_contents, appName);
            builder.setMessage(message);
            builder.setPositiveButton(R.string.rate_it_rate, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MNReviewApp.showReviewActivity(activity);
                    prefs.edit().putInt(KEY_ASK_STATE_ORDINAL, ASK_STATE.REVIEWED.ordinal()).apply();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.rate_it_no_thanks, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_KEY_REVIEW, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (state.equals(ASK_STATE.FIRST)) {
                        //ask again later
                        editor.putInt(KEY_LAUNCH_COUNT, VALUE_DEFAULT_LAUNCH_COUNT);
                        editor.putInt(KEY_ASK_STATE_ORDINAL, ASK_STATE.LAST.ordinal());
                    } else if (state.equals(ASK_STATE.LAST)) {
                        //never ask after
                        editor.putInt(KEY_ASK_STATE_ORDINAL, ASK_STATE.REVIEWED.ordinal());
                    }
                    editor.apply();
                    dialog.dismiss();
                }
            });
            AlertDialog reviewDialog = builder.create();
            reviewDialog.setCancelable(false);
            reviewDialog.setCanceledOnTouchOutside(false);
            reviewDialog.show();
        }
    }
}
