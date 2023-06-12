package com.example.demo.entity;

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
