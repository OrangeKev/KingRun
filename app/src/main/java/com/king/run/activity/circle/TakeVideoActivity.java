package com.king.run.activity.circle;

import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.view.CircleButtonView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;

@ContentView(R.layout.activity_take_video)
public class TakeVideoActivity extends BaseActivity {
    @ViewInject(R.id.surface_view)
    SurfaceView surfaceView;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String path;
    private Camera camera;
    private int cameraId = 0;
    @ViewInject(R.id.circle_btn_view)
    CircleButtonView circle_btn_view;
    //    @ViewInject(R.id.video_view)
//    VideoView video_view;
    @ViewInject(R.id.iv_change)
    ImageView iv_change;
    @ViewInject(R.id.btn_done)
    Button btn_done;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        initViews();
        if (checkRecordAudioPermission())
            initCamera();
    }

    private void initVideoView() {
//        MediaController mc = new MediaController(this);
//        mc.setMediaPlayer(video_view);
//        mc.setVisibility(View.INVISIBLE);
//        video_view.setMediaController(mc);
//        //设置视频控制器
//        video_view.setMediaController(new MediaController(this));
//        //播放完成回调
//        video_view.setOnCompletionListener(new MyPlayerOnCompletionListener());
        try {
//            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this, Uri.parse(path));
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 然后开始播放视频
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        action = getIntent().getAction();
        mediaPlayer = new MediaPlayer();
        circle_btn_view.setOnLongClickListener(new CircleButtonView.OnLongClickListener() {
            @Override
            public void onLongClick() {
                startRecord();
            }

            @Override
            public void onNoMinRecord(int currentTime) {
                senToa("时间太短");
                //当结束录制之后，就将当前的资源都释放
                mediaRecorder.release();
                camera.release();
                mediaRecorder = null;
                //然后再重新初始化所有的必须的控件和对象
                camera = Camera.open();
                mediaRecorder = new MediaRecorder();
                doChange(surfaceView.getHolder());
            }

            @Override
            public void onRecordFinishedListener() {
                stopRecord();
//                video_view.setVisibility(View.VISIBLE);
//                surfaceView.setVisibility(View.INVISIBLE);
//                Uri uri = Uri.parse(path);
//                video_view.setVideoURI(uri);
//                //开始播放视频
//                video_view.start();
                initVideoView();
                circle_btn_view.setVisibility(View.INVISIBLE);
                iv_change.setVisibility(View.GONE);
                btn_done.setVisibility(View.VISIBLE);
            }
        });
        //为SurfaceView设置回调函数
        surfaceView.getHolder().addCallback(callback);
    }

    @Override
    protected void gotRecordAudioPermissionResult(boolean isGrant) {
        super.gotRecordAudioPermissionResult(isGrant);
        if (isGrant)
            initCamera();
        else {
            senToa(R.string.permission_record_audio);
        }
    }

    private void initCamera() {
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/video.mp4";
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.setKeepScreenOn(true);
        OpenCameraAndSetSurfaceviewSize(0);
    }

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

        //在控件创建的时候，进行相应的初始化工作
        public void surfaceCreated(SurfaceHolder holder) {
            //打开相机，同时进行各种控件的初始化mediaRecord等
            mediaPlayer.setDisplay(holder);
//            camera = Camera.open();
            try {
                camera = Camera.open();
            } catch (RuntimeException e) {
                camera = Camera.open(Camera.getNumberOfCameras() - 1);
                System.out.println("open()方法有问题");
            }
            mediaRecorder = new MediaRecorder();
        }

        //当控件发生变化的时候调用，进行surfaceView和camera进行绑定，可以进行画面的显示
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            doChange(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }

    };

    //当我们的程序开始运行，即使我们没有开始录制视频，我们的surFaceView中也要显示当前摄像头显示的内容
    private void doChange(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            //设置surfaceView旋转的角度，系统默认的录制是横向的画面，把这句话注释掉运行你就会发现这行代码的作用
            camera.setDisplayOrientation(getDegree());
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getDegree() {
        //获取当前屏幕旋转的角度
        int rotating = this.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        //根据手机旋转的角度，来设置surfaceView的显示的角度
        switch (rotating) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    private Void OpenCameraAndSetSurfaceviewSize(int cameraId) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
//        camera = Camera.open();//id用于选中不同的摄像头，有的相机会有很多的
        try {
            camera = Camera.open(cameraId);
        } catch (RuntimeException e) {
            camera = Camera.open(Camera.getNumberOfCameras() - 1);
            System.out.println("open()方法有问题");
        }
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size pre_size = parameters.getPreviewSize();
        Camera.Size pic_size = parameters.getPictureSize();//和预览的尺寸不同，这是拍照后实际相片的尺寸，会比较大
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);//获取选中的摄像头信息
        int camera_number = Camera.getNumberOfCameras();//获取摄像头数量
        Log.d("", camera_number + "");
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//分辨是否前置摄像头
//            textview.setText("There are " + camera_number + " camera."
//                    + "This is the Front Camera!");
//            senToa("There are " + camera_number + " camera."
//                    + "This is the Front Camera!");
        } else {
//            senToa("There are " + camera_number + " camera."
//                    + "This is the Back Camera!");
////            textview.setText();
        }
        lp.height = pre_size.width * 2;
        lp.width = pre_size.height * 2;
        return null;
    }

    @Event(value = {R.id.iv_change, R.id.btn_done, R.id.iv_finish})
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_change:
                camera.stopPreview();
                camera.release();
                if (cameraId == 0) {
                    cameraId = 1;
                } else {
                    cameraId = 0;
                }
                OpenCameraAndSetSurfaceviewSize(cameraId);
                SetAndStartPreview(surfaceView.getHolder());
                break;
            case R.id.btn_done:
                Bundle bundle = new Bundle();
                bundle.putString("video", path);
                if (action.equals("publish"))
                    setResultAct(bundle);
                else
                    jumpBundleActvity(PublishActivity.class, bundle);
                finish();
                break;
            case R.id.iv_finish:
                finish();
                break;
        }
    }

    private Void SetAndStartPreview(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private void stopRecord() {
        //当结束录制之后，就将当前的资源都释放
        mediaRecorder.release();
        camera.release();
        mediaRecorder = null;
        //然后再重新初始化所有的必须的控件和对象
//        camera = Camera.open();
//        mediaRecorder = new MediaRecorder();
//        doChange(surfaceView.getHolder());
    }

    private void startRecord() {
        //先释放被占用的camera，在将其设置为mediaRecorder所用的camera
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        //设置音频的来源  麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置视频的来源
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoSize(640, 480);
        mediaRecorder.setVideoFrameRate(25);
        mediaRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
        if (cameraId == 0)
            mediaRecorder.setOrientationHint(90);
        else
            mediaRecorder.setOrientationHint(270);
        //设置保存的路径
        mediaRecorder.setOutputFile(path);
        //开始录制
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
