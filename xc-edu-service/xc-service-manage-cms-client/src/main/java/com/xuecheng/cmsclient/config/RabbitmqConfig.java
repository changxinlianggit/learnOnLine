package com.xuecheng.cmsclient.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    //队列bean的名称
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";
    //交换机的名称
    public static final String EX_ROUTING_CMS_POSTPAGE="ex_routing_cms_postpage";

    @Value("${xuecheng.mq.queue}")
    String queue;//这个是队列名称

    @Value("${xuecheng.mq.routingKey}")
    String routingKey;//从配置文件中取 routingkey


    //声明交换机
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EX_ROUTING_CMS_POSTPAGE(){
        //声明交换机，并且持久化
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    //声明队列
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue queue_cms_postpage(){
        //声明队列，需要指定队列的名称，我们这里的名称是从yml文件中取出来的
        Queue queue1 = new Queue(queue);
        return queue1;
    }

    /**
     * 绑定交换机,
     * 通过使用Qualifier来指定bean的名称注入对象
     * @return
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(EX_ROUTING_CMS_POSTPAGE)Exchange exchange,
                                            @Qualifier(QUEUE_CMS_POSTPAGE)Queue queue ){
        //绑定的时候指定了routingkey，就是说生产者如果想让这个程序消费就需要有这个routingkey
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }



}
