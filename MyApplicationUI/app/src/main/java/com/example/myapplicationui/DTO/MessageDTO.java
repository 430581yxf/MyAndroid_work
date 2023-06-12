package com.example.myapplicationui.DTO;

import com.example.myapplicationui.entity.Message;
import com.example.myapplicationui.entity.MessageImage;

import lombok.Data;

@Data
public class MessageDTO {
    int type;
    Message message;
}
