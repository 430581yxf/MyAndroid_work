package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.entity.MessageImage;
import com.example.demo.mapper.ImageMapper;
import com.example.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageMapper imageMapper;

    @PutMapping("/add")
    public R add(@RequestBody MessageImage messageImage){
        long l = imageService.addImage(messageImage);
        System.out.println(l);
        return R.success(l);
    }
    @GetMapping("/get/{id}")
    public R get(@PathVariable long id){
        MessageImage messageImage = imageService.getById(id);
        return R.success(messageImage);
    }

}
