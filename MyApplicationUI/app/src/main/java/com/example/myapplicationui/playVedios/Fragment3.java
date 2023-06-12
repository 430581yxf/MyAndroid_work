package com.example.myapplicationui.playVedios;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplicationui.R;
import com.example.myapplicationui.TureMainActivity;
import com.example.myapplicationui.entity.ShortVideo;
import com.example.myapplicationui.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//这个就是放了两个图标点击进入不同的页面不写了
public class Fragment3 extends Fragment{
    private Button button,moreOperate;
    private User user;
    public Fragment3(User user){this.user=user;}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        button=view.findViewById(R.id.video_entry);
        moreOperate=view.findViewById(R.id.more_play);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建 Intent 对象
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        moreOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), TureMainActivity.class);
                startActivity(intent);
            }
        });



    }
}