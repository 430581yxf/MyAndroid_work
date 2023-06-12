package com.example.myapplicationui.playVedios;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplicationui.R;
import com.example.myapplicationui.entity.ShortVideo;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
//适配不说了

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyVideoHolder> {
    private List<ShortVideo> videos;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    RecyclerView recyclerView;
    private IjkMediaPlayer mediaPlayer;

    MyRecyclerViewAdapter(List<ShortVideo> videos) {
        this.videos = videos;
    }

    @NonNull
    @Override
    public MyVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView=(RecyclerView) parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videoplayer, parent, false);
        MyVideoHolder myVideoHolder = new MyVideoHolder(view);
        return myVideoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyVideoHolder holder, @SuppressLint("RecyclerView") int position) {
        ShortVideo video = videos.get(position);
        holder.bindData(video);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
