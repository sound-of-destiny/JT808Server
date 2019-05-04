package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.LocationData;
import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.protocol.downMsg.ServerCommonResponseMsg;
import cn.edu.sdu.JT808Server.server.ChannelGroupManager;
import cn.edu.sdu.JT808Server.util.JT808Const;
import cn.edu.sdu.JT808Server.util.JT808ProtocolUtils;
import cn.edu.sdu.JT808Server.util.MQUtil;
import cn.edu.sdu.JT808Server.util.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class JT808ServerLocationHandler extends SimpleChannelInboundHandler<ServerData.TerminalLocationMsg> {

    private static final String EXCHANGE_NAME = "JT808Server_LocationData_Exchange";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData.TerminalLocationMsg msg) throws Exception {

        JT808ProtocolUtils.commonResponse(ctx, msg.getFlowId(),
                JT808Const.msg_id_terminal_location, ServerCommonResponseMsg.success);

        log.info("【位置信息】　来自　" + ctx.channel().remoteAddress());

        ChannelGroup channelGroup = ChannelGroupManager.getInstance().findByChannelGroupName("LocationMsg");
        if (channelGroup != null) {
            channelGroup.writeAndFlush(new TextWebSocketFrame(msg.toString()));
        }

        MQUtil.fanout(EXCHANGE_NAME, msg.toByteArray());

        String terminalPhone = msg.getTerminalPhone();
        String SHORT_LOCATION_QUEUE_NAME = "JT808Server_ShortLocationData_Queue_" + terminalPhone;
        LocationData.TerminalLocationMsg terminalLocationMsg = LocationData.TerminalLocationMsg.newBuilder()
                .setLatitude(msg.getLatitude())
                .setLongitude(msg.getLongitude())
                .setTime(msg.getTime()).build();
        MQUtil.toQueue(SHORT_LOCATION_QUEUE_NAME, terminalLocationMsg.toByteArray());

        try (Jedis jedis = new Jedis(RedisUtil.REDIS_HOST)) {
            jedis.set(terminalPhone.getBytes(), msg.toByteArray());
        }
    }



}
