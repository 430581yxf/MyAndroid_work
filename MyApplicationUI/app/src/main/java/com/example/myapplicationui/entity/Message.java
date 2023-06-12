package com.example.myapplicationui.entity;

import java.io.Serializable;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message implements Serializable {
    public long id;
    public String senderId;
    public String receiverId;
    public String textContent;
    public int imageCount;
    int isReceive;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int sec;
}
