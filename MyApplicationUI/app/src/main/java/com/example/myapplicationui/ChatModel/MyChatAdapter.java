package com.example.myapplicationui.ChatModel;

import static com.example.myapplicationui.common.ConstVariety.RECEIVE_VIEW_TYPE_IMAGE;
import static com.example.myapplicationui.common.ConstVariety.RECEIVE_VIEW_TYPE_TEXT;
import static com.example.myapplicationui.common.ConstVariety.SEND_VIEW_TYPE_IMAGE;
import static com.example.myapplicationui.common.ConstVariety.SEND_VIEW_TYPE_TEXT;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplicationui.DTO.MessageDTO;
import com.example.myapplicationui.R;

import java.io.FileNotFoundException;
import java.util.List;
//这个就是消息适配器
public class MyChatAdapter extends RecyclerView.Adapter {

    private List<MessageDTO> messageDTOS;
    MyChatAdapter(List<MessageDTO> messageDTOS){
        this.messageDTOS=messageDTOS;
    }
    @Override
    public int getItemViewType(int position) {
        MessageDTO messageDTO = messageDTOS.get(position);
        return messageDTO.getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //判断四种消息
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == RECEIVE_VIEW_TYPE_TEXT ) {
            View view = inflater.inflate(R.layout.receive_message_item, parent, false);
            return new MyChatItemHolder.ReceiveTextMessageHolder(view);
        } else if (viewType == SEND_VIEW_TYPE_IMAGE) {
            View view = inflater.inflate(R.layout.send_image, parent, false);
            return new MyChatItemHolder.SendImageMessageHolder(view);
        }else if (viewType == RECEIVE_VIEW_TYPE_IMAGE) {
            View view = inflater.inflate(R.layout.receive_image, parent, false);
            return new MyChatItemHolder.ReceiveImageMessageHolder(view);
        }
        //发送文本消息
        View view = inflater.inflate(R.layout.send_message_item, parent, false);
        return new MyChatItemHolder.SendTextMessageHolder(view);
    }
//消息holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String data=messageDTOS.get(position).getMessage().getTextContent();
        int type=messageDTOS.get(position).getType();
        System.out.println("data:"+data+"type"+type);
        if (type==SEND_VIEW_TYPE_TEXT) {
            ((MyChatItemHolder.SendTextMessageHolder) holder).bindData(data);
        } else if (type==RECEIVE_VIEW_TYPE_TEXT) {
            ((MyChatItemHolder.ReceiveTextMessageHolder)holder).bindData(data);
        }else  if (type== SEND_VIEW_TYPE_IMAGE){
            try {
                ((MyChatItemHolder.SendImageMessageHolder)holder).bindData(data);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else if (type== RECEIVE_VIEW_TYPE_IMAGE){
            try {
                ((MyChatItemHolder.ReceiveImageMessageHolder)holder).bindData(data);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageDTOS.size();
    }
}
