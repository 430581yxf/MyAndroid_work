package com.example.myapplicationui.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UserProfile implements Serializable {
    public String id;
    public String address;
    public Date birthdate;
    public String sex;
    public Date createdTime;
    public String phoneNumber;
    public String otherInfo;
}