package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Videos;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShortVideoMapper extends BaseMapper<Videos> {
}
