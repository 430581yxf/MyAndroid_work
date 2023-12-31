package com.example.myapplicationui.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    private String id;
    private String name;
    private String password;
    private String avatar;
    private String remark;
}
