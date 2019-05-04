package cn.edu.sdu.JT808Server.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

public class ReceiveShortLocationDataWorker implements Runnable {
    private String terminalPhone;

    public ReceiveShortLocationDataWorker(String terminalPhone){
        this.terminalPhone = terminalPhone;
    }

    @Override
    public void run() {
        final String SHORT_LOCATION_QUEUE_NAME = "JT808Server_ShortLocationData_Queue_" + terminalPhone;
        try {
            Channel channel = MQUtil.getChannel();
            channel.queueDeclare(SHORT_LOCATION_QUEUE_NAME, true, false, false, null);
            channel.basicQos(1);

            System.out.println(" [*] ShortLocation Worker 正在等待消息");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    System.out.println("[ShortLocation Worker] 收到消息");
                    File dir = new File("JT808ServerShortLocationData/" + LocalDate.now());
                    if (!dir.exists()) {
                        if (!dir.mkdirs()) {
                            System.out.print("【创建JT808ServerOriginData/LocalDate/terminalPhone文件夹失败】");
                        }
                    }
                    File file = new File(dir, terminalPhone + ".protobuf");
                    if (!file.exists()) {
                        if (!file.createNewFile()) {
                            System.out.print("【创建terminalPhone.protobuf文件失败】");
                        }
                    }
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true));
                    dos.write(delivery.getBody());
                    dos.close();
                } finally {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            channel.basicConsume(SHORT_LOCATION_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
