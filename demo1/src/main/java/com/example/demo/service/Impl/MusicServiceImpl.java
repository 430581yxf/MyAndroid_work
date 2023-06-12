package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Music;
import com.example.demo.mapper.MusicMapper;
import com.example.demo.service.MusicService;
import org.springframework.stereotype.Service;

@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements MusicService {
}
