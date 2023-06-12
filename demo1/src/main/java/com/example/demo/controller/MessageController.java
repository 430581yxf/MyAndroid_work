package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.R;
import com.example.demo.entity.Message;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("msg")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @PutMapping("/send")
    public R sendMsg(@RequestBody Message message){
        LocalDateTime localDateTime=LocalDateTime.now();
        message.setYear(localDateTime.getYear());
        message.setMonth(localDateTime.getMonth().ordinal());
        message.setDay(localDateTime.getDayOfMonth());
        message.setHour(localDateTime.getHour());
        message.setMinute(localDateTime.getMinute());
        message.setSec(localDateTime.getSecond());
        messageService.save(message);
        return R.success(message);
    }
    @GetMapping("/get/{id}/{friend}")
    public R getMsg(@PathVariable String id,@PathVariable String friend){
        System.out.println(id+friend);
        LambdaQueryWrapper<Message> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Message::getReceiverId,id);
        lambdaQueryWrapper.eq(Message::getSenderId,friend);
        lambdaQueryWrapper.orderByAsc(Message::getYear);
        lambdaQueryWrapper.orderByAsc(Message::getMonth);
        lambdaQueryWrapper.orderByAsc(Message::getDay);
        lambdaQueryWrapper.orderByAsc(Message::getHour);
        lambdaQueryWrapper.orderByAsc(Message::getMinute);
        lambdaQueryWrapper.orderByAsc(Message::getSec);
        List<Message> list = messageService.list(lambdaQueryWrapper);
        List<Long> idList = list.stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        boolean b = messageService.removeByIds(idList);
        return R.success(list);
    }

}
