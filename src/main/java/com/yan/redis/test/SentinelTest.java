package com.yan.redis.test;

import com.yan.redis.common.Constants;
import com.yan.redis.util.JedisSentinelPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

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
        Jedis sentinel = null;
        try {
            jedis = JedisSentinelPoolUtil.getJedisPoolInstance().getResource();
            logger.info(jedis.incr("counter").toString());

            sentinel = new Jedis(Constants.HOST_IP, 26379);
            List<Map<String, String>> masters = sentinel.sentinelMasters();
            for (Map<String, String> master : masters) {
                logger.info(master.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            JedisSentinelPoolUtil.release(jedis);
        }
    }
}
