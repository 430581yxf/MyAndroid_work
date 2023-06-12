package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.R;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.entity.UserProfile;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserProfileService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserProfileService userProfileService;
    @Override
    public R getinfo(String id) {
        UserDTO userDTO=new UserDTO();
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId,id);
        User one = getOne(lambdaQueryWrapper);
        if(one!=null) {
            one.setPassword("");
            LambdaQueryWrapper<UserProfile> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(UserProfile::getId, id);
            UserProfile one1 = userProfileService.getOne(lambdaQueryWrapper1);
            userDTO.setUser(one);
            userDTO.setUserProfile(one1);
            return R.success(userDTO);
        }return R.error("查无此人");
    }

    @Override
    public R putinfo(UserProfile profile) {
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId,profile.getId());
        User one = getOne(lambdaQueryWrapper);
        if(one==null)
            return R.error("没有此人");
        profile.setCreatedTime(new Date());
        if(userProfileService.save(profile)){
            return R.success("添加信息成功");
        }
        return null;
    }
}
