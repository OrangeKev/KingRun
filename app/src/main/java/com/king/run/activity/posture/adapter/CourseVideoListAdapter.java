package com.king.run.activity.posture.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.king.run.R;
import com.king.run.util.Utils;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import org.xutils.common.util.DensityUtil;

import java.util.List;

/**
 * 作者：水子wade on 2018/1/30 21:02
 * 邮箱：674618016@qq.com
 */
public class CourseVideoListAdapter extends RcvSingleAdapter<String> {
    private Context context;

    public CourseVideoListAdapter(Context context, List<String> datas) {
        super(context, R.layout.activity_course_list_row, datas);
        this.context = context;
    }

    @Override
    public void onBindView(RcvHolder holder, String itemData, int position) {
        RelativeLayout ly = holder.findView(R.id.ly);
        RelativeLayout ly_out = holder.findView(R.id.ly_out);
        RelativeLayout ly_right = holder.findView(R.id.ly_right);
        int width = Utils.getDisplayWidth((Activity) context);
        int layoutWidth = (int) (width * 0.347);
        int layoutHeight = (int) (layoutWidth * 0.56);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) ly.getLayoutParams();
        params.height = layoutHeight + DensityUtil.dip2px(16);
        ly.setLayoutParams(params);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) ly_out.getLayoutParams();
        params1.height = layoutHeight;
        ly_out.setLayoutParams(params1);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ly_right.getLayoutParams();
        params2.width = layoutWidth;
        ly_right.setLayoutParams(params2);
    }
}
