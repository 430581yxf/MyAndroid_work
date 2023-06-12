package com.example.myapplicationui.ChatModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationui.R;
import com.example.myapplicationui.Utils.AppUtils;

import java.io.File;
import java.io.FileNotFoundException;
//这里实现的就是四种或各种消息的holder但是原本还想播放声音的后来放弃了，设想很美好，放弃也很美好。
public class MyChatItemHolder {
    //发送text消息holder
    public static class SendTextMessageHolder extends RecyclerView.ViewHolder{
        private String type="send_text";
        private TextView textView;
        public SendTextMessageHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.send_text_message_view);
        }
        public void bindData(String text){
            textView.setText(text);
        };
    }
    //接收消息holder
    public static class ReceiveTextMessageHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private String type="receive_text";
        public ReceiveTextMessageHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.receive_text_message_view);
        }
        public void bindData(String text){
            System.out.println("binddata"+text);
            textView.setText(text);

        };
    }
//    发送图片holder

    public static class SendImageMessageHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private String type="send_image";
        private Context context;
        public SendImageMessageHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            imageView=itemView.findViewById(R.id.send_image);
        }
        public void bindData(String imgFile) throws FileNotFoundException {
            String [] strings=imgFile.split("/");
            String string = strings[strings.length - 1];
            File file = new File(context.getExternalFilesDir(null), string);
            imageView.setImageBitmap(AppUtils.fileToBitmap(file));
        };
    }
    //接收图片holder
    public static class ReceiveImageMessageHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private String type="receive_image";
        private Context context;
        public ReceiveImageMessageHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.receive_image);
            context=itemView.getContext();
        }
        public void bindData(String imgFile) throws FileNotFoundException {
            String [] strings=imgFile.split("/");
            String string = strings[strings.length - 1];
            File file = new File(context.getExternalFilesDir(null), string);
            if (imageView != null) {
                imageView.setImageBitmap(AppUtils.fileToBitmap(file));
            } else {
                // 处理 ImageView 对象为空的情况
                System.out.println("空对象");
            }
        };

    }
    //没用上不说了
    public class SendVoiceMessageHolder extends RecyclerView.ViewHolder{
        private String type="send_voice";

        public SendVoiceMessageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public class ReceiveVoiceMessageHolder extends RecyclerView.ViewHolder{
        private String type="recieve_voice";
        public ReceiveVoiceMessageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
