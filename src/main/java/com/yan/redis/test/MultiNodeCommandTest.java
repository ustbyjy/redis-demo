package com.yan.redis.test;

import com.yan.redis.util.JedisClusterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2016/11/25.
 */
public class MultiNodeCommandTest {
    private static Logger logger = LoggerFactory.getLogger(MultiNodeCommandTest.class);
    private static JedisCluster jedisCluster;

    public static void main(String[] args) {
        jedisCluster = JedisClusterUtil.getJedisClusterInstance();
        String key;
        String value;
        for (int i = 0; i < 1000; i++) {
            key = "k" + i;
            value = "v" + i;
            logger.info("key={}，value={}，result={}", key, value, jedisCluster.set(key, value));
        }

        Map<String, JedisPool> nodes = jedisCluster.getClusterNodes();
        Jedis jedis;
        Set<String> keys = new HashSet<String>();
        for (Map.Entry<String, JedisPool> entry : nodes.entrySet()) {
            jedis = entry.getValue().getResource();
            if (!isMaster(jedis)) {
                continue;
            }
            Set<String> nodeKeys = jedis.keys("*");
            logger.info("address={}，keys={}", entry.getKey(), nodeKeys.toString());
            keys.addAll(nodeKeys);
        }
        logger.info(keys.toString());
        JedisClusterUtil.destroy();
    }

    private static boolean isMaster(Jedis jedis) {
        String info = jedis.info("replication");
        String[] infoArray = info.split(System.getProperty("line.separator"));
        String role = null;
        for (String infoItem : infoArray) {
            if (infoItem.indexOf("role") == 0) {
                role = infoItem.substring(5, infoItem.length());
            }
        }

        return null != role && role.equals("master");
    }

}
