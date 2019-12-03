package com.itheima;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class producer_Publish {

    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";

    /**
     * 生产者发布订阅的模式
     * 发布订阅的模式中也可以做成  一个对列对应一个消费者、一个对列对应两个消费者
     * 发布订阅模式最大的特点就是在这两个的基础上加上了交换机，交换机绑定队列，消费者监听队列
     * 当生产者发送消息过来，每一个队列中的消费者都会消费，当然，如果一个对列中有对个消费者的话
     * 还是使用默认的轮训算法去消费
     */

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connections = getConnections();
        //建立通道
        Channel channel = connections.createChannel();
        //声明交换机,以及交换机的类型
        channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
        //声明队列，绑定交换机
        channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
        channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
        //队列绑定交换机
        channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_FANOUT_INFORM, "");
        channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_FANOUT_INFORM, "");
        //发送消息
        for (int i = 0; i < 10; i++) {
            String message = "消费者发送过来的消息";
            /**
             * 1.交换机名称
             * 2.路由key
             * 3.消息属性
             * 4.消息内容
             */
            channel.basicPublish(EXCHANGE_FANOUT_INFORM, "", null, message.getBytes());
            System.out.println("成功发送消息：" + message);
        }
        channel.close();

        connections.close();


    } //获取连接
    public static Connection getConnections() {
        Connection connection=null;
        try {
            ConnectionFactory connectionFactory=new ConnectionFactory();
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");//设置虚拟主机
            connectionFactory.setHost("localhost");//连接的ip
            //打开通道
            connection=connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return connection;

    }

}
