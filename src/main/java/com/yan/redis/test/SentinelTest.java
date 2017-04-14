package com.yan.redis.test;

import com.yan.redis.common.Constants;
import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Administrator
 * Date: 2017/4/14
 * Time: 11:58
 */
public class SentinelTest {
    public static void main(String[] args) throws InterruptedException {
        Jedis master = new Jedis(Constants.HOST_IP, 6379);
        Jedis slaver0 = new Jedis(Constants.HOST_IP, 6380);
        Jedis slaver1 = new Jedis(Constants.HOST_IP, 6381);

    }
}
