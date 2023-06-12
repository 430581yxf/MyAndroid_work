package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.R;
import com.example.demo.dto.FriendDTO;
import com.example.demo.entity.Friends;
import com.example.demo.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("friends")
public class FriendsController {
    @Autowired
    private FriendsService friendsService;

    @GetMapping("/list/{id}")
    public R list(@PathVariable String id){
        return friendsService.listOfFriendsUser(id);
    }
    @PostMapping("/add")
    @Transactional
    public R add(@RequestBody Friends friends){
        if(friends.getFriends().equals(friends.getId())){
            return R.error("不能添加自己");
        }
        friends.setAddTime(new Date());
        if (friendsService.save(friends)) {

           return  R.success("添加成功");
        }
        return R.error("添加失败");
    }
}
