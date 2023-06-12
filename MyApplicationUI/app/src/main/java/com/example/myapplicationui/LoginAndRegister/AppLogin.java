package com.example.myapplicationui.LoginAndRegister;

import static com.example.myapplicationui.common.ConstVariety.USER_LOGIN_POST_SUCCESSFUL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplicationui.MainPager.MainActivity;

import com.example.myapplicationui.R;
import com.example.myapplicationui.common.Res;
import com.example.myapplicationui.common.MyOkHttpClient;
import com.example.myapplicationui.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class AppLogin extends AppCompatActivity {
    private Handler handler;
    private Button ToRegBtn,LogBtn;
    private EditText username,password;
    Gson gson=new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不多说了处理线程返回的消息
        handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==USER_LOGIN_POST_SUCCESSFUL){
                    Type type = new TypeToken<Res<User>>() {}.getType();
                    System.out.println("msg.obj"+msg.obj);
                    Res<User> res = gson.fromJson(msg.obj.toString(), type);
                    User user = res.getData();
                    System.out.println(user.toString());
                    System.out.println(user.getId().equals(username.getText().toString().trim()));
                    System.out.println(user.getPassword().equals(password.getText().toString().trim()));
                    if(user.getId().equals(username.getText().toString().trim())&&
                            user.getPassword().equals(password.getText().toString().trim())) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    }
                }else{

                }
            }
        };
        setContentView(R.layout.activity_app_login);
        ToRegBtn=findViewById(R.id.loginPage_reg);
        LogBtn=findViewById(R.id.loginPage_login);
        username=findViewById(R.id.loginPage_nickname);
        username.setText("2971659793");

        password=findViewById(R.id.loginPage_password);
        password.setText("43051yxf");
//        autoLogin();
        BtnListen();

    }
//点击进入注册页面
    private void BtnListen(){
        ToRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AppLogin.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //点击进行登陆验证
        LogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userid=username.getText().toString().trim();
                String pwd=password.getText().toString().trim();
                final User[] user = {new User(userid, "", pwd, null, null)};
                if(!pwd.equals("")&&!userid.equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MyOkHttpClient.doPost(user[0],"/user/login",handler);
                        }
                    }).start();
                }
                Toast.makeText(AppLogin.this, "请写入完整信息", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //自动登录，没有使用，因为没有token，方正就是不想用，放弃了
    private void autoLogin(){
        SharedPreferences sp = getSharedPreferences("loginsp", Context.MODE_PRIVATE);
        int id=sp.getInt("userid",0);
        if(id!=0){
            Log.d("haha", "autoLogin: ");
            Intent intent=new Intent(AppLogin.this,MainActivity.class);
            intent.putExtra("userid",id);
            startActivity(intent);
            finish();
        }
    }

}