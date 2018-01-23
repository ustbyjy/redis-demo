package com.yan.redis.test;

import com.yan.redis.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardPoolTest {
    private static int index = 1;

    private static ShardedJedisPool shardedJedisPool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(Integer.valueOf(PropertiesUtil.props.getProperty("redis.maxIdle")));
        poolConfig.setMaxTotal(Integer.valueOf(PropertiesUtil.props.getProperty("redis.maxTotal")));
        poolConfig.setMaxWaitMillis(Long.valueOf(PropertiesUtil.props.getProperty("redis.maxWait")));
        poolConfig.setTestOnBorrow(Boolean.valueOf(PropertiesUtil.props.getProperty("redis.testOnBorrow")));

        List<JedisShardInfo> shardInfoList = new ArrayList<JedisShardInfo>();
        HostAndPort hostAndPort1 = HostAndPort.parseString(PropertiesUtil.props.getProperty("redis.cluster.node.1"));
        HostAndPort hostAndPort2 = HostAndPort.parseString(PropertiesUtil.props.getProperty("redis.cluster.node.2"));
        HostAndPort hostAndPort3 = HostAndPort.parseString(PropertiesUtil.props.getProperty("redis.cluster.node.3"));
        JedisShardInfo shardInfo1 = new JedisShardInfo(hostAndPort1.getHost(), hostAndPort1.getPort());
        JedisShardInfo shardInfo2 = new JedisShardInfo(hostAndPort2.getHost(), hostAndPort2.getPort());
        JedisShardInfo shardInfo3 = new JedisShardInfo(hostAndPort3.getHost(), hostAndPort3.getPort());
        shardInfoList.add(shardInfo1);
        shardInfoList.add(shardInfo2);
        shardInfoList.add(shardInfo3);

        shardedJedisPool = new ShardedJedisPool(poolConfig, shardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            String key = generateKey();
            ShardedJedis shardedJedis = null;
            try {
                shardedJedis = shardedJedisPool.getResource();
                Client client = shardedJedis.getShard(key).getClient();
                System.out.println(key + "=" + client.getHost() + ":" + client.getPort());
            } finally {
                if (shardedJedis != null) {
                    shardedJedis.close();
                }
            }
        }
    }

    private static String generateKey() {
        return String.valueOf(Thread.currentThread().getId()) + "_" + (index++);
    }

}
