/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yooiistudios.morningkit.panel.weather.model;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.yooiistudios.morningkit.R;

/**
 * Defines app-wide constants and utilities
 */
public final class LocationUtils {
    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    public static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    public static final int FAST_CEILING_IN_SECONDS = 1;

    // Update interval in milliseconds
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    // 현재 위치의 날씨는 위치 정보가 필요한데, 위치 사용을 취소할 경우 도시 검색으로 옵션을 바꾸어야 하는데
    // 그 때 활용되는 콜백 메서드
    public interface OnLocationListener {
        void onLocationTrackingCanceled();
    }

    public static void showLocationUnavailableDialog(final Context context,
                                                     final OnLocationListener listener) {
        AlertDialog.Builder builder = makeAlertDialogBuilder(context, false);
        String title = context.getString(R.string.weather) + " - "
                + context.getString(R.string.dialog_location_service_disabled_title);
        builder.setTitle(title);
        builder.setMessage(R.string.dialog_location_service_disabled_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onLocationTrackingCanceled();
                }
            }
        });

        // 캔슬 방지
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        builder.show();
    }

    private static AlertDialog.Builder makeAlertDialogBuilder(Context context, boolean useHoloLight){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            return makeDialogBuilder(context, useHoloLight);
        }
        else{
            return new AlertDialog.Builder(context);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static AlertDialog.Builder makeDialogBuilder(Context context, boolean useHoloLight){
//		return new AlertDialog.Builder(context, getDialogThemeOverHoneycomb());
//		return new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        if (useHoloLight) {
            return new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        }
        else {
            return new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
        }
    }
}
