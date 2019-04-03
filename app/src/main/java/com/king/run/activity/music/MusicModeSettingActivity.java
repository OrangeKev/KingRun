package com.king.run.activity.music;

import android.os.Bundle;

import com.king.run.R;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_music_mode_setting)
public class MusicModeSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        title_tv_title.setText("设置音乐播放模式");
    }
}
