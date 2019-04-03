package com.king.run.activity.sport;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.ScreenSwitchUtils;
import com.king.run.view.CameraPreview;
import com.king.run.view.FocusView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@ContentView(R.layout.activity_camera)
public class CameraActivity extends BaseActivity {
    private CameraPreview cameraPreview;
    @ViewInject(R.id.sb1)
    SeekBar sb1;
    private int flashMode = -1;  //-1 auto  0 on  1off
    @ViewInject(R.id.fl_preview)
    RelativeLayout fl_preview;
    private ScreenSwitchUtils mInstance;
    private boolean portrait;
    @ViewInject(R.id.view_focus)
    FocusView mFocusView;
    @ViewInject(R.id.iv_capture)
    ImageView btn_capture;
    @ViewInject(R.id.iv_flash)
    ImageView iv_flash;
    @ViewInject(R.id.iv_return)
    ImageView iv_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = ScreenSwitchUtils.init(this);
        if (!checkCameraHardware()) {
            senToa("没有检测到相机");
            finish();
            return;
        }
        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cameraPreview.setZoom(progress);
            }
        });

    }

    public boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    @Event(value = {R.id.tv, R.id.iv_capture, R.id.iv_flash, R.id.iv_return})
    private void getEvent(View view) {
        portrait = mInstance.isPortrait();
        Log.i("拍照时屏幕方向状态", portrait + "");
        Camera.Parameters params = cameraPreview.getCameraParams();
//        params.setRotation(90);
        Camera camera = cameraPreview.getCameraInstance();
        switch (view.getId()) {
            case R.id.tv:
//                change(cameraPreview.getCameraInstance());
                break;
            case R.id.iv_capture:
                // 照相
                camera.autoFocus(null);
                camera.takePicture(null, null, mPicture);
                camera.setParameters(params);
                break;
            case R.id.iv_flash:
                if (flashMode == -1) {//auto
                    ((ImageView) view).setImageResource(R.mipmap.flash_on);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    flashMode = 0;
                } else if (flashMode == 0) {//on
                    ((ImageView) view).setImageResource(R.mipmap.flash_off);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    flashMode = 1;
                } else {
                    ((ImageView) view).setImageResource(R.mipmap.flash_auto);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    flashMode = -1;
                }
                camera.setParameters(params);
                break;
            case R.id.iv_return:
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
        mInstance.stop();
    }

    @Override
    protected void onResume() {
        mInstance.start(this);
        portrait = mInstance.isPortrait();
        sb1.setProgress(0);
        cameraPreview = new CameraPreview(this);
        cameraPreview.setFocusView(mFocusView);
        fl_preview.addView(cameraPreview);
        Camera.Parameters params = cameraPreview.getCameraParams();
        if (params == null) {
            finish();
            Toast.makeText(this, "打开相机失败", Toast.LENGTH_SHORT).show();
        } else {
            int maxZoom = params.getMaxZoom();
            sb1.setMax(maxZoom);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        cameraPreview.releaseCamera();
        fl_preview.removeAllViews();
        cameraPreview = null;
        super.onPause();
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {// 没有sd卡
                return;
            }

            File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "KingRun");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File pictureFile = new File(dir, System.currentTimeMillis() + ".jpg");
            try {
                pictureFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                senToa(pictureFile.getPath());
                //拍完预览
//                Intent intent = new Intent(CameraActivity.this, PictureActivity.class);
//                intent.putExtra("type", getIntent().getIntExtra("type", 0));
//                intent.setData(Uri.fromFile(pictureFile));
//                intent.putExtra("portrait",portrait);
//                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };

    private int cameraPosition;

    public void change(Camera camera) {
        //切换前后摄像头
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    camera.stopPreview();//停掉原来摄像头的预览
                    camera.release();//释放资源
                    camera = null;//取消原来摄像头
                    camera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        camera.setPreviewDisplay(cameraPreview.mHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    camera.startPreview();//开始预览
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    camera.stopPreview();//停掉原来摄像头的预览
                    camera.release();//释放资源
                    camera = null;//取消原来摄像头
                    camera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        camera.setPreviewDisplay(cameraPreview.mHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    camera.startPreview();//开始预览
                    cameraPosition = 1;
                    break;
                }
            }

        }
    }
}
