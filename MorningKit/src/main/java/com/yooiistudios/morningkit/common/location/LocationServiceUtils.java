package com.yooiistudios.morningkit.common.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.yooiistudios.morningkit.R;

/**
 * Created by Dongheyon Jeong in Randombox_Android from Yooii Studios Co., LTD. on 15. 6. 16.
 * <p/>
 * LocationServiceUtils
 * 위치 서비스에 관한 정보를 제공하는 클래스
 */
public class LocationServiceUtils {
    private LocationServiceUtils() {
        throw new AssertionError("You MUST NOT create the instance of this class!!");
    }

    public static boolean isEnabled(Context context) {
        LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static void showEnableLocationServiceDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.weather_use_current_location);
        builder.setMessage(R.string.dialog_location_service_disabled_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
