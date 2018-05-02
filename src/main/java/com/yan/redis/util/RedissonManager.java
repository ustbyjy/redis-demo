package com.yan.redis.util;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;

import java.util.concurrent.TimeUnit;

public class RedissonManager {
    private static Logger logger = LoggerFactory.getLogger(RedissonManager.class);

    private static String ip = PropertiesUtil.props.getProperty("redis.host");
    private static Integer port = Integer.valueOf(PropertiesUtil.props.getProperty("redis.port"));

    private static final String scheme = "redis://";
    private static Config config = new Config();
    private static Redisson redisson = null;

    static {
        HostAndPort hostAndPort = new HostAndPort(ip, port);
        config.useSingleServer().setAddress(scheme + hostAndPort.toString());
        redisson = (Redisson) Redisson.create(config);
        logger.info("初始化Redisson完成");
    }

    public static Redisson getRedisson() {
        return redisson;
    }

    /**
     * 若执行业务执行很快，小于 waitTime ，会出现多个节点获取到分布式锁的情况，因此将 waitTime 设为0
     *
     * @param args
     */
    public static void main(String[] args) {
        Redisson redisson = getRedisson();
        String lockKey = "lock";
        RLock lock = redisson.getLock(lockKey);
        boolean lockResult = false;
        try {
            lockResult = lock.tryLock(0, 5, TimeUnit.SECONDS);
            logger.info("lock result: " + lockResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (!lockResult) {
                return;
            }
            lock.unlock();
        }
    }

}
