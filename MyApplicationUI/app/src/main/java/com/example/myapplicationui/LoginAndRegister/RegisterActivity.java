package com.example.myapplicationui.LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplicationui.MainPager.MainActivity;
import com.example.myapplicationui.R;
import com.example.myapplicationui.common.MyDBUserOpenHelper;

public class RegisterActivity extends AppCompatActivity {
    private static final String database_name="user.db";
    private Button RegBtn;
    private Button ToLogBtn;
    private Intent intent;
    private EditText username;
    private EditText password;
    private EditText password2;
    private SQLiteDatabase db;
    private MyDBUserOpenHelper myDBHelper;
    //别看了这个注册不了，有一个已经写好的账户，注册功能呢没写全知识存储到了本地sqlite主要是着这个没意思，还要写对应的没意思的后端代码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDBHelper=new MyDBUserOpenHelper(RegisterActivity.this,database_name,null,1);
        setContentView(R.layout.activity_register_activvity);
        RegBtn=findViewById(R.id.submit);
        ToLogBtn=findViewById(R.id.back_login);
        username=findViewById(R.id.usename);
        password=findViewById(R.id.usepwd);
        password2=findViewById(R.id.usepwd2);
        ToLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str1=username.getText().toString().trim(),
                        str2=password.getText().toString().trim(),
                        str3=password2.getText().toString().trim();
            if((!TextUtils.isEmpty(str1))&&
                    (!TextUtils.isEmpty(str2))&&
                    (!TextUtils.isEmpty(str3))){
                if(!str2.equals(str3))
                    Toast.makeText(RegisterActivity.this, "确认密码不一致+str1"+str1+"\nstr2"+str2, Toast.LENGTH_SHORT).show();
                else{
                    db=myDBHelper.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    values.put("username",username.getText().toString());
                    values.put("password",password.getText().toString());
                    long userid = db.insert("user", null, values);
                    if( userid!=-1){
                        String[] columns = {"userid"};
                        String selection = "username = '"+str1+"'";
                        Cursor cursor = db.query("user", columns, selection, null, null, null, null);

// 遍历查询结果集并输出

                        while (cursor.moveToNext()) {
                            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("userid"));
                            SharedPreferences sh=getSharedPreferences("loginsp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sh.edit();
                            editor.putInt("userid",id);
                            editor.apply();
                            Toast.makeText(RegisterActivity.this, "shareperference插入成功", Toast.LENGTH_SHORT).show();
                        }

// 关闭游标
                        cursor.close();
                        intent=new Intent(RegisterActivity.this, MainActivity.class);
                        intent.putExtra("userid",userid);
                        startActivity(intent);
                        finish();
                    }

                }



            }
            else
                Toast.makeText(RegisterActivity.this, "请输入完整的信息", Toast.LENGTH_SHORT).show();
            }
        });
    }
}