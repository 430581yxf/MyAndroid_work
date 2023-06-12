package com.example.demo.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.MessageImage;
import com.example.demo.mapper.ImageMapper;
import com.example.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, MessageImage> implements ImageService{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long addImage(MessageImage messageImage) {
        String sql = "INSERT INTO message_image (content) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setBytes(1,messageImage.getContent());
                    return ps;
                }, keyHolder);
        return keyHolder.getKey().longValue();
    }

}
