package com.yan.redis.test;

import com.yan.redis.util.JedisPoolUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Calendar;

public class ZSetTest {
    private static Logger logger = LoggerFactory.getLogger(ZSetTest.class);

    private static String REDIS_KEY = "zset_test";

    public static void main(String[] args) throws Exception {
        before();
    }

    public static void before() throws Exception {
        JedisPoolUtil.getJedisPoolInstance();

        try (Jedis jedis = JedisPoolUtil.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            int count = 1000000;
            int defaultLength = 8;
            Calendar calendar = DateUtils.toCalendar(DateUtils.parseDate("2018-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            for (int i = 1; i <= count; i++) {
                // 49231B3F-0EF3-45CE-9752-D8CE011C7EC6
                String mac = RandomStringUtils.randomAlphabetic(28) + StringUtils.repeat("0", defaultLength - Integer.valueOf(i).toString().length()) + i;
                long timestamp = calendar.getTimeInMillis() / 1000;
                logger.info("timestamp={}, i={}, mac={}", timestamp, i, mac);
                pipeline.zadd(REDIS_KEY, timestamp, mac);
                if (i % 10000 == 0) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    pipeline.sync();
                }
            }
        }
    }

}
