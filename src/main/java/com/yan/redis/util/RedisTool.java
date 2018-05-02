package com.yan.redis.util;

import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisTool {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 尝试获取分布式锁
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param jedis     Redis客户端
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        String lockKey = "lock";
        String requestId = UUID.randomUUID().toString();
        int expireTimeInMilliseconds = 5000;

        boolean lockResult = tryGetDistributedLock(jedis, lockKey, requestId, expireTimeInMilliseconds);
        System.out.println("lock result: " + lockResult);

        try {
            int executionTimeInSeconds = 3;
            TimeUnit.SECONDS.sleep(executionTimeInSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean unLockResult = releaseDistributedLock(jedis, lockKey, requestId);
        System.out.println("unlock result: " + unLockResult);
    }

}
