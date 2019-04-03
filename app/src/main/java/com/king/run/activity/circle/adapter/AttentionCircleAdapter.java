package com.king.run.activity.circle.adapter;

import android.content.Context;

import com.king.run.R;
import com.king.run.activity.mine.model.FansInfo;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * 作者：水子wade on 2017/11/12 21:39
 * 邮箱：674618016@qq.com
 */
public class AttentionCircleAdapter extends RcvSingleAdapter<FansInfo> {
    public AttentionCircleAdapter(Context context, List<FansInfo> datas) {
        super(context, R.layout.activity_attention_circle_list_row, datas);
    }

    @Override
    public void onBindView(RcvHolder holder, FansInfo itemData, int position) {

    }
}
