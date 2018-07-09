package com.yan.redis.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BRPOPTest {
    private static final Logger logger = LoggerFactory.getLogger(BRPOPThread.class);

    private static String key = "lr";

    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis("10.236.45.120", 6379);
        logger.info("开始时添加队列");
        for (int i = 0; i < 10; i++) {
            jedis.lpush(key, "" + i);
        }

        BRPOPThread r = new BRPOPThread();
        Thread t = new Thread(r);
        t.start();

        TimeUnit.SECONDS.sleep(3);

        r.stop();

        logger.info("停掉线程后添加队列");
        for (int i = 0; i < 100; i++) {
            jedis.lpush(key, "" + i);
        }

        TimeUnit.SECONDS.sleep(3);

        logger.info("删除队列");
        jedis.del(key);
    }

    private static class BRPOPThread implements Runnable {
        private Jedis jedis;
        public volatile boolean flag = true;

        public void run() {
            jedis = new Jedis("10.236.45.120", 6379);
            while (flag) {
                List<String> resultList = jedis.brpop(key, "0");
                logger.info(resultList.get(0) + ": " + resultList.get(1));
            }
        }

        public void stop() {
            logger.info("停止线程");
            jedis.disconnect();
            flag = false;
        }
    }

}
