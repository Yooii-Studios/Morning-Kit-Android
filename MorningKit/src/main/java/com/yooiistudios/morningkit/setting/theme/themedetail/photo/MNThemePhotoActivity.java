package com.yooiistudios.morningkit.setting.theme.themedetail.photo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stevenkim.photo.SKBitmapLoader;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeInfo;
import com.yooiistudios.morningkit.setting.MNSettingDetailActivity;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;

import java.io.FileNotFoundException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MNThemePhotoActivity extends MNSettingDetailActivity {
    @InjectView(R.id.setting_theme_detail_photo_container) RelativeLayout backgroundLayout;
    private MNThemePhotoFragment photoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_theme_detail_photo);
        ButterKnife.inject(this);

        backgroundLayout.setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(MNTheme.getCurrentThemeType(this)));

        if (savedInstanceState == null) {
            // http://developer.android.com/guide/components/fragments.html 참고
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // animate - (upstack)incoming enterAnim, (backstack)outgoing exitAnim /
            // in reverse - (backstack)incoming enterAnim, (upstack)outgoing exitAnim
            transaction.setCustomAnimations(R.anim.fragment_enter, 0);

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            photoFragment = MNThemePhotoFragment.newInstance();
            transaction.replace(R.id.setting_theme_detail_photo_container, photoFragment);

            // Commit the transaction
            transaction.commit();
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

                    startCropActivty(data, ratio, SKBitmapLoader.getPortraitImageUri(), SKBitmapLoader.REQ_CROP_FROM_PORTRAIT);
                    break;
                }
                case SKBitmapLoader.REQ_CODE_PICK_IMAGE_LANDSCAPE: {
                    int width = MNDeviceSizeInfo.getDeviceWidth(this);
                    int height = MNDeviceSizeInfo.getDeviceHeight(this);
                    float ratio = (float)width / (float)height;

                    startCropActivty(data, ratio, SKBitmapLoader.getLandscapeImageUri(), SKBitmapLoader.REQ_CROP_FROM_LANDSCAPE);
                    break;
                }
                case SKBitmapLoader.REQ_CROP_FROM_PORTRAIT:
                    // 모토롤라 처리
                    if (Build.MANUFACTURER.equals("motorola")) {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        if (bitmap != null) {
//                            Log.i("MNThemePhotoActivity", "bitmap: " + bitmap.getWidth() + "/" + bitmap.getHeight());
                        }
                        try {
                            SKBitmapLoader.saveBitmapToUri(this, SKBitmapLoader.getPortraitImageUri(), bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    // 저장은 자동으로 되기에 리프레시만 해주면 됨
                    photoFragment.refresh();
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
                    photoFragment.refresh();
                    break;
            }
        } else {
            Toast.makeText(this, "Can't load an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCropActivty(Intent data, float ratio, Uri imageUri, int requestCode) {

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
}
