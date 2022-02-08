package com.liuyin.community;

import com.liuyin.community.dao.DiscussPostMapper;
import com.liuyin.community.dao.UserMapper;
import com.liuyin.community.entity.DiscussPost;
import com.liuyin.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * @Author liu-Yin
 * @Create 2022-02-07 -10:35
 * @Description
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class  mapperTest {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private DiscussPostMapper discussPostMapper;
    
    @Test
    public void testSelectById(){
        User user = userMapper.selectById(101);
        System.out.println(user);
        
        user = userMapper.selectByName("liubei");
        System.out.println(user);
        
        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }
    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("张三");
        user.setEmail("1150544257@qq.com");
        user.setActivationCode("000");
       int row =  userMapper.insertUser(user);
        System.out.println(row);
        System.out.println(user.getId());
        
    }
    @Test
    public void updateUser(){
        int row = userMapper.updateStatus(150,1);
        System.out.println(row);
        
        row = userMapper.updateHeader(150,"http://www.nowcoder.com/102.png");
        System.out.println(row);
        
        row = userMapper.updatePassword(150,"hello");
        System.out.println(row);
    }
    @Test
    public void testDiscussPostMapper(){
        
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0,0,10);
        for(DiscussPost post : list){
            System.out.println(post);
        }
        
        int row = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(row);
    }
}
