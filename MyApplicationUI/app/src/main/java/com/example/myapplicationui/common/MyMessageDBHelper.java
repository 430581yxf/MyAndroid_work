package com.example.myapplicationui.common;

import static com.example.myapplicationui.common.ConstVariety.CREATE_MESSAGE_SQL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplicationui.entity.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

//这个就是消息存储helper
public class MyMessageDBHelper extends SQLiteOpenHelper {
    private String name=null;

    public MyMessageDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name+".db", null, 1);
        this.name=name;}
    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MESSAGE_SQL);

    }
    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) NOT NULL");
    }
    /*
    * save Message
    */
    @SuppressLint("Range")
    public void save(Message message)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql="INSERT INTO message (sender_id, receiver_id, text_content, image_count, is_receive,year,month,day,hour,minute,sec)\n" +
                "VALUES (?, ?, ?, ?, ?, ?,?,?,?,?,?)";
        String [] StrArr=new String[]{message.getSenderId(),message.getReceiverId()
                ,message.getTextContent(),String.valueOf(message.getImageCount()),String.valueOf(message.getIsReceive())
                ,String.valueOf(message.getYear()),String.valueOf(message.getMonth()),String.valueOf(message.getDay()),
                String.valueOf(message.getHour()),String.valueOf(message.getMinute()),String.valueOf(message.getSec())};
        db.execSQL(sql,StrArr);
    }
    public void delete(Integer id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM message WHERE id = ?",
                new Integer[]{id});
    }
    public List<Message> getScrollData(int maxResult,int offset,String id)
    {
        List<Message> messages = new ArrayList<Message>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM message where sender_id==? or receiver_id==?\n" +
                        "ORDER BY year ASC, month ASC, day ASC, hour ASC, minute ASC, sec ASC LIMIT ? offset ?",
                new String[]{id,id,String.valueOf(maxResult),String.valueOf(offset)});
        while(cursor.moveToNext())
        {
            @SuppressLint("Range") int id_col = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String sender_id_col = cursor.getString(cursor.getColumnIndex("sender_id"));
            @SuppressLint("Range") String receiver_id_col = cursor.getString(cursor.getColumnIndex("receiver_id"));
            @SuppressLint("Range") String text_content_col = cursor.getString(cursor.getColumnIndex("text_content"));
            @SuppressLint("Range") int image_count_col = cursor.getInt(cursor.getColumnIndex("image_count"));
            @SuppressLint("Range") int  year_col = cursor.getInt(cursor.getColumnIndex("year"));
            @SuppressLint("Range") int  month_col = cursor.getInt(cursor.getColumnIndex("month"));
            @SuppressLint("Range") int  day_col = cursor.getInt(cursor.getColumnIndex("day"));
            @SuppressLint("Range") int  hour_col = cursor.getInt(cursor.getColumnIndex("hour"));
            @SuppressLint("Range") int  minute_col = cursor.getInt(cursor.getColumnIndex("minute"));
            @SuppressLint("Range") int  sec_col = cursor.getInt(cursor.getColumnIndex("sec"));
            @SuppressLint("Range") int is_receive_col = cursor.getInt(cursor.getColumnIndex("is_receive"));
            Message message=new Message(id_col,sender_id_col,receiver_id_col,text_content_col,image_count_col,
                    is_receive_col,year_col,month_col,day_col,hour_col,minute_col,sec_col);
            Instant instant = null;


            messages.add(message);
        }
        cursor.close();
        return messages;
    }
}
