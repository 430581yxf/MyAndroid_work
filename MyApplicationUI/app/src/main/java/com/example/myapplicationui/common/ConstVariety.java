package com.example.myapplicationui.common;

//定义的常量
public class ConstVariety {

    public static String HOST_NAME="http://111.230.29.132:8080";
    //服务端主机ip
//    public static String HOST_NAME="http://192.168.187.1:8080";
//    websocket服务器ip
    public static String HOST_NAME_WS="ws://111.230.29.132:8080";
    public static int  WEBSOCKET_CONNECT_SUCESSFUL=0;//websocket连接成功
    public static int  WEBSOCKET_RECEIVE_SUCESSFUL=-1;//websocket接收消息成功
    public static int  WEBSOCKET_CONNECT_ERROR=-2;//连接成功


//    public static String DATA_HOST="bj-cdb-f6yh0fje.sql.tencentcdb.com";
    //下面四种就是消息类型
    public static int SEND_VIEW_TYPE_TEXT = 1;
    public static int RECEIVE_VIEW_TYPE_TEXT = 2;
    public static int SEND_VIEW_TYPE_IMAGE=3;
    public static int RECEIVE_VIEW_TYPE_IMAGE=4;
    //像网络请求消息
    public static int MESSAGE_GET_FAILURE=5;
    public static int MESSAGE_GET_SUCCESSFUL=6;
//发送图片
    public static int SEND_IMAGE_ERROR=7;
    public static int SEND_IMAGE_SUCCESSFUL=8;
    //接受图片
    public static int RECEIVE_IMAGE_SUCCESSFUL=9;
    public static int RECEIVE_IMAGE_ERROR=10;

    //获取彭友列表成功
    public static int FRIEND_LIST_GET_SUCCESSFUL=11;

    public static int FRIEND_LIST_GET_ERROR=12;
    //添加朋友
    public static int FRIEND_ADD_POST_SUCCESSFUL=12;
    public static int FRIEND_ADD_POST_ERROR=13;
    //用户登录

    public static int USER_LOGIN_POST_ERROR=15;

    public static int USER_LOGIN_POST_SUCCESSFUL=16;
    //获取用户详细信息
    public static int USER_PROFILE_GET_SUCCESSFUL=17;
    public static int USER_PROFILE_GET_ERROR=18;
    //视频信息请求
    public static int VIDEO_LIST_GET_ERROR=19;
    public static int VIDEO_LIST_GET_SUCCESSFUL=20;
    //音乐信息请求
    public static int MUSIC_LIST_GET_SUCCESSFUL=22;
    public static int MUSIC_LIST_GET_ERROR=21;
    //音乐播放完成
    public static int MUSIC_PLAY_COMPLETION=23;


    //视频请求地址
    public static String VIDEO_PATH="rtmp://111.230.29.132:1935/vid/";
//    音乐请求地址
    public static String MUSIC_PATH="http://111.230.29.132/music/";



//这个是创建sqlite表的sql语句这个不是我写的是用工具生成的，比我写得好。
    public  static String CREATE_MESSAGE_SQL="CREATE TABLE message ("+
            "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "sender_id TEXT NOT NULL,"+
            "receiver_id TEXT NOT NULL,"+
            "text_content TEXT DEFAULT NULL,"+
            "image_count INTEGER DEFAULT 0,"+
            "is_receive INTEGER DEFAULT 0,"+
            "year INTEGER DEFAULT NULL,"+
            "month INTEGER DEFAULT NULL,"+
            "day INTEGER DEFAULT NULL,"+
            "hour INTEGER DEFAULT NULL,"+
            "minute INTEGER DEFAULT NULL,"+
            "sec INTEGER DEFAULT NULL"+
    ")";
}
