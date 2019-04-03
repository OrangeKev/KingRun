package com.king.run.activity.sport.walk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.PicassoUtil;
import com.king.run.util.QQConstants;
import com.king.run.util.ThirdShare;
import com.king.run.util.Util;
import com.king.run.util.Utils;
import com.king.run.util.WxConstants;
import com.king.run.util.image.FileUtil;
import com.king.run.view.ScripView;
import com.king.run.view.ShareToDialog;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
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

@ContentView(R.layout.activity_select_success)
public class SelectSuccessActivity extends BaseActivity {
    private String filePath;
    @ViewInject(R.id.iv_big)
    ImageView iv_big;
    @ViewInject(R.id.ly_right)
    LinearLayout ly_right;
    @ViewInject(R.id.scrip)
    ScripView scrip;
    @ViewInject(R.id.tv_step)
    TextView tv_step;
    @ViewInject(R.id.tv_km)
    TextView tv_km;
    @ViewInject(R.id.tv_kcal)
    TextView tv_kcal;
    private int step;
    private String km, kcal;
    private Tencent mTencent;
    private IWXAPI api;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private WbShareHandler wbShareHandler;
    private static final int THUMB_SIZE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        initViews();
        regToWb();
        regToQQ();
        regToWx();
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
        filePath = getIntent().getExtras().getString("file");
        step = getIntent().getExtras().getInt("step");
        km = getIntent().getExtras().getString("km");
        kcal = getIntent().getExtras().getString("kcal");
        tv_step.setTypeface(typeface);
        tv_step.setText(step + "");
        tv_km.setText(String.format(getResources().getString(R.string.have_walk), km));
        tv_kcal.setText(String.format(getResources().getString(R.string.have_consume), kcal));
        scrip.setText(step, km, kcal);
        scrip.setImg(filePath);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) iv_big.getLayoutParams();
        layoutParams2.width = Utils.getDisplayWidth((Activity) context);
        layoutParams2.height = Utils.getDisplayWidth((Activity) context) - DensityUtil.dip2px(16);
        layoutParams2.setMargins(DensityUtil.dip2px(16), 0, DensityUtil.dip2px(16), 0);
        iv_big.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) ly_right.getLayoutParams();
        layoutParams1.width = (Utils.getDisplayWidth((Activity) context) - DensityUtil.dip2px(16)) * 2 / 5;
        layoutParams1.height = Utils.getDisplayWidth((Activity) context) - DensityUtil.dip2px(16);
        layoutParams1.setMargins(DensityUtil.dip2px(16), 0, 0, 0);
        ly_right.setLayoutParams(layoutParams1);
        PicassoUtil.displayImage(iv_big, filePath, context);
    }

    @Event(value = {R.id.btn_sure, R.id.tv_cancel})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                getSharePic();
                break;
            case R.id.tv_cancel:
                finish();
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
}
