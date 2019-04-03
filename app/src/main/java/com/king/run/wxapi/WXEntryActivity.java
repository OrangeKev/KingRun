package com.king.run.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.base.MyApplication;
import com.king.run.util.WxConstants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_wxentry)
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WxConstants.APP_ID, false);
        // 将该app注册到微信
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        finish();
//        senToa("baseresp.getType = " + resp.getType());
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.success;
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                String token = sendResp.code;
                Message message = new Message();
                message.obj = token;
                message.what = 1;
                MyApplication.getInstance().getWxHandler().sendMessage(message);
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.unsupported;
                break;
            default:
                result = R.string.unknown;
                break;
        }
    }
}
