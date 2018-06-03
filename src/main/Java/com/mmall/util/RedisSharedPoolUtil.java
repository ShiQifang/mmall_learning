package com.mmall.util;

import com.mmall.common.RedisSharedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by Administrator on 2018/5/30.
 */
@Slf4j
public class RedisSharedPoolUtil {

    /**
     * 重新设置key的有效期
     *
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisSharedPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("set  key:{} expire Time ", key);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置key和value的有效期
     *
     * @param key
     * @param value
     * @param exTime
     * @return
     */
    public static String setEx(String key, String value, int exTime) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisSharedPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error:{}", key, value, e);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }

    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisSharedPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error:{}", key, value, e);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }

    public static String get(String key) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisSharedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error ,", key);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisSharedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error ,", key);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }
    
}
