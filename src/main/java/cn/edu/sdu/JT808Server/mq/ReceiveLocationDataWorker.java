package cn.edu.sdu.JT808Server.mq;

import cn.edu.sdu.JT808Server.util.MongoUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.bson.Document;

import java.nio.charset.StandardCharsets;

public class ReceiveLocationDataWorker implements Runnable {
    private static final String QUEUE_NAME = "JT808Server_LocationData_Queue";

    public void run() {
        try {
            Channel channel = MQUtil.getChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.basicQos(1);

            System.out.println(" [*] Location Worker 正在等待消息");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                System.out.println("[Location Worker] 收到消息");
                try (MongoClient mongoClient = MongoClients.create(MongoUtil.mongoServer)) {
                    Document document = Document.parse(new String(delivery.getBody(), StandardCharsets.UTF_8));
                    String time = (String) document.get("time");
                    MongoDatabase mongoDatabase = mongoClient.getDatabase(MongoUtil.mongodb);
                    MongoCollection<Document> collection = mongoDatabase.getCollection("TerminalLocationMsg-" + time.substring(0, 10));
                    collection.createIndex(Indexes.text("terminalPhone"));
                    collection.insertOne(document);
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
