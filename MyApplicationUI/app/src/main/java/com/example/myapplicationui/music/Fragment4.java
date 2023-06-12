package com.example.myapplicationui.music;

import static com.example.myapplicationui.common.ConstVariety.MUSIC_LIST_GET_SUCCESSFUL;
import static com.example.myapplicationui.common.ConstVariety.MUSIC_PLAY_COMPLETION;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.myapplicationui.R;
import com.example.myapplicationui.common.MyOkHttpClient;
import com.example.myapplicationui.common.Res;
import com.example.myapplicationui.entity.Music;
import com.example.myapplicationui.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
//音乐界面老师都说了音乐界面了看来是一个很热门的作业话题，写一个主要是复杂一点的逻辑
public class Fragment4 extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayout musicDisplayArea;
    private ImageView musicAvatar,musicDetailAvatar;
    private TextView musicContent;
    private ConstraintLayout musicDetailArea;
    private LinearLayout musicDetailTop, showDetailArea;
    private TextView detailMusicName,detailMusicPlayer,detialMusicContent;
    private Button lastMusicBtn,displayMusicBtn,nextMusicBtn,detailBackBtn,musicDisplay;


    private Gson gson=new Gson();
    private MusicService.MusicBinder binder;

    List<Music> Lists=new ArrayList<>();
    private User user;
    private boolean isPlay=false;
    private Intent intent;
    private String nowPath="";
    private String newPath="";
    private Handler handler=null;
    public Fragment4(User user){this.user=user;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        intent = new Intent(getContext(),MusicService.class);
        getContext().bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        MyRecyclerViewAdapter myRecyclerViewAdapter=new MyRecyclerViewAdapter();
        //
        handler=new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                //就是开始的时候获取与音乐信息列表
                if (msg.what == MUSIC_LIST_GET_SUCCESSFUL) {
                    Type type = new TypeToken<Res<List<Music>>>() {}.getType();
                    Res<List<Music>> res = gson.fromJson((String)msg.obj, type);
                    List<Music> list=res.getData();
                    addMusicInfo(list);
                    //音乐播放完成
                }else if(msg.what==MUSIC_PLAY_COMPLETION){
                    try {
                        //自动播放完需要切换下一首
                        nowPath=newPath;
                        //获取下一首
                        getNewPath();
                        binder.resetMusic();
                        binder.controlVideoPlay(nowPath,handler);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }            }
        };
        //发情请求获取音乐的信息
        new Thread(()->MyOkHttpClient.doGet("/music/list/"+user,handler,null,getContext())).start();
        recyclerView.setAdapter(myRecyclerViewAdapter);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(manager);
        //这歌就是左右滑动item其实没啥用花里胡哨
        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // 设置支持的拖拽和滑动的方向
                return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // 处理拖拽事件
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 处理滑动事件
                viewHolder.itemView.animate()
                        .translationX(viewHolder.itemView.getWidth() * -0.5f)
                        .alpha(0.5f)
                        .setDuration(300)
                        .setInterpolator(new DecelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // 动画结束后，将 item 从 RecyclerView 中移除
                                myRecyclerViewAdapter.removeItem(viewHolder.getAdapterPosition());
                            }
                        })
                        .start();
            }
        }).attachToRecyclerView(recyclerView);
        //开始的时候隐藏音乐播放详细页面
        musicDetailArea.setY(-musicDetailArea.getHeight());
        //点击弹出音乐详细页面，当然数据没有绑定，主要是想看一看动画效果
        musicDisplayArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator downAnim = ObjectAnimator.ofFloat(musicDetailArea, "translationY", 0, -musicDetailArea.getHeight());
                downAnim.setDuration(800);
                downAnim.setInterpolator(new AccelerateInterpolator());
                downAnim.start();
                recyclerView.setEnabled(false);
                musicDisplayArea.setEnabled(false);
            }
        });
        //详细页面返回列表，也就是隐藏详细列表
        detailBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator downAnim = ObjectAnimator.ofFloat(musicDetailArea, "translationY",  -musicDetailArea.getHeight(),0);
                downAnim.setDuration(800);
                downAnim.setInterpolator(new AccelerateInterpolator());
                downAnim.start();
                recyclerView.setEnabled(true);
                musicDisplayArea.setEnabled(true);
            }
        });
//        点击播放按钮里面的逻辑你去看还是有点啰嗦就是认为习惯
        musicDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlay){
                    binder.pauseMusic();
                    displayMusicBtn.setText(R.string.display_music);
                    musicDisplay.setText(R.string.display_music);
                    isPlay=false;
                }else if(!isPlay&&nowPath.equals("")){
                    isPlay=true;
                    getNowPath();
                    try {
                        binder.controlVideoPlay(nowPath,handler);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    displayMusicBtn.setText(R.string.stop_music);
                    musicDisplay.setText(R.string.stop_music);
                }
                else {
                    isPlay=true;
                    binder.RestartStartMusic();
                    displayMusicBtn.setText(R.string.stop_music);
                    musicDisplay.setText(R.string.stop_music);

                }
            }
        });
        //详细页面点击时监听器其实和上面是一样的
        displayMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(isPlay){
                binder.pauseMusic();
                isPlay=false;
                displayMusicBtn.setText(R.string.display_music);
                musicDisplay.setText(R.string.display_music);
            }else if(!isPlay&&nowPath.equals("")){
                isPlay=true;
                getNowPath();
                try {
                    binder.controlVideoPlay(nowPath,handler);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                displayMusicBtn.setText(R.string.stop_music);
                musicDisplay.setText(R.string.stop_music);
            }
            else {
                binder.RestartStartMusic();
                isPlay=true;
                displayMusicBtn.setText(R.string.stop_music);
                musicDisplay.setText(R.string.stop_music);
            }
            }
        });
//        点击播放下一首歌逻辑不讲了有缘人去看吧
        lastMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            getNewLastPath();
            nowPath=newPath;
            getNewPath();
            binder.resetMusic();
            isPlay=true;
                try {
                    binder.controlVideoPlay(nowPath,handler);
                    displayMusicBtn.setText(R.string.stop_music);
                    musicDisplay.setText(R.string.stop_music);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        //点击播放上一首歌
        nextMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            getNewPath();
            nowPath=newPath;
            getNewPath();
            binder.resetMusic();
                isPlay=true;
                try {
                    binder.controlVideoPlay(nowPath,handler);
                    displayMusicBtn.setText(R.string.stop_music);
                    musicDisplay.setText(R.string.stop_music);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    //获取布局对象
    private void initView(View view) {
        recyclerView = view.findViewById(R.id.fragment_recyclerview);
        musicDisplayArea = view.findViewById(R.id.music_display_area);
        musicAvatar = view.findViewById(R.id.music_avatar);
        musicContent = view.findViewById(R.id.music_content);
        musicDisplay = view.findViewById(R.id.music_display);
        musicDetailArea = view.findViewById(R.id.music_detail_area);
        musicDetailTop = view.findViewById(R.id.music_detail_top);
        detailBackBtn = view.findViewById(R.id.detail_back_btn);
        showDetailArea = view.findViewById(R.id.show_detail_area);
        musicDetailAvatar = view.findViewById(R.id.music_detail_avatar);
        detailMusicName = view.findViewById(R.id.detail_music_name);
        detailMusicPlayer = view.findViewById(R.id.detail_music_player);
        detialMusicContent = view.findViewById(R.id.detial_music_content);
        lastMusicBtn = view.findViewById(R.id.last_detail_music);
        displayMusicBtn = view.findViewById(R.id.display_detail_music);
        nextMusicBtn = view.findViewById(R.id.next_detail_music);

    }
//音乐列表的适配器

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(getContext(),R.layout.list_item,null);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }
//音乐列表的holder
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Music music=Lists.get(position);
        holder.bindData(music);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //不在播放而且现在播放路径与点击路径不相等
                //从新来过
                if(!isPlay&&!nowPath.equals(holder.musicPath)) {
                    isPlay=true;
                    nowPath = holder.musicPath;
                    getNewPath();
                    try {
                        binder.resetMusic();
                        binder.controlVideoPlay(nowPath, handler);
                        displayMusicBtn.setText(R.string.stop_music);
                        musicDisplay.setText(R.string.stop_music);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                //在播放而且现在播放路径与点击路径相等不做任何动作
                else if(isPlay&&nowPath.equals(holder.musicPath)){}
                //在播放而且现在播放路径与点击路径不相等重新来过
                else if(isPlay&&!nowPath.equals(holder.musicPath)){
                    isPlay=true;
                    nowPath = holder.musicPath;
                    getNewPath();
                    try {
                        binder.resetMusic();
                        binder.controlVideoPlay(nowPath, handler);
                        displayMusicBtn.setText(R.string.stop_music);
                        musicDisplay.setText(R.string.stop_music);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                //不在播放但是相等继续播放
                else if(!isPlay&&nowPath.equals(holder.musicPath)){
                    binder.RestartStartMusic();
                    displayMusicBtn.setText(R.string.stop_music);
                    musicDisplay.setText(R.string.stop_music);
                }
            }
        });
        }
        @Override
        public int getItemCount() {
            return Lists.size();
        }
        public void removeItem(int position) {
            Lists.remove(position);
            notifyItemRemoved(position);
        }
    }
    //这个就有点作了用不用泛型都无所谓·这个类只有一种在用
    public <T> void addMusicInfo(T music){

        int count=Lists.size();
        Lists.addAll((List<Music>)music);
        MyRecyclerViewAdapter adapter = (MyRecyclerViewAdapter) recyclerView.getAdapter();
// 通知适配器有数据变动发生，需要进行刷新
        adapter.notifyItemInserted(Lists.size() - 1);
// 定位到新添加的 item
        recyclerView.scrollToPosition(count-1);
    }
//绑定service
    public ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            System.out.println("serviceConnect------------");
            binder=(MusicService.MusicBinder)iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            System.out.println("sericeDisconnect--------");
            binder=null;
        }
    };
    //获取当前需要播放的歌曲的路径
    public void getNewPath() {
        for (int i = 0; i < Lists.size(); i++)
            if (Lists.get(i).getMusicPath().equals(nowPath)) {
                if (i == Lists.size() - 1)
                    newPath = Lists.get(0).getMusicPath();
                else {
                    newPath = Lists.get(i + 1).getMusicPath();
                    break;
                }
            }
    }
//    获取点击了播放上一首歌按钮lastBtn播放的下一首歌的路径
    public void getNewLastPath() {
        for (int i = 0; i < Lists.size(); i++)
            if (Lists.get(i).getMusicPath().equals(nowPath)) {
                if (i == 0)
                    newPath = Lists.get(Lists.size()-1).getMusicPath();
                else {
                    newPath = Lists.get(i - 1).getMusicPath();
                    break;
                }
            }
    }
    //获取自然状态下也就是自动播放下一首歌的路径
    public void getNowPath(){
        nowPath=Lists.get(0).getMusicPath();
        getNewPath();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unbindService(serviceConnection);
        getActivity().finish();
    }
}
