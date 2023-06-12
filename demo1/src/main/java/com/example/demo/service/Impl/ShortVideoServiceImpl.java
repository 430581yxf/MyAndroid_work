package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Videos;
import com.example.demo.mapper.ShortVideoMapper;
import com.example.demo.service.ShortVideoService;
import org.springframework.stereotype.Service;

@Service
public class ShortVideoServiceImpl extends ServiceImpl<ShortVideoMapper, Videos> implements ShortVideoService {
}
