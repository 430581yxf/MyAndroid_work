package com.example.myapplicationui.DTO;

import com.example.myapplicationui.entity.User;

import java.util.List;

import lombok.Data;
//DTO说是数据传输对象，其实就是在传输对象的时候发现没有对应的实体类所以就定义一下
@Data
public class FriendDTO {
//    用户本人id
    String userid;
    List<User> friendList;

}
