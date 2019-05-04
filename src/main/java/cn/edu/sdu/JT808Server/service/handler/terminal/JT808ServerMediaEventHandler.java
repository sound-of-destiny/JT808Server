package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protocol.upMsg.TerminalMediaEventMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class JT808ServerMediaEventHandler extends SimpleChannelInboundHandler<TerminalMediaEventMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TerminalMediaEventMsg msg) throws Exception {
        log.info(msg.toString());
    }
}
