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
public class ReadWriteTest {
    private static Logger logger = LoggerFactory.getLogger(ReadWriteTest.class);

    public static void main(String[] args) {
        Jedis jedisWriteResource = null;
        Jedis jedisReadResource = null;
        try {
            jedisWriteResource = JedisSentinelPoolUtil.getWriteResource();
            jedisReadResource = JedisSentinelPoolUtil.getReadResource();

            logger.info(jedisWriteResource.set("k1", "13579"));
            Thread.sleep(500);
            logger.info(jedisReadResource.get("k1"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            JedisSentinelPoolUtil.release(jedisReadResource);
            JedisSentinelPoolUtil.release(jedisWriteResource);
        }
    }
}
