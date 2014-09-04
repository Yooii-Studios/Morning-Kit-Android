package com.yooiistudios.morningkit.common.recommend;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.yooiistudios.morningkit.R;

import java.util.List;

/**
 * Created by Wooseong Kim from Yooii Studios Co., LTD. on 2014. 9. 4.
 *
 * FacebookPostUtils
 *  특정 링크를 기기에 앱이 존재한다면 앱으로, 아니라면 웹으로 공유하는 유틸리티 클래스
 */
public class FacebookPostUtils {
    private FacebookPostUtils() { throw new AssertionError("Must not create this class!"); }

    public static final int REQ_FACEBOOK_APP = 41938;
    public static final int REQ_FACEBOOK_WEB = 54837;

    public static void postAppLink(Activity activity) {
        Context context = activity.getApplicationContext();
        String appName = context.getString(R.string.recommend_app_full_name);
        String title = context.getString(R.string.recommend_title) + " [" + appName + "]";

        String link = "https://play.google.com/store/apps/details?id=com.yooiistudios.morningkit";

        Intent appPostIntent = new Intent(Intent.ACTION_SEND);
        appPostIntent.setType("text/plain");
        appPostIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        appPostIntent.putExtra(Intent.EXTRA_TEXT, link);
        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(appPostIntent, 0);

        // Check if there is FB app
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                final ActivityInfo activityInfo = app.activityInfo;
                final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                appPostIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                appPostIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                appPostIntent.setComponent(name);
                activity.startActivityForResult(appPostIntent, REQ_FACEBOOK_APP);
                return;
            }
        }

        // If we failed (not native FB app installed), try share through SEND
        String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + link;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        activity.startActivityForResult(intent, REQ_FACEBOOK_WEB);
    }
}
