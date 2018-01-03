package com.yan.redis.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/11/25.
 */
public class JedisClusterUtil {
    private static Logger logger = LoggerFactory.getLogger(JedisClusterUtil.class);
    private static volatile JedisCluster instance = null;

    private JedisClusterUtil() {
    }

    /**
     * @return JedisCluster
     */
    public static JedisCluster getJedisClusterInstance() {
        if (null == instance) {
            synchronized (JedisClusterUtil.class) {
                if (null == instance) {
                    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
                    poolConfig.setMaxIdle(Integer.valueOf(PropertiesUtil.props.getProperty("redis.maxIdle")));
                    poolConfig.setMaxTotal(Integer.valueOf(PropertiesUtil.props.getProperty("redis.maxTotal")));
                    poolConfig.setMaxWaitMillis(Long.valueOf(PropertiesUtil.props.getProperty("redis.maxWait")));
                    poolConfig.setTestOnBorrow(Boolean.valueOf(PropertiesUtil.props.getProperty("redis.testOnBorrow")));

                    Set<HostAndPort> nodeList = new HashSet<HostAndPort>();
                    nodeList.add(HostAndPort.parseString(PropertiesUtil.props.getProperty("redis.cluster.node.1")));
                    nodeList.add(HostAndPort.parseString(PropertiesUtil.props.getProperty("redis.cluster.node.2")));
                    nodeList.add(HostAndPort.parseString(PropertiesUtil.props.getProperty("redis.cluster.node.3")));
                    nodeList.add(HostAndPort.parseString(PropertiesUtil.props.getProperty("redis.cluster.node.4")));
                    nodeList.add(HostAndPort.parseString(PropertiesUtil.props.getProperty("redis.cluster.node.5")));
                    nodeList.add(HostAndPort.parseString(PropertiesUtil.props.getProperty("redis.cluster.node.6")));

                    instance = new JedisCluster(nodeList, poolConfig);
                }
            }
        }
        return instance;
    }

    public static void destroy() {
        if (null == instance) {
            try {
                instance.close();
            } catch (IOException e) {
                logger.error("销毁Cluster失败", e);
            }
        }
    }
}
