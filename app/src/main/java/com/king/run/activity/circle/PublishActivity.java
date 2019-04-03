package com.king.run.activity.circle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.king.run.R;
import com.king.run.activity.circle.model.CircleInfo;
import com.king.run.activity.mine.adapter.MinePicAdapter;
import com.king.run.activity.mine.model.Albums;
import com.king.run.baidumap.LocManage;
import com.king.run.base.BaseActivity;
import com.king.run.model.BaseResult;
import com.king.run.util.StringUtil;
import com.king.run.util.Url;
import com.king.run.util.Utils;
import com.king.run.util.image.FileUtil;

import net.arvin.pictureselector.entities.ImageEntity;
import net.arvin.pictureselector.utils.PSConfigUtil;
import net.arvin.pictureselector.utils.PSConstanceUtil;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

@ContentView(R.layout.activity_publish)
public class PublishActivity extends BaseActivity {
    @ViewInject(R.id.ly_pub_top)
    RelativeLayout ly_pub_top;
    private LocManage mLocManage;
    @ViewInject(R.id.tv_location)
    TextView tv_location;
    private double lat, lng;
    @ViewInject(R.id.et_content)
    EditText et_content;
    @ViewInject(R.id.tv_add_circle)
    TextView tv_add_circle;
    private static final int ADD_CIRCLE_CODE = 123;
    private CircleInfo circleInfo;
    private ArrayList<ImageEntity> selectedImages = new ArrayList<>();
    private static final int AVATAR_CODE = 40;
    private static final int VIDEO_CODE = 50;
    @ViewInject(R.id.gv_pic)
    GridView gv_pic;
    private MinePicAdapter adapter;
    List<Albums> list = new ArrayList<>();
    @ViewInject(R.id.surface_view)
    SurfaceView surface_view;
    private String videoPath;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSign();
        init();
        initViews();
    }

    private void initSign() {
        mLocManage = new LocManage(context);
        if (checkLocationPermission()) {
            initByGotLocationPermission();
        }
    }

    /**
     * 获取定位权限后的初始化
     */
    public void initByGotLocationPermission() {
        getLocation();
    }

    /**
     * 自动获取定位信息
     */
    private void getLocation() {
        mLocManage.getLocation(gotLocCallBack);
    }

    @Override
    protected void gotLocationPermissionResult(boolean isGrant) {
        super.gotLocationPermissionResult(isGrant);
        if (isGrant)
            initByGotLocationPermission();
        else
            senToa(R.string.permission_location);
    }

    private void initViews() {
        if (getIntent().hasExtra("video")) {
            videoPath = getIntent().getExtras().getString("video", "");
            if (StringUtil.isNotBlank(videoPath)) {
                surface_view.setVisibility(View.VISIBLE);
                initVideoView();
            } else {
                surface_view.setVisibility(View.GONE);
            }
        } else {
            surface_view.setVisibility(View.GONE);
        }
        if (getIntent().hasExtra("file")) {
            compress(getIntent().getExtras().getString("file"));
        }
        if (getIntent().hasExtra("fileList")) {
            selectedImages = getIntent().getExtras().getParcelableArrayList("fileList");
            for (int i = 0; i < selectedImages.size(); i++) {
                compress(selectedImages.get(i).getPath());
            }
        }
        if (getIntent().hasExtra("id")) {
            id = getIntent().getExtras().getInt("id");
        }
        title_iv_back.setVisibility(View.GONE);
        title_tv_back.setText(R.string.cancel);
        title_tv_title.setText(R.string.publish_content);
        title_tv_right.setText(R.string.publish);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ly_pub_top.getLayoutParams();
        int height = Utils.getDisplayHeight((Activity) context);
        int width = Utils.getDisplayWidth((Activity) context);
        params.width = width;
        params.height = (int) (height * 0.45);
        ly_pub_top.setLayoutParams(params);
        adapter = new MinePicAdapter(context);
        gv_pic.setAdapter(adapter);
        title_tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * “framePic” 首帧图  附件
                 “video” 短视频  附件
                 “album” 图片  附件
                 “content”:发布内容
                 “circleIds”:发布圈子id,多个圈子用英文“,”分割
                 “lng”:精度 long
                 “lat”:维度  long
                 */
                String content = et_content.getText().toString().trim();
                if (StringUtil.isBlank(content)) {
                    et_content.setError("圈子内容不能为空");
                    return;
                }
                RequestParams requestParams = new RequestParams(Url.PUBLISH_URL);
                requestParams.addBodyParameter("content", content);
                requestParams.addBodyParameter("lat", lat + "");
                requestParams.addBodyParameter("lng", lng + "");
                List<KeyValue> lists = new ArrayList<>();
                if (null != list && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        lists.add(new KeyValue("album", new File(list.get(i).getUrlPre())));
                    }
                }
                if (StringUtil.isNotBlank(videoPath)) {
                    lists.add(new KeyValue("video", new File(videoPath)));
                    lists.add(new KeyValue("framePic", new File(FileUtil.getFirstImg())));
                }
                if (!lists.isEmpty()) {
                    MultipartBody body = new MultipartBody(lists, "UTF-8");
                    requestParams.setRequestBody(body);
                }
                if (id != 0 && id != -1) {
                    requestParams.addBodyParameter("eId", String.valueOf(id));
                }
                if (null != circleInfo)
                    requestParams.addBodyParameter("circleIds", circleInfo.getId() + "");
                httpPost("publish", requestParams);
            }
        });
    }

    private void initVideoView() {
        int width = Utils.getDisplayWidth((Activity) context);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) surface_view.getLayoutParams();
        params.width = width / 2;
        params.height = width / 2;
        surface_view.setLayoutParams(params);
        player = new MediaPlayer();
        try {
            player.setDataSource(this, Uri.parse(videoPath));
            getFirst(videoPath);
            holder = surface_view.getHolder();
            holder.addCallback(new MyCallBack());
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 然后开始播放视频
                    player.start();
                    player.setLooping(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != player) {
            player.release();
            player = null;
        }
    }

    @Event(value = {R.id.tv_add_circle, R.id.ly_add_pic, R.id.ly_take_video})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_add_circle:
                jumpBundleActvityforResult(AddCircleListActivity.class, null, ADD_CIRCLE_CODE);
                break;
            case R.id.ly_add_pic:
                if (StringUtil.isNotBlank(videoPath)) {
                    senToa("当前只能选择视频");
                    return;
                }
                if (checkCameraStoragePermission())
                    open();
                break;
            case R.id.ly_take_video:
                if (null != list && !list.isEmpty()) {
                    senToa("当前只能选择图片");
                    return;
                }
                if (checkRecordAudioPermission()) {
                    jumpBundleActvityforResult(TakeVideoActivity.class, null, VIDEO_CODE, "publish");
                }
                break;
        }
    }

    private void open() {
        PSConfigUtil.getInstance().setMaxCount(9);
        PSConfigUtil.getInstance().showSelector((Activity) context, AVATAR_CODE, selectedImages);
    }

    @Override
    protected void gotRecordAudioPermissionResult(boolean isGrant) {
        super.gotRecordAudioPermissionResult(isGrant);
        if (isGrant)
            jumpBundleActvityforResult(TakeVideoActivity.class, null, VIDEO_CODE, "publish");
        else
            senToa(R.string.permission_record_audio);
    }

    @Override
    protected void gotCameraStoragePermissionResult(boolean isGrant) {
        super.gotCameraStoragePermissionResult(isGrant);
        if (isGrant)
            open();
        else
            senToa(R.string.permission_camera_storage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case ADD_CIRCLE_CODE:
                circleInfo = (CircleInfo) data.getExtras().getSerializable("circleInfo");
                tv_add_circle.setText(circleInfo.getName());
                break;
            case AVATAR_CODE:
                list.clear();
                selectedImages = data.getParcelableArrayListExtra(PSConstanceUtil.PASS_SELECTED);
                for (int i = 0; i < selectedImages.size(); i++) {
                    compress(selectedImages.get(i).getPath());
                }
                break;
            case VIDEO_CODE:
                videoPath = data.getExtras().getString("video", "");
                if (StringUtil.isNotBlank(videoPath)) {
                    surface_view.setVisibility(View.VISIBLE);
                    initVideoView();
                } else {
                    surface_view.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "publish":
                BaseResult baseResult = JSON.parseObject(result, BaseResult.class);
                if (baseResult.getCode() == 0) {
                    senToa("发布成功");
                    finish();
                }
                break;
        }
    }

    /**
     * 获取百度经纬度回调
     */
    private LocManage.GotLocCallBackShort gotLocCallBack = new LocManage.GotLocCallBackShort() {

        @Override
        public void onGotLocShort(double latitude, double longitude, BDLocation location) {
            if (latitude == 0.0d || longitude == 0.0d) {
                return;
            } else {
                String street = location.getAddress().street;
                tv_location.setText(street);
                lat = latitude;
                lng = longitude;
            }
        }

        @Override
        public void onGotLocShortFailed(String location) {
            //目前不会执行到这里
        }
    };

    private void compress(String path) {
        Luban.get(this)
                .load(new File(path))                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                        showDia("加载中...");
                    }

                    @Override
                    public void onSuccess(File file) {
                        Albums album = new Albums();
                        album.setUrlPre(file.getAbsolutePath());
                        list.add(album);
                        adapter.setAlbums(list);
                        adapter.notifyDataSetChanged();
                        Log.e("xwk", "compressedImage path=" + file.getAbsolutePath());
                        Log.e("xwk", "压缩结果" + String.format("Size : %s",
                                FileUtil.getReadableFileSize(file.length())));
                        hideDia();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                        senToa(e.getMessage());
                    }
                }).launch();    //启动压缩
    }

    /**
     * 获取视频第一帧图片
     */
    private void getFirst(String path) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
        FileUtil.saveBitmapFile(bitmap);
    }

    @Override
    public void setFinish() {
        setResultAct(null);
        super.setFinish();
    }
}
