package com.king.run.activity.sport.run;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.model.http.res.ExerciseDetailInfo;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.StepAlgorithm;
import com.king.run.util.StringUtil;
import com.king.run.util.ThirdShare;
import com.king.run.util.TimeUtil;
import com.king.run.util.Utils;
import com.king.run.util.image.FileUtil;
import com.king.run.view.CircleImageView;
import com.king.run.view.ShareToDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

@ContentView(R.layout.activity_run_finish_details)
public class RunFinishDetailsActivity extends BaseActivity {
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
    private ExerciseDetailInfo detailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ReductoCondSSK.ttf");
        tvKm.setTypeface(typeface);
        tvRunTime.setTypeface(typeface);
        tvSpeed.setTypeface(typeface);
        tvKcal.setTypeface(typeface);
        if (getIntent().hasExtra("detailInfo")) {
            detailInfo = (ExerciseDetailInfo) getIntent().getExtras().getSerializable("detailInfo");
            if (null != detailInfo) {
                tvKm.setText(detailInfo.getKilometre());
                if (detailInfo.getSecond() > 0) {
                    int hour = (int) (detailInfo.getSecond() / 3600);
                    int min = (int) ((detailInfo.getSecond() % 3600) / 60);
                    int second = (int) ((detailInfo.getSecond() % 3600) % 60);
                    String ho, m, s;
                    if (hour < 10) {
                        ho = "0" + hour;
                    } else ho = hour + "";
                    if (min < 10) {
                        m = "0" + min;
                    } else m = min + "";
                    if (second < 10) {
                        s = "0" + second;
                    } else s = second + "";
                    tvRunTime.setText(ho + ":" + m + ":" + s);
                } else {
                    tvRunTime.setText("--");
                }
                if (StringUtil.isNotBlank(detailInfo.getPace()))
                    tvSpeed.setText(detailInfo.getPace());
                else
                    tvSpeed.setText("--");
                tvKcal.setText(detailInfo.getCalorie());
                tvGoal.setText(String.format(getResources().getString(R.string.sport_target), detailInfo.getTarget()));
                if (StringUtil.isNotBlank(detailInfo.getProgress()))
                    pb.setProgress(Integer.parseInt(detailInfo.getProgress()));
                tvCurrentTime.setText(TimeUtil.getDateToString(detailInfo.getDate()));
                tv_user_name.setText(PrefName.getNickName(context));
                PicassoUtil.displayImage(iv_user, PrefName.getAVATAR(context), context);
            }
        }
        title_tv_title.setText(R.string.sport_detail);
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
                        ThirdShare.shareToCircle(file.getAbsolutePath(), context);
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
