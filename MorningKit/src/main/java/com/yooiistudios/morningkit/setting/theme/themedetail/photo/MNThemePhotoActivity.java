package com.yooiistudios.morningkit.setting.theme.themedetail.photo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.stevenkim.photo.SKBitmapLoader;
import com.yooiistudios.morningkit.MNApplication;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.analytic.MNAnalyticsUtils;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeInfo;
import com.yooiistudios.morningkit.setting.MNSettingDetailActivity;

import java.io.FileNotFoundException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

public class MNThemePhotoActivity extends MNSettingDetailActivity {
    private static final String TAG = "ThemePhotoActivity";
    private MNThemePhotoFragment photoFragment;

    @Getter @InjectView(R.id.setting_theme_detail_photo_container) RelativeLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_theme_detail_photo);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            // http://developer.android.com/guide/components/fragments.html 참고
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            photoFragment = MNThemePhotoFragment.newInstance();
            transaction.replace(R.id.setting_theme_detail_photo_container, photoFragment);

            // Commit the transaction
            transaction.commit();

            MNAnalyticsUtils.startAnalytics((MNApplication) getApplication(), TAG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case SKBitmapLoader.REQ_CODE_PICK_IMAGE_PORTRAIT: {
                    int width = MNDeviceSizeInfo.getDeviceWidth(this);
                    int height = MNDeviceSizeInfo.getDeviceHeight(this);
                    float ratio = (float)height / (float)width;

                    startCropActivity(data, ratio, SKBitmapLoader.getPortraitImageUri(), SKBitmapLoader.REQ_CROP_FROM_PORTRAIT);
                    break;
                }
                case SKBitmapLoader.REQ_CODE_PICK_IMAGE_LANDSCAPE: {
                    int width = MNDeviceSizeInfo.getDeviceWidth(this);
                    int height = MNDeviceSizeInfo.getDeviceHeight(this);
                    float ratio = (float)width / (float)height;

                    startCropActivity(data, ratio, SKBitmapLoader.getLandscapeImageUri(), SKBitmapLoader.REQ_CROP_FROM_LANDSCAPE);
                    break;
                }
                case SKBitmapLoader.REQ_CROP_FROM_PORTRAIT:
                    // 모토롤라 처리
                    if (Build.MANUFACTURER.equals("motorola")) {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        try {
                            SKBitmapLoader.saveBitmapToUri(this, SKBitmapLoader.getPortraitImageUri(), bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    // 저장은 자동으로 되기에 리프레시만 해주면 됨
                    if (photoFragment != null) {
                        photoFragment.refresh();
                    }
                    break;

                case SKBitmapLoader.REQ_CROP_FROM_LANDSCAPE:
                    // 모토롤라 처리 - 나중에 처리하자
                    /*
                    if (Build.MANUFACTURER.equals("motorola")) {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        if (bitmap != null) {
                            Log.i("MNThemePhotoActivity", "bitmap: " + bitmap.getWidth() + "/" + bitmap.getHeight());
                        }
                        try {
                            SKBitmapLoader.saveBitmapToUri(this, SKBitmapLoader.getLandscapeImageUri(), bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    */
                    // 저장은 자동으로 되기에 리프레시만 해주면 됨
                    if (photoFragment != null) {
                        photoFragment.refresh();
                    }
                    break;
            }
        } else {
            Toast.makeText(this, "Can't load an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCropActivity(Intent data, float ratio, Uri imageUri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        intent.putExtra("aspectX", 100);
        intent.putExtra("aspectY", (int) (100 * ratio));
        intent.putExtra("noFaceDetection", true);	// 얼굴 찾지 않기

        if (getPackageManager() != null) {
            List list = getPackageManager().queryIntentActivities(intent, 0);
            intent.setData(data.getData());

            // 모토로라 해결을 위한 코드 - 나중에 해결하자
//            if (Build.MANUFACTURER.equals("motorola")) {
//                intent.putExtra("return-data", true);
//            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);    // 이 부분이 추가되면 모토롤라 폰은 배경이 바뀐다.
//            }

            Intent i = new Intent(intent);
            ResolveInfo res = (ResolveInfo) list.get(0);

            if (res.activityInfo != null) {
                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(intent, requestCode);
            } else {
                Toast.makeText(this, "Can't load an image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Can't load an image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        // Activity visible to user
        super.onStart();
        FlurryAgent.onStartSession(this, MNFlurry.KEY);
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // Activity no longer visible
        super.onStop();
        FlurryAgent.onEndSession(this);
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(containerLayout, R.string.permission_granted, Snackbar.LENGTH_SHORT).show();
            photoFragment.refreshWithoutFinish();
        } else {
            Snackbar.make(containerLayout, R.string.permission_not_granted, Snackbar.LENGTH_SHORT).show();
        }
    }
}
