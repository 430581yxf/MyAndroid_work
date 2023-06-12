package com.example.myapplicationui.entity;

import androidx.navigation.Navigator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageImage {
    public long id;
    long messageId;
    public byte[] content;
}
