package com.yan.redis.pubsub;

import redis.clients.jedis.JedisPubSub;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Administrator
 * Date: 2017/4/13
 * Time: 18:17
 */
public class Subscriber extends JedisPubSub {

    public Subscriber() {
    }

    // 接收到消息时触发
    public void onMessage(String channel, String message) {
        System.out.println(String.format("receive redis published message, channel %s, message %s", channel, message));
    }

    // 订阅时触发
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("subscribe redis channel success, channel %s, subscribedChannels %d", channel, subscribedChannels));
    }

    // 取消订阅时触发
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d", channel, subscribedChannels));
    }
}
