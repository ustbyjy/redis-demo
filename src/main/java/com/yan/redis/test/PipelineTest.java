package com.yan.redis.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Arrays;
import java.util.List;

public class PipelineTest {

    public static void main(String[] args) {
//        multiDelTest();
        multiOperateTest();
    }

    private static void multiDelTest() {
        Jedis jedis = new Jedis("10.236.45.120", 6379);
        Pipeline pipeline = jedis.pipelined();
        List<String> keyList = Arrays.asList("k1", "k2", "k3");
        for (String key : keyList) {
            pipeline.del(key);
        }
        pipeline.sync();
        jedis.close();
    }

    private static void multiOperateTest() {
        Jedis jedis = new Jedis("10.236.45.120", 6379);
        Pipeline pipeline = jedis.pipelined();
        pipeline.set("hello", "world");
        pipeline.incr("counter");
        List<Object> resultList = pipeline.syncAndReturnAll();
        for (Object object : resultList) {
            System.out.println("result：" + object);
        }
        jedis.close();
    }
}
