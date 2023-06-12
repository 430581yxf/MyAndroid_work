package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Message;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public  class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
}
