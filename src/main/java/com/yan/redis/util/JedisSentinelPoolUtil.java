package com.yan.redis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

/**
 * Created by Administrator on 2016/11/25.
 */
public class JedisSentinelPoolUtil {
    private static Logger logger = LoggerFactory.getLogger(JedisSentinelPoolUtil.class);

    private static volatile JedisSentinelPool jedisSentinelPool = null;
    private static volatile List<JedisPool> jedisSlavePools = Collections.synchronizedList(new ArrayList<JedisPool>());

    private static final String REPLICATION_SECTION = "replication";
    private static final String REPLICATION_SECTION_SLAVE = "slave";

    private JedisSentinelPoolUtil() {
    }

    static {
        init();
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
    private static void init() {
        if (jedisSentinelPool == null) {
            synchronized (JedisSentinelPoolUtil.class) {
                if (null == jedisSentinelPool) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxIdle(Integer.valueOf(PropertiesUtil.props.getProperty("redis.maxIdle")));
                    poolConfig.setMaxTotal(Integer.valueOf(PropertiesUtil.props.getProperty("redis.maxTotal")));
                    poolConfig.setMaxWaitMillis(Long.valueOf(PropertiesUtil.props.getProperty("redis.maxWait")));
                    poolConfig.setTestOnBorrow(Boolean.valueOf(PropertiesUtil.props.getProperty("redis.testOnBorrow")));
                    Set<String> sentinels = new HashSet<String>();
                    sentinels.add(PropertiesUtil.props.getProperty("redis.sentinel.1"));
                    sentinels.add(PropertiesUtil.props.getProperty("redis.sentinel.2"));
                    sentinels.add(PropertiesUtil.props.getProperty("redis.sentinel.3"));
                    jedisSentinelPool = new JedisSentinelPool(PropertiesUtil.props.getProperty("redis.masterName"), sentinels, poolConfig);

                    initSlaves(jedisSentinelPool);
                }
            }
        }
    }

    private static void initSlaves(JedisSentinelPool jedisWritePool) {
        Jedis jedis = jedisWritePool.getResource();
        String info = jedis.info(REPLICATION_SECTION);
        String[] infoArray = info.split(System.getProperty("line.separator"));
        String host;
        String port;
        JedisPoolConfig poolConfig;
        JedisPool jedisPool;
        for (String infoItem : infoArray) {
            if (infoItem.indexOf(REPLICATION_SECTION_SLAVE) == 0) {
                host = infoItem.substring(infoItem.indexOf("ip=") + 3, infoItem.indexOf(","));
                port = infoItem.substring(infoItem.indexOf("port=") + 5, infoItem.indexOf(",", infoItem.indexOf("port=")));
                poolConfig = new JedisPoolConfig();
                poolConfig.setMaxIdle(Integer.valueOf(PropertiesUtil.props.getProperty("redis.maxIdle")));
                poolConfig.setMaxTotal(Integer.valueOf(PropertiesUtil.props.getProperty("redis.maxTotal")));
                poolConfig.setMaxWaitMillis(Long.valueOf(PropertiesUtil.props.getProperty("redis.maxWait")));
                poolConfig.setTestOnBorrow(Boolean.valueOf(PropertiesUtil.props.getProperty("redis.testOnBorrow")));
                jedisPool = new JedisPool(poolConfig, host, Integer.valueOf(port));
                jedisSlavePools.add(jedisPool);
            }
        }
    }

    public static Jedis getWriteResource() {
        return jedisSentinelPool.getResource();
    }

    public static Jedis getReadResource() {
        for (JedisPool jedisSlavePool : jedisSlavePools) {
            try {
                return jedisSlavePool.getResource();
            } catch (Exception e) {
                continue;
            }
        }
        throw new JedisException("Could not get a resource from the pool");
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
