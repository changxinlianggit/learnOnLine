package com.itheima.mq;

import com.itheima.config.RabbitmqConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveHandler {

    //消费者监听队列, 指定监听队列的名称
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    // 接收生产者发送过来的消息
    /**
     * msg 是消息内容
     * message 也可以拿出来消息
     * Channel 交换机
     */
    public void receive_email(String msg, Message message, Channel channel){
        System.out.println("email 成功消费消息");
    }


    //监听短信的队列
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void receive_cms(String msg, Message message, Channel channel){
        System.out.println("sms 成功消费消息");
    }
}
