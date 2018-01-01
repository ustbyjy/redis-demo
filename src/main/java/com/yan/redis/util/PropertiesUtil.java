package com.yan.redis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Properties props;

    static {
        String fileName = "redis.properties";
        props = new Properties();
        try {
            props.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName));
            logger.info("配置文件读取成功");
        } catch (IOException e) {
            logger.error("配置文件读取异常", e);
        }
    }
}
