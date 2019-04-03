package com.king.run.activity.other;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.model.BaseResult;
import com.king.run.util.CameraPhotoUtil;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.StringUtil;
import com.king.run.util.Url;
import com.king.run.util.image.FileUtil;
import com.king.run.view.BottomDialog;
import com.king.run.view.CircleImageView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 作者：shuizi_wade on 2017/8/18 09:41
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_complete_avatar_nickname)
public class CompleteAvatarNickNameActivity extends BaseActivity {
    private static final int IMAGE = 123;
    private static final int CAMERA = 124;
    @ViewInject(R.id.iv_avatar)
    CircleImageView iv_avatar;
    private String imageUrl, nickName;
    private Uri photoUri;
    private File actualImage;
    @ViewInject(R.id.et_nickname)
    EditText et_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    @Override
    public void setFinish() {
        super.setFinish();
        jumpActvity(HomeActivity.class);
    }

    private void initViews() {
        title_tv_title.setText(R.string.avatar_nickname);
    }

    @Event(value = {R.id.iv_avatar, R.id.btn_save})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                if (checkCameraStoragePermission()) {
                    openCameraStorage();
                }
                break;
            case R.id.btn_save:
                nickName = et_nickname.getText().toString().trim();
                if (StringUtil.isBlank(imageUrl)) {
                    senToa("请上传头像");
                    return;
                }
                if (StringUtil.isBlank(nickName)) {
                    et_nickname.setError("请输入您的昵称");
                    return;
                }
                compress();
                break;
        }
    }

    private void compress() {
        Luban.get(this)
                .load(actualImage)                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        showDia("加载中...");
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
//                        if (isAvatar) {
//                            avatarPath = file.getAbsolutePath();
                            PicassoUtil.displayImage(iv_avatar, file.getAbsolutePath(), context);
//                        } else
//                            addImg(file.getAbsolutePath());
                        http(file.getAbsolutePath());
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

    private void http(String imgUrl) {
        RequestParams params = new RequestParams(Url.EDIT_AVATAR_NICKNAME);
        // 使用multipart表单上传文件
        params.setMultipart(true);
        params.addBodyParameter("file", new File(imgUrl), null);
        params.addBodyParameter("token", PrefName.getToken(context));
        params.addBodyParameter("nickName", nickName);
        params.addBodyParameter("sex", "0");
        params.addBodyParameter("height", "170");
        params.addBodyParameter("weight", "60");
        params.addBodyParameter("year", "1992");
        params.addBodyParameter("age", "18");
        params.addBodyParameter("location", "陕西西安");
        httpPost("complete", params);
    }

    @Override
    protected void gotCameraStoragePermissionResult(boolean isGrant) {
        super.gotCameraStoragePermissionResult(isGrant);
        if (isGrant) {
            openCameraStorage();
        } else {
            senToa(R.string.permission_camera_storage);
        }
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "complete":
                BaseResult baseResult = JSON.parseObject(result, BaseResult.class);
                senToa(baseResult.getMsg());
                jumpActvity(HomeActivity.class);
                finish();
                break;
        }
    }

    private void openCameraStorage() {
        new BottomDialog(context, new BottomDialog.ClickListener() {
            @Override
            public void openCamera() {
//                imageUrl = CameraPhotoUtil.take_photo(context);
                photoUri = CameraPhotoUtil.take_photo_uri(context);
            }

            @Override
            public void openPicture() {
                CameraPhotoUtil.openAlbum(context);
            }
        }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        jumpActvity(HomeActivity.class);
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case CameraPhotoUtil.IMAGE:
                if (data == null) {
                    senToa("请选择图片!");
                    return;
                }
                try {
                    actualImage = FileUtil.from(this, data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (actualImage == null) {
                    senToa("请选择图片");
                } else {
                    compress();
                }
                break;
            case CameraPhotoUtil.CAMERA:
                if (data == null) {
                    try {
                        actualImage = FileUtil.from(this, photoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (actualImage == null) {
                        senToa("请拍照");
                    } else {
                        compress();
                    }
                }
//                PicassoUtil.displayImage(iv_avatar, imageUrl, context);
                break;
        }
    }


    /**
     * 4.4以下系统处理图片的方法
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imageUrl = CameraPhotoUtil.getImagePath(context, uri, data);
        PicassoUtil.displayImage(iv_avatar, imageUrl, context);
    }

    /**
     * 4.4及以上系统处理图片的方法
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImgeOnKitKat(Intent data) {
        imageUrl = CameraPhotoUtil.getRealImagePath(data, context);
        PicassoUtil.displayImage(iv_avatar, imageUrl, context);
    }
}
