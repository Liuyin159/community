package com.liuyin.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @Author liu-Yin
 * @Create 2022-01-23 -22:45
 * @Description
 */
@Repository
public class AlphaDaoMybatisImpl implements AlphaDao{

    @Override
    public String select() {
        return "mybatis";
    }
}
