package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.MessageImage;

public interface ImageService extends IService<MessageImage> {
    long addImage(MessageImage messageImage);
}
