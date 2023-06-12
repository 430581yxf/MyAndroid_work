package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.R;
import com.example.demo.entity.User;
import com.example.demo.entity.UserProfile;
import org.springframework.stereotype.Service;
@Service
public interface UserService extends IService<User> {
    
    R getinfo(String id);

    R putinfo(UserProfile profile);
}
