package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.R;
import com.example.demo.dto.FriendDTO;
import com.example.demo.entity.Friends;
import com.example.demo.entity.User;
import com.example.demo.mapper.FriendsMapper;
import com.example.demo.service.FriendsService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper,Friends> implements FriendsService {
    @Autowired
    private UserService userService;
    /*
    * 查询此人相关的朋友以及朋友的信息
    */

    @Override

    public R listOfFriendsUser(String id) {
        FriendDTO friendDTO=new FriendDTO();
        LambdaQueryWrapper<Friends> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Friends::getId,id);
        List<Friends> list = list(lambdaQueryWrapper);
        List<User> collect = list.stream().map(item -> {
            User user ;
            user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getId, item.getFriends()));
            if (user!=null)
            user.setPassword("");
            return user;
        }).collect(Collectors.toList());
        LambdaQueryWrapper<Friends> lambdaQueryWrapper1 = new LambdaQueryWrapper<Friends>().eq(Friends::getFriends,id);
        List<Friends> list1 = list(lambdaQueryWrapper1);
        List<User> collect1 = list1.stream().map(item -> {
            User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getId, item.getId()));
            if (user != null)
                user.setPassword("");
            return user;
        }).collect(Collectors.toList());
        collect.addAll(collect1);
        friendDTO.setFriendList(collect);
        for (int i = 0; i < collect.size(); i++) {
            if(collect.get(i)==null){
                collect.remove(i);
            }
        }
        return R.success(friendDTO);
    }
}
