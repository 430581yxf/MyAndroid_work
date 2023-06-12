package com.example.myapplicationui.entity;

import lombok.Data;

@Data
public class PublicMessage {
    public long id;
    public String senderId;
    public String text;
    public int imageType;
    public int imageCount;
    public int likeCount;
    public int commentCount;
}
