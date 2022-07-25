package com.liuyin.community.service;

import com.liuyin.community.dao.LoginTicketMapper;
import com.liuyin.community.dao.UserMapper;
import com.liuyin.community.entity.LoginTicket;
import com.liuyin.community.entity.User;
import com.liuyin.community.util.CommunityUtill;
import com.liuyin.community.util.MailClient;
import com.liuyin.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.liuyin.community.util.CommunityConstant;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author liu-Yin
 * @Create 2022-02-07 -16:17
 * @Description
 */
@Service
public class UserService implements CommunityConstant{

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private MailClient mailClient;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    //一般注入配置文件的值用value注解,即把配置文件中的某一个变量的值付给下面变量
    @Value("${server.servlet.context-path}")
    private String contextPath;
    
    @Value("${community.path.domain}")
    private String domain;
    
    
    public User getUserById(int id){
//        return userMapper.selectById(id);
        User user = getCache(id);
        if(user == null){
            user = initCache(id);
        }
        return user;
    }
    public User findUserByName(String name){
        
        return userMapper.selectByName(name);
    }
    
    public Map<String,Object> register(User user){
        HashMap<String, Object> map = new HashMap<>();
        //1.验证账号合法性，不合法则把错误信息传入map并返回
        
        //空值验证
        if(user == null){
            throw new  IllegalArgumentException("参数不能为空");
        }
        
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
        }
        
        //是否已存在
        if(userMapper.selectByName(user.getUsername())!= null){
            map.put("usernameMsg","用户已存在");
            return map;
        }
        
        if(userMapper.selectByEmail(user.getEmail()) != null){
            map.put("emailMsg","邮箱已存在");
            return map;
        }
        
        //2.若合法,则插入数据，同时发送激活邮件，生成加密密码和激活码
        //邮箱和账户名和密码是传过来的，不用创建
        user.setSalt(CommunityUtill.generateUUID().substring(0,5));
        user.setPassword(CommunityUtill.md5(user.getPassword()+user.getSalt()));
        user.setStatus(0);
        user.setActivationCode(CommunityUtill.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        
        //3.若合法，激活邮件的发送
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        //激活对应的url
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() +"/"+ user.getActivationCode();
        context.setVariable("url",url);
        
        //域对象经过模板引擎渲染后得到发送内容
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);
        
        return map;
    }
    
    //激活函数
    public  int activation(int userId,String code){
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return CommunityConstant.ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            clearCache(userId);
            return CommunityConstant.ACTIVATION_SUCCESS;
        }else {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }
    //登录验证
    public Map<String,Object> login(String username,String password,int expiredSeconds){
        HashMap<String, Object> map = new HashMap<>();
        
        //空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","输入的账号不能为空");
            return map;
        }
        
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","输入密码不能为空");
            return map;
        }
        
        //验证账号
        //先根据username得到user对象
        User user = userMapper.selectByName(username);
        
        if(user == null){
            map.put("usernameMsg","该账号不存在");
            return map;
        }
        if(user.getStatus() == 0){
            map.put("usernameMsg","该账号未激活");
            return map;
        }
        //验证密码

        String s = CommunityUtill.md5(password + user.getSalt());
        if(!user.getPassword().equals(s)){
            map.put("passwordMsg","密码错误");
            return map;
            
        }
        
        //验证通过，创建loginTicket对象,并插入数据库
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setTicket(CommunityUtill.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        //loginTicketMapper.insertLoginTicket(loginTicket);
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);
        
        map.put("ticket",loginTicket.getTicket());
        return map;


    }
    public void logout(String ticket){
        //loginTicketMapper.updateStatus(ticket,1);
        //更改ticket的status状态并重新放入redis
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }
    //根据ticket得到loginTicket对象
    public LoginTicket findLoginTicket(String ticket){
//        return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }
    
    public int updateHeader(int userId,String headUrl){
        int rows = userMapper.updateHeader(userId, headUrl);
        clearCache(userId);
        return rows;

    }

    // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2.取不到时初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    /**
     * 
     * @param userId
     * @return
     * 权限赋值，1是管理员，2是版主，3是普通用户
     */
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.getUserById(userId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }
}
