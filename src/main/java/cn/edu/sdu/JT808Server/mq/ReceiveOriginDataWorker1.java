package cn.edu.sdu.JT808Server.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

public class ReceiveOriginDataWorker1 implements Runnable {

    private static final String QUEUE_NAME = "JT808Server_OriginData_Queue";

    public void run() {
        try {
            Channel channel = MQUtil.getChannel();

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.basicQos(1);

            System.out.println(" [*] Origin Worker1 正在等待消息");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    File data = new File("./JT808ServerOriginData/" + LocalDate.now() + "-1.bytes");
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(data, true));
                    dos.write(delivery.getBody());
                    dos.close();
                } finally {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
