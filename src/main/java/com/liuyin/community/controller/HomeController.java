package com.liuyin.community.controller;

import com.liuyin.community.entity.DiscussPost;
import com.liuyin.community.entity.Page;
import com.liuyin.community.entity.User;
import com.liuyin.community.service.DiscussPostService;
import com.liuyin.community.service.LikeService;
import com.liuyin.community.service.UserService;
import com.liuyin.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
public class HomeController implements CommunityConstant {
    
    @Autowired
    private DiscussPostService discussPostService;
    
    
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;

    /**
     * 首页方法
     * @param model
     * @param page
     * @return
     */
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page,
                               @RequestParam(name = "orderMode",defaultValue = "0") int orderMode){
        //springmvc会自动实例化model和page，并将page注入model
        //获取总评论数
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index?orderMode="+orderMode);
        
        //list是当前页所有的评论对象
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(),orderMode);
        
        //根据查出来的userId通过userservice组装成为一个新的集合
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(DiscussPost post : list){
                //每个map对应一页的一个帖子信息和用户信息
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.getUserById(post.getUserId());
                map.put("user",user);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);
                discussPosts.add(map);
            }
        }
        //discussPosts是一个装map的list
        //通过超链接传参，改变page对象的current值，使得动态改变页数。
        model.addAttribute("discussPosts",discussPosts);
        
        model.addAttribute("orderMode",orderMode);
        return "/index";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }
}
