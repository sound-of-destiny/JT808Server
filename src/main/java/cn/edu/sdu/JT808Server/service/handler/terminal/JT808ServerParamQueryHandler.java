package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.server.BusinessManager;
import cn.edu.sdu.JT808Server.util.MQUtil;
import cn.edu.sdu.JT808Server.util.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class JT808ServerParamQueryHandler extends SimpleChannelInboundHandler<ServerData.TerminalParamQueryResponseMsg> {

    private static final String QUEUE_NAME = "JT808Server_Param_Queue";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData.TerminalParamQueryResponseMsg msg) throws Exception {

        int replyFlowId = msg.getFlowId();
        log.info(msg.getTerminalParam().toString());

        String terminalPhone = msg.getTerminalPhone();
        BusinessManager.getInstance().removeByReplyFlowId(replyFlowId);
        log.info("【查询终端参数应答】　" + msg);

        try (Jedis jedis = new Jedis(RedisUtil.REDIS_HOST)) {
            jedis.select(RedisUtil.TerminalParamQueryResponseMsg);
            jedis.set(terminalPhone.getBytes(), msg.toByteArray());
        }

        MQUtil.toQueue(QUEUE_NAME, msg.toByteArray());

    }
}
