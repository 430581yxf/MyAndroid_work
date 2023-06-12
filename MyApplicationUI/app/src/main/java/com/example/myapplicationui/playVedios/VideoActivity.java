package com.example.myapplicationui.playVedios;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.myapplicationui.common.ConstVariety.VIDEO_LIST_GET_SUCCESSFUL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationui.ChatModel.MyChatAdapter;
import com.example.myapplicationui.DTO.MessageDTO;
import com.example.myapplicationui.R;
import com.example.myapplicationui.common.MyOkHttpClient;
import com.example.myapplicationui.common.Res;
import com.example.myapplicationui.entity.ShortVideo;
import com.example.myapplicationui.entity.User;
import com.example.myapplicationui.myInterface.OnViewPagerListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
//短视频播放页面也是绑定页面,这个页面样式花了点时间,但是数据没有绑定完全,不说了残次品,说起来伤心
public class VideoActivity extends AppCompatActivity{
    private User user;
    private Gson gson=new Gson();
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private IjkMediaPlayer mediaPlayer;
    RecyclerView recyclerView;
    String str;
    private int mposition=-1;
    MyLayoutManager manager;
    MyRecyclerViewAdapter adapter;
    private List<ShortVideo> videos=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        User user=(User) intent.getSerializableExtra("user");
        setContentView(R.layout.activity_video);

        Handler handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==VIDEO_LIST_GET_SUCCESSFUL){
                    Type type = new TypeToken<Res<List<ShortVideo>>>() {}.getType();
                    System.out.println("msg.obj"+msg.obj);
                    Res<List<ShortVideo>> res = gson.fromJson((String)msg.obj, type);
                    List<ShortVideo> list=res.getData();
                    addMessageShow(list);
                }
            }
        };
        recyclerView = findViewById(R.id.video_recyclerview);
        // 设置布局管理器（线性布局或者网格布局）
        manager = new MyLayoutManager(this, OrientationHelper.VERTICAL, false);
        manager.setOrientation(RecyclerView.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(manager);
        adapter = new MyRecyclerViewAdapter(videos);
        recyclerView.setAdapter(adapter);
        MyOkHttpClient.doGet("/video/list/"+user.getId(),handler,null,this);
    }

    public <T> void addMessageShow(T video){
        videos.removeAll(videos);
        videos.addAll((List<ShortVideo>)video);
        MyRecyclerViewAdapter adapter = (MyRecyclerViewAdapter) recyclerView.getAdapter();
// 通知适配器有数据变动发生，需要进行刷新
        adapter.notifyItemInserted(videos.size() - 1);
// 定位到新添加的 item
        recyclerView.scrollToPosition(0);
    }


}