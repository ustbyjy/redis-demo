package com.yan.redis.util;

import com.yan.redis.common.Constants;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Administrator on 2016/11/25.
 */
public class JedisPoolUtil {
    private static volatile JedisPool jedisPool = null;

    private JedisPoolUtil() {
    }

    /**
     * JedisPool连接池参数及含义：
     * maxActive                     最大连接数
     * maxIdle                        最大空闲连接数
     * minIdle                        最少空闲连接数
     * maxWaitMillis              连接池资源耗尽后，调用者最大等待资源，-1表示一直等
     * testOnBorrow              向连接池申请资源之前是否检查连接有效性，每次借用多执行一次ping命令
     * testOnReturn               向连接池归还资源之前是否检查连接有效性，每次归还多执行一次ping命令
     * testWhileIdle               向连接池申请资源之前是否做连接空闲检测，空闲超时的连接会被移除
     *
     * @return JedisPool
     */
    public static JedisPool getJedisPoolInstance() {
        if (jedisPool == null) {
            synchronized (JedisPoolUtil.class) {
                if (null == jedisPool) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxIdle(8);
                    poolConfig.setMaxWaitMillis(1000);
                    poolConfig.setMaxTotal(8);
                    poolConfig.setTestOnBorrow(true);
                    jedisPool = new JedisPool(poolConfig, Constants.HOST_IP, 6379);
                }
            }
        }
        return jedisPool;
    }

    /**
     * Jedis在关闭连接时首先判断是否使用了连接池，若使用了连接池则归还，若未使用直接关闭，具体查看源代码
     *
     * @param jedis
     */
    public static void release(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
