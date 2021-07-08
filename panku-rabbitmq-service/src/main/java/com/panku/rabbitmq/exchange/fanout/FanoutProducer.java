package com.panku.rabbitmq.exchange.fanout;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @description:
 * @author: crazy.coder
 */
public class FanoutProducer {

    public static final String EXCHANGE_NAME = "exchange_c1";

    public static void main(String[] args) throws Exception{
        //连接信道
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println("生产者发出消息：：" + message);
        }
    }


}
