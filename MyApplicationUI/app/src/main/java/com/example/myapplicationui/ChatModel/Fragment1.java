package com.example.myapplicationui.ChatModel;

import static com.example.myapplicationui.common.ConstVariety.FRIEND_LIST_GET_SUCCESSFUL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplicationui.DTO.FriendDTO;
import com.example.myapplicationui.R;
import com.example.myapplicationui.Utils.AppUtils;
import com.example.myapplicationui.common.MyOkHttpClient;
import com.example.myapplicationui.common.Res;
import com.example.myapplicationui.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
//这个就是好友列表
public class Fragment1 extends Fragment {
    private User user;
    private Gson gson=new Gson();
    List<User> list=new ArrayList<>();

    private FriendDTO friendList=new FriendDTO();
    private RecyclerView chatRecycleView;
    Handler handler;

    public Fragment1(User user) {
        System.out.println(user.toString());
        this.user=user;
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler=new Handler(Looper.getMainLooper()){
            @Override
            //处理请求的好友返回的数据
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==FRIEND_LIST_GET_SUCCESSFUL){
                    Type type = new TypeToken<Res<FriendDTO>>() {}.getType();
                    System.out.println("msg.obj"+msg.obj);
                    Res<FriendDTO> res = gson.fromJson(msg.obj.toString(), type);
                    list= res.getData().getFriendList();
                    friendList=new FriendDTO();
                    friendList.setFriendList(list);
                    initView();
                }
                else{

                }
            }
        };
        //这个就是发起请求
        getFriendsList();
        //
        friendList.setFriendList(list);
    }

    private void getFriendsList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpClient.doGet("/friends/list/"+user.getId(),handler,null,getContext());
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatRecycleView=view.findViewById(R.id.ChatRecyclerView);
        initView();
    }

    private void initView() {
        ChatListAdapter chatListAdapter=new ChatListAdapter();
        chatRecycleView.setAdapter(chatListAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        chatRecycleView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        chatRecycleView.setLayoutManager(layoutManager);
    }
//这个就是好友列表适配器
    private class ChatListAdapter extends RecyclerView.Adapter<ChatListHolder>{
        @NonNull
        @Override
        public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=View.inflate(getContext(),R.layout.chatlistitem,null);
            ChatListHolder chatListHolder=new ChatListHolder(view);
            return chatListHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull ChatListHolder holder, int position) {
            if(friendList.getFriendList()!=null) {
                User user1 = friendList.getFriendList().get(position);
                holder.bindDate(user1);
                holder.itemView.setLongClickable(true);
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return false;
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ChatRoom.class);
                        intent.putExtra("friend", user1.getId());
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return friendList.getFriendList()==null?null:friendList.getFriendList().size();
        }

        public void removeItem(int position) {
            friendList.getFriendList().remove(position);
            notifyItemRemoved(position);
        }
    }

    //这个就是好友列表holder
    private class ChatListHolder extends RecyclerView.ViewHolder{
        String friends;

        private ImageButton avatar;
        private TextView name;
        private TextView notic;

        public ChatListHolder(@NonNull View itemView) {
            super(itemView);
            avatar=itemView.findViewById(R.id.FriendAvatar);
            name=itemView.findViewById(R.id.FriendName);
            notic=itemView.findViewById(R.id.notic_message);
        }
        @SuppressLint("SuspiciousIndentation")
        public void bindDate(User friend)
        {
            if(friend!=null) {
                friends = friend.getId();
                avatar.setImageBitmap(AppUtils.StringToIcon(friend.getAvatar() == null ? "" : friend.getAvatar()));
                if (friend.getRemark() != null)
                    name.setText(friend.getRemark());
                name.setText(friend.getName());
            }
        }
    }
    //设置item间距
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // 判断是否是第一个item，如果是则设置marginTop
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}