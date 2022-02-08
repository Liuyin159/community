package com.liuyin.community.dao;

import com.liuyin.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author liu-Yin
 * @Create 2022-02-07 -9:56
 * @Description
 */
@Mapper
public interface UserMapper {
    User selectById(int id);
    
    User selectByName(String name);
    
    User selectByEmail(String Email);
    
    int insertUser(User user);
    
    int updateStatus(int id,int status);
    
    int updateHeader(int id,String headerUrl);
    
    int updatePassword(int id,String password);
}
