package com.king.run.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.king.run.activity.circle.PublishActivity;
import com.king.run.base.BaseActivity;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import net.arvin.pictureselector.entities.ImageEntity;

import java.util.ArrayList;

/**
 * 作者：shuizi_wade on 2018/1/11 13:44
 * 邮箱：674618016@qq.com
 */
public class ThirdShare {

    private static final int THUMB_SIZE = 150;
    private static int mTargetScene = SendMessageToWX.Req.WXSceneSession;

    public static void shareToWb(Bitmap bitmap, WbShareHandler wbShareHandler) {
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        weiboMultiMessage.imageObject = imageObject;
//        weiboMultiMessage.mediaObject = weiboMultiMessage;
        wbShareHandler.shareMessage(weiboMultiMessage, false);
    }

    public static void shareToWx(Bitmap bitmap, IWXAPI api) {
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

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static void onClickShareQQ(String path, Context context, Tencent mTencent) {
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

    public static void shareToCircle(String path, Context context) {
        ArrayList<ImageEntity> selectedImages = new ArrayList<>();
        ImageEntity imageEntity = new ImageEntity(path);
        selectedImages.add(imageEntity);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("fileList", selectedImages);
        ((BaseActivity) context).jumpBundleActvity(PublishActivity.class, bundle);
    }
}
