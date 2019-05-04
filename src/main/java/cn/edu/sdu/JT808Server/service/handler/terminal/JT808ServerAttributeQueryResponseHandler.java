package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.util.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class JT808ServerAttributeQueryResponseHandler extends SimpleChannelInboundHandler<ServerData.TerminalAttributeQueryResponseMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData.TerminalAttributeQueryResponseMsg msg) throws Exception {
        log.info("【查询终端属性应答】\n {}", msg.toString());

        String terminalPhone = msg.getTerminalPhone();
        try (Jedis jedis = new Jedis(RedisUtil.REDIS_HOST)) {
            jedis.select(RedisUtil.TerminalAttributeQueryResponseMsg);
            jedis.set(terminalPhone.getBytes(), msg.toByteArray());
        }

    }
}
