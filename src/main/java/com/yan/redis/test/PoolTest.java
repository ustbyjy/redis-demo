package com.yan.redis.test;

import com.yan.redis.util.JedisPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * Created by Administrator on 2016/11/25.
 */
public class PoolTest {
    private static Logger logger = LoggerFactory.getLogger(PoolTest.class);

    public static void main(String[] args) {
//        simpleTest();
        exhaustPoolTest();
    }

    private static void simpleTest() {
        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set("aa", "bb");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            JedisPoolUtil.release(jedis);
        }
    }

    private static void exhaustPoolTest() {
        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        for (int i = 0; i < 8; i++) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                System.out.println(jedis.ping());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        // 超出连接池大小，抛异常
        jedisPool.getResource().ping();
    }
}
