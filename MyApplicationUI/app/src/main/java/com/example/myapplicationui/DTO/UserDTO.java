package com.example.myapplicationui.DTO;

import com.example.myapplicationui.entity.User;
import com.example.myapplicationui.entity.UserProfile;

import java.io.Serializable;

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
