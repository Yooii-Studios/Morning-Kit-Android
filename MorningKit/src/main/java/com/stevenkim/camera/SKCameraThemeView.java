package com.stevenkim.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

/**
 * 사용하려는 어플리케이션의 AndroidManifest.xml 파일에서
 * 카메라의 permission을 체크할 것
 * 
 * 카메라의 상태를 체크해서 전/후면 카메라를 보여준다. 
 * 파라미터: CameraInfo.CAMERA_FACING_BACK/FRONT
 * 액티비티에서 생성하고 레이아웃에 addView 해 준다.
 * @author DeepSea
 */
public class SKCameraThemeView extends SurfaceView implements
		SurfaceHolder.Callback {
	
	private static final String TAG = "SKChameleonCameraView";
	
	SurfaceHolder mHolder;
	Camera mCamera;
	int mCameraFacingInfo;
	Context m_context;

	public SKCameraThemeView(Context context, int cameraFacingInfo) {
		super(context);
		// SurfaceHolder.Callback 을 설정함으로써 Surface 가 생성/소멸되었음을 알 수 있습니다.
		mHolder = getHolder();
        if (mHolder != null) {
            mHolder.addCallback(this);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
        }
		// 보여줄 카메라 선택(전면/후면)
		mCameraFacingInfo = cameraFacingInfo;
		
		m_context = context;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		Log.i(TAG, "surfaceChanged");
		
		// 후면 카메라가 없다면 null 값이 뜸
		if (mCamera != null) {
			// 방향에 따라 카메라 회전
			int rotation = ((Activity)m_context).getWindowManager().getDefaultDisplay().getRotation();
			if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
				Log.i(TAG, "0");
				mCamera.setDisplayOrientation(90);		
			}else if(rotation == Surface.ROTATION_90){
				Log.i(TAG, "90");
				mCamera.setDisplayOrientation(0);	
			}else if (rotation == Surface.ROTATION_270) {
				Log.i(TAG, "270");
				mCamera.setDisplayOrientation(180);	
			}
			
			// 표시할 영역의 크기를 알았으므로 해당 크기로 Preview를 시작합니다.
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(width, height);
//			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		/*
		// Surface가 생성되었다면, 카메라의 인스턴스를 받아온 후 카메라의 Preview 를 표시할 위치를 설정합니다.
		
		// 모닝 킷으로 옮기니 죽는다.
		// 카메라를 호출하는 부분에서 동기화가 되어있지 않으면 종종 일어나는 에러라고 한다. sync 가 필요함
		synchronized (this) {
			try {
				mCamera = Camera.open(mCameraFacingInfo);
				// mCamera = Camera.open();
				
				// 후면 카메라가 없으면 null 값을 띄움. 전면 카메라의 경우도 고려를 해야함
				if (mCamera != null) {
					try {
						mCamera.setPreviewDisplay(holder);
						mCamera.setDisplayOrientation(90);	// 이게 빠지면 정상 각도로 나오질 않는다, 회전에 대응할 수 있어야 함
					} catch (IOException exception) {
						mCamera.release();
						mCamera = null;
					}
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		*/
		
		synchronized (this) {
            int cameraFacingInfo = -1;

            if (m_context.getPackageManager() != null) {
                // FEATURE_CAMERA = 후면 / FEATURE_CAMERA_FRONT = 전면
                boolean hasBackCamera = m_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

                // 후면 카메라가 있는 경우는 선택의 여지를 주고, 없는 경우는 전면만 불러온다.
                if (hasBackCamera) {
                    try{
                        cameraFacingInfo = mCameraFacingInfo;
                        mCamera = Camera.open(cameraFacingInfo);
                    } catch (Exception e){
                        e.printStackTrace();
                        mCamera = Camera.open(0);
                    }
//                } else if(CameraInfo.CAMERA_FACING_FRONT > -1){ // 예전 코드인데 이해를 못하겠음
                } else if(m_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
                    try{
                        cameraFacingInfo = CameraInfo.CAMERA_FACING_FRONT;
                        mCamera = Camera.open(cameraFacingInfo);
                    } catch (Exception e){
                        try{
                            mCamera = Camera.open(0); // Put in for Nexus 7 as CameraInfo.CAMERA_FACING_FRONT=1 but it only loads if the id is actually 0
                            cameraFacingInfo = 0;
                        } catch (Exception exception){
                            cameraFacingInfo = -1;
                        }
                    }
                }
                if (cameraFacingInfo < 0) {
                    Toast.makeText(m_context, "No camera found.", Toast.LENGTH_LONG)
                            .show();
                }
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(holder);
                        // 알맞은 방향으로 보이게 각각 수정
                        int rotation = ((Activity)m_context).getWindowManager().getDefaultDisplay().getRotation();
                        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
//                            Log.i(TAG, "0");
                            mCamera.setDisplayOrientation(90);
                        }else if(rotation == Surface.ROTATION_90){
//                            Log.i(TAG, "90");
                            mCamera.setDisplayOrientation(0);
                        }else if (rotation == Surface.ROTATION_270) {
//                            Log.i(TAG, "270");
                            mCamera.setDisplayOrientation(180);
                        }
                    } catch (IOException exception) {
                        mCamera.release();
                        mCamera = null;
                    }
                }
            }
        }
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 다른 화면으로 돌아가면, Surface가 소멸됩니다. 따라서 카메라의 Preview도 중지해야 합니다. 카메라는 공유할 수 있는 자원이 아니기에, 사용하지 않을
		// 경우 -액티비티가 일시정지 상태가 된 경우 등 - 자원을 반환해야합니다.
		Log.i(TAG, "surfaceDestroyed");
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release(); 
			mCamera = null;	
		}
	}
	
	public void setCameraFacingInfo(int cameraFacingInfo) {
		mCameraFacingInfo = cameraFacingInfo;
	}
}
