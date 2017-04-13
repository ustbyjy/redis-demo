package com.yan.redis.test;

import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2016/11/25.
 */
public class TestMS {
    public static void main(String[] args) {
        Jedis master = new Jedis("127.0.0.1", 6379);
        Jedis slaver = new Jedis("127.0.0.1", 6380);
        slaver.slaveof("127.0.0.1", 6379);
        master.set("k2", "v2");
        System.out.println(master.get("k2"));
    }
}
