package com.yan.redis.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/11/25.
 */
public class APITest {
    private static Logger logger = LoggerFactory.getLogger(APITest.class);

    public static void main(String[] args) {
//        simpleTest();
//        tryCatchTest();
        dataTypesTest();
    }

    private static void simpleTest() {
        Jedis jedis = new Jedis("10.236.40.159", 6379);

        System.out.println(jedis.ping());

        System.out.println(jedis.get("k3"));
        jedis.set("k3", "hello");
        System.out.println(jedis.get("k3"));

        Set<String> keys = jedis.keys("*");
        System.out.println("How many keys? " + keys.size());
        System.out.println(keys);
    }

    private static void tryCatchTest() {
        Jedis jedis = null;
        try {
            // 端口设成6380测试连接不通抛异常
            jedis = new Jedis("10.236.40.159", 6380);
            jedis.get("hello");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    private static void dataTypesTest() {
        Jedis jedis = null;
        try {
            jedis = new Jedis("10.236.40.159", 6379);
            // string
            jedis.set("hello", "world");
            System.out.println(jedis.get("hello"));
            System.out.println(jedis.incr("counter"));
            // hash
            jedis.hset("myhash", "f1", "v1");
            jedis.hset("myhash", "f2", "v2");
            Map<String, String> map = jedis.hgetAll("myhash");
            System.out.println(map);
            // list
            jedis.rpush("mylist", "1");
            jedis.rpush("mylist", "2");
            jedis.rpush("mylist", "3");
            List<String> list = jedis.lrange("mylist", 0, -1);
            System.out.println(Arrays.toString(list.toArray()));
            List<String> myList1 = jedis.brpop("mystlist1", String.valueOf(2));
            System.out.println(Arrays.toString(myList1.toArray()));
            // set
            jedis.sadd("myset", "a");
            jedis.sadd("myset", "b");
            jedis.sadd("myset", "a"); // 重复过滤
            Set<String> set = jedis.smembers("myset");
            System.out.println(set);
            // zset
            jedis.zadd("myzset", 99, "tom");
            jedis.zadd("myzset", 66, "peter");
            jedis.zadd("myzset", 33, "james");
            Set<Tuple> zSet = jedis.zrangeWithScores("myzset", 0, -1); // tuple：数组
            for (Tuple tuple : zSet) {
//                System.out.printf("score：%s，value：%s\n", tuple.getScore(), tuple.getElement());
                System.out.println(MessageFormat.format("score：{0}，value：{1}", tuple.getScore(), tuple.getElement()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
