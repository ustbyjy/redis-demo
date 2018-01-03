package com.yan.redis.test;

import com.yan.redis.util.JedisClusterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;


/**
 * Created by Administrator on 2016/11/25.
 */
public class ClusterTest {
    private static Logger logger = LoggerFactory.getLogger(ClusterTest.class);
    private static JedisCluster jedisCluster;

    public static void main(String[] args) {
        jedisCluster = JedisClusterUtil.getJedisClusterInstance();
        logger.info("set result={}", jedisCluster.set("name", "yjy"));
        logger.info("get result={}", jedisCluster.get("name"));
        JedisClusterUtil.destroy();
    }

}
