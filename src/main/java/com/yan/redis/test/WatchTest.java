package com.yan.redis.test;

import com.yan.redis.common.Constants;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * 开启两个线程，一个线程watch k4，并开启事务set k4，另一个线程也set k4.
 * 注意：要保证前一个线程先watch k4，并且第二个线程先于第一个完成k4的set操作.
 */
public class WatchTest {
    public static final Jedis jedis1 = new Jedis(Constants.HOST_IP, 6379);
    public static final Jedis jedis2 = new Jedis(Constants.HOST_IP, 6379);

    public static void main(String[] args) {
//        testWatch();
        testUnwatch();
    }

    private static void testWatch() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    jedis1.watch("k4");
                    Transaction tx = jedis1.multi();
                    tx.set("k4", "v4");
                    Thread.sleep(5000);
                    List<Object> resultList = tx.exec();
                    for (Object obj : resultList) {
                        System.out.println(obj);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    String result = jedis2.set("k4", "v44");
                    System.out.println(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void testUnwatch() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    jedis1.watch("k4");
                    Transaction tx = jedis1.multi();
                    Thread.sleep(3000);
                    tx.set("k4", "v4");
                    tx.set("k5", "v55555");
                    tx.set("k6", "v66666");
                    jedis1.unwatch();
                    List<Object> resultList = tx.exec();
                    for (Object obj : resultList) {
                        System.out.println(obj);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    String result = jedis2.set("k4", "v44444");
                    System.out.println(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
