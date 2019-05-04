package cn.edu.sdu.JT808Server.service.handler.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JT808ServerMediaAttributeHandler extends SimpleChannelInboundHandler<ServerData.TerminalUploadMediaAttributeMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData.TerminalUploadMediaAttributeMsg msg) throws Exception {
        log.info(msg.toString());
    }
}
