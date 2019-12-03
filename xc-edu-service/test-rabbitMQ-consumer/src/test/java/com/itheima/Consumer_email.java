package com.itheima;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/***
 * 消费者、
 */
public class Consumer_email {
    private static final String QUEUE = "hello world";

    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";


    public static void main(String[] args) throws Exception {
        //创建连接
        Connection connections = getConnections();
        //创建通道
        Channel channel = connections.createChannel();
        //声明交换机,以及交换机的类型
        channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
        //声明队列
        channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
        //绑定
        channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_FANOUT_INFORM, "");
        //默认处理消息的方法
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String s = new String(body, "utf-8");

                System.out.println("消费的消息是：：：：" + s);
            }
        };
        //监听队列
        channel.basicConsume(QUEUE_INFORM_EMAIL, true, consumer);

    }

    //获取连接
    public static Connection getConnections() {
        Connection connection = null;
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");//设置虚拟主机
            connectionFactory.setHost("localhost");//连接的ip
            //打开通道
            connection = connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return connection;

    }
}
