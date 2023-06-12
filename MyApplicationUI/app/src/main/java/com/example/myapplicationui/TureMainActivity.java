package com.example.myapplicationui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplicationui.MainPager.MainActivity;
//小插曲,看了一个视频发现一个好玩的后来慢慢放弃了.但是做了个小demo看看就行
//TrueMainActivity主要是调试的时候想的一个名字(真正的主页面)其实是假的
//服务端很简单的也不要自己配置Mapper只需要自己把实体类的名字和数据库保持对应的格式就好了,
// 这个对象和数据库都可以用工具生成,而且很方便
public class TureMainActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;
    private ImageView imageView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ture_main);
        lottieAnimationView=findViewById(R.id.dance_image);
        imageView=findViewById(R.id.show_flower);
        button=findViewById(R.id.login_btn);
        button.setEnabled(false);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               Intent intent= new Intent();
//               intent.setClass(TureMainActivity.this, MainActivity.class);
//               startActivity(intent);
//            }
//        });
    }
}