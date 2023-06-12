package com.example.myapplicationui.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBUserOpenHelper extends SQLiteOpenHelper {
    public MyDBUserOpenHelper(Context context, String name, CursorFactory factory,
                              int version) {super(context, name, null, 1); }
    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(userid INTEGER PRIMARY KEY AUTOINCREMENT,username VARCHAR(20) UNIQUE,password VARCHAR(20))");
    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) NOT NULL");
    }
}