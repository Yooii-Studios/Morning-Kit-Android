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
    private static final String KEY_REVIEWED = "KEY_REVIEWED";

    public static void showReviewDialogIfConditionMet(final Activity activity) {
        if (activity.getSharedPreferences(MNUnlockActivity.SHARED_PREFS,
                Context.MODE_PRIVATE).getBoolean(MNUnlockActivity.REVIEW_USED, false)) {
            return;
        }
        final SharedPreferences prefs = activity.getSharedPreferences(PREFS_KEY_REVIEW, Context.MODE_PRIVATE);
        int launchCount = prefs.getInt(KEY_LAUNCH_COUNT, 1);

        //check launch count.
        if (shouldShowDialog(prefs, launchCount)) {
            showDialog(activity, prefs.edit());
        }
        if (launchCount <= 40) {
            prefs.edit().putInt(KEY_LAUNCH_COUNT, ++launchCount).apply();
        }
    }

    /*
    // Test
    public static void showTestReviewDialogIfNecessary(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY_REVIEW, Context.MODE_PRIVATE);
        for (int i = 1; i < 70; i++) {
            if (i == 30) {
                prefs.edit().putBoolean(KEY_REVIEWED, true).apply();
            }
            if (shouldShowDialog(prefs, i)) {
                MNLog.now("review count: " + i);
            }
        }
    }
    */

    private static boolean shouldShowDialog(SharedPreferences prefs, int launchCount) {
        return !prefs.getBoolean(KEY_REVIEWED, false) && (launchCount == 10 || launchCount == 40);
    }

    private static void showDialog(final Activity activity, final SharedPreferences.Editor editor) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT);
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
                editor.putBoolean(KEY_REVIEWED, true).apply();
                MNReviewApp.showReviewActivity(activity);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.rate_it_no_thanks, null);
        AlertDialog reviewDialog = builder.create();
        reviewDialog.setCancelable(false);
        reviewDialog.setCanceledOnTouchOutside(false);
        reviewDialog.show();
    }
}
