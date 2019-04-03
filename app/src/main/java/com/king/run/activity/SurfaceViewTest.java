package com.king.run.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.king.run.R;
import com.king.run.base.BaseActivity;
import com.king.run.util.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;

/**
 * 作者：shuizi_wade on 2018/1/2 15:53
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.surface_layout)
public class SurfaceViewTest extends BaseActivity {
    MediaPlayer player;
    @ViewInject(R.id.surfaceView)
    SurfaceView surface_view;
    SurfaceHolder holder;
    @ViewInject(R.id.button1)
    Button play;
    @ViewInject(R.id.button2)
    Button pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVideoView();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
            }
        });
    }

    private void initVideoView() {
        int width = Utils.getDisplayWidth((Activity) context);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) surface_view.getLayoutParams();
        params.width = width / 2;
        params.height = width / 2;
        surface_view.setLayoutParams(params);
        player = new MediaPlayer();
        try {
            player.setDataSource(this, Uri.parse("http://www.jiuaiqinqiang.com/Uploads/2017-11-20/20171120_29.mp4"));
            holder = surface_view.getHolder();
            holder.addCallback(new MyCallBack());
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 然后开始播放视频
                    player.start();
                    player.setLooping(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != player) {
            player.release();
            player = null;
        }
    }
}
