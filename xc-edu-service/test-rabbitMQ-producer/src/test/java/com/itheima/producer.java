package com.itheima;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 */
public class producer {
    private static final String QUEUE = "helloWorld";
    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";


    public static void main(String[] args) throws IOException {

    }



    /**
     * 一条队列等对个消费者
     * 一条队列对应一个消费者
     */
    public static void producer_yi_duo() {
        try {
            Connection connection = getConnections();
            Channel channel = connection.createChannel();
            //单通道，不需要声明交换机，使用的默认的即可
            channel.queueDeclare(QUEUE, true, false, false, null);
            //发送多条消息
            for (int i = 0; i < 10; i++) {
                //编辑消息，发送
                String message = "你好消费者";

                /**
                 * 一、交换机名称
                 * 二、路由key
                 * 三、消息包含的属性
                 * 四、消息的字节
                 */
                channel.basicPublish("", QUEUE, null, message.getBytes());
                System.out.println("成功发送下消息：" + message);
            }
            //关闭连接
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
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
