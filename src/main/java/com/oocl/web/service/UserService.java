package com.oocl.web.service;

import com.oocl.web.entities.User;
import com.oocl.web.util.JedisUtil;
import com.oocl.web.util.TokenUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class UserService {
    org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    public List getAllUsers(){
        Logger logger = Logger.getLogger("aa");
        RedisSerializer redisSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setDefaultSerializer(redisSerializer);
        List users = redisTemplate.opsForList().range("users", 0,-1);
        if(users == null || users.size() <=0){
            synchronized (this){
                if(users == null || users.size() <=0){
                    logger.info("从数据库获取数据");
                    users = Arrays.asList(new User("Erric", "123456"), new User("Jaylon", "123456"));
                    redisTemplate.opsForList().leftPush("users", users);
                }
            }
        }
        return users;
    }
//    private Jedis jedis = new Jedis("127.0.0.1", 6379);
    public String login(User user) {
        User DBUser = new User("123456","123456");
//        String token = jedis.get(user.getUserName());
        String token = (String) JedisUtil.get(user.getUserName());
        if(token == null)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("userName", user.getUserName());
            logger.info("---------------create token---------------------------");
            token = TokenUtil.createToken(map);
            logger.info("---------------"+token+"-------------------");
            JedisUtil.set(user.getUserName(), token);
        }
        return token;
    }
}
