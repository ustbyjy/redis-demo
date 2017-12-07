package com.yan.redis.test;

import com.yan.redis.util.JedisSentinelPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Administrator
 * Date: 2017/4/14
 * Time: 11:58
 */
public class SentinelTest {
    private static Logger logger = LoggerFactory.getLogger(SentinelTest.class);

    public static void main(String[] args) {
        Jedis jedis = null;
        try {
            jedis = JedisSentinelPoolUtil.getJedisPoolInstance().getResource();
            System.out.println(jedis.incr("counter"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            JedisSentinelPoolUtil.release(jedis);
        }
    }
}
