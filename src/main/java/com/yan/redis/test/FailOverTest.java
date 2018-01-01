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
public class FailOverTest {
    private static Logger logger = LoggerFactory.getLogger(FailOverTest.class);

    public static void main(String[] args) {
        Jedis jedis = null;
        Jedis sentinel = null;
        try {
            jedis = JedisSentinelPoolUtil.getJedisPoolInstance().getResource();

            // 查看master
            sentinel = new Jedis(Constants.HOST_IP, 26379);
            List<Map<String, String>> masters = sentinel.sentinelMasters();
            for (Map<String, String> master : masters) {
                logger.info(master.toString());
            }
            // 执行命令
            logger.info(jedis.incr("counter").toString());

            // 休眠，等待故障转移
            Thread.sleep(20000);

            // 查看master
            sentinel = new Jedis(Constants.HOST_IP, 26379);
            masters = sentinel.sentinelMasters();
            for (Map<String, String> master : masters) {
                logger.info(master.toString());
            }
            try {
                // 用原jedis执行命令
                logger.info(jedis.incr("counter").toString());
            } catch (Exception e) {
                logger.error("不能使用旧master", e);
            }
            jedis = JedisSentinelPoolUtil.getJedisPoolInstance().getResource();
            logger.info(jedis.incr("counter").toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            JedisSentinelPoolUtil.release(jedis);
        }
    }
}
