package com.king.run.activity.sport.run;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.circle.PublishActivity;
import com.king.run.base.BaseActivity;
import com.king.run.util.DateUtils;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.QQConstants;
import com.king.run.util.ThirdShare;
import com.king.run.util.Util;
import com.king.run.util.Utils;
import com.king.run.util.WxConstants;
import com.king.run.util.image.FileUtil;
import com.king.run.view.CircleImageView;
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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

@ContentView(R.layout.activity_run_finish)
public class RunFinishActivity extends BaseActivity {
    @ViewInject(R.id.tv_km)
    private TextView tvKm;

    @ViewInject(R.id.tv_run_time)
    private TextView tvRunTime;

    @ViewInject(R.id.tv_speed)
    private TextView tvSpeed;

    @ViewInject(R.id.tv_kcal)
    private TextView tvKcal;

    @ViewInject(R.id.tv_target_km)
    private TextView tvGoal;

    @ViewInject(R.id.tv_time)
    private TextView tvCurrentTime;

    @ViewInject(R.id.tv_goal_status)
    private TextView tvGoalStatus;

    @ViewInject(R.id.pb_run_finish)
    private ProgressBar pb;
    @ViewInject(R.id.tv_user_name)
    TextView tv_user_name;
    @ViewInject(R.id.iv_user)
    CircleImageView iv_user;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        initDatas();
        checkStoragePermission();
    }

    @Override
    protected void gotStoragePermissionResult(boolean isGrant) {
        super.gotStoragePermissionResult(isGrant);
        if (!isGrant) senToa(R.string.permission_phone_storage);
    }


    private void initDatas() {
        tvCurrentTime.setText(DateUtils.getDate());
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            id = bundle.getInt("id");
            tvKm.setText(bundle.getString("km"));
            tvRunTime.setText(bundle.getString("time"));
            tvKcal.setText(bundle.getString("kcal"));
            tvSpeed.setText(bundle.getString("speed"));
            if (bundle.getBoolean("hasGoal", false)) {
                tvGoal.setText(bundle.getString("goal"));
                int percent = bundle.getInt("percent", 0);
                if (percent == 100) {
                    tvGoalStatus.setText(bundle.getString("目标达成"));
                } else {
                    tvGoalStatus.setText(bundle.getString("目标未达成"));
                }
                pb.setProgress(percent);
            } else {
                tvGoal.setVisibility(View.INVISIBLE);
                tvGoalStatus.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initViews() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ReductoCondSSK.ttf");
        tvKm.setTypeface(typeface);
        tvRunTime.setTypeface(typeface);
        tvSpeed.setTypeface(typeface);
        tvKcal.setTypeface(typeface);
        tv_user_name.setText(PrefName.getNickName(context));
        PicassoUtil.displayImage(iv_user, PrefName.getAVATAR(context), context);
        title_tv_title.setText(R.string.sport_finish);
        title_iv_back.setBackgroundResource(R.mipmap.common_icon_close_black);
        title_relate_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_filly_transparent));
    }

    @Event(value = {R.id.btn_share_record, R.id.title_iv_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.title_iv_back:
                finish();
                break;
            case R.id.btn_share_record:
                final Bitmap bitmap = Utils.activityShot((Activity) context);
                final File file = FileUtil.saveBitmapFile(bitmap);
                new ShareToDialog(context, new ShareToDialog.ClickListener() {
                    @Override
                    public void shareToWeixin() {
                        ThirdShare.shareToWx(bitmap, api);
                    }

                    @Override
                    public void shareToCircle() {
                        Bundle bundle = new Bundle();
                        if (id != -1)
                            bundle.putInt("id", id);
                        jumpBundleActvity(PublishActivity.class, bundle);
//                        ThirdShare.shareToCircle(file.getAbsolutePath(), context);
                    }

                    @Override
                    public void shareToWeibo() {
                        ThirdShare.shareToWb(bitmap, wbShareHandler);
                    }

                    @Override
                    public void shareToQQ() {
                        ThirdShare.onClickShareQQ(file.getAbsolutePath(), context, mTencent);
                    }
                }).show();
                break;
        }
    }
}
