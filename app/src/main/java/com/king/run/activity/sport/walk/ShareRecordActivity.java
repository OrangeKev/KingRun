package com.king.run.activity.sport.walk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.sport.walk.adapter.ShareRecordAdapter;
import com.king.run.base.BaseActivity;
import com.king.run.util.QQConstants;
import com.king.run.util.ThirdShare;
import com.king.run.util.Util;
import com.king.run.util.Utils;
import com.king.run.util.WxConstants;
import com.king.run.util.image.FileUtil;
import com.king.run.view.BottomDialog;
import com.king.run.view.HorizontalListView;
import com.king.run.view.ScripView;
import com.king.run.view.ShareToDialog;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;

@ContentView(R.layout.activity_share_record_copy)
public class ShareRecordActivity extends BaseActivity implements WbShareCallback {
    @ViewInject(R.id.iv_big)
    ImageView iv_big;
    @ViewInject(R.id.hor_lv)
    HorizontalListView hor_lv;
    @ViewInject(R.id.ly_right)
    LinearLayout ly_right;
    @ViewInject(R.id.scrip)
    ScripView scrip;
    private WbShareHandler wbShareHandler;
    private static final int THUMB_SIZE = 150;
    private static final int IMAGE = 123;
    private ShareRecordAdapter adapter;
    private int[] icons = {
            R.mipmap.train_share_pic_1,
            R.mipmap.train_share_pic_2,
            R.mipmap.train_share_pic_3,
            R.mipmap.train_share_pic_4,
            R.mipmap.train_share_pic_5,
            R.mipmap.train_share_pic_6,};
    private int[] bgs = {
            R.drawable.share_record_ly_bg_1,
            R.drawable.share_record_ly_bg_2,
            R.drawable.share_record_ly_bg_3,
            R.drawable.share_record_ly_bg_4,
            R.drawable.share_record_ly_bg_5,
            R.drawable.share_record_ly_bg_6,
    };
    private Tencent mTencent;
    private IWXAPI api;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private int step;
    private String km, kcal;
    @ViewInject(R.id.tv_step)
    TextView tv_step;
    @ViewInject(R.id.tv_km)
    TextView tv_km;
    @ViewInject(R.id.tv_kcal)
    TextView tv_kcal;
    private int resId = R.mipmap.train_share_pic_1;
    private static final int CAMERA_SELF = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        regToQQ();
        regToWx();
        regToWb();
        checkStoragePermission();
    }

    @Override
    protected void gotStoragePermissionResult(boolean isGrant) {
        super.gotStoragePermissionResult(isGrant);
        if (!isGrant) senToa(R.string.permission_phone_storage);
    }

    private void regToWb() {
        wbShareHandler = new WbShareHandler(this);
        wbShareHandler.registerApp();
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(context, WxConstants.APP_ID, true);
        api.registerApp(WxConstants.APP_ID);
    }

    private void regToQQ() {
        mTencent = Tencent.createInstance(QQConstants.APP_ID, this.getApplicationContext());
    }

    private void initViews() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ReductoCondSSK.ttf");
        tv_step.setTypeface(typeface);
        step = getIntent().getExtras().getInt("step");
        km = getIntent().getExtras().getString("km");
        kcal = getIntent().getExtras().getString("kcal");
        tv_step.setText(step + "");
        tv_km.setText(String.format(getResources().getString(R.string.have_walk), km));
        tv_kcal.setText(String.format(getResources().getString(R.string.have_consume), kcal));
        scrip.setText(step, km, kcal);
        scrip.setImg(resId);
        title_tv_title.setText(R.string.share_record);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_big.getLayoutParams();
        layoutParams.width = Utils.getDisplayWidth(ShareRecordActivity.this) - DensityUtil.dip2px(32);
        layoutParams.height = Utils.getDisplayWidth(ShareRecordActivity.this) - DensityUtil.dip2px(32);
        iv_big.setLayoutParams(layoutParams);
        iv_big.setBackgroundResource(icons[0]);
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) ly_right.getLayoutParams();
        layoutParams1.width = (Utils.getDisplayWidth(ShareRecordActivity.this) - DensityUtil.dip2px(32)) * 2 / 5;
        layoutParams1.height = Utils.getDisplayWidth(ShareRecordActivity.this) - DensityUtil.dip2px(32);
        ly_right.setLayoutParams(layoutParams1);
        ly_right.setBackgroundResource(bgs[0]);
        adapter = new ShareRecordAdapter(context, icons);
        hor_lv.setAdapter(adapter);
        hor_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    iv_big.setBackgroundResource(icons[i - 1]);
                    resId = icons[i - 1];
                    scrip.setImg(resId);
                    ly_right.setBackgroundResource(bgs[i - 1]);
                } else {
                    if (checkCameraStoragePermission()) {
                        showDialog();
                    }
                }
            }
        });
    }

    @Event(value = {R.id.btn_share})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_share:
                getSharePic();
                break;
        }
    }

    /**
     * 获取分享图片
     */
    private void getSharePic() {
        scrip.getSnap(new ScripView.OnSnapListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                share(bitmap);
            }

            @Override
            public void onError() {
                senToa("error");
            }
        });
    }

    /**
     * 进行分享
     *
     * @param bitmap
     */
    private void share(final Bitmap bitmap) {
        final File file = FileUtil.saveBitmapFile(bitmap);
        new ShareToDialog(context, new ShareToDialog.ClickListener() {
            @Override
            public void shareToWeixin() {
                shareToWx(bitmap);
            }

            @Override
            public void shareToCircle() {
                ThirdShare.shareToCircle(file.getAbsolutePath(), context);
            }

            @Override
            public void shareToWeibo() {
                shareToWb(bitmap);
            }

            @Override
            public void shareToQQ() {
                onClickShareQQ(file.getAbsolutePath());
            }
        }).show();
    }

    private void shareToWb(Bitmap bitmap) {
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        weiboMultiMessage.imageObject = imageObject;
//        weiboMultiMessage.mediaObject = weiboMultiMessage;
        wbShareHandler.shareMessage(weiboMultiMessage, false);
    }

    private void shareToWx(Bitmap bitmap) {
        WXImageObject object = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = object;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void onClickShareQQ(String path) {
        final Bundle params = new Bundle();
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  "其他附加功能");
        mTencent.shareToQQ((Activity) context, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Logger.d(o);
            }

            @Override
            public void onError(UiError uiError) {
                Logger.d(uiError);
            }

            @Override
            public void onCancel() {
                Logger.d("");
            }
        });
    }

    private void showDialog() {
        new BottomDialog(context, new BottomDialog.ClickListener() {
            @Override
            public void openCamera() {
                jumpBundleActvityforResult(SelfCameraActivity.class, null, CAMERA_SELF, "share");
            }

            @Override
            public void openPicture() {
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        }).show();
    }

    @Override
    protected void gotCameraStoragePermissionResult(boolean isGrant) {
        super.gotCameraStoragePermissionResult(isGrant);
        if (isGrant)
            showDialog();
        else {
            senToa(R.string.permission_camera_storage);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        Bundle bundle = new Bundle();
        bundle.putInt("step", step);
        bundle.putString("km", km);
        bundle.putString("kcal", kcal);
        switch (requestCode) {
            case IMAGE:
                if (data == null) {
                    senToa("请选择图片!");
                    return;
                }
                try {
                    File actualImage = FileUtil.from(this, data.getData());
                    bundle.putString("file", actualImage.getAbsolutePath());
                    jumpBundleActvity(SelectSuccessActivity.class, bundle);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case CAMERA_SELF:
                String file = data.getExtras().getString("file");
                bundle.putString("file", file);
                jumpBundleActvity(SelectSuccessActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onWbShareSuccess() {

    }

    @Override
    public void onWbShareCancel() {

    }

    @Override
    public void onWbShareFail() {

    }
}
