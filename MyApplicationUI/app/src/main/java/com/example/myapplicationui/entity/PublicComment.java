package com.example.myapplicationui.entity;

import lombok.Data;

@Data
public class PublicComment {
    public long id;
    public long messageId;
    public String commenterContent;
}
