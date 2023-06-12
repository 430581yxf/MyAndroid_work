package com.example.myapplicationui.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Friends implements Serializable {
    public String id;
    public String friends;
    public Date addTime;
    public int isDelete;
    public String remark;
}
