package com.example.demo.dto;

import com.example.demo.entity.Message;
import com.example.demo.entity.MessageImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Serializable {
    Message message;
    MessageImage messageImage;
}
