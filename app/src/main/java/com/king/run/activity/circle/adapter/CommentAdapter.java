package com.king.run.activity.circle.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.circle.model.Comment;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.StringUtil;
import com.king.run.util.Url;
import com.king.run.view.CircleImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 作者：水子wade on 2017/11/29 22:40
 * 邮箱：674618016@qq.com
 */
public class CommentAdapter extends BaseAdapter {
    private List<Comment> comments = new ArrayList<>();
    private Context context;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.comment_list_row, null);
            x.view().inject(holder, view);
            view.setTag(holder);
        } else holder = (Holder) view.getTag();
        Comment comment = comments.get(i);
        if (StringUtil.isNotBlank(comment.getToUserName())) {
//            SpannableString spannableString = new SpannableString(comment.getReviewerName() + " 回复 "
//                    + comment.getToUserName() + ":" + comment.getContent());
//            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_color_blue)), 0,
//                    comment.getReviewerName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_color_blue)), comment.getReviewerName().length() + 4,
//                    comment.getReviewerName().length() + 4 + comment.getToUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            holder.tv_comment.setText(spannableString);

            holder.tv_comment_user.setText(comment.getReviewerName());
            SpannableString spannableString = new SpannableString("回复"
                    + comment.getToUserName() + ":" + comment.getContent());
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_color_blue)), 2,
                    comment.getReviewerName().length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_content.setText(spannableString);
        } else {
            holder.tv_comment_user.setText(comment.getReviewerName());
            holder.tv_content.setText(comment.getContent());
            SpannableString spannableString = new SpannableString(comment.getReviewerName() + ":" + comment.getContent());
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_color_blue)), 0,
                    comment.getReviewerName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            holder.tv_comment.setText(spannableString);
        }
        String url = comment.getAvator();
        if (null != comment.getAvator()) {
            if (comment.getAvator().contains("http:"))
                url = Url.BASE_URL + url;
            PicassoUtil.displayImage(holder.iv_avatar, url, context);
        }

        long time = System.currentTimeMillis();
        long between = (time - comment.getDate()) / 1000;
        String showTime;
        int betweenDay = getTimeDistance(comment.getDate(), time);
        if (betweenDay < 1) {
            if (between < 3600) {
                if (between / 60 < 1) {
                    showTime = "刚刚";
                } else
                    showTime = between / 60 + "分钟前";
            } else {
                showTime = between / 3600 + "小时前";
            }
        } else if (betweenDay > 30) {
            showTime = betweenDay / 30 + "月前";
        } else {
            showTime = betweenDay + "天前";
        }
        holder.tv_time.setText(showTime);
        return view;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    private class Holder {
        @ViewInject(R.id.tv_content)
        TextView tv_content;
        @ViewInject(R.id.tv_time)
        TextView tv_time;
        @ViewInject(R.id.iv_avatar)
        CircleImageView iv_avatar;
        @ViewInject(R.id.tv_comment_user)
        TextView tv_comment_user;
    }

    private static int getTimeDistance(long beginDate, long endDate) {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTimeInMillis(beginDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endDate);
        long beginTime = beginCalendar.getTime().getTime();
        long endTime = endCalendar.getTime().getTime();
        int betweenDays = (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));//先算出两时间的毫秒数之差大于一天的天数

        endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);//使endCalendar减去这些天数，将问题转换为两时间的毫秒数之差不足一天的情况
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);//再使endCalendar减去1天
        if (beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH))//比较两日期的DAY_OF_MONTH是否相等
            return betweenDays + 1; //相等说明确实跨天了
        else
            return betweenDays; //不相等说明确实未跨天
    }
}
