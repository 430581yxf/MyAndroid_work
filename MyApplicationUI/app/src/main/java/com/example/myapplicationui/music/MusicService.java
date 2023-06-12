package com.example.myapplicationui.music;
import static com.example.myapplicationui.common.ConstVariety.MUSIC_PATH;
import static com.example.myapplicationui.common.ConstVariety.MUSIC_PLAY_COMPLETION;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;
//这个的主要就是ibinder对象的方法控制音乐播放
public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private MusicBinder binder=new MusicBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public class MusicBinder extends Binder{
        //开始请求资源播放的按钮调用的函数
        public void controlVideoPlay(String path, Handler handler) throws IOException {
            String sourcePath=MUSIC_PATH+path+".mp3";
            System.out.println(sourcePath);
            mediaPlayer.setDataSource(sourcePath);
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 准备完成，在这里可以开始播放音乐
                    mediaPlayer.start();
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        System.out.println("mediaPlaying");
                    } else {
                        System.out.println("mediaError");
                    }
                }
            });
            //监听播放完成后的就想handler发送数据这个handler是真好用,线程间数据传送就用它
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Message msg=new Message();
                    msg.what=MUSIC_PLAY_COMPLETION;
                    handler.sendMessage(msg);
                }
            });
        }
//停止没用过只用暂停
        public void stopMusic(){
                mediaPlayer.stop();
        }
        //重置
        public void resetMusic(){
            mediaPlayer.reset();
        }
//暂停
        public void pauseMusic(){
            mediaPlayer.pause();
        }
        //就是继续播放不换歌曲
        public void RestartStartMusic(){
            mediaPlayer.start();
        }
    }
}
