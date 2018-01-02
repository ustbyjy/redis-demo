package com.yan.redis.test;

import com.yan.redis.util.JedisSentinelPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.util.Slowlog;

import java.util.Date;
import java.util.List;


public class SlowLogTest {
    private static Logger logger = LoggerFactory.getLogger(SlowLogTest.class);

    public static void main(String[] args) {
        Jedis jedis = JedisSentinelPoolUtil.getWriteResource();
        List<Slowlog> slowLogList = jedis.slowlogGet();
        for (Slowlog slowLog : slowLogList) {
            logger.info("id={}，timeStamp={}，executionTime={}，args={}", slowLog.getId(), new Date(slowLog.getTimeStamp() * 1000), slowLog.getExecutionTime(), slowLog.getArgs().toString());
        }
    }

}
