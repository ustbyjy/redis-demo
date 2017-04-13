package com.yan.redis.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 */
public class TestTransaction {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.92.128", 6379);
        Transaction tx = jedis.multi();
        tx.set("k4", "v4");
        tx.set("k5", "v5");
        tx.set("k6", "v6");
        List<Object> resultList = tx.exec();
        for (Object obj : resultList) {
            System.out.println(obj);
        }
    }
}
