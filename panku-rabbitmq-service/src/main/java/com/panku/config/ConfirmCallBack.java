package com.panku.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: crazy.coder
 * @create: 2021-07-15
 */
@Slf4j
@Component
public class ConfirmCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //注入
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1.发消息 交换机接收到消息回调
     *  1.1 correlationData 保存回调消息的ID及相关信息
     *  1.2 ack 交换机收到消息了 true
     *  1.3 cause null
     * 2.交换机接受消息失败 回调
     *  2.1 correlationData 保存回调消息的ID及相关信息
     *  2.2 ack 交换机收到消息了 false
     * @param correlationData
     * @param ack
     * @param cause 失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        String id =  correlationData.getId() != null ? correlationData.getId():"";
        if (ack){
            log.info("交换机已收到id={}的消息", id );
        } else {
            log.info("交换机未收到id={}的消息，由于【{}】", id, cause );
        }

    }

    /**
     * 可以在当消息传递过程中不可达目的地时将消息回退给生产者
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息【{}】被【{}】交换机退回，退回原因【{}】，路由key【{}】",new String(message.getBody()), exchange, replyText, routingKey);
    }
}
