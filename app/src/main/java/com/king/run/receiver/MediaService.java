package com.king.run.receiver;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.king.run.activity.music.model.MusicInfo;

import java.io.IOException;

/**
 * 作者：shuizi_wade on 2018/2/9 09:09
 * 邮箱：674618016@qq.com
 */
public class MediaService extends Service {
    private static final String TAG = "MediaService";
    private MyBinder mBinder = new MyBinder();
    private MusicInfo musicInfo = new MusicInfo();
    //标记当前歌曲的序号
    private int i = 0;
    //初始化MediaPlayer
    public MediaPlayer mMediaPlayer = new MediaPlayer();

    @Override
    public IBinder onBind(Intent intent) {
        musicInfo = (MusicInfo) intent.getExtras().getSerializable("detail");
        iniMediaPlayerFile(i);
        mBinder.playMusic();
        return mBinder;
    }

    public class MyBinder extends Binder {
        /**
         * 播放音乐
         */
        public void playMusic() {
            if (!mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mMediaPlayer.start();
            }
        }

        /**
         * 暂停播放
         */
        public void pauseMusic() {
            if (mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mMediaPlayer.pause();
            }
        }

        /**
         * 下一首
         */
        public void nextMusic() {
            if (mMediaPlayer != null && i < musicInfo.getMusics().size() && i >= 0) {
                //切换歌曲reset()很重要很重要很重要，没有会报IllegalStateException
                mMediaPlayer.reset();
                iniMediaPlayerFile(i + 1);
                //这里的if只要是为了不让歌曲的序号越界，因为只有4首歌
                if (i == 2) {

                } else {
                    i = i + 1;
                }
                playMusic();
            }
        }

        /**
         * 上一首
         */
        public void preciousMusic() {
            if (mMediaPlayer != null && i < musicInfo.getMusics().size() && i > 0) {
                mMediaPlayer.reset();
                iniMediaPlayerFile(i - 1);
                if (i == 1) {

                } else {

                    i = i - 1;
                }
                playMusic();
            }
        }


        /**
         * 关闭播放器
         */
        public void closeMedia() {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        }

        /**
         * 获取当前播放音乐
         *
         * @return
         */
        public String getCurrent() {
            return musicInfo.getMusics().get(i).getMusicName();
        }
    }

    /**
     * 添加file文件到MediaPlayer对象并且准备播放音频
     */
    private void iniMediaPlayerFile(int dex) {
        //获取文件路径
        try {
            //此处的两个方法需要捕获IO异常
            //设置音频文件到MediaPlayer对象中
            mMediaPlayer.setDataSource(musicInfo.getMusics().get(dex).getMusicUrl());
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mBinder.nextMusic();
                }
            });
            //让MediaPlayer对象准备
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG, "设置资源，准备阶段出错");
            e.printStackTrace();
        }
    }
}
