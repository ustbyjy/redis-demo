package com.yan.redis.util;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class JedisShardedPool {
    private static ShardedJedisPool pool;

    private static Integer maxTotal = Integer.valueOf(PropertiesUtil.props.getProperty("redis.maxTotal"));
    private static Integer maxIdle = Integer.valueOf(PropertiesUtil.props.getProperty("redis.maxIdle"));
    private static Integer minIdle = Integer.valueOf(PropertiesUtil.props.getProperty("redis.minIdle"));

    private static Boolean testOnBorrow = Boolean.valueOf(PropertiesUtil.props.getProperty("redis.testOnBorrow"));
    private static Boolean testOnReturn = Boolean.valueOf(PropertiesUtil.props.getProperty("redis.testOnReturn"));
    private static Boolean blockWhenExhausted = Boolean.valueOf(PropertiesUtil.props.getProperty("redis.blockWhenExhausted"));

    private static String firstRedisHost = PropertiesUtil.props.getProperty("redis.shard.node.host.1");
    private static Integer firstRedisPort = Integer.valueOf(PropertiesUtil.props.getProperty("redis.shard.node.port.1"));
    private static String secondRedisHost = PropertiesUtil.props.getProperty("redis.shard.node.host.2");
    private static Integer secondRedisPort = Integer.valueOf(PropertiesUtil.props.getProperty("redis.shard.node.port.2"));

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(blockWhenExhausted);

        JedisShardInfo firstShardInfo = new JedisShardInfo(firstRedisHost, firstRedisPort);
        JedisShardInfo secondShardInfo = new JedisShardInfo(secondRedisHost, secondRedisPort);

        List<JedisShardInfo> shardInfoList = new ArrayList<JedisShardInfo>(2);

        shardInfoList.add(firstShardInfo);
        shardInfoList.add(secondShardInfo);

        pool = new ShardedJedisPool(config, shardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }

    public static ShardedJedis getResource() {
        return pool.getResource();
    }

    public static void returnResource(ShardedJedis jedis) {
        jedis.close();
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for (int i = 0; i < 10; i++) {
            jedis.set("key" + i, "value" + i);
        }
        returnResource(jedis);
    }

}
