package com.example.myapplicationui.MainPager;

import static com.example.myapplicationui.common.ConstVariety.FRIEND_ADD_POST_SUCCESSFUL;
import static com.example.myapplicationui.common.ConstVariety.USER_PROFILE_GET_SUCCESSFUL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationui.DTO.FriendDTO;
import com.example.myapplicationui.DTO.UserDTO;
import com.example.myapplicationui.R;
import com.example.myapplicationui.common.MyOkHttpClient;
import com.example.myapplicationui.common.Res;
import com.example.myapplicationui.entity.Friends;
import com.example.myapplicationui.entity.User;
import com.example.myapplicationui.entity.UserProfile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
//搜索页面功能就很拉
public class SqllitFragment extends Fragment {
    private LinearLayout infoShowArea;
    private EditText searchContext;
    private Button searchButton;
    private Friends friends;
    private ImageView infoAvatar;
    private TextView infoName;
    private User user1;
    private TextView infoLocation;
    private Handler handler;
    private TextView infoSex;
    private TextView infoAge;
    private TextView infoOther;
    private Button addButton;
    private Gson gson=new Gson();

    public SqllitFragment(User user) {
        friends=new Friends();
        this.user1=user;
        friends.setId(user1.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sqllit, container, false);
        handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what==USER_PROFILE_GET_SUCCESSFUL){
                Type type = new TypeToken<Res<UserDTO>>() {}.getType();
                Res<UserDTO> res = gson.fromJson(msg.obj.toString(), type);
                UserDTO userDTO=res.getData();
                    if(userDTO!=null){
                        User user=res.getData().getUser();
                    if(infoShowArea.getVisibility()==View.GONE){
                        infoShowArea.setVisibility(View.VISIBLE);
                        UserProfile userProfile=res.getData().getUserProfile();
                        if(userProfile==null){
                            Toast.makeText(getContext(), "此人没有填信息", Toast.LENGTH_SHORT).show();
                            if(infoShowArea.getVisibility()==View.VISIBLE){
                                infoShowArea.setVisibility(View.GONE);}
                        }else{
                        friends.setFriends(user.getId());
                        flushView(user,userProfile);}
                    }
                    }
                    else {
                        Toast.makeText(getContext(), "nobody please insert right or enable Id", Toast.LENGTH_SHORT).show();
                        if(infoShowArea.getVisibility()==View.VISIBLE){
                            infoShowArea.setVisibility(View.GONE);}
                }
                }else if(msg.what==FRIEND_ADD_POST_SUCCESSFUL){
                    Type type = new TypeToken<Res<String>>() {}.getType();
                    System.out.println("msg.obj"+msg.obj);
                    Res<String> res = gson.fromJson(msg.obj.toString(), type);
                    if(res.getCode()==0)
                        Toast.makeText(getContext(), "已经是好友", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "操作错误", Toast.LENGTH_SHORT).show();
                }
            }
        };

        initView(view);
        // add listener to search button
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String keyword = searchContext.getText().toString();
                // TODO: do search using keyword
                String str=searchContext.getText().toString().trim();
                if(!str.equals("")){
                    System.out.println(str);
                    MyOkHttpClient.doGet("/user/profile/"+str,handler,null,getContext());
                }
                else {
                    Toast.makeText(getContext(), "please insert something like Id", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // add listener to add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String str=searchContext.getText().toString().trim();
                if(friends==null&&!str.equals("")){
                    friends.setFriends(str);
                    friends.setId(user1.getId());
                }
                MyOkHttpClient.doPost(friends,"/friends/add",handler);
                // TODO: add friend
            }
        });

        return view;
    }
    private void flushView(User user,UserProfile userProfile) {
        infoName.setText(user.getName());
        infoLocation.setText(userProfile.getAddress());
        infoSex.setText(userProfile.getSex());
        infoAge.setText(userProfile.getBirthdate()==null?"":userProfile.getBirthdate().toString());
        infoOther.setText(userProfile.getOtherInfo());
    }

    private void initView(View view) {
        infoShowArea=view.findViewById(R.id.info_show_area);
        searchContext = view.findViewById(R.id.search_context);
        searchButton = view.findViewById(R.id.fragment_search_btn);
        infoAvatar = view.findViewById(R.id.info_avatar);
        infoName = view.findViewById(R.id.info_name);
        infoLocation = view.findViewById(R.id.info_location);
        infoSex = view.findViewById(R.id.info_sex);
        infoAge = view.findViewById(R.id.info_age);
        infoOther = view.findViewById(R.id.info_other);
        addButton = view.findViewById(R.id.addButton);

    }

    // TODO: add methods for updating/showing user information
}