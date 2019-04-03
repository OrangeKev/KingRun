package com.king.run.activity.statistics.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.model.http.res.ExerciseDetailInfo;
import com.king.run.util.TimeUtil;
import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * 作者：shuizi_wade on 2018/1/12 16:21
 * 邮箱：674618016@qq.com
 */
public class StatisticsListAdapter extends RcvSingleAdapter<ExerciseDetailInfo> {
    private Context context;

    public StatisticsListAdapter(Context context, List<ExerciseDetailInfo> datas) {
        super(context, R.layout.item_line_statistic, datas);
        this.context = context;
    }

    @Override
    public void onBindView(RcvHolder h, ExerciseDetailInfo entity, int position) {
        String time;
        if (entity.getSecond() > 0) {
            int hour = (int) (entity.getSecond() / 3600);
            int min = (int) ((entity.getSecond() % 3600) / 60);
            int second = (int) ((entity.getSecond() % 3600) % 60);
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
            time = ho + ":" + m + ":" + s;
        } else time = "--";
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ReductoCondSSK.ttf");
        TextView tv_unit_value = h.findView(R.id.tv_unit_value);
        tv_unit_value.setTypeface(typeface);
        tv_unit_value.setText(entity.getKilometre());
        TextView tv_time = h.findView(R.id.tv_time);
        tv_time.setTypeface(typeface);
        tv_time.setText(time);
        h.setTvText(R.id.tv_unit, R.string.km);
        h.setTvText(R.id.tv_date, TimeUtil.getTimeZheng(entity.getDate()));
    }
}
