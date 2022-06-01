package com.liuyin.community;

/**
 * @Author liu-Yin
 * @Create 2022-02-07 -21:40
 * @Description
 */

import com.liuyin.community.dao.DiscussPostMapper;
import com.liuyin.community.dao.UserMapper;
import com.liuyin.community.entity.DiscussPost;
import com.liuyin.community.entity.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.UUID;

/**
 * @Author liu-Yin
 * @Create 2022-02-07 -10:35
 * @Description
 * 打印日志
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoggerTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);
    
    @Test
    public void testLogger(){
        System.out.println(logger.getName());
        logger.debug("debug log");
        logger.info("info log");
        logger.warn("warn log");
        logger.error("error log");
        
    }
    
    @Test
    public  void testMd(){
        System.out.println(UUID.randomUUID().toString());
    }
}
