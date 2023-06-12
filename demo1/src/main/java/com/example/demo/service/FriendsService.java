package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.R;
import com.example.demo.entity.Friends;

public interface FriendsService extends IService<Friends> {
    R listOfFriendsUser(String id);
}
