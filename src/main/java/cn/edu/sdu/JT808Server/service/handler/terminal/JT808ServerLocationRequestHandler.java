package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.protocol.upMsg.TerminalLocationRequestMsg;
import cn.edu.sdu.JT808Server.server.BusinessManager;
import cn.edu.sdu.JT808Server.util.MQUtil;
import cn.edu.sdu.JT808Server.util.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class JT808ServerLocationRequestHandler extends SimpleChannelInboundHandler<TerminalLocationRequestMsg> {

    private static final String QUEUE_NAME = "JT808Server_LocationData_Queue";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TerminalLocationRequestMsg msg) throws Exception {
        int replyFlowId = msg.getReplyFlowId();
        BusinessManager.getInstance().removeByReplyFlowId(replyFlowId);
        log.info("【查询位置信息应答】");

        ServerData.TerminalLocationMsg terminalLocationMsg = msg.getTerminalLocationMsg();
        try (Jedis jedis = new Jedis(RedisUtil.REDIS_HOST)) {
            jedis.select(RedisUtil.TerminalAttributeQueryResponseMsg);
            jedis.set(terminalLocationMsg.getTerminalPhone().getBytes(), terminalLocationMsg.toByteArray());
        }

        MQUtil.toQueue(QUEUE_NAME, terminalLocationMsg.toByteArray());
    }
}
