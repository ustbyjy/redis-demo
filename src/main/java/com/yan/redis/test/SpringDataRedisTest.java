package com.yan.redis.test;

import com.yan.redis.dao.UserDao;
import com.yan.redis.entity.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Administrator
 * Date: 2017/4/17 0017
 * Time: 11:19
 */
public class SpringDataRedisTest {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/spring.xml");
        UserDao userDao = (UserDao) ac.getBean("userDao");

        User user1 = new User();
        user1.setId(5);
        user1.setName("yanjingyang");
        userDao.save(user1);

        User user2 = userDao.get(5);
        System.out.println(user2);

        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) ac.getBean("stringRedisTemplate");

        stringRedisTemplate.opsForValue().set("redis.string.1", "nice");
        String string1 = stringRedisTemplate.opsForValue().get("redis.string.1");
        System.out.println(string1);

        stringRedisTemplate.opsForValue().set("key111", "v111", 3000, TimeUnit.SECONDS);
        Long expireTime = stringRedisTemplate.getExpire("key111");
        System.out.println("expire time: " + expireTime + "s.");

        stringRedisTemplate.opsForSet().add("names", "张三", "李四", "王五");
        Set<String> nameSet = stringRedisTemplate.opsForSet().members("names");
        System.out.println(nameSet);

        stringRedisTemplate.opsForList().leftPush("numbers", "1");
        String number = stringRedisTemplate.opsForList().leftPop("numbers");
        System.out.println(number);
        stringRedisTemplate.opsForList().leftPush("numbers", "1");
        stringRedisTemplate.opsForList().leftPush("numbers", "2");
        stringRedisTemplate.opsForList().leftPush("numbers", "3");
        List<String> numberList = stringRedisTemplate.opsForList().range("numbers", 0, 3);
        System.out.println(numberList);
    }
}
