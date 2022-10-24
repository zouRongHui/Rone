package org.rone.core.util.rabbitMQ;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * MQ发送方
 * @author rone
 */
public class SenderDemo {

    public static void main(String[] args) throws Exception {
//        testDemo();
//        testFanOut();
//        testDirect();
        testTopic();
    }

    /**
     * topic类型的交换机
     */
    private static void testTopic() throws Exception {
        String exchangeName = "test.topic.exchange";
        String[] routingKeys = new String[]{"topic.debug.first", "topic.debug.second", "topic.debug.third", "topic.error", "topic.info"};
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection=factory.newConnection();
        Channel channel=connection.createChannel();

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);

        for (int i = 0; i < routingKeys.length; i++) {
            for (int j = 0; j < 3; j++) {
                String message = "vampire " + i + " - " + j;
                channel.basicPublish(exchangeName, routingKeys[i], null, message.getBytes());
                System.out.println("[Sender] Sent '" + message + "' in '" + routingKeys[i] + "'");
            }
        }
        channel.close();
        connection.close();
    }

    /**
     * direct类型的交换机
     */
    private static void testDirect() throws Exception {
        String exchangeName = "test.direct.exchange";
        String[] routingKeys = new String[]{"direct.debug", "direct.error", "direct.info"};
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection=factory.newConnection();
        Channel channel=connection.createChannel();

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);

        for (int i = 0; i < routingKeys.length; i++) {
            for (int j = 0; j < 3; j++) {
                String message = "vampire " + i + " - " + j;
                channel.basicPublish(exchangeName, routingKeys[i], null, message.getBytes());
                System.out.println("[Sender] Sent '" + message + "'");
            }
        }
        channel.close();
        connection.close();
    }

    /**
     * fanout类型的交换机
     */
    private static void testFanOut() throws Exception {
        String exchangeName = "test.fanout.exchange";
        ConnectionFactory factory=new ConnectionFactory();
        Connection connection=factory.newConnection();
        Channel channel=connection.createChannel();

        //fanout表示分发，所有的消费者得到同样的队列信息
        channel.exchangeDeclare(exchangeName, "fanout");
        //分发信息
        for (int i=0;i<5;i++){
            String message="vampire "+i;
            channel.basicPublish(exchangeName, "",null, message.getBytes());
            System.out.println("[Sender] Sent '" + message + "'");
        }
        channel.close();
        connection.close();
    }

    /**
     * demo
     */
    private static void testDemo() throws Exception {
        String queueName = "test.queue";
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ相关信息，不设置即使用默认配置
//        factory.setHost("localhost");
//        factory.setUsername("guest");
//        factory.setPassword("guest");
        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();

        //声明一个队列
        //第一个参数表示队列名称;
        //第二个参数为是否持久化（true表示是，队列将在服务器重启时生存）;
        //第三个参数为是否是独占队列（创建者可以使用的私有队列，断开后自动删除）;
        //第四个参数为当所有消费者客户端连接断开时是否自动删除队列;
        //第五个参数为队列的其他参数
        channel.queueDeclare(queueName, false, false, false, null);

        for (int i = 0; i < 100; i++) {
            String message = "vampire " + i;
            //发送消息到队列中
            //第一个参数为交换机名称;
            //第二个参数为队列映射的路由key;
            //第三个参数为消息的其他属性;
            //第四个参数为发送信息的主体
            channel.basicPublish("", queueName, null,
                    message.getBytes("UTF-8"));
            System.out.println("[Sender] Sent '" + message + "'");
        }

        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
