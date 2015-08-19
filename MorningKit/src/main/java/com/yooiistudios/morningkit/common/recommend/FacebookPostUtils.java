package com.yooiistudios.morningkit.common.recommend;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * Created by Wooseong Kim from Yooii Studios Co., LTD. on 2014. 9. 4.
 *
 * FacebookPostUtils
 *  특정 링크를 기기에 앱이 존재한다면 앱으로, 아니라면 웹으로 공유하는 유틸리티 클래스
 */
public class FacebookPostUtils {
    private FacebookPostUtils() { throw new AssertionError("Must not create this class!"); }

    public static final int REQ_FACEBOOK = 41938;

    public static void postAppLink(Activity activity) {
        String link = "https://play.google.com/store/apps/details?id=com.yooiistudios.morningkit&hl=en";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_SUBJECT, title); // NB: no effect!
        intent.putExtra(Intent.EXTRA_TEXT, link);
        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(intent, 0);

        // See if official Facebook app is found
        boolean isFacebookAppFound = false;
        for (final ResolveInfo app : activityList) {
            if (app.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(app.activityInfo.packageName);
                isFacebookAppFound = true;
                break;
            }

            // 기존 안되는 코드. 페이스북 열린 채로 추천 버튼을 누르면 앱이 그냥 뜸. 
//            if ((app.activityInfo.name).contains("facebook")) {
//                final ActivityInfo activityInfo = app.activityInfo;
//                final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
//                appPostIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                appPostIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                appPostIntent.setComponent(name);
//                activity.startActivityForResult(appPostIntent, REQ_FACEBOOK);
//                return;
//            }
        }

        // If we failed (not native FB app installed), try share through SEND
        // As fallback, launch sharer.php in a browser
        if (!isFacebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + link;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        activity.startActivityForResult(intent, REQ_FACEBOOK);
    }
}
