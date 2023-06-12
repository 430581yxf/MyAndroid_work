package com.example.myapplicationui.playVedios;

import static com.example.myapplicationui.common.ConstVariety.VIDEO_PATH;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationui.R;
import com.example.myapplicationui.Utils.AppUtils;
import com.example.myapplicationui.entity.ShortVideo;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MyVideoHolder extends RecyclerView.ViewHolder implements SurfaceHolder.Callback{

    private EditText editText;
    private String videoPath;
    private Button searchBtn;
    private ImageButton imageButton;
    private Button ThumbBtn;
    private Button CommentBtn;
    private Button StoreBtn;
    private Button ShareBtn;
    private TextView VideoTitle;
    private TextView VideoDesc;
    private TextView num_likes;
    private TextView num_comment;
    private TextView num_store;
    private Boolean isLike;
    private Boolean isStore;
    private TextView authorName;
    private int authorId;
    private SeekBar seekBar;
    private SurfaceView surfaceView;
    private IjkMediaPlayer ijkMediaPlayer;
    private SurfaceHolder surfaceHolder;
    public MyVideoHolder(@NonNull View itemView) {
        super(itemView);
        surfaceView=itemView.findViewById(R.id.surface_view);
        editText=itemView.findViewById(R.id.video_search);
        searchBtn=itemView.findViewById(R.id.video_search_btn);
        ThumbBtn=itemView.findViewById(R.id.video_thumb);
        CommentBtn=itemView.findViewById(R.id.video_comment);
        StoreBtn=itemView.findViewById(R.id.video_comment);
        ShareBtn=itemView.findViewById(R.id.video_share);
        VideoTitle=itemView.findViewById(R.id.video_title);
        VideoDesc=itemView.findViewById(R.id.video_desc);
        num_store=itemView.findViewById(R.id.video_num_store);
        num_comment=itemView.findViewById(R.id.video_num_comment);
        num_likes=itemView.findViewById(R.id.video_num_likes);
        authorName=itemView.findViewById(R.id.VideoMakerName);
        seekBar=itemView.findViewById(R.id.video_seekBar);
        imageButton=itemView.findViewById(R.id.video_iamge);
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }
    public void bindData(ShortVideo video){

        videoPath=video.getPath();
        ijkMediaPlayer=new IjkMediaPlayer();
        VideoTitle.setText(video.getTitle());
        VideoDesc.setText(video.getDescription());
        num_likes.setText(video.getNumLikes() + "");
        num_comment.setText(video.getNumComments() + "");
        num_store.setText(video.getNumStores()+ "");
        try {
            imageButton.setImageBitmap(AppUtils.StringToIcon(video.getImageUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            imageButton.setImageResource(R.mipmap.wode);
        }
        editText.setHint("类似视频");
        authorName.setText(video.getMakerName());
        authorId = video.getMakerId();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // 创建 MediaPlayer 实例并设置数据源
            ijkMediaPlayer = new IjkMediaPlayer();
            ijkMediaPlayer.setDataSource(VIDEO_PATH+videoPath);
            ijkMediaPlayer.setDisplay(surfaceHolder);
            ijkMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    int weight,height;
                    // 准备完成后开始播放视频
                    ijkMediaPlayer.start();
                }
            });
            ijkMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.stop();
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
        }
    }
}
