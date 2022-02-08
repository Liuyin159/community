package com.liuyin.community.service;

import com.liuyin.community.dao.UserMapper;
import com.liuyin.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author liu-Yin
 * @Create 2022-02-07 -16:17
 * @Description
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    public User getUserById(int id){
        return userMapper.selectById(id);
    }
}
