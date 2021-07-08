package com.panku.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @description: rabbitmq 连接工具类
 * @author: crazy.coder
 */
public class RabbitMQUtils {
    public static Channel getChannel(){
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂ip 连接rabbitmq的队列
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("admin");
        try {
            //创建连接
            Connection connection = factory.newConnection();
            //获取信道 channel
            Channel channel = connection.createChannel();
            return channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
