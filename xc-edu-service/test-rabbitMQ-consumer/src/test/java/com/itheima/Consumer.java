package com.itheima;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/***
 * 消费者、
 */
public class Consumer {
    private static final String QUEUE="hello world";

    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_FANOUT_INFORM="exchange_fanout_inform";


    public static void main(String[] args) throws Exception {

    }

    /**
     * 一对一的模式/一对多的工作模式，就是启动多个消费者
     * @throws Exception
     */
    public static void consumer_yi() throws Exception{
        //创建连接工厂
        Connection connection=getConnections();
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE,true,false,false,null);
        //接收消息，消费的方法
        DefaultConsumer consumer=new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(consumerTag);
                //交换机
                String exchange = envelope.getExchange();
                System.out.println("交换机"+exchange);
                //路由key
                String routingKey = envelope.getRoutingKey();
                System.out.println("路由key"+routingKey);
                //消息id
                long deliveryTag = envelope.getDeliveryTag();
                System.out.println("消息id"+deliveryTag);

                //获取消息内容
                String string=new String(body,"utf-8");
                System.out.println("内容"+string);

            }
        };

        /**
         * 监听队列
         *  一、队列名称
         *  二、是否自动回复
         *  三、消费的方法
         */
        channel.basicConsume(QUEUE,true,consumer);

    }


    //获取连接
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
