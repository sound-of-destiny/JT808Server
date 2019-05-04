package cn.edu.sdu.JT808Server.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MQUtil {

    public static String host = "202.194.14.72";
    public static String virtualHost = "jt808";
    public static String username = "admin";
    public static String password = "123";

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    public static Channel getChannel() {
        Channel channel = null;
        do {
            try {
                ConnectionFactory factory = getConnectionFactory();
                Connection connection = factory.newConnection();
                channel = connection.createChannel();
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                    Thread.sleep(1000);
                    System.out.println("try to connect to rabbitMQ");
                } catch (InterruptedException ie) {
                    System.out.println("rabbitMQ Error");
                }
            }
        } while (channel == null);
        return channel;
    }
}
