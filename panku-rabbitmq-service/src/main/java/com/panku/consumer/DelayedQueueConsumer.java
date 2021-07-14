package com.panku.consumer;

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
public class DelayedQueueConsumer {

    //接收消息
    @RabbitListener(queues = "delayed.queue")
    public void receiveDelayed(Message message){
        String messages = new String(message.getBody());
        log.info("当前时间：{} 收到延迟队列生产者的消息：【{}】。", new Date().toString(), messages);
    }

}
