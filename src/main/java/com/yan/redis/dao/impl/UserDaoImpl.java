package com.yan.redis.dao.impl;

import com.yan.redis.dao.UserDao;
import com.yan.redis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Administrator
 * Date: 2017/4/17 0017
 * Time: 11:14
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @Autowired
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    public void save(final User user) {
        redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(redisTemplate.getStringSerializer().serialize("user.uid." + user.getId()),
                        redisTemplate.getStringSerializer().serialize(user.getName()));
                return null;
            }
        });
    }

    public User get(final Integer id) {
        return redisTemplate.execute(new RedisCallback<User>() {
            public User doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] key = redisTemplate.getStringSerializer().serialize("user.uid." + id);
                if (connection.exists(key)) {
                    byte[] value = connection.get(key);
                    String name = redisTemplate.getStringSerializer().deserialize(value);
                    User user = new User();
                    user.setId(id);
                    user.setName(name);
                    return user;
                }
                return null;
            }
        });
    }
}
