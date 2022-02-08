package com.liuyin.community.service;

import com.liuyin.community.dao.DiscussPostMapper;
import com.liuyin.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liu-Yin
 * @Create 2022-02-07 -16:09
 * @Description
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper mapper;
    
    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        return mapper.selectDiscussPosts(userId,offset,limit);
    }
    public int findDiscussPostRows(int userId){
        return mapper.selectDiscussPostRows(userId);
    }
}
