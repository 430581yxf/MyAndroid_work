package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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