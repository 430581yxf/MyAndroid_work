package com.example.demo.dto;


import java.io.Serializable;

import com.example.demo.entity.User;
import com.example.demo.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO implements Serializable {
    User user;
    UserProfile userProfile;
}
