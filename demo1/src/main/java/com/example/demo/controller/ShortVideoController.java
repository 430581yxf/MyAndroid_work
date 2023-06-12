package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.entity.Videos;
import com.example.demo.service.ShortVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("video")
public class ShortVideoController {
    @Autowired
    private ShortVideoService shortVideoService;
    @GetMapping("/list/{id}")
    public R list(@PathVariable String id){
        List<Videos> list = shortVideoService.list();
        return R.success(list);
    }

}
