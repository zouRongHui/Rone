package org.rone.core.util.rabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * MQ接收方
 * @author rone
 */
public class ReceiverDemo {

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
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection=factory.newConnection();
        Channel channel=connection.createChannel();

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, "topic.debug.*");
        System.out.println("[Receiver] Waiting for messages");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[Receiver] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
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

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, routingKeys[0]);
        channel.queueBind(queueName, exchangeName, routingKeys[1]);
        System.out.println("[Receiver] Waiting for messages");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[Receiver] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * fanout类型的交换机
     */
    private static void testFanOut() throws Exception {
        String exchangeName = "test.fanout.exchange";
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明一个交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

        //产生一个随机的队列名称
        String queueName = channel.queueDeclare().getQueue();
        //将队列和交换机绑定
        channel.queueBind(queueName, exchangeName, "");

        System.out.println("[Receiver] Waiting for messages");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[Receiver] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * demo
     */
    private static void testDemo() throws Exception {
        System.out.println(" [Receiver] Waiting for messages. To exit press CTRL+C");
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

        //设置同时处理的最大消息数
        channel.basicQos(3);

        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
                String message = new String(body, "UTF-8");
                System.out.println(" [Receiver] Received '" + message + "'");
                System.out.println("Exchange: " + envelope.getExchange());
                System.out.println("routingKey: " + envelope.getRoutingKey());
                System.out.println("deliveryTag: " + envelope.getDeliveryTag());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        //RabbitMQ中的消息确认机制
        //设置是否自动回复队列应答，true:RabbitMQ发送出消息该消息就会从消息队列中删除，false:需要消费者手动确认才会删除消息
        boolean autoAck = false;
        channel.basicConsume(queueName, autoAck, consumer);
    }
}
