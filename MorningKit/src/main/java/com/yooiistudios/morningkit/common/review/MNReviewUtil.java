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
 */
public class MNReviewUtil {

    private static final String SPNAME_REVIEW = "Review";
    private static final String KEY_LAUNCHCOUNT = "Launch Count";
    private static final String KEY_ASKAGAIN = "Ask Again";

    private static final int VALUE_DEFAULT_LAUNCHCOUNT = 0;

    private static final int ASKLIMIT_FIRST = 10;
    private static final int ASKLIMIT_AGAIN = 40;
    private enum STATE {
        NECESSARY,
        ASK_AGAIN,
        NEVER_ASK;
    }
    public static void checkRate(final Activity activity) {
        if (activity.getSharedPreferences(MNUnlockActivity.SHARED_PREFS,
                Context.MODE_PRIVATE).getBoolean(
                MNUnlockActivity.REVIEW_USED, false)) {
            return;
        }

        SharedPreferences sharedPreferences = activity.getSharedPreferences
                (SPNAME_REVIEW, Context.MODE_PRIVATE);

        //check launch count.
        int launchCount = sharedPreferences.getInt(KEY_LAUNCHCOUNT,
                VALUE_DEFAULT_LAUNCHCOUNT);
        int stateOrdinal = sharedPreferences.getInt(KEY_ASKAGAIN,
                STATE.NECESSARY.ordinal());
        final STATE state = STATE.values()[stateOrdinal];
        int askLimit;
        if (state.equals(STATE.NECESSARY)) {
            askLimit = ASKLIMIT_FIRST;
        }
        else if (state.equals(STATE.ASK_AGAIN)) {
            askLimit = ASKLIMIT_AGAIN;
        }
        else {
            return;
        }
        if (launchCount < askLimit) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_LAUNCHCOUNT, ++launchCount);
            editor.commit();

            if (launchCount >= askLimit) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    builder = new AlertDialog.Builder(activity,
                            AlertDialog.THEME_HOLO_DARK);
                } else {
                    builder = new AlertDialog.Builder(activity);
                }
                builder.setTitle(activity.getString(R.string.rate_morning_kit));
                String appName = activity.getString(R.string.app_name);
                String message = activity.getString(R.string.rate_it_contents, appName);
                builder.setMessage(message);
                builder.setPositiveButton(R.string.rate_it_rate, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        MNReviewApp.showReviewActivity(activity);

                        SharedPreferences sharedPreferences = activity.getSharedPreferences(SPNAME_REVIEW, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(KEY_ASKAGAIN, STATE.NEVER_ASK.ordinal());
                        editor.commit();

                        dialog.dismiss();
                    }
                });
//                builder.setNeutralButton(R.string.dialog_rateprompt_neutral, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        SharedPreferences sharedPreferences = activity
//                                .getSharedPreferences(SPNAME_REVIEW,
//                                        Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putInt(KEY_LAUNCHCOUNT,
//                                VALUE_DEFAULT_LAUNCHCOUNT);
//                        editor.putInt(KEY_ASKAGAIN, STATE.ASK_AGAIN.ordinal());
//                        editor.commit();
//
//                        dialog.dismiss();
//                    }
//                });
                builder.setNegativeButton(R.string.rate_it_no_thanks, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = activity.getSharedPreferences(SPNAME_REVIEW, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (state.equals(STATE.NECESSARY)) {
                            //ask again later
                            editor.putInt(KEY_LAUNCHCOUNT,
                                    VALUE_DEFAULT_LAUNCHCOUNT);
                            editor.putInt(KEY_ASKAGAIN, STATE.ASK_AGAIN.ordinal());
                        }
                        else if (state.equals(STATE.ASK_AGAIN)) {
                            //never ask after
                            editor.putInt(KEY_ASKAGAIN, STATE.NEVER_ASK.ordinal());
                        }
                        editor.commit();

                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        }
    }
}
