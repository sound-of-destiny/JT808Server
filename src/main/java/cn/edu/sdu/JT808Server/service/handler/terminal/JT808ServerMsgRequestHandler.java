package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protocol.upMsg.TerminalMessageRequestMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class JT808ServerMsgRequestHandler extends SimpleChannelInboundHandler<TerminalMessageRequestMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TerminalMessageRequestMsg msg) throws Exception {

    }
}
