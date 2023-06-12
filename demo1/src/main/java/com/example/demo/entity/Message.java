package com.example.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
