package com.panku.rabbitmq.exchange.direct;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import java.util.Scanner;

/**
 * @description: 直接 -> 路由交换机 routingKey 不同
 * @author: crazy.coder
 */
public class DirectProducer {

    public static final String EXCHANGE_NAME = "exchange_direct_c1";

    public static void main(String[] args) throws Exception {
        //连接信道
        Channel channel = RabbitMQUtils.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String scannerStr = scanner.next();
            String[] scs = scannerStr.split(":");
            String routingKey = scs[0];
            String message = scs[1];
            //根据routingKey 将消息绑定指定路由
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println("生产者发出消息：：" + message);
        }
    }

}
