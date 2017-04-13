package com.yan.redis.test;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by Administrator on 2016/11/25.
 */
public class TestAPI {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.92.128", 6379);

        System.out.println(jedis.ping());

        System.out.println(jedis.get("k3"));

        Set<String> keys = jedis.keys("*");
        System.out.println("How many keys? " + keys.size());
        System.out.println(keys);
    }
}
