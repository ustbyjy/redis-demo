package com.yan.redis.test;

import com.yan.redis.common.Constants;
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
public class PubSubTest {

    public static void main(String[] args) {
        int redisPort = 6379;
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), Constants.HOST_IP, redisPort);
        System.out.println(String.format("redis pool is starting, redis ip %s, redis port %d", Constants.HOST_IP, redisPort));

        SubThread subThread = new SubThread(jedisPool);
        subThread.start();

        Publisher publisher = new Publisher(jedisPool);
        publisher.start();
    }
}
