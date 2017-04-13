package com.yan.redis.test;

import com.yan.redis.pubsub.Publisher;
import com.yan.redis.pubsub.SubThread;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Administrator
 * Date: 2017/4/13
 * Time: 18:23
 */
public class TestPubSub {

    public static void main(String[] args) {
        String redisIp = "192.168.92.128";
        int redisPort = 6379;
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), redisIp, redisPort);
        System.out.println(String.format("redis pool is starting, redis ip %s, redis port %d", redisIp, redisPort));

        SubThread subThread = new SubThread(jedisPool);
        subThread.start();

        Publisher publisher = new Publisher(jedisPool);
        publisher.start();
    }
}
