package com.oocl.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisUtil {
    private static JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool){
        JedisUtil.jedisPool = jedisPool;
    }

    public static void set(String key, Object value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(key.getBytes(), ObjectByteConvertUtil.objectToBytes(value));
    }

    public static Object get(String key){

        Jedis jedis = jedisPool.getResource();
        byte[] keyBytes = key.getBytes();
        byte[] valueBytes = jedis.get(keyBytes);
        if(valueBytes == null) return null;
        Object object =  ObjectByteConvertUtil.bytesToObject(valueBytes);
        return object;
    }

    public static boolean existKey(String key){
        Jedis jedis = jedisPool.getResource();
        byte[] keyBytes = key.getBytes();
        return jedis.exists(keyBytes);
    }
}
