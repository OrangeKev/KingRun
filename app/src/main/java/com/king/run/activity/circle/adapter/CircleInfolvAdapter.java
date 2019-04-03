package com.king.run.activity.circle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.circle.model.CircleInfo;
import com.king.run.intface.MyItemClickListener;
import com.king.run.view.BindFootView;
import com.king.run.view.FootViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/11/10 16:29
 * 邮箱：674618016@qq.com
 */
public class CircleInfolvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private static final int TYPE_HEADER = 2;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private MyItemClickListener itemClickListener;
    private List<CircleInfo> list = new ArrayList<>();
    private View mHeaderView;

    public CircleInfolvAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.fragment_circle_list_row, null);
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
        } else if (viewType == TYPE_HEADER) {
            return new Holder(mHeaderView);
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
        return list.size() + 2;
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
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setList(List<CircleInfo> list) {
        this.list = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_user_name;
        public TextView tv_join_size;
        public ImageView iv_avatar;

        public ViewHolder(View view) {
            super(view);
            tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tv_join_size = (TextView) view.findViewById(R.id.tv_join_size);
        }
    }

    class Holder extends RecyclerView.ViewHolder {

//        TextView text;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
//            text = (TextView) itemView.findViewById(R.id.text);
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
