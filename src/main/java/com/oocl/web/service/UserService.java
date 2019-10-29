package com.oocl.web.service;

import com.oocl.web.entities.Permission;
import com.oocl.web.entities.Role;
import com.oocl.web.entities.User;
import com.oocl.web.exception.WrongUserInfoException;
import com.oocl.web.mapper.UserMapper;
import com.oocl.web.util.JedisUtil;
import com.oocl.web.util.TokenUtil;
import com.oocl.web.util.UUIDUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public List getAllUsers(){
        List users  = (List) JedisUtil.get("users");
        if(users == null || users.size() <=0){
            synchronized (this){
                if(users == null || users.size() <=0){
                    logger.info("从数据库获取数据");
                    users = userMapper.selectAllUser();
                    JedisUtil.set("users", users);
                }
            }
        }
        return users;
    }

    public String login(User user) throws WrongUserInfoException {
        logger.info("user login");
        String token = (String) JedisUtil.get(user.getUserName());
        User redisUser = (User) JedisUtil.get(token);
        if(redisUser == null){
            User dbUser =  userMapper.selectUserByUserName(user);
            if(user == null) throw  new WrongUserInfoException();
            if(!user.getUserPassword().equals(dbUser.getUserPassword()))throw new WrongUserInfoException();
            if(token == null)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("userName", user.getUserName());
                token = TokenUtil.createToken(map);
            }
            JedisUtil.set(token, dbUser);
            JedisUtil.set(user.getUserName(), token);
        }
        return token;
    }

    public boolean checkPermission(String token, String roleName) {
        User user = (User) JedisUtil.get(token);
        for(Role role: user.getRoles()){
            if(role.getRoleName().equals(roleName)) return true;
        }
        return false;
    }
}
