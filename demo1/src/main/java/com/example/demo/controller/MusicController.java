package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.entity.Music;
import com.example.demo.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {
    @Autowired
    private MusicService musicService;
    @GetMapping("/list/{id}")
    public R list(@PathVariable String id){
        List<Music> list = musicService.list();
        return R.success(list);
    }
}
