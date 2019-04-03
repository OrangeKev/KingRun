package com.king.run.activity.mine.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.mine.model.FansInfo;
import com.king.run.intface.MyItemClickListener;
import com.king.run.util.PicassoUtil;
import com.king.run.view.BindFootView;
import com.king.run.view.CircleImageView;
import com.king.run.view.FootViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：shuizi_wade on 2017/8/24 14:33
 * 邮箱：674618016@qq.com
 */
public class FansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<FansInfo> list = new ArrayList<>();
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private MyItemClickListener itemClickListener;
    private boolean isFans;
    private AttentionClick attentionClick;

    public FansAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.activity_fans_list_row, null);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.itemView.setTag(position);
            if (isFans)
                viewHolder.btn_attention.setVisibility(View.VISIBLE);
            else
                viewHolder.btn_attention.setVisibility(View.GONE);
            PicassoUtil.displayImageSquare(viewHolder.iv_avatar, list.get(position).getAvatar(), context);
            viewHolder.tv_user_name.setText(list.get(position).getUserName());
            if (list.get(position).isFollowing()) {
                viewHolder.btn_attention.setBackgroundResource(R.drawable.btn_attention_true);
                viewHolder.btn_attention.setText(R.string.attention_on);
                viewHolder.btn_attention.setTextColor(ContextCompat.getColor(context, R.color.sub_class_content_text_color));
            } else {
                viewHolder.btn_attention.setBackgroundResource(R.drawable.btn_attention_false);
                viewHolder.btn_attention.setText(R.string.attention);
                viewHolder.btn_attention.setTextColor(ContextCompat.getColor(context, R.color.red));
            }
            viewHolder.btn_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attentionClick.attentionClick(position);
                }
            });
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

    public void setFans(boolean fans) {
        isFans = fans;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_user_name;
        public CircleImageView iv_avatar;
        public Button btn_attention;

        public ViewHolder(View view) {
            super(view);
            tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            iv_avatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
            btn_attention = (Button) view.findViewById(R.id.btn_attention);
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

    public interface AttentionClick {
        void attentionClick(int position);
    }

    public void setOnItemAttentionClickListener(AttentionClick attentionClick) {
        this.attentionClick = attentionClick;
    }
}
