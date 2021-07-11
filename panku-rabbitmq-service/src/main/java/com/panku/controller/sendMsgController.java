package com.panku.controller;

import com.panku.config.DelayQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @description: 发送消息
 * @author: crazy.coder
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class sendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送延迟消息
     * @param message
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间 {} ,发送 【{}】消息给用户。" , new Date().toString(),message);
        //交换机、routingkey、消息体
        rabbitTemplate.convertAndSend("normal_exchange","RA","来自TTL为10S的队列消息-" + message);
        rabbitTemplate.convertAndSend("normal_exchange","RB","来自TTL为40S的队列消息-" + message);
    }

    /**
     * 发送延迟消息，并设定ttl
     * @param message
     * @param ttlTime
     */
    @GetMapping("/sendByTtl/{ttlTime}/{message}")
    public void sendMsgByExprieTime(@PathVariable String message, @PathVariable String ttlTime){
        log.info("{}: 发送 【{}】消息给用户，设置延迟时间{}ms" , new Date().toString(),message, ttlTime);
        //交换机、routingkey、消息体
        rabbitTemplate.convertAndSend("normal_exchange","RC", "来自自定义TTL队列消息-" + message,msg->{
            //设置发送消息时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    //发送消息及时间
    /**
     * 发送延迟消息，并设定ttl
     * @param message
     * @param delayedTime
     */
//    @GetMapping("/sendDelayMsg/{message}/{delayedTime}")
//    public void sendMsgForDelayed(@PathVariable String message, @PathVariable Integer delayedTime){
//        log.info("{}: 发送 【{}】消息给用户，设置延迟时间{}ms" , new Date().toString(),message, delayedTime);
//        //交换机、routingkey、消息体
//        rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_EXCHANGE,DelayQueueConfig.DELAY_KEY, "来自Delayed队列消息-" + message, msg->{
//            //设置发送消息时长
//            msg.getMessageProperties().setDelay(delayedTime);
//            return msg;
//        });
//    }

}
