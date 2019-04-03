package com.king.run.activity.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.mine.model.FansInfo;
import com.king.run.intface.MyItemClickListener;
import com.king.run.view.BindFootView;
import com.king.run.view.CircleImageView;
import com.king.run.view.FootViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：shuizi_wade on 2017/8/24 14:33
 * 邮箱：674618016@qq.com
 */
public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<FansInfo> list = new ArrayList<>();
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private MyItemClickListener itemClickListener;

    public CollectAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.activity_collect_list_row, null);
            //这边可以做一些属性设置，甚至事件监听绑定
            final ViewHolder itemViewHolder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            });
            return itemViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.item_more, null);
            //这边可以做一些属性设置，甚至事件监听绑定
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.itemView.setTag(position);

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            BindFootView.onBindFootView(footViewHolder, load_more_status);
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setList(List<FansInfo> list) {
        this.list = list;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public ImageView iv_avatar;
        public TextView tv_time;

        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
