package com.example.myapplicationui.music;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationui.R;
import com.example.myapplicationui.databinding.VideoplayerBinding;
import com.example.myapplicationui.entity.Music;
//不写了吧,小日子都学会了
class MyViewHolder extends RecyclerView.ViewHolder {
     Button likeBtn;

    TextView mTitleTv;
    TextView mTitleContent,itemPlayer;
    public String musicPath;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        likeBtn=itemView.findViewById(R.id.music_like);
        itemPlayer=itemView.findViewById(R.id.item_player);
        mTitleTv = itemView.findViewById(R.id.item_title);
        mTitleContent = itemView.findViewById(R.id.item_content);
        itemView.setTranslationX(0);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void bindData(Music music){
        itemPlayer.setText(music.getPayer());
        mTitleTv.setText(music.getTitle());
        mTitleContent.setText(music.getContent());
        musicPath=music.getMusicPath();
    }
}