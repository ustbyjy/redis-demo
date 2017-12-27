package com.yan.redis.test;

import com.yan.redis.util.JedisSentinelPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * setnx 可以用于新增操作，即当key不存在时才set
 * set key value xx 可以用于更新操作，即当key存在时才set
 */
public class KeyAddOrUpdateTest {
    private static Logger logger = LoggerFactory.getLogger(KeyAddOrUpdateTest.class);

    public static void main(String[] args) {
        Jedis jedis = JedisSentinelPoolUtil.getJedisPoolInstance().getResource();
        // add
        Long addResult = jedis.setnx("kkk", "vvv");
        if (addResult == 1) {
            logger.info("add success");
        } else {
            logger.info("add fail");
            String updateResult = jedis.set("kkk", "vvv123", "xx");
            if ("OK".equals(updateResult.toUpperCase())) {
                logger.info("update success");
            } else {
                logger.info("update fail");
            }
        }
    }

}
