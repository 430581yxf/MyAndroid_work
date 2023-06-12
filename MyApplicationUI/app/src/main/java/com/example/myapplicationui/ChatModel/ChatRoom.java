package com.example.myapplicationui.ChatModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.example.myapplicationui.common.ConstVariety.MESSAGE_GET_FAILURE;
import static com.example.myapplicationui.common.ConstVariety.MESSAGE_GET_SUCCESSFUL;
import static com.example.myapplicationui.common.ConstVariety.RECEIVE_IMAGE_SUCCESSFUL;
import static com.example.myapplicationui.common.ConstVariety.RECEIVE_VIEW_TYPE_IMAGE;
import static com.example.myapplicationui.common.ConstVariety.RECEIVE_VIEW_TYPE_TEXT;
import static com.example.myapplicationui.common.ConstVariety.SEND_IMAGE_SUCCESSFUL;
import static com.example.myapplicationui.common.ConstVariety.SEND_VIEW_TYPE_IMAGE;
import static com.example.myapplicationui.common.ConstVariety.SEND_VIEW_TYPE_TEXT;
import static com.example.myapplicationui.common.ConstVariety.WEBSOCKET_CONNECT_ERROR;
import static com.example.myapplicationui.common.ConstVariety.WEBSOCKET_CONNECT_SUCESSFUL;
import static com.example.myapplicationui.common.ConstVariety.WEBSOCKET_RECEIVE_SUCESSFUL;

import com.example.myapplicationui.DTO.ImageDTO;
import com.example.myapplicationui.DTO.MessageDTO;
import com.example.myapplicationui.R;
import com.example.myapplicationui.Utils.AppUtils;
import com.example.myapplicationui.common.MyMessageDBHelper;
import com.example.myapplicationui.common.MyOkHttpClient;
import com.example.myapplicationui.common.Res;
import com.example.myapplicationui.entity.Message;
import com.example.myapplicationui.entity.MessageImage;
import com.example.myapplicationui.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/*
*用户聊天页面 每个方法注释在方法上面
*/

public class ChatRoom extends AppCompatActivity {
    private MyMessageDBHelper messageDBHelper;
    private Intent intent;
    String friend;
    private User user;
    private WebSocketClient client;
    private ConstraintLayout root_layout;
    Typeface typeface;
    private Gson gson=new Gson();
    private Handler handler;
    private List<String> ToolName=new ArrayList<>();
    private List<String> ToolIcon =new ArrayList<>();
    private LinearLayout chatRoomTop,areaWrite,moreOperateArea;
    private TextView showName,otherNowDoing;
    private Button backBtn,detailOperate;
    private RecyclerView showMessage,toolBox;
    private EditText sendMessageEdit;
    private Button recordVoice,moreOperate,sendBtn,send_emoji_btn;
    private List<MessageDTO> messageDTOS=new ArrayList<>();


   @Override
    protected void onCreate(Bundle savedInstanceState) {
       typeface = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
       //获取前面MainActivity传来的数据
       intent=getIntent();
        user= (User) intent.getSerializableExtra("user");
        friend=intent.getStringExtra("friend");
        //查询本地消息数据
        messageDBHelper=new MyMessageDBHelper(getApplicationContext(),user.getId()+"message",null,1);
        List<Message> messages=messageDBHelper.getScrollData(100,0,friend);
       List<MessageDTO> messageDTOS1=messages.stream().map(item->{
           MessageDTO messageDTO=new MessageDTO();
           messageDTO.setMessage(item);
           //如果是一共是四种结果 两个人聊天，两种消息类型
           if(item.getSenderId().equals(user.getId())&&item.getImageCount()<1)
               messageDTO.setType(SEND_VIEW_TYPE_TEXT);//send_text
           else if(item.getSenderId().equals(friend)&&item.getImageCount()>0)
               messageDTO.setType(RECEIVE_VIEW_TYPE_IMAGE);//send_image
           else if(item.getSenderId().equals(user.getId())&&item.getImageCount()>0)
               messageDTO.setType(SEND_VIEW_TYPE_IMAGE);//receive_text
           else if(item.getSenderId().equals(friend)&&item.getImageCount()<1)
               messageDTO.setType(RECEIVE_VIEW_TYPE_TEXT);//receive_image
           return messageDTO;
       }).collect(Collectors.toList());

       //处理各种线程处理的结果
        handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull android.os.Message msg) {
                //webscoket连接成功
                if(msg.what==WEBSOCKET_CONNECT_SUCESSFUL)
                otherNowDoing.setText(msg.obj.toString());
                //webscoket接收消息成功
                else if(msg.what==WEBSOCKET_RECEIVE_SUCESSFUL){
                    Message message=gson.fromJson((String)msg.obj,Message.class);
                     messageDBHelper.save(message);
                             MessageDTO messageDTO=new MessageDTO();
                             //如果是照片信息·就继续向服务器发起get请求
                     if(message.getImageCount()>0){
                         new Thread(()->{MyOkHttpClient.doGet("/image/get/"+message.getTextContent(),handler,message,getApplicationContext());}).start();
                     }else{
                         //收到的是text消息
                     messageDTO.setType(RECEIVE_VIEW_TYPE_TEXT);
                     messageDTO.setMessage(message);
                     //这个是刷新消息
                     addMessageShow(messageDTO);
                     }
                }
                //websocket连接错误显示错误
                else if(msg.what==WEBSOCKET_CONNECT_ERROR){
                    otherNowDoing.setText("connect error or message error");
                }
                //接收消息错误
                else if(msg.what==MESSAGE_GET_FAILURE){
                    Toast.makeText(ChatRoom.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
                //消息接收成功解析消息，刷新页面
                else if(msg.what==MESSAGE_GET_SUCCESSFUL){
                 Type type = new TypeToken<Res<List<Message>>>() {}.getType();
                    System.out.println("msg.obj"+msg.obj);
                    Res<List<Message>> res = gson.fromJson((String)msg.obj, type);
                    List<Message> list=res.getData();
                    list.stream().map(message->{
                        MessageDTO messageDTO=new MessageDTO();
                        messageDTO.setMessage(message);
                        if(message.getImageCount()>0)
                            new Thread(()->{MyOkHttpClient.doGet("/image/get/"+message.getTextContent(),handler,message,getApplicationContext());}).start();
                        else{
                            messageDTO.setType(RECEIVE_VIEW_TYPE_TEXT);
                            messageDBHelper.save(message);
                            addMessageShow(messageDTO);
                        }
                        return message;}).collect(Collectors.toList());}
                //发送图片成功发送图片消息。这里的逻辑在文档里面写着我就不在说了。
                else if(msg.what==SEND_IMAGE_SUCCESSFUL){
                    Type type = new TypeToken<Res<Integer>>() {}.getType();
                    System.out.println("msg.obj"+msg.obj);
                    Res<Integer> res = gson.fromJson((String)msg.obj, type);
                    Integer ImageId=res.getData();
                    Message message=new Message(0,user.getId(),friend,String.valueOf(ImageId),1,0,0,0,0,0,0,0);
                    AppUtils.setTime(message);
                    client.send(gson.toJson(message));
                }
                //接收图片成功解析数据显示土拍你
                else if(msg.what==RECEIVE_IMAGE_SUCCESSFUL){
                    Message message= gson.fromJson(msg.obj.toString(), Message.class);
                    messageDBHelper.save(message);
                    MessageDTO messageDTO=new MessageDTO();
                    messageDTO.setType(RECEIVE_VIEW_TYPE_IMAGE);
                    messageDTO.setMessage(message);
                    addMessageShow(messageDTO);
                }
            }
        };

       //创建websocket连接对象
       client=MyOkHttpClient.doSocket(handler,user);
         //开启线程向服务器发起请求，向服务器发起未接受消息的信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyOkHttpClient.doGet("/msg/get/"+user.getId()+"/"+friend,handler,null,getApplicationContext());
            }
        }).start();
        messageDTOS.addAll(messageDTOS1);
        super.onCreate(savedInstanceState);
        //这个就是初始化图标没什么
        InitData();
        setContentView(R.layout.activity_chat_room);
        //获取布局文件的组件
        InitView();
        //各种动作改变样式设置监听器
        sendMessageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String str=sendMessageEdit.getText().toString().trim();
                if(sendBtn.getVisibility()== GONE&&!str.equals("")){
                    sendBtn.setVisibility(View.VISIBLE);
                    moreOperate.setVisibility(GONE);
                }
                else if(sendBtn.getVisibility()== VISIBLE&&str.equals("")){
                    sendBtn.setVisibility(GONE);
                    moreOperate.setVisibility(VISIBLE);
                    System.out.println("str:"+str);
                }
            }
        });
        //点击发送消息就会发起请求或者发送消息给websocket服务器
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=sendMessageEdit.getText().toString();
                if(str!=""){
                    Message message=new Message();
                    AppUtils.setTime(message);
                    message.setSenderId(user.getId());
                    message.setReceiverId(friend);
                    message.setTextContent(str);
                    if (client!=null) {
                        Gson gosn=new Gson();
                        client.send(gosn.toJson(message));
                        MessageDTO messageDTO=new MessageDTO();
                        if(message.getImageCount()>0)
                            messageDTO.setType(SEND_VIEW_TYPE_IMAGE);
                        else
                            messageDTO.setType(SEND_VIEW_TYPE_TEXT);
                        messageDTO.setMessage(message);
                        //将数据插入到数据库
                        messageDBHelper.save(message);
                        sendMessageEdit.setText("");
                        sendBtn.setVisibility(GONE);
                        moreOperate.setVisibility(VISIBLE);
                        System.out.println(messageDTO);
                        addMessageShow(messageDTO);
                    }
                }
            }
        });
        //获取焦点监听器改变样式
        sendMessageEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(moreOperateArea.getVisibility()==VISIBLE){
                    moreOperateArea.setVisibility(GONE);
                }
            }
        });
        //就是不点击这个moreoperate按钮点击外面就会把图标收起来
        root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moreOperateArea.getVisibility() == View.VISIBLE ||toolBox.getVisibility()==View.VISIBLE){
                    moreOperateArea.setVisibility(GONE);
                    toolBox.setVisibility(GONE);                                }
            }
        });
//        点击返回
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //这个也是刷新绑定消息函数
        showMessageInit(messageDTOS);
        //点击更多工具就会弹出工具框，别看花样多就实现了一个发送图片
        moreOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sendMessageEdit.hasFocus()){
                    sendMessageEdit.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(moreOperate.getWindowToken(), 0);
                }
                if (moreOperateArea.getVisibility() == View.INVISIBLE || moreOperateArea.getVisibility() == GONE||toolBox.getVisibility()==View.INVISIBLE||toolBox.getVisibility()== GONE) {
                    moreOperateArea.setVisibility(View.VISIBLE);
                    toolBox.setVisibility(View.VISIBLE);
                } else {
                                moreOperateArea.setVisibility(GONE);
                                toolBox.setVisibility(GONE);                                }
            }
        });
    }
//刷新消息视图
    private void showMessageInit(List<MessageDTO> messageDTOS) {
        MyChatAdapter myChatAdapter=new MyChatAdapter(messageDTOS);
        showMessage.setAdapter(myChatAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        showMessage.setLayoutManager(linearLayoutManager);
        showMessage.scrollToPosition(messageDTOS.size() - 1);
    }
//初始化图标
    private void InitData() {
        ToolName.add("相册");ToolName.add("相机");ToolName.add("视频通话");ToolName.add("位置");
        ToolName.add("红包");ToolName.add("转账");ToolName.add("语音输入");ToolName.add("我的收藏");
        ToolName.add("名片");ToolName.add("文件");ToolName.add("音乐");
        ToolIcon.add(getResources().getString(R.string.photos));
        ToolIcon.add(getResources().getString(R.string.camera));
        ToolIcon.add(getResources().getString(R.string.videocall));
        ToolIcon.add(getResources().getString(R.string.location));
        ToolIcon.add(getResources().getString(R.string.redbag));
        ToolIcon.add(getResources().getString(R.string.pay));
        ToolIcon.add(getResources().getString(R.string.voiceinput));
        ToolIcon.add(getResources().getString(R.string.Myreserve));
        ToolIcon.add(getResources().getString(R.string.namecard));
        ToolIcon.add(getResources().getString(R.string.file));
        ToolIcon.add(getResources().getString(R.string.music));
    }
//获取布局页面对象
    private void InitView() {
        root_layout=findViewById(R.id.root_layout);
        send_emoji_btn=findViewById(R.id.send_emoji_btn);
        chatRoomTop = findViewById(R.id.ChatRoomTop);
        showName = findViewById(R.id.show_name);
        otherNowDoing = findViewById(R.id.other_now_doing);
        backBtn = findViewById(R.id.back_btn);
        detailOperate = findViewById(R.id.detail_operate);
        showMessage = findViewById(R.id.show_message);
        sendMessageEdit = findViewById(R.id.send_message_edit);
        recordVoice = findViewById(R.id.record_voice);
        moreOperate = findViewById(R.id.more_operate);
        sendBtn = findViewById(R.id.send_btn);
        areaWrite = findViewById(R.id.area_write);
        moreOperateArea = findViewById(R.id.more_operate_area);
        toolBox = findViewById(R.id.ToolBox);
        ToolRecyclerViewAdapter toolRecyclerViewAdapter=new ToolRecyclerViewAdapter();
        toolBox.setAdapter(toolRecyclerViewAdapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        toolBox.setLayoutManager(gridLayoutManager);
    }
//    这个就是因为工具框使用了也是recyclerview所以这里就在类里面配置了一个适配器
    private class ToolRecyclerViewAdapter extends RecyclerView.Adapter<ToolRecyclerViewHolder>{

        @NonNull
        @Override
        public ToolRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_tool, parent, false);
            ToolRecyclerViewHolder toolRecyclerViewHolder=new ToolRecyclerViewHolder(view);
            return toolRecyclerViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull ToolRecyclerViewHolder holder, int position) {
            holder.bindData(ToolIcon.get(position),ToolName.get(position));
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.textView.getText().toString().equals("相册"))
                        photoShop();
                    Toast.makeText(ChatRoom.this, "相册", Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return ToolName.size();
        }
    }
//点击发起startintent打开系统的相册
    private void photoShop() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");//筛选器
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }
//这个是系统相册关闭后调用的函数选择了相册就开启弹窗这个连接websocket会断开所以这里需要重连重新初始化这个对象
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        client=MyOkHttpClient.doSocket(handler,user);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(R.layout.receive_image, null);

// 查找 ImageView 控件，并设置图片资源
            ImageView imageView = layout.findViewById(R.id.receive_image);
            imageView.setImageBitmap(bitmap);
            byte[] bytes=null;
            try {
                bytes=AppUtils.bitmapToBytes(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
// 创建 AlertDialog.Builder 对象，并设置标题、视图和按钮
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("图片预览");
            builder.setView(layout);
            builder.setCancelable(false);
            Bitmap finalBitmap = bitmap;
            byte[] finalBytes = bytes;
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Message message=new Message(0,user.getId(),friend, AppUtils.bitmapToString(finalBitmap),1,0,0,0,0,0,0,0);
                    AppUtils.setTime(message);
                    if(message!=null){
                        MessageImage messageImage=new MessageImage();
                        messageImage.setContent(finalBytes);
      new Thread(()->{MyOkHttpClient.doPut(messageImage,"/image/add",handler);}).start();
                        try {
                            message.setTextContent(AppUtils.saveBase64ImageToFile(message.getTextContent(),getApplicationContext()));
                            messageDBHelper.save(message);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        MessageDTO messageDTO=new MessageDTO();
                        messageDTO.setMessage(message);
                        messageDTO.setType(SEND_VIEW_TYPE_IMAGE);

                        addMessageShow(messageDTO);
                    }
                }
            });
            builder.setNegativeButton("取消",null);

// 创建并显示 AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    //工具框holder就是一个按钮和一个textview
    private class ToolRecyclerViewHolder extends RecyclerView.ViewHolder{
    private Button button;
    private TextView textView;

        public ToolRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            button=itemView.findViewById(R.id.tool_icon);
            textView=itemView.findViewById(R.id.tool_desc);
            button.setTypeface(typeface);
            textView.setGravity(Gravity.CENTER);
        }
        public void bindData(String ToolIcon,String ToolName){
            button.setText(ToolIcon);
            textView.setText(ToolName);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (client != null) {
            client.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.close();
        }
        finish();
    }
    //刷新页面样式
    public void addMessageShow(MessageDTO messageDTO){
        messageDTOS.add(messageDTO);

        MyChatAdapter adapter = (MyChatAdapter) showMessage.getAdapter();

// 通知适配器有数据变动发生，需要进行刷新
        adapter.notifyItemInserted(messageDTOS.size() - 1);

// 定位到新添加的 item
        showMessage.scrollToPosition(messageDTOS.size() - 1);
    }
}