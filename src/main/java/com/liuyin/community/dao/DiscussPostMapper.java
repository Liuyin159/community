package com.liuyin.community.dao;

import com.liuyin.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author liu-Yin
 * @Create 2022-02-07 -15:44
 * @Description
 */
@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);
    
    int selectDiscussPostRows(@Param("userId") int userId);
}
