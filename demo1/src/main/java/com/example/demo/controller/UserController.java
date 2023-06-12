package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.R;
import com.example.demo.dto.FriendDTO;
import com.example.demo.entity.User;
import com.example.demo.entity.UserProfile;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public R login(@RequestBody User user){

        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId,user.getId());
        User one = userService.getOne(lambdaQueryWrapper);
        if(one!=null){
            return R.success(one);
        }
        return R.error("登陆失败");
    }
    @PostMapping("/reg")
    public R reg(@RequestBody User user){
        String id="";
        //随机生成Id
        for(int i=0;i<10;i++)
            id+=(int) (Math.random() * 10);
        user.setId(id);
        if(userService.save(user)){
            return R.success(user);
        }
        return R.error("注册失败");
    }
    @GetMapping("/{id}")
    public R list(@PathVariable String id){
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId,id);
        User one = userService.getOne(lambdaQueryWrapper);
        if(one!=null)
            return R.success(one);
        return R.error("查无此人");
    }
    @PostMapping("/profile")
    public R profile(@RequestBody UserProfile profile){
     return userService.putinfo(profile);
    }
    @GetMapping("/profile/{id}")
    public R getprofile(@PathVariable String id){
        return userService.getinfo(id);
    }
}
