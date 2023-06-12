package com.example.myapplicationui.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Music implements Serializable {
    public String title;
    public String payer;
    public String content;
    public String musicPath;
}