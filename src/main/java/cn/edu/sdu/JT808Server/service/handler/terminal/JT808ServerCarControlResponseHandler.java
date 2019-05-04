package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.protocol.upMsg.TerminalCarControlResponseMsg;
import cn.edu.sdu.JT808Server.server.BusinessManager;
import cn.edu.sdu.JT808Server.util.MQUtil;
import cn.edu.sdu.JT808Server.util.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class JT808ServerCarControlResponseHandler extends SimpleChannelInboundHandler<TerminalCarControlResponseMsg> {

    private static final String QUEUE_NAME = "JT808Server_LocationData_Queue";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TerminalCarControlResponseMsg msg) throws Exception {
        int replyFlowId = msg.getReplyFlowId();
        ServerData.TerminalLocationMsg terminalLocationMsg = msg.getTerminalLocationMsg();
        BusinessManager.getInstance().removeByReplyFlowId(replyFlowId);
        // TODO 该怎么存
        try (Jedis jedis = new Jedis(RedisUtil.REDIS_HOST)) {
            jedis.select(RedisUtil.TerminalAttributeQueryResponseMsg);
            jedis.set(terminalLocationMsg.getTerminalPhone().getBytes(), terminalLocationMsg.toByteArray());
        }

        MQUtil.toQueue(QUEUE_NAME, terminalLocationMsg.toByteArray());
    }
}
