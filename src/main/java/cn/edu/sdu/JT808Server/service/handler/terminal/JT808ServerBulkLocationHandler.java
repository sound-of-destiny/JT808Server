package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.LocationData;
import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.protocol.downMsg.ServerCommonResponseMsg;
import cn.edu.sdu.JT808Server.util.JT808Const;
import cn.edu.sdu.JT808Server.util.JT808ProtocolUtils;
import cn.edu.sdu.JT808Server.util.MQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JT808ServerBulkLocationHandler extends SimpleChannelInboundHandler<ServerData.TerminalBulkLocationMsg> {

    private static final String EXCHANGE_NAME = "JT808Server_LocationData_Exchange";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData.TerminalBulkLocationMsg msg) throws Exception {

        JT808ProtocolUtils.commonResponse(ctx, msg.getFlowId(),
                JT808Const.msg_id_bulk_location_upload, ServerCommonResponseMsg.success);

        // TODO
        // MQUtil.fanout(EXCHANGE_NAME, msg.toByteArray());

        int num = msg.getDataNum();
        String terminalPhone = msg.getTerminalPhone();
        String SHORT_LOCATION_QUEUE_NAME = "JT808Server_ShortLocationData_Queue_" + terminalPhone;
        ConnectionFactory factory = MQUtil.getConnectionFactory();
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(SHORT_LOCATION_QUEUE_NAME, true, false, false, null);
            for (int i = 0; i < num; i++) {
                LocationData.TerminalLocationMsg terminalLocationMsg = LocationData.TerminalLocationMsg.newBuilder()
                        .setLatitude(msg.getTerminalLocationMsg(i).getLatitude())
                        .setLongitude(msg.getTerminalLocationMsg(i).getLongitude())
                        .setTime(msg.getTerminalLocationMsg(i).getTime()).build();
                channel.basicPublish("", SHORT_LOCATION_QUEUE_NAME, MessageProperties.PERSISTENT_BASIC,
                        terminalLocationMsg.toByteArray());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
