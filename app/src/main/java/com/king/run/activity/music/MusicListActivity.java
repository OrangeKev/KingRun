package com.king.run.activity.music;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.music.adapter.MusicListAdapter;
import com.king.run.activity.music.model.MusicDetails;
import com.king.run.activity.music.model.MusicInfo;
import com.king.run.base.BaseActivity;
import com.king.run.util.PicassoUtil;
import com.king.run.util.Utils;
import com.king.run.view.CircleImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_music_list)
public class MusicListActivity extends BaseActivity {

    @ViewInject(R.id.ly_top)
    RelativeLayout ly_top;
    @ViewInject(R.id.iv_bg_top)
    ImageView iv_bg_top;
    @ViewInject(R.id.iv_bg)
    CircleImageView iv_bg;
    @ViewInject(R.id.tv_music_num)
    TextView tv_music_num;
    @ViewInject(R.id.tv_slow_run)
    TextView tv_slow_run;
    @ViewInject(R.id.tv_all_music_type)
    TextView tv_all_music_type;
    private MusicListAdapter adapter;
    @ViewInject(R.id.lv)
    ListView lv;
    private List<MusicDetails> list = new ArrayList<>();
    private MusicInfo musicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        musicInfo = (MusicInfo) getIntent().getExtras().getSerializable("detail");
        list = musicInfo.getMusics();
        adapter = new MusicListAdapter(context, list);
        tv_music_num.setText(list.size() + "首");
        tv_all_music_type.setText(list.size() + "首离线歌曲");
        lv.setAdapter(adapter);
        title_relate_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparency_color));
        title_iv_back.setImageResource(R.mipmap.common_icon_back_white);
        title_tv_title.setTextColor(ContextCompat.getColor(context, R.color.white_color));
        title_tv_title.setText(musicInfo.getTitle());
        tv_slow_run.setText(musicInfo.getTitle());
        int width = Utils.getDisplayWidth((Activity) context);
        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) ly_top.getLayoutParams();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_bg.getLayoutParams();
        params.width = (int) (width * 0.278);
        params.height = (int) (width * 0.278);
        iv_bg.setLayoutParams(params);
        int resId = Utils.getResource(musicInfo.getImage().split("\\.")[0]);
        iv_bg.setImageResource(resId);
        p.height = (int) (width * 0.735);
        ly_top.setLayoutParams(p);
        PicassoUtil.displayImageDim(iv_bg_top, resId, context);
    }
}
