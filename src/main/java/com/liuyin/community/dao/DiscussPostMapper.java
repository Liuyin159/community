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
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit,int orderMode);
    
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);
    
    int updateCommentCount(int id,int commentCount);
    
    //根据id改变类型
    int updateType(int id, int type);
    
    //根据id改变状态
    int updateStatus(int id, int status);
    
    //更新帖子分数
    int updateScore(int id, double score);
}
