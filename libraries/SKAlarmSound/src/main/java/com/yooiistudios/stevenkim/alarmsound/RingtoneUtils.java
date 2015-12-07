package com.yooiistudios.stevenkim.alarmsound;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2015. 10. 21.
 *
 * RingtoneUtils
 *  시스템 벨소리 URI 를 Media URI 로 변화환시켜주는 유틸리티 클래스
 **/
public class RingtoneUtils {
    private RingtoneUtils() { throw new AssertionError("You MUST not create class!"); }

    public static Uri getSystemRingtoneMediaUri(Context context) {
        Uri systemRingtoneUri = Settings.System.DEFAULT_RINGTONE_URI;
        Uri mediaUri = systemRingtoneUri;

        if (systemRingtoneUri.getAuthority().equals(Settings.AUTHORITY)) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(systemRingtoneUri, new String[]{
                        Settings.NameValueTable.VALUE}, null, null, null);
                if (c != null && c.moveToFirst()) {
                    String val = c.getString(0);
                    mediaUri = Uri.parse(val);
                }
            } catch (Exception ignored) {} finally {
                c.close();
            }
        }
        return mediaUri;
    }
}
