package com.yan.redis.test;

import com.yan.redis.common.Constants;
import redis.clients.jedis.Jedis;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/25.
 */
public class MasterSlaverTest {

    public static void main(String[] args) throws InterruptedException {
        Jedis master = new Jedis(Constants.HOST_IP, 6379);
        Jedis slaver0 = new Jedis(Constants.HOST_IP, 6380);
        Jedis slaver1 = new Jedis(Constants.HOST_IP, 6381);

        master.set("k1", "time:" + new Date().getTime());

        System.out.println("master: " + master.get("k1"));
        Thread.sleep(1000);

        System.out.println("slaver0: " + slaver0.get("k1"));
        System.out.println("slaver1: " + slaver1.get("k1"));

        System.out.println();
        System.out.println("Build slave ...\n");

        // 一仆二主
        slaver0.slaveof(Constants.HOST_IP, 6379);
        slaver1.slaveof(Constants.HOST_IP, 6379);

        Thread.sleep(1000);
        System.out.println("slaver0: " + slaver0.get("k1"));
        System.out.println("slaver1: " + slaver1.get("k1"));

        System.out.println();
        System.out.println("Remove salve ...\n");

        // 输入SLAVEOF ON ONE 即可去掉主从关系，反客为主
        slaver0.slaveofNoOne();
        slaver1.slaveofNoOne();
    }
}
