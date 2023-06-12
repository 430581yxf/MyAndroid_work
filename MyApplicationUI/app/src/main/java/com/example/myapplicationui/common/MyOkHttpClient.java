package com.example.myapplicationui.common;

import static com.example.myapplicationui.common.ConstVariety.*;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.myapplicationui.DTO.ImageDTO;
import com.example.myapplicationui.Utils.AppUtils;
import com.example.myapplicationui.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//这个就是网络请求工具类，因为很多地方用到了网络请求，为了减少写重复代码，避免写重复代码还是不现实我太菜了，其他地方依旧写了很多重复代码
public class  MyOkHttpClient {
    //post请求发起参数是一个对象，在这里会转成json字符串，因为后端需要。
    // 泛型就不解释了，你写个Object再转一下效果也是一样的，但是泛型就不要转类型了
    //之后消息返回根据请求路径辨别需要返回的是啥
    public static <T> void doPost(T Object, String path, Handler handler) {
        Gson gson = new Gson();
        String json = gson.toJson(Object);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        String url = HOST_NAME + path;
        String[] responseData = {null};
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("haha", "onFailure: " + e);
                responseData[0] = "失败";
                Message msg = new Message();
                if (path.contains("friends/add"))
                    msg.what = FRIEND_ADD_POST_ERROR;
                else
                    msg.what = USER_LOGIN_POST_ERROR;
                msg.obj = responseData[0];
                handler.sendMessage(msg);
            }


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                responseData[0] = response.body().string();
                Log.d("MainActivity", responseData[0]);  // 打印响应数据
                Message msg = new Message();
                if (path.contains("friends/add"))
                    msg.what = FRIEND_ADD_POST_SUCCESSFUL;
                else
                    msg.what = USER_LOGIN_POST_SUCCESSFUL;
                msg.obj = responseData[0];
                handler.sendMessage(msg);
            }

        });

    }

    //put也是一样，请求方式改成put就是喜欢玩花样，和post没有写代码上的区别
    public static <T> void doPut(T Object, String path, Handler handler) {
        Gson gson = new Gson();
        String json = gson.toJson(Object);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        String url = HOST_NAME + path;
        String[] responseData = {null};
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 连接超时时间
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("haha", "onFailure: " + e);
                responseData[0] = "失败";
                Message msg = new Message();
                if(path.contains("/msg"))
                msg.what = 0;
                else if (path.contains("/image/add"))
                    msg.what=SEND_IMAGE_ERROR;
                msg.obj = responseData[0];
                handler.sendMessage(msg);
            }


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                responseData[0] = response.body().string();
                Log.d("MainActivity", responseData[0]);  // 打印响应数据
                Message msg = new Message();
                if(path.contains("/msg"))
                    msg.what = 1;
                else if (path.contains("/image/add"))
                    msg.what=SEND_IMAGE_SUCCESSFUL;
                msg.obj = responseData[0];
                handler.sendMessage(msg);
            }

        });

    }

    //get请求你懂的
    public static <T> void doGet(String path, Handler handler, T object, Context context) {
        String[] responseData = {null};
        Gson gson = new Gson();
        String url = HOST_NAME + path;
        Response response;
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Message msg = new Message();
                //如果是请求message的请求
                if(path.contains("/msg/get/"))
                    msg.what = MESSAGE_GET_FAILURE;
                else if(path.contains("friends/list"))
                    msg.what=FRIEND_LIST_GET_ERROR;
                else if (path.contains("/image/get")){
                    msg.what= RECEIVE_IMAGE_ERROR;
                    responseData[0]+="message:"+object;
                }else if(path.contains("user/profile")){
                    msg.what=USER_PROFILE_GET_ERROR;
                }else if(path.contains("video/list")){
                    msg.what=VIDEO_LIST_GET_ERROR;
                }else if (path.contains("/music/list")){
                    msg.what=MUSIC_LIST_GET_ERROR;
                }
                msg.obj = responseData[0];
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                responseData[0] = response.body().string();
                Log.d("MainActivity", responseData[0]);  // 打印响应数据
                Message msg = new Message();
                if(path.contains("/msg/get/"))
                    msg.what = MESSAGE_GET_SUCCESSFUL;
                else if(path.contains("friends/list"))
                    msg.what=FRIEND_LIST_GET_SUCCESSFUL;
                else if (path.contains("/image/get")){
                    msg.what=RECEIVE_IMAGE_SUCCESSFUL;
                    Type type = new TypeToken<Res<ImageDTO>>() {}.getType();
                    Res<ImageDTO> imageDTORes= gson.fromJson(responseData[0], type);
                    String filePath=AppUtils.saveBase64ImageToFile(imageDTORes.getData().getContent(),context);
                    com.example.myapplicationui.entity.Message object1 = (com.example.myapplicationui.entity.Message) object;
                    object1.setTextContent(filePath);
                    msg.obj=gson.toJson(object1);
                    handler.sendMessage(msg);
                    return;
                }else if(path.contains("user/profile")){
                    msg.what=USER_PROFILE_GET_SUCCESSFUL;
                }else if(path.contains("video/list")){
                    msg.what=VIDEO_LIST_GET_SUCCESSFUL;
                }else if (path.contains("/music/list")){
                    System.out.println("/music_successful");
                    msg.what=MUSIC_LIST_GET_SUCCESSFUL;
                }
                msg.obj = responseData[0];
                handler.sendMessage(msg);
            }
        });
    }
//websocket也是一样的思路没区别只有功能上面的区别
    public static WebSocketClient doSocket(Handler handler, User user) {
        URI uri = URI.create(HOST_NAME_WS + "/websocket");
        WebSocketClient client = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Message msg=new Message();
                // 连接建立成功
                msg.what=WEBSOCKET_CONNECT_SUCESSFUL;
                msg.obj="连接成功";
            handler.sendMessage(msg);
            }

            @Override
            public void onMessage(String s) {
                Message message=new Message();
                // 处理收到的消息
                message.what=WEBSOCKET_RECEIVE_SUCESSFUL;
                message.obj=s;
                System.out.println("receive msg"+s);
                handler.sendMessage(message);
            }

            @Override
            public void onClose(int i, String s, boolean b) {

                // 连接关闭

                System.out.println("退出聊天室");
            }

            @Override
            public void onError(Exception e) {
                Message message=new Message();
                // 处理连接错误
                message.what=WEBSOCKET_CONNECT_ERROR;
                message.obj=e;
                System.out.println(e);
                handler.sendMessage(message);
            }
        };
        Gson gson=new Gson();
        client.addHeader("user",gson.toJson(user));
        client.connect();
        return client;
    }
}
