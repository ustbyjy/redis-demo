package com.yan.redis.test;


import com.yan.redis.util.JedisSentinelPoolUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

public class DistributedIdGeneratorTest {
    private static Logger logger = Logger.getLogger(DistributedIdGeneratorTest.class);

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                public void run() {
                    Jedis jedis = JedisSentinelPoolUtil.getJedisPoolInstance().getResource();
                    Long id = jedis.incr("user:id:generator");
                    logger.info(Thread.currentThread().getName() + "：" + id);
                }
            }).start();
        }
    }
}
