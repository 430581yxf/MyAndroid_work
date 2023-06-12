package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class MessageImage implements Serializable {
    @TableId(type = IdType.AUTO)
    public long id;
    public byte[] content;
}
