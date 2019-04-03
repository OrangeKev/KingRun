package com.king.run.receiver;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import com.king.run.activity.music.model.MusicInfo;
import com.orhanobut.logger.Logger;

import java.io.IOException;

/**
 * 作者：水子wade on 2018/2/8 21:55
 * 邮箱：674618016@qq.com
 */
public class AudioService extends Service implements MediaPlayer.OnCompletionListener {
    MediaPlayer player;
    private MusicInfo musicInfo = new MusicInfo();
    private final IBinder binder = new AudioBinder();
    private int current = 0;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        musicInfo = (MusicInfo) arg0.getExtras().getSerializable("detail");
        Logger.d(musicInfo.getDesc());
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    //在这里我们需要实例化MediaPlayer对象

    /**
     * 该方法在SDK2.0才开始有的，替代原来的onStart方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicInfo = (MusicInfo) intent.getExtras().getSerializable("detail");
        try {
            player = new MediaPlayer();
            player.setDataSource(musicInfo.getMusics().get(current).getMusicUrl());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                player.start();
            }
        });
        player.setOnCompletionListener(this);
        Logger.d(musicInfo.getDesc());

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 当Audio播放完的时候触发该动作
     */
    @Override
    public void onCompletion(MediaPlayer p) {
        next();
    }


    public void onDestroy() {
        //super.onDestroy();
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
    }

    //为了和Activity交互，我们需要定义一个Binder对象
    public class AudioBinder extends Binder {

        //返回Service对象
        public AudioService getService() {
            return AudioService.this;
        }
    }

    //后退播放进度
    public void haveFun() {
        if (player.isPlaying() && player.getCurrentPosition() > 2500) {
            player.seekTo(player.getCurrentPosition() - 2500);
        }
    }

    public void next() {
        if (current == musicInfo.getMusics().size() - 1)
            current = 0;
        else
            current++;
        playMusic();
    }

    public void pre() {
        if (current == 0)
            current = musicInfo.getMusics().size() - 1;
        else
            current--;
        playMusic();
    }

    private void playMusic() {
        try {
            player.stop();
            player.release();
            player = new MediaPlayer();
            player.setDataSource(musicInfo.getMusics().get(current).getMusicUrl());
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    player.start();
                }
            });
            player.setOnCompletionListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
