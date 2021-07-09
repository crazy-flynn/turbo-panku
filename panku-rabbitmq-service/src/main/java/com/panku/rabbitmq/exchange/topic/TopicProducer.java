package com.panku.rabbitmq.exchange.topic;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: crazy.coder
 */
public class TopicProducer {

    public static final String EXCHANGE_NAME = "exchange_topic";

    public static void main(String[] args) throws Exception {
        //连接信道
        Channel channel = RabbitMQUtils.getChannel();
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("quke.orange.rabbit","队列1.2");
        contentMap.put("lazy.orange.easy","队列1.2");
        contentMap.put("quke.orange.fox","队列1");
        contentMap.put("lazy.pink.easy","队列2");
        contentMap.put("lazy.pink.rabbit","队列2一次");
        contentMap.put("quke.browm.fox","丢弃");
        contentMap.put("quke.orange.male.rabbit","丢弃");
        contentMap.put("lazy.orange.male.rabbit","队列2");
        for (Map.Entry<String, String> content : contentMap.entrySet()) {
            channel.basicPublish(EXCHANGE_NAME, content.getKey(), null, content.getValue().getBytes());
        }
    }


}
