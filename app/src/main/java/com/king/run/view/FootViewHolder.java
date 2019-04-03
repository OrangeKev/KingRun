package com.king.run.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.run.R;


/**
 * 作者：shuizi_wade on 2016/11/29 15:09
 * 邮箱：674618016@qq.com
 */

public class FootViewHolder extends RecyclerView.ViewHolder {
    public TextView foot_view_item_tv;
    public ProgressBar pro;
    public RelativeLayout mLoadLayout;

    public FootViewHolder(View view) {
        super(view);
        foot_view_item_tv = (TextView) view.findViewById(R.id.id_text);
        pro = (ProgressBar) view.findViewById(R.id.pro_num);
        mLoadLayout = (RelativeLayout) view.findViewById(R.id.mLoadLayout);
    }
}
