package com.itheima.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";

    //声明交换机
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        // durable(true)表示持久化交换机中的数据
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    //声明队列 邮件的
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        Queue queue = new Queue(QUEUE_INFORM_EMAIL);
        return queue;
    }

    //声明队列 短信的队列，bean里面的名称就是个该bean指定一个name属性
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        Queue queue=new Queue(QUEUE_INFORM_SMS);
        return queue;
    }

    //绑定队列
    //@Qualifier(QUEUE_INFORM_SMS) 这个就是通过bean的名称来注入的
    @Bean
    public Binding QUEUE_INFORM_SMS_EXCHANGE(@Qualifier(QUEUE_INFORM_SMS) Queue queue,
                                             @Qualifier(EXCHANGE_TOPICS_INFORM)Exchange exchange ){
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.sms.#").noargs();
    }

    //给邮件队列绑定
    @Bean
    public Binding QUEUE_INFORM_EMAIL_EXCHANGE(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue,
                                               @Qualifier(EXCHANGE_TOPICS_INFORM)Exchange exchange ){
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.email.#").noargs();
    }

}
