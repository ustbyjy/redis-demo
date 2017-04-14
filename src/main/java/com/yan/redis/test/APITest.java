package com.yan.redis.test;

import com.yan.redis.common.Constants;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by Administrator on 2016/11/25.
 */
public class APITest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis(Constants.HOST_IP, 6379);

        System.out.println(jedis.ping());

        System.out.println(jedis.get("k3"));

        Set<String> keys = jedis.keys("*");
        System.out.println("How many keys? " + keys.size());
        System.out.println(keys);
    }
}
