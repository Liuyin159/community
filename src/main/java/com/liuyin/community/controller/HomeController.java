package com.liuyin.community.controller;

import com.liuyin.community.entity.DiscussPost;
import com.liuyin.community.entity.Page;
import com.liuyin.community.entity.User;
import com.liuyin.community.service.DiscussPostService;
import com.liuyin.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author liu-Yin
 * @Create 2022-02-07 -16:21
 * @Description
 */
@Controller
public class HomeController {
    
    @Autowired
    private DiscussPostService discussPostService;
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    // 方法调用钱,SpringMVC会自动实例化Model和Page,并将Page注入Model.
    // 所以,在thymeleaf中可以直接访问Page对象中的数据.
    public String getIndexPage(Model model, Page page){
        //获取总数据数
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        
        //获取当前页
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        
        //根据查出来的userId通过userservice组装成为一个新的集合
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(DiscussPost post : list){
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.getUserById(post.getUserId());
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
}
