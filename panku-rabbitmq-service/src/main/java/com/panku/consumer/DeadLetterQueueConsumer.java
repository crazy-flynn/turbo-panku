package com.panku.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description:
 * @author: crazy.coder
 * @create: 2021-07-09
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {

    //接收消息
    @RabbitListener(queues = "dead_letter_queue")
    public void receive(Message message, Channel channel){
        String messages = new String(message.getBody());
        log.info("当前时间：{} 收到生产者的消息：【{}】。", new Date().toString(), messages);
    }

}
