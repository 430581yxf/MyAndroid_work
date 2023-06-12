package com.example.demo.dto;


import java.util.List;

import com.example.demo.entity.User;
import lombok.Data;

@Data
public class FriendDTO {
//    用户本人id
    String userid;
    List<User> friendList;

}
