package com.yan.redis.dao;

import com.yan.redis.entity.User;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Administrator
 * Date: 2017/4/17
 * Time: 11:10
 */
public interface UserDao {

    void save(final User user);

    User get(final Integer id);
}
